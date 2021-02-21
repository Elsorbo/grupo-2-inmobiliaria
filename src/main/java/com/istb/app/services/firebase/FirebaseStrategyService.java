
package com.istb.app.services.firebase;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.istb.app.models.FileUpload;

@Component
public class FirebaseStrategyService implements FirebaseStrategy {
	
	private StorageOptions options;
	
	@Value("${firebase.storage.bucket}")
	private String bucket;
	
	@Value("${firebase.storage.projectId}")
	private String projectId;
	
	@Value("${firebase.storage.privateKeyId}")
	private String privateKeyId;
	
	@Value("${firebase.storage.privateKey}")
	private String privateKey;
	
	@Value("${firebase.storage.clientX509CertUrl}")
	private String clientX509CertUrl;
	
	@Value("${firebase.storage.clientId}")
	private String clientId;
	
	@Value("${firebase.storage.clientEmail}")
	private String clientEmail;
	
	Logger log = LoggerFactory.getLogger(FirebaseStrategyService.class);

	private final static String FOLDER = "inmobiliaria";

	private String path;
	
	@PostConstruct
	private void initializeFirebase() throws Exception {
		
		this.path = "/";
		this.options = StorageOptions.newBuilder()
			.setProjectId(this.projectId)
			.setCredentials(GoogleCredentials.fromStream(getFirebaseCredential()))
			.build();
	
	}
	
	@Override
	public List<FileUpload> uploadFiles(MultipartFile[] multipartFiles, String path) 
		throws Exception {
	
		this.path = String.format("/%s/", path);
		List<FileUpload> files = uploadFiles(multipartFiles);
		this.path = "/";

		return files;
	
	}

	@Override
	public FileUpload uploadFile(MultipartFile multipartFile) throws Exception {
		File file = convertMultiPartToFile(multipartFile);
	    Path filePath = file.toPath();
	    String objectName = generateFileName(multipartFile);
	    
	    Storage storage = this.options.getService();
	    BlobId blobId = BlobId.of(this.bucket, FOLDER.concat(this.path).concat(objectName));
	    	    
	    BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
			.setContentType(multipartFile.getContentType())
			.setAcl(Arrays.asList( Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER) ))
			.build();
	    
	    Blob blob = storage.create(blobInfo, Files.readAllBytes(filePath));
	    
	    log.info("Resp: " + blob);
	    
	    return new FileUpload(blob.getBucket(), 
			blob.getName(), 
			blob.getGeneration(), 
			blob.getSize(), 
			blob.getContentType(),
			this.getURL(blob.getName()));
	}

	@Override
	public List<FileUpload> uploadFiles(MultipartFile[] files) throws Exception {
		List<FileUpload> filesUploads = new ArrayList<>();
		
		Arrays.asList(files).forEach(file -> {
			try {
				filesUploads.add(this.uploadFile(file));
			} catch (Exception e) {
				log.error("Error: " + e.getMessage());
			}
		});
		
		return filesUploads;
	
	}

	@Override
	public boolean deleteFile(String name) {
	
		Storage storage = this.options.getService();
		BlobId blobId = BlobId.of(this.bucket, name);
		boolean deleted = storage.delete(blobId);
	
		log.info("File deleted: " + deleted);
	
		return deleted;
	
	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
	}
	
	private String getURL (String name) {
		return "https://storage.googleapis.com/"
			.concat(this.bucket)
			.concat("/")
			.concat(name);
	}
	
	private File convertMultiPartToFile (MultipartFile file) throws IOException {
		File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
		FileOutputStream fos = new FileOutputStream(convertedFile);
		fos.write(file.getBytes());
		fos.close();
		return convertedFile;
	}
	
	private InputStream getFirebaseCredential () throws Exception {
		Map<String, String> credentials = new HashMap<String, String>();
		credentials.put("type", "service_account");
		credentials.put("project_id", this.projectId);
		credentials.put("private_key_id", this.privateKeyId);
		credentials.put("private_key", this.privateKey.replace("\\n", "\n"));
		credentials.put("client_email", this.clientEmail);
		credentials.put("client_id", this.clientId);
		credentials.put("auth_uri", "https://accounts.google.com/o/oauth2/auth");
		credentials.put("token_uri", "https://oauth2.googleapis.com/token");
		credentials.put("auth_provider_x509_cert_url", "https://www.googleapis.com/oauth2/v1/certs");
		credentials.put("client_x509_cert_url", this.clientX509CertUrl);
		ObjectMapper mapper = new ObjectMapper();
		String mapString = mapper.writeValueAsString(credentials);
		
		return new ByteArrayInputStream(mapString.getBytes(StandardCharsets.UTF_8));
	}

}
