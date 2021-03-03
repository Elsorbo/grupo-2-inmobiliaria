
package com.istb.app.util;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public class ControllerUtils {

	public static ResponseEntity<?> getJSONBindErrors(BindingResult bindObjt) {

		return ResponseEntity.badRequest()
			.contentType(MediaType.APPLICATION_JSON)
			.body(bindObjt.getAllErrors());

	}

	public static ResponseEntity<?> getJSONErrors(List<String> errors) {

		return ResponseEntity.badRequest()
			.contentType(MediaType.APPLICATION_JSON)
			.body(errors);

	}
	
	public static ResponseEntity<?> getJSONOkResponse(Object data) {

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(data);

	}
	
}
