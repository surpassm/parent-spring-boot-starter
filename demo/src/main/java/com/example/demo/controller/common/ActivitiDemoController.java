package com.example.demo.controller.common;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author mc
 * version 1.0v
 * date 2019/3/10 10:27
 * description TODO
 */
@CrossOrigin
@RestController
@RequestMapping("/activiti/demo/")
@Api(tags  =  "2、部门Api")
public class ActivitiDemoController {

    /** 流程定义和部署相关的存储服务 */
    @Resource
    private RepositoryService repositoryService;

    /** 流程运行时相关的服务 */
    @Resource
    private RuntimeService runtimeService;

    /** 节点任务相关操作接口 */
    @Resource
    private TaskService taskService;

    /** 流程图生成器 */
    @Resource
    private ProcessDiagramGenerator processDiagramGenerator;

    /** 历史记录相关服务接口 */
    @Resource
    private HistoryService historyService;

    @PostMapping("start")
    @ApiOperation(value = "启动流程")
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public void start(){
        //https://blog.csdn.net/qq_40451631/article/details/84937251#2_Activiti_77
    }
}
