package com.istb.app.services.firebase;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.istb.app.models.FileUpload;

public interface FirebaseStrategy {
	
	/**
	 * 
	 * @param multipartFile
	 * @return A fileUpload to firebase
	 * @throws Exception
	 */
	FileUpload uploadFile(MultipartFile multipartFile) throws Exception;

	/**
	 * 
	 * @param multipartFile
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public List<FileUpload> uploadFiles(
		MultipartFile[] multipartFile, String path) throws Exception;
	
	/**
	 * 
	 * @param multipartFile array
	 * @return List fileUploads in firebase
	 * @throws Exception
	 */
	List<FileUpload> uploadFiles (MultipartFile[] files) throws Exception;
	
	/**
	 * 
	 * @param name
	 * @return true or false
	 */
	boolean deleteFile (String name);
	
}
