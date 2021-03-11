
package com.istb.app.controller.dashboard;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.istb.app.entity.ReciboPago;
import com.istb.app.models.FileUpload;
import com.istb.app.repository.ReciboPagoRepositoryI;
import com.istb.app.services.dashboard.ReciboPagoService;
import com.istb.app.services.firebase.FirebaseStrategyService;
import com.istb.app.util.ControllerUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class ReciboPagoController {

	@Autowired
	public ReciboPagoService receiptMananger;

	@Autowired
	private FirebaseStrategyService fbManager;
	
	@Autowired
	private ReciboPagoRepositoryI receiptRepository;

	private List<String> meses = Arrays.asList( 
		"enero", "febrero", "marzo",
		"abril", "mayo", "junio",
		"julio", "agosto", "septiembre",
		"octubre", "noviembre", "diciembre");

	@GetMapping("/recibos")
	@Transactional
	public String getRecibosPago(Model attributes, Authentication account) {

		attributes.addAttribute("sectionTitle", "recibos");
		attributes.addAttribute("recibos", 
			receiptRepository.findByArrendatario_Usuario_Usuario(account.getName()));
		attributes.addAttribute("meses", meses);
		
		return "recibo";
		
	}
	
	@GetMapping("/recibo")
	@Transactional
	public ResponseEntity<?> getReceipts(
		@RequestParam(name = "tenantid", defaultValue = "0") int arrendatarioId) {

		if( arrendatarioId > 0 ) {
			return ControllerUtils.getJSONOkResponse(
				receiptRepository
					.findByFacturadoAndArrendatario_IdOrderByFechaCreacionDesc(
						false, arrendatarioId)); }
		else { 
			return ControllerUtils.getJSONOkResponse( 
				receiptRepository.findAll(
					PageRequest.of(0, 6, Sort.by("fechaCreacion"))) ); }
	
	}

	@PostMapping(value="/recibo")
	public ResponseEntity<?> addReceipt(
		@Valid @RequestBody ReciboPago receipt, BindingResult bindObjt) {
		
		if(bindObjt.hasErrors()) { 
			ControllerUtils.getJSONBindErrors(bindObjt); }
		
		return ControllerUtils.getJSONOkResponse(
			receiptMananger.addReceipt(receipt).get("recibo") );

	}

	@PostMapping("/receiptpics")
	@Transactional
	public ResponseEntity<?> addRepairPic(MultipartFile[] receiptpic) 
		throws Exception {
			
		List<FileUpload> files = fbManager.uploadFiles(receiptpic, "recibos");
		
		return ControllerUtils.getJSONOkResponse(files.get(0));

	}
	
}
