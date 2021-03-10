
package com.istb.app.controller.dashboard;

import com.istb.app.entity.Factura;
import com.istb.app.repository.ArrendatarioRepositoryI;
import com.istb.app.repository.FacturaRepositoryI;
import com.istb.app.repository.ReciboPagoRepositoryI;
import com.istb.app.services.dashboard.FacturaService;
import com.istb.app.util.AccountUtils;
import com.istb.app.util.ControllerUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class FacturaController {

	@Autowired
	private FacturaService billManager;
	
	@Autowired
	private FacturaRepositoryI billRepository;
	
	@GetMapping("/facturas")
	public String getFacturas(Model attributes, Authentication account) {

		attributes.addAttribute("sectionTitle", "facturas");
		
		if( !AccountUtils.hasRole(account, "ARRENDATARIO") ) {
			
			if( AccountUtils.hasRole(account, "ADMINISTRADOR")) {
				billManager.setAdminAttributes(attributes); } 
			else { 
				billManager.setEmployeeAttributes(account.getName(), attributes); }
			
		} else { 
			billManager.setTenantAttributes(account.getName(), attributes); }
		
		return "factura";
		
	}

	@GetMapping(value="/factura")
	public ResponseEntity<?> getFactura(
		Model attributes, Authentication account) {
		
		return ControllerUtils.getJSONOkResponse(
			billRepository.findAll(PageRequest.of(0, 6)) );
		
	}

	@PostMapping(value="/factura")
	public ResponseEntity<?> addBilling(@RequestBody Factura bill) {
		
		return ControllerUtils.getJSONOkResponse(billManager.addFactura(bill));

	}
	
}
