package com.example.demo.service.common.impl;

import com.example.demo.exception.StorageException;
import com.example.demo.service.common.StorageService;
import com.github.surpassm.common.pojo.File;
import com.github.surpassm.tool.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @author mc
 * Create date 2019/3/25 14:31
 * Version 1.0
 * Description
 */
@Slf4j
@Service
public class StorageServiceImpl implements StorageService {

	private final Path rootLocation= Paths.get("upload-dir");

	@Override
	public File store(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
			}
			String coustem = FileUtils.nowDate() + "/" + file.getOriginalFilename();
			Path resolve = this.rootLocation.resolve(coustem);
			java.io.File dest = new java.io.File(resolve.toFile().getPath());
			//判断文件父目录是否存在
			if (!dest.getParentFile().exists()) {
				boolean mkdirs = dest.getParentFile().mkdirs();
			}
			Files.copy(file.getInputStream(), resolve);
			String path = resolve.toFile().getPath().replace("\\", "/");
			return File.builder().url(path).build();
		} catch (IOException e) {
			throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		java.io.File fileOld = this.rootLocation.toFile();
		try {
//			return Files.walk(this.rootLocation, 1)
//					.filter(path -> !path.equals(this.rootLocation))
//					.map(this.rootLocation::relativize);

			return Files.list(this.rootLocation);
		} catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}
	}

	private void func(java.io.File fileNow){
		java.io.File[] fileOld = fileNow.listFiles();
		assert fileOld != null;
		for(java.io.File file:fileOld){
			//若是目录，则递归打印该目录下的文件
			if(file.isDirectory()) {
				func(file);
			}
			//若是文件，直接打印
			if(file.isFile()) {
				Path path = file.toPath();
			}
		}
	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public FileSystemResource loadAsResource(String getFileNameUrl) {
		FileSystemResource fileSystemResource = new FileSystemResource(getFileNameUrl);
		if(fileSystemResource.exists() || fileSystemResource.isReadable()) {
			return fileSystemResource;
		}
		else {
			throw new StorageException("Could not read file: " + getFileNameUrl);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}
	@Override
	public Resource serveFile(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if(resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageException("Could not read file: " + filename);
			}
		} catch (MalformedURLException e) {
			throw new StorageException("Could not read file: " + filename, e);
		}
	}
}
