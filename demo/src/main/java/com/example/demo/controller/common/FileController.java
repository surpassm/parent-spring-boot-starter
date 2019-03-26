package com.example.demo.controller.common;

import com.example.demo.exception.StorageException;
import com.example.demo.service.common.StorageService;
import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.common.pojo.File;
import com.github.surpassm.config.annotation.AuthorizationToken;
import com.github.surpassm.tool.util.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mc
 * Create date 2019/3/12 15:14
 * Version 1.1
 * Description
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/file/")
@Api(tags = "文件上传Api")
public class FileController {

	@javax.annotation.Resource
	private StorageService storageService;

	@PostMapping("uploads")
	@ApiOperation("批量文件上传")
	@ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
	public Result uploads(@ApiParam(hidden = true) @AuthorizationToken String accessToken,HttpServletRequest request, @RequestParam MultipartFile[] files) {
		List<File> result = new ArrayList<>();
		if (files != null && files.length != 0) {
			for (MultipartFile file : files) {
				try {
					File upload = FileUtils.upload(file, request, "upload");
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
		File store = storageService.store(file);
		return Result.ok(store);
	}

	@GetMapping("getFileNameUrl")
	@ApiOperation(value = "文件下载")
	public ResponseEntity<Resource> getFileNameUrl(@RequestParam String getFileNameUrl){
		FileSystemResource file = storageService.loadAsResource(getFileNameUrl);
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
		List<String> serveFile = storageService
				.loadAll()
				.stream()
				.map(path ->
						MvcUriComponentsBuilder
								.fromMethodName(FileController.class, "serveFile", path.toFile().getPath().replace("\\","/"))
								.build().toString())
				.collect(Collectors.toList());
		return Result.ok(serveFile);
	}

	@GetMapping("getPath")
	@ApiOperation(value = "后端专用",hidden = true)
	public ResponseEntity<Resource> serveFile(@RequestParam String path) throws IOException {
		Resource file = storageService.serveFile(path);
		return ResponseEntity
				.ok()
				.contentLength(file.getFile().length())
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getFilename())
				.body(file);
	}
}
