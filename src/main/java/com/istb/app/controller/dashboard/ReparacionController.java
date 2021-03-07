
package com.istb.app.controller.dashboard;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.istb.app.entity.Arrendatario;
import com.istb.app.entity.Reparacion;
import com.istb.app.entity.Usuario;
import com.istb.app.repository.ReparacionRepositoryI;
import com.istb.app.services.dashboard.ReparacionService;
import com.istb.app.util.AccountUtils;
import com.istb.app.util.ControllerUtils;
import com.istb.app.util.enums.EstadoReparacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ReparacionController {

	@Autowired
	private ReparacionService repairManager;

	@Autowired
	public ReparacionRepositoryI repairRepository;

	@GetMapping("/reparaciones")
	@Transactional
	public String getReparaciones(Model attributes, Authentication account) {

		attributes.addAttribute("sectionTitle", "reparaciones");
		
		if( !AccountUtils.hasRole(account, "ARRENDATARIO") ) {
			attributes.addAttribute("reparaciones", repairRepository
				.findByEstadoOrderByFechaCreacionDesc(EstadoReparacion.SOLICITADA)); }
		else { 
			attributes.addAttribute("reparaciones", repairRepository
				.findByArrendatario_Usuario_UsuarioOrderByFechaCreacionDesc(
					account.getName())); }
		
		return "reparacion";
		
	}

	@PostMapping("/reparacion")
	public ResponseEntity<?> addRepair(@Valid @RequestBody Reparacion repair, 
		BindingResult bindObjt, Authentication account) {
		
		if( bindObjt.hasErrors() ) { 
			return ControllerUtils.getJSONBindErrors(bindObjt); }
		
		repair.setArrendatario( new Arrendatario() );
		repair.getArrendatario().setUsuario( new Usuario() );
		repair.getArrendatario().getUsuario().setUsuario(account.getName());
		
		return ControllerUtils.getJSONOkResponse(repairManager.addRepair(repair));
		
	}

	@PutMapping(value="/reparacion/{id}")
	public ResponseEntity<?> acceptRepair(@PathVariable Integer id) {
		
		return ControllerUtils.getJSONOkResponse( 
			repairManager.acceptRepair(id) );
		
	}

	@GetMapping("/reparacion")
	public ResponseEntity<?> getRepair() {

		return ControllerUtils.getJSONOkResponse( repairRepository.findAll(PageRequest.of(0, 5)) );

	}

}
