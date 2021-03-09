
package com.istb.app.controller.dashboard;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.istb.app.entity.Arrendatario;
import com.istb.app.repository.ArrendatarioRepositoryI;
import com.istb.app.repository.InmuebleRepositoryI;
import com.istb.app.services.dashboard.ArrendatarioService;
import com.istb.app.util.AccountUtils;
import com.istb.app.util.ControllerUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
public class ArrendatarioController {

	@Autowired
	private ArrendatarioService arrendatarioManager;

	@Autowired
	private InmuebleRepositoryI inmuebleRepository;

	@Autowired
	private ArrendatarioRepositoryI arrendatarioRepository;
	
	@GetMapping("/arrendatarios")
	@Transactional
	public String getArrendatarios(Model attributes, Authentication account) {

		attributes.addAttribute("sectionTitle", "arrendatarios");
		if(AccountUtils.hasRole(account, "ADMINISTRADOR")) {
		
			attributes.addAttribute("inmuebles", 
				inmuebleRepository.findByAlquiladoOrderByIdAsc(true));
			attributes.addAttribute("paginator", 
				arrendatarioRepository.findAll( PageRequest.of(0, 5) ));

		} else {
			
			attributes.addAttribute("inmuebles", inmuebleRepository
				.findByAlquiladoAndEmpleados_Usuario_UsuarioOrderByIdAsc(
					true, account.getName()
				) 
			);
			attributes.addAttribute("paginator", 
				arrendatarioRepository.findByEmpleado_Usuario_Usuario(
					account.getName(), PageRequest.of(0, 5) 
				)
			);
		
		}
		
		return "arrendatario";
		
	}

	@GetMapping("/arrendatario")
	@Transactional
	public ResponseEntity<?> obtenerArrendatarios(
		@RequestParam(defaultValue = "0") int pageNumber) {

		return ControllerUtils.getJSONOkResponse(
			arrendatarioRepository.findAll( PageRequest.of(pageNumber, 5) ));
		
	}
	
	@GetMapping("/arrendatario/{id}")
	@Transactional
	public String obtenerArrendatario(@PathVariable int id, Model attributes) {

		Optional<Arrendatario> optTenant = arrendatarioRepository.findById(id);

		if( optTenant.isEmpty() ) {
			return "redirect:/arrendatarios"; }
		
		attributes.addAttribute("account", optTenant.get());

		return "editarArrendatario";

	}

	@PostMapping("/arrendatario")
	@Transactional
	public ResponseEntity<?> nuevoArrendatario(
		@Valid @RequestBody Arrendatario tenant, BindingResult bindObjt) {
		
		if( bindObjt.hasErrors() ) { 
			return ControllerUtils.getJSONBindErrors(bindObjt); }

		return ControllerUtils.getJSONOkResponse(
			arrendatarioManager.agregarArrendatario(tenant).get("arrendatario") );
		
	}

	@PutMapping("/arrendatario")
	@Transactional
	public ResponseEntity<?> actualizarArrendatario(
		@Valid @RequestBody Arrendatario tenant, BindingResult bindObjt) {

		if( bindObjt.hasErrors() ) { 
			return ControllerUtils.getJSONBindErrors(bindObjt); }

		return ControllerUtils.getJSONOkResponse(
			arrendatarioManager.actualizarArrendatario(tenant).get("arrendatario") );

	}

	@DeleteMapping("/arrendatario/{id}")
	@Transactional
	public ResponseEntity<?> eliminarArrendatario(@PathVariable int id) {

		Map<String, Object> result = arrendatarioManager.eliminarArrendatario(id);

		if(result.containsKey("errors")) { 
			return ControllerUtils.getJSONErrors(
				(List<String>) result.get("errors") ); }
		
		return ControllerUtils.getJSONOkResponse(result);

	}

}
