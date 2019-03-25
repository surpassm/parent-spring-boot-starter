package com.example.demo.service.common;

import com.github.surpassm.common.pojo.File;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author mc
 * Create date 2019/3/25 14:30
 * Version 1.0
 * Description
 */
public interface StorageService {


	File store(MultipartFile file);

	Stream<Path> loadAll();

	Path load(String filename);

	FileSystemResource loadAsResource(String getFileNameUrl);

	void deleteAll();

	Resource serveFile(String filename);
}
