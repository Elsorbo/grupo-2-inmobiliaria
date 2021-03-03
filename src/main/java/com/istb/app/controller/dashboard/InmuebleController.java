
package com.istb.app.controller.dashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.istb.app.entity.Fotos;
import com.istb.app.entity.Inmueble;
import com.istb.app.models.FileUpload;
import com.istb.app.repository.EmpleadoRepositoryI;
import com.istb.app.repository.FotosRepository;
import com.istb.app.repository.InmuebleRepositoryI;
import com.istb.app.repository.ServicioRepositoryI;
import com.istb.app.services.dashboard.InmuebleService;
import com.istb.app.services.firebase.FirebaseStrategyService;
import com.istb.app.util.AccountUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

@Controller
public class InmuebleController {

	@Autowired
	private FotosRepository fotosRepository;

	@Autowired
	private FirebaseStrategyService fbManager;
	
	@Autowired
	private InmuebleRepositoryI inmuebleRepository;

	@Autowired
	private EmpleadoRepositoryI empleadoRepository;

	@Autowired
	private ServicioRepositoryI servicioRepository;

	@Autowired
	private InmuebleService inmuebleManager;
	
	@GetMapping("/inmuebles")
	@Transactional
	public String getInmueble(Model attributes, Authentication account) {

		attributes.addAttribute("sectionTitle", "inmuebles");
		attributes.addAttribute("servicios", servicioRepository.findAll());

		if(AccountUtils.hasRole(account, "ADMINISTRADOR")) {
			attributes.addAttribute("empleados", empleadoRepository.findAll()); }
		
		attributes.addAttribute("paginator", 
			inmuebleRepository.findAll( PageRequest.of(0, 5) ));
		
		return "inmueble";
		
	}

	@GetMapping("/infoinmuebles")
	@Transactional
	public String inmueblesPage(Model attributes, 
		@RequestParam(defaultValue = "none") String zone) {

		if( !zone.equals("none") ) {
		
			attributes.addAttribute("inmuebles", 
				inmuebleRepository.findByLocalidad(zone));
				
			attributes.addAttribute("zona", zone);
		
		} else { 
			
			attributes.addAttribute("inmuebles", 
				inmuebleRepository.findAll(
					PageRequest.of(0, 3, Sort.by("id").descending())).getContent() );
				
			attributes.addAttribute("zona", "Ultimos inmuebles");
		
		}

		return "inmuebles";

	}
	
	@Transactional
	@GetMapping("/infoinmueble/{id}")
	public String inmueblesDetalle(@PathVariable int id, Model attributes) {

		Optional<Inmueble> inmueble = inmuebleRepository.findById(id);
		
		if(!inmueble.isPresent()) { 
			return "redirect:/infoinmuebles"; }
		
		attributes.addAttribute("inmueble", inmueble.get());
		attributes.addAttribute("servicios", 
			inmueble.get().getServicios().stream()
			.map( servicio -> servicio.getNombre() )
			.collect( Collectors.toList()) );

		return "detalleinmueble";

	}

	@GetMapping("/inmueble/{id}")
	@Transactional
	public String editarInmueble( 
		@PathVariable int id, Authentication account, Model attributes ) {

		Optional<Inmueble> inmueble = inmuebleRepository.findById(id);

		if( !inmueble.isPresent() ) { 
			return "redirect:/inmuebles"; }

			
		attributes.addAttribute("inmueble", inmueble.get());
		attributes.addAttribute("sectionTitle", "Editar inmueble");
		attributes.addAttribute("servicios", servicioRepository.findAll());
		if(AccountUtils.hasRole(account, "ADMINISTRADOR")) {
			attributes.addAttribute("empleados", empleadoRepository.findAll()); }
		
		attributes.addAttribute("locaciones", Arrays.asList(
			"Los Laureles", "Los Perales",
			"El Paraiso", "Cdla. El mamey",
			"El Salto", "Cdla. 4 de mayo",
			"Barreiro", "Av. Juan X marcos" )
		);

		return "editarinmueble";

	}

	@GetMapping("/inmueble")
	@Transactional
	public ResponseEntity<?> getInmueble(
		@RequestParam(defaultValue = "0") int pageNumber) {

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body( inmuebleRepository.findAll(PageRequest.of(pageNumber, 5)) );

	}
	
	@PostMapping("/inmueble")
	@Transactional
	public ResponseEntity<?> addInmueble(
		@Valid @RequestBody Inmueble inmueble, BindingResult bindObjt) {

		if(bindObjt.hasErrors()) {

			return ResponseEntity.badRequest()
				.contentType(MediaType.APPLICATION_JSON)
				.body(bindObjt.getAllErrors());

		}

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body( inmuebleManager.crearInmueble(inmueble).get("inmueble") );

	}

	@PostMapping("/inmueblepics")
	@Transactional
	public ResponseEntity<?> addInmueblePic(MultipartFile[] inmueblepics) 
		throws Exception {
			
		List<Fotos> fotos = new ArrayList<>();
		List<FileUpload> files = fbManager.uploadFiles(
			inmueblepics, "inmuebles");

		files.forEach((file) -> {

			Fotos foto = new Fotos();
			foto.setNombre_foto(file.getName());
			foto.setUrl_foto(file.getUrl());
			
			fotosRepository.save(foto);

			fotos.add(foto);

		});

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(fotos);

	}
	
	@PutMapping("/inmueble")
	@Transactional
	public ResponseEntity<?> updateInmueble(
		@Valid @RequestBody Inmueble inmueble, BindingResult bindObjt) {

		if(bindObjt.hasErrors()) {

			return ResponseEntity.badRequest()
				.contentType(MediaType.APPLICATION_JSON)
				.body( bindObjt.getAllErrors() );

		}

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body( inmuebleManager.actualizarInmueble(inmueble).get("inmueble") );

	}

	@DeleteMapping("/inmueble/{id}")
	@Transactional
	public ResponseEntity<?> deleteInmueble(@PathVariable int id) {

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body( inmuebleManager.eliminarInmueble(id) );

	}

}
