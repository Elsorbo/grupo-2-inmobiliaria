
package com.istb.app.controller.dashboard;

import java.util.List;

import javax.transaction.Transactional;

import com.google.rpc.context.AttributeContext.Response;
import com.istb.app.entity.Empleado;
import com.istb.app.repository.EmpleadoRepositoryI;
import com.istb.app.repository.UsuarioRepositoryI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class EmployeeController {

	@Autowired
	private EmpleadoRepositoryI empleadoManager;

	@Autowired
	private UsuarioRepositoryI usuarioManager;

	@GetMapping("/empleado/{id}")
	public String editEmpleado(@PathVariable Integer id, Model attributes) {

		attributes.addAttribute("sectionTitle", "arrendatarios");
		attributes.addAttribute("account", empleadoManager.findById(id).get());

		return "editarEmpleado";
		
	}

	@GetMapping("/empleado")
	public ResponseEntity<?> getEmpleados() {

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(empleadoManager.findAll());
		
	}

	@PostMapping("/empleado")
	@Transactional
	public ResponseEntity<?> addEmployee(@RequestBody Empleado empleado) {
		
		empleado.getUsuario().setEstado(true);

		usuarioManager.save(empleado.getUsuario());
		empleadoManager.save(empleado);

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(empleado);
		
	}
	
	@PutMapping("/empleado")
	public ResponseEntity<?> updateEmployee(Empleado empleado) {

		
		return null;
		
	}
	
	@DeleteMapping("/empleado")
	public ResponseEntity<?> deleteEmployee(Empleado empleado) {
		
		return null;
		
	}
	
}
