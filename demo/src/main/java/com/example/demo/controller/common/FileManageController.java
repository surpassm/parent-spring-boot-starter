package com.example.demo.controller.common;

import com.example.demo.entity.common.FileManage;
import com.example.demo.exception.StorageException;
import com.example.demo.service.common.FileManageService;
import com.github.surpassm.common.constant.Constant;
import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.common.pojo.SurpassmFile;
import com.github.surpassm.config.annotation.AuthorizationToken;
import com.github.surpassm.tool.util.FileUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
  * @author mc
  * Create date 2019-04-01 10:47:03
  * Version 1.0
  * Description 文件管理控制层
  */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/fileManage/")
@Api(tags  =  "文件管理Api")
public class FileManageController {

    @Resource
    private FileManageService fileManageService;

    @PostMapping("getById")
    @ApiOperation(value = "根据主键删除")
    @ApiResponses({
            @ApiResponse(code=Constant.SUCCESS_CODE,message=Constant.SUCCESS_MSG),
            @ApiResponse(code=Constant.FAIL_SESSION_CODE,message=Constant.FAIL_SESSION_MSG),
            @ApiResponse(code=Constant.FAIL_CODE,message=Constant.FAIL_MSG,response=Result.class)})
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result deleteGetById(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
                                @ApiParam(value = "主键",required = true)@RequestParam(value = "id") Integer id) {
        return fileManageService.deleteGetById(accessToken,id);
    }

    @PostMapping("findById")
    @ApiOperation(value = "根据主键查询")
    @ApiResponses({
            @ApiResponse(code=Constant.FAIL_SESSION_CODE,message=Constant.FAIL_SESSION_MSG),
            @ApiResponse(code=Constant.SUCCESS_CODE,message=Constant.SUCCESS_MSG,response=FileManage.class),
            @ApiResponse(code=Constant.FAIL_CODE,message=Constant.FAIL_MSG,response=Result.class)})
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result findById(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
                           @ApiParam(value = "主键",required = true)@RequestParam(value = "id") Integer id) {
        return fileManageService.findById(accessToken,id);
    }

    @PostMapping("pageQuery")
    @ApiOperation(value = "条件分页查询")
    @ApiResponses({@ApiResponse(code=Constant.SUCCESS_CODE,message=Constant.SUCCESS_MSG,response=FileManage.class),
                   @ApiResponse(code=Constant.FAIL_SESSION_CODE,message=Constant.FAIL_SESSION_MSG)})
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result pageQuery(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
                            @ApiParam(value = "第几页", required = true) @RequestParam(value = "page") Integer page,
                            @ApiParam(value = "多少条",required = true)@RequestParam(value = "size") Integer size,
                            @ApiParam(value = "排序字段")@RequestParam(value = "sort",required = false) String sort,
                            FileManage fileManage) {
        return fileManageService.pageQuery(accessToken,page, size, sort, fileManage);
    }



	@PostMapping("uploads")
	@ApiOperation("批量文件上传")
	@ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
	public Result uploads(@ApiParam(hidden = true) @AuthorizationToken String accessToken, HttpServletRequest request, @RequestParam MultipartFile[] files) {
		List<SurpassmFile> result = new ArrayList<>();
		if (files != null && files.length != 0) {
			for (MultipartFile file : files) {
				try {
					SurpassmFile upload = FileUtils.upload(file, request, "upload");
					result.add(upload);
				} catch (Exception e) {
					log.info("文件上传失败", e);
				}
			}
		}
		return Result.ok(result);
	}

	@PostMapping("/upload")
	@ApiOperation(value = "文件上传")
	@ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
	public Result store(@ApiParam(hidden = true) @AuthorizationToken String accessToken,
						@RequestParam("file") MultipartFile file) {
		SurpassmFile store = fileManageService.store(file);
		return Result.ok(store);
	}

	@GetMapping("getFileNameUrl")
	@ApiOperation(value = "文件下载")
	public ResponseEntity<org.springframework.core.io.Resource> getFileNameUrl(@RequestParam String getFileNameUrl){
		FileSystemResource file = fileManageService.loadAsResource(getFileNameUrl);
		return ResponseEntity
				.ok()
				.contentLength(file.getFile().length())
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getFilename())
				.body(file);
	}

	@ExceptionHandler(StorageException.class)
	public Result handleStorageFileNotFound(StorageException exc) {
		return Result.fail("文件有重名,请重命名文件");
	}


	@GetMapping("listUploadedFiles")
	@ApiOperation(value = "返回所有文件列表")
	public Result listUploadedFiles() {
		List<String> serveFile = fileManageService
				.loadAll()
				.stream()
				.map(path ->
						MvcUriComponentsBuilder
								.fromMethodName(FileManageController.class, "serveFile", path.toFile().getPath().replace("\\","/"))
								.build().toString())
				.collect(Collectors.toList());
		return Result.ok(serveFile);
	}

	@GetMapping("getPath")
	@ApiOperation(value = "后端专用",hidden = true)
	public ResponseEntity<org.springframework.core.io.Resource> serveFile(@RequestParam String path) throws IOException {
		org.springframework.core.io.Resource file = fileManageService.serveFile(path);
		return ResponseEntity
				.ok()
				.contentLength(file.getFile().length())
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getFilename())
				.body(file);
	}
}
