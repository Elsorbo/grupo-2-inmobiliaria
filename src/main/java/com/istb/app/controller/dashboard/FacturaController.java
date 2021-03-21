
package com.istb.app.controller.dashboard;

import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.istb.app.entity.Factura;
import com.istb.app.entity.ReciboPago;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FacturaController {

	@Autowired
	private FacturaService billManager;
	
	@Autowired
	private FacturaRepositoryI billRepository;

	@Autowired
	private ReciboPagoRepositoryI receiptRepository;
	
	@GetMapping("/facturas")
	@Transactional
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

	@GetMapping("/factura")
	@Transactional
	public ResponseEntity<?> getFactura(
		Model attributes, Authentication account) {
		
		return ControllerUtils.getJSONOkResponse(
			billRepository.findAll(PageRequest.of(0, 6)) );
		
	}

	@PostMapping("/factura")
	@Transactional
	public ResponseEntity<?> addBilling(@RequestParam int reciboID,
		@Valid @RequestBody Factura bill, BindingResult bindObjt) {
		
		if( bindObjt.hasErrors() ) { 
			return ControllerUtils.getJSONBindErrors(bindObjt); }
		
		Map<String, Object> data = billManager.addFactura(bill);
		if( data.containsKey("factura") ) { 
			receiptRepository.findById(reciboID).get().setFacturado(true); }

		return ControllerUtils.getJSONOkResponse(data.get("factura"));
		
	}

	@GetMapping("/recibo/{id}")
	@Transactional
	public String generateFactura(
		@PathVariable(name = "id") int receiptId, Model attributes) {

		attributes.addAttribute("sectionTitle", "factura");
		Optional<ReciboPago> storedReceipt = receiptRepository.findById(receiptId);

		if( storedReceipt.isEmpty() ) { 
			return "redirect:/facturas"; }
		
		attributes.addAttribute("recibo", storedReceipt.get());

		return "generatebilling";

	}

	@GetMapping("/factura/{id}")
	public String getMethodName(@PathVariable int id, Model attributes) {
		
		Optional<Factura> bill = billRepository.findById(id);

		if(bill.isPresent()) {
			attributes.addAttribute("factura", bill.get()); }
		else {
			return "redirect:/lostbill"; }

		return "detallefactura";

	}
		
}
