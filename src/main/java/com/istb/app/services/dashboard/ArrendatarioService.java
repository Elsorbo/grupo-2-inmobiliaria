
package com.istb.app.services.dashboard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import com.istb.app.entity.Arrendatario;
import com.istb.app.entity.Inmueble;
import com.istb.app.entity.Usuario;
import com.istb.app.repository.ArrendatarioRepositoryI;
import com.istb.app.repository.InmuebleRepositoryI;
import com.istb.app.repository.UsuarioRepositoryI;
import com.istb.app.services.accounts.AccountsServiceI;
import com.istb.app.services.firebase.FirebaseStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

/**
 * ArrendatarioService
 */
@Service
public class ArrendatarioService {

	@Autowired
	private FirebaseStrategy fbmanager;

	@Autowired
	private AccountsServiceI accountManager;

	@Autowired
	private UsuarioRepositoryI usuarioRepository;
	
	@Autowired
	private InmuebleRepositoryI inmuebleRepository;

	@Autowired
	private ArrendatarioRepositoryI arrendatarioRepository;

	@Transactional
	public Map<String, Object> agregarArrendatario(Arrendatario tenant) {

		Inmueble inmueble = inmuebleRepository.findById(
			tenant.getInmuebles().iterator().next().getId()).get();
		Map<String, Object> data = new HashMap<>();
		
		data.put("arrendatario", accountManager.createTenantAccount(tenant));

		tenant.getInmuebles().add(inmueble);
		tenant.setEmpleado(inmueble.getEmpleados().iterator().next());
		arrendatarioRepository.save(tenant);
		
		inmueble.setAlquilado(false);
		inmuebleRepository.save(inmueble);
		
		return data;
		
	}
	
	@Transactional
	public Map<String, Object> actualizarArrendatario(Arrendatario tenant) {

		Usuario storedUser = null;
		Map<String, Object> data = new HashMap<>();
		Optional<Arrendatario> optTenant = arrendatarioRepository
			.findById( tenant.getId() );

		if( optTenant.isEmpty() ) { 
		
			data.put("errors", 
				Arrays.asList("No se ha encontrado al arrendatario")); } 
		else {
		
			storedUser = optTenant.get().getUsuario();
			storedUser.setEstado(tenant.getUsuario().getEstado());
			storedUser.setCedula(tenant.getUsuario().getCedula());
			storedUser.setCorreo(tenant.getUsuario().getCorreo());
			storedUser.setNombres(tenant.getUsuario().getNombres());
			storedUser.setApellidos(tenant.getUsuario().getApellidos());
			storedUser.setDescripcion(tenant.getUsuario().getDescripcion());
			
			if( tenant.getUsuario().getContrasena() != null ) {
				storedUser.setContrasena( PasswordEncoderFactories
					.createDelegatingPasswordEncoder()
					.encode(tenant.getUsuario().getContrasena()) ); }

			usuarioRepository.save(storedUser);
			data.put("arrendatario", optTenant.get());
			
		}

		return data;

	}

	@Transactional
	public Map<String, Object> eliminarArrendatario(int id) {

		Usuario storedUser = null;
		Arrendatario storedTenant = null;
		Map<String, Object> data = new HashMap<>();
		Optional<Arrendatario> optTenant = arrendatarioRepository.findById(id);

		if( optTenant.isEmpty() ) { 
			data.put("errors", 
				Arrays.asList("No se ha encontrado al arrendatario que desea eliminar")); }
		else { 

			storedTenant = optTenant.get();
			storedUser = storedTenant.getUsuario();

			if( !storedUser.getNombreImagenPerfil().equals("default") ) {
				fbmanager.deleteFile( storedUser.getNombreImagenPerfil() ); }
			
			storedTenant.getInmuebles().forEach( (i) -> {
				
				i.setAlquilado(true);
				inmuebleRepository.save(i);
			
			});
			arrendatarioRepository.delete(storedTenant);
			usuarioRepository.delete(storedUser);
		
		}
		
		return data;

	}
	
}
