package com.ashwinsproject.jobportal.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	
	public static void saveFile(String uploadDir,String filename, MultipartFile multipartFile)
	throws IOException{
		Path uploadPath = Paths.get(uploadDir);
		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		try(InputStream InputStream = multipartFile.getInputStream();){
			Path path = uploadPath.resolve(filename);
			System.out.println("FilePath " + path);
			System.out.println("fileName " + filename);
			Files.copy(InputStream,path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe) {
			throw new IOException("Could not save image file: " + filename, ioe);
		}
	}
}
