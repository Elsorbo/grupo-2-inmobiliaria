
package com.istb.app.controller.dashboard;

import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.istb.app.entity.Empleado;
import com.istb.app.repository.EmpleadoRepositoryI;
import com.istb.app.services.accounts.AccountsServiceI;
import com.istb.app.services.firebase.FirebaseStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmployeeController {

	@Autowired
	private FirebaseStrategy fbmanager;

	@Autowired
	private AccountsServiceI accountsManager;

	@Autowired
	private EmpleadoRepositoryI empleadoManager;

	@GetMapping("/empleados")
	@Transactional
	public String getEmpleados(Model attributes) {

		attributes.addAttribute("sectionTitle", "empleados");
		attributes.addAttribute("paginator", 
			empleadoManager.findAll( PageRequest.of(0, 5) ));
		
		return "empleado";
		
	}

	@GetMapping("/empleado/{id}")
	@Transactional
	public String editEmpleado(@PathVariable Integer id, Model attributes) {

		Optional<Empleado> employee = empleadoManager.findById(id);
		
		if(!employee.isPresent()) { 
			return "redirect:/empleados"; }
		
		attributes.addAttribute("sectionTitle", "Editar empleado");
		attributes.addAttribute("account", employee.get());
		
		return "editarEmpleado";
		
	}

	@GetMapping("/empleado")
	@Transactional
	public ResponseEntity<?> getEmpleados(
		@RequestParam(defaultValue = "0") int pageNumber) {

		Pageable page = PageRequest.of(pageNumber, 5);

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(empleadoManager.findAll(page));
		
	}

	@PostMapping("/empleado")
	@Transactional
	public ResponseEntity<?> addEmployee(
		@Valid @RequestBody Empleado empleado, BindingResult bindObjt) {
		
		if (bindObjt.hasErrors() ) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.contentType(MediaType.APPLICATION_JSON)
				.body(bindObjt.getAllErrors()); }
		
		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(accountsManager.createEmployeeAccount(empleado));
		
	}
	
	@PutMapping("/empleado/{id}")
	@Transactional
	public ResponseEntity<?> updateEmployee(
		@PathVariable int id, @Valid @RequestBody Empleado empleado, BindingResult bindObjt) {

		if(bindObjt.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.contentType(MediaType.APPLICATION_JSON)
				.body(bindObjt.getAllErrors()); }

		Empleado employee = empleadoManager.findById(id).get();

		employee.setTelefono(empleado.getTelefono());
		employee.getUsuario().setEstado(empleado.getUsuario().getEstado());
		employee.getUsuario().setCedula(empleado.getUsuario().getCedula());
		employee.getUsuario().setCorreo(empleado.getUsuario().getCorreo());
		employee.getUsuario().setNombres(empleado.getUsuario().getNombres());
		employee.getUsuario().setApellidos(empleado.getUsuario().getApellidos());
		employee.getUsuario().setDescripcion(empleado.getUsuario().getDescripcion());

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(empleadoManager.save(employee));
		
	}
	
	@DeleteMapping("/empleado/{id}")
	@Transactional
	public ResponseEntity<?> deleteEmployee(@PathVariable int id) {
		
		Empleado empleado = empleadoManager.findById(id).get();
		
		if( !empleado.getUsuario().getNombreImagenPerfil().equals("default") ) {
			fbmanager.deleteFile(
				empleado.getUsuario().getNombreImagenPerfil()); }
		
		empleadoManager.deleteById(id);

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body("{\"message\": \"Usuario eliminado correctamente\"}");
		
	}
	
}
