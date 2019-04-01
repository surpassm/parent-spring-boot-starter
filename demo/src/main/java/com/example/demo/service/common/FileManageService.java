package com.example.demo.service.common;

import com.example.demo.entity.common.FileManage;
import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.common.pojo.SurpassmFile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

/**
  * @author mc
  * Create date 2019-04-01 10:47:03
  * Version 1.0
  * Description 文件管理接口
  */
public interface FileManageService {
    /**
	 * 新增
	 * @param fileManage 对象
	 * @return 前端返回格式
	 */
    Result insert(String accessToken, FileManage fileManage);
    /**
	 * 修改
	 * @param fileManage 对象
	 * @return 前端返回格式
	 */
    Result update(String accessToken, FileManage fileManage);
    /**
	 * 根据主键删除
	 * @param id 标识
	 * @return 前端返回格式
	 */
    Result deleteGetById(String accessToken, Integer id);
    /**
	 * 根据主键查询
	 * @param id 标识
	 * @return 前端返回格式
	 */
    Result findById(String accessToken, Integer id);
    /**
	 * 条件分页查询
	 * @param page 当前页
	 * @param size 显示多少条
	 * @param sort 排序字段
	 * @param fileManage 查询条件
	 * @return 前端返回格式
	 */
    Result pageQuery(String accessToken, Integer page, Integer size, String sort, FileManage fileManage);


	SurpassmFile store(MultipartFile file);

	List<Path> loadAll();

	Path load(String filename);

	FileSystemResource loadAsResource(String getFileNameUrl);

	void deleteAll();

	Resource serveFile(String fileUrl);
}
