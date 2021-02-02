package com.istb.app.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class FileUpload {
	@Getter @Setter
	private String bucket;
	
	@Getter @Setter
	private String name;
	
	@Getter @Setter
	private Long generation;
	
	@Getter @Setter
	private Long size;
	
	@Getter @Setter
	private String contentType;
	
	@Getter @Setter
	private String url;
}
