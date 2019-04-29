package com.github.surpassm.activiti;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author mc
 * version 1.0v
 * date 2019/3/10 10:23
 * description Activiti工作流工具类
 */
public class ActivitiUtils {

    /**
     * 获取流程走过的线
     * @param bpmnModel 流程对象模型
     * @param processDefinitionEntity 流程定义对象
     * @param historicActivityInstances 历史流程已经执行的节点，并已经按执行的先后顺序排序
     * @return List<String> 流程走过的线
     */
    public static List<String> getHighLightedFlows(BpmnModel bpmnModel, ProcessDefinitionEntity processDefinitionEntity, List<HistoricActivityInstance> historicActivityInstances) {
        // 用以保存高亮的线flowId
        List<String> highFlows = new ArrayList<>();
        if(historicActivityInstances == null || historicActivityInstances.size() == 0) {
			return highFlows;
		}

        // 遍历历史节点
        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {
            // 取出已执行的节点
            HistoricActivityInstance activityimpl = historicActivityInstances.get(i);

            // 用以保存后续开始时间相同的节点
            List<FlowNode> sameStartTimeNodes = new ArrayList<>();

            // 获取下一个节点（用于连线）
            FlowNode sameActivityImpl = getNextFlowNode(bpmnModel, historicActivityInstances, i, activityimpl);
            //FlowNode sameActivityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(i + 1).getActivityId());

            // 将后面第一个节点放在时间相同节点的集合里
            if(sameActivityImpl != null) {
				sameStartTimeNodes.add(sameActivityImpl);
			}

            // 循环后面节点，看是否有与此后继节点开始时间相同的节点，有则添加到后继节点集合
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
				// 后续第一个节点
                HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);
				// 后续第二个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);
                if (activityImpl1.getStartTime().getTime() != activityImpl2.getStartTime().getTime()) {
					break;
				}

                // 如果第一个节点和第二个节点开始时间相同保存
                FlowNode sameActivityImpl2 = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityImpl2.getActivityId());
                sameStartTimeNodes.add(sameActivityImpl2);
            }

            // 得到节点定义的详细信息
            FlowNode activityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(i).getActivityId());
            // 取出节点的所有出去的线，对所有的线进行遍历
            List<SequenceFlow> pvmTransitions = activityImpl.getOutgoingFlows();
            for (SequenceFlow pvmTransition : pvmTransitions) {
                // 获取节点
                FlowNode pvmActivityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(pvmTransition.getTargetRef());

                // 不是后继节点
                if(!sameStartTimeNodes.contains(pvmActivityImpl)) {
					continue;
				}

                // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                highFlows.add(pvmTransition.getId());
            }
        }

        //返回高亮的线
        return highFlows;
    }



    /**
     * 获取下一个节点信息
     * @param bpmnModel 流程模型
     * @param historicActivityInstances 历史节点
     * @param i 当前已经遍历到的历史节点索引（找下一个节点从此节点后）
     * @param activityimpl 当前遍历到的历史节点实例
     * @return FlowNode 下一个节点信息
     */
    private static FlowNode getNextFlowNode(BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstances, int i, HistoricActivityInstance activityimpl) {
        // 保存后一个节点
        FlowNode sameActivityImpl = null;

        // 如果当前节点不是用户任务节点，则取排序的下一个节点为后续节点
        if(!"userTask".equals(activityimpl.getActivityType())) {
            // 是最后一个节点，没有下一个节点
            if(i == historicActivityInstances.size()) {
				return null;
			}
            // 不是最后一个节点，取下一个节点为后继节点  找到紧跟在后面的一个节点
            sameActivityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(i + 1).getActivityId());
            // 返回
            return sameActivityImpl;
        }

        // 遍历后续节点，获取当前节点后续节点
        for (int k = i + 1; k <= historicActivityInstances.size() - 1; k++) {
            // 后续节点
            HistoricActivityInstance activityimp2 = historicActivityInstances.get(k);
            // 都是userTask，且主节点与后续节点的开始时间相同，说明不是真实的后继节点
            if("userTask".equals(activityimp2.getActivityType()) && activityimpl.getStartTime().getTime() == activityimp2.getStartTime().getTime()) {
				continue;
			}
            // 找到紧跟在后面的一个节点
            sameActivityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(k).getActivityId());
            break;
        }
        return sameActivityImpl;
    }
	public static void reject(HistoryService historyService, RuntimeService runtimeService, RepositoryService repositoryService
			, TaskService taskService, String taskId, Map<String, Object> parameterMap) {
		HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		if (null == historicTaskInstance) {
			throw new RuntimeException("无效任务ID[ " + taskId + " ]");
		}
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(historicTaskInstance.getProcessInstanceId()).singleResult();
		if (null == processInstance) {
			throw new RuntimeException("该流程已完成!无法回退");
		}
		// 获取流程定义对象
		BpmnModel bpmnModel = repositoryService.getBpmnModel(historicTaskInstance.getProcessDefinitionId());
		List<org.activiti.bpmn.model.Process> processes = bpmnModel.getProcesses();
		Process process = processes.get(0);
		List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
		FlowNode sourceNode = (FlowNode) process.getFlowElement(historicTaskInstance.getTaskDefinitionKey());
		taskList.forEach(obj -> {
			FlowNode currentNode = (FlowNode) process.getFlowElement(obj.getTaskDefinitionKey());
			// 获取原本流程连线
			List<SequenceFlow> outComingSequenceFlows = currentNode.getOutgoingFlows();
			// 配置反转流程连线
			SequenceFlow sequenceFlow = new SequenceFlow();
			sequenceFlow.setTargetFlowElement(sourceNode);
			sequenceFlow.setSourceFlowElement(currentNode);
			sequenceFlow.setId("callback-flow");

			List<SequenceFlow> newOutComingSequenceFlows = new ArrayList<>();
			newOutComingSequenceFlows.add(sequenceFlow);
			currentNode.setOutgoingFlows(newOutComingSequenceFlows);
			// 配置任务审批人 完成任务
			taskService.complete(obj.getId(), parameterMap);
			// 复原流程
			currentNode.setOutgoingFlows(outComingSequenceFlows);
		});
		// 更新流程状态
		/*HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
		if (null != historicProcessInstance) {
			// 流程已完成
			if (null != historicProcessInstance.getEndTime()) {

			} else {
				// 流程未完成

				List<HistoricTaskInstance> taskLists = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstance.getProcessInstanceId()).orderByTaskCreateTime().desc().list();
				if (!taskLists.isEmpty()) {
					HistoricTaskInstance historicTaskInstances = taskLists.get(0);
				}
			}
		}*/
	}
}
