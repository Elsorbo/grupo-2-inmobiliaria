
package com.istb.app.controller.dashboard;

import com.istb.app.entity.Empleado;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

	@PostMapping("/empleados")
	public ResponseEntity<?> addEmployee(Empleado empleado) {
		
		return null;
		
	}
	
	@PutMapping("/empleados")
	public ResponseEntity<?> updateEmployee(Empleado empleado) {

		
		return null;
		
	}
	
	@DeleteMapping("/empleados")
	public ResponseEntity<?> deleteEmployee(Empleado empleado) {
		
		return null;
		
	}
	
}
