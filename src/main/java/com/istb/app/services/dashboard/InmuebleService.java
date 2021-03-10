
package com.istb.app.services.dashboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.istb.app.entity.Empleado;
import com.istb.app.entity.Inmueble;
import com.istb.app.repository.ArrendatarioRepositoryI;
import com.istb.app.repository.EmpleadoRepositoryI;
import com.istb.app.repository.FotosRepositoryI;
import com.istb.app.repository.InmuebleRepositoryI;
import com.istb.app.repository.ServicioRepositoryI;
import com.istb.app.services.firebase.FirebaseStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class InmuebleService {

	@Autowired
	private FirebaseStrategy fbManager;
	
	@Autowired
	private FotosRepositoryI fotosRepository;

	@Autowired
	private EmpleadoRepositoryI empleadoRepository;

	@Autowired
	private ServicioRepositoryI servicioRepository;

	@Autowired
	private InmuebleRepositoryI inmuebleRepository;

	@Autowired
	private ArrendatarioRepositoryI arrendatarioRepository;

	@Transactional
	public Map<String, Object> crearInmueble(Inmueble inmueble) {
		
		int idEmpleado;
		Empleado empleado = null;
		Authentication account = null;
		List<Integer> idFotos, idServices;

		idEmpleado = inmueble.getEmpleados().iterator().next().getId();
		
		idServices = inmueble.getServicios().stream()
			.map( service -> service.getId() )
			.collect(Collectors.toList());
		
		idFotos = inmueble.getFotos().stream()
			.map( foto -> foto.getId() )
			.collect(Collectors.toList());

		if( idEmpleado < 0 ) {
		
			account = SecurityContextHolder.getContext().getAuthentication();
			empleado = empleadoRepository
				.findByUsuario_Usuario(account.getName());
		
		} else { 
			empleado = empleadoRepository.findById(idEmpleado).get(); }
		
		if( !inmueble.isComercializado() ) { 
			inmueble.setAlquilado(true); }
		
		inmueble.setFotos( fotosRepository.findAllById(idFotos) );
		inmueble.setServicios( servicioRepository.findAllById(idServices) );
		inmueble.getEmpleados().clear();
		inmueble.getEmpleados().add(empleado);
		
		inmuebleRepository.save(inmueble);
		
		inmueble.getServicios().forEach( servicio -> {

			servicio.getInmuebles().add(inmueble);
			servicioRepository.save(servicio);
			
		});
		
		inmueble.getEmpleados().forEach( emp -> { 
			
			emp.getInmuebles().add(inmueble);
			empleadoRepository.save(emp);
		
		});
		
		inmueble.getFotos().forEach( foto -> {

			foto.setInmueble(inmueble);
			fotosRepository.save(foto);

		});

		Map<String, Object> datos = new HashMap<>();
		datos.put("inmueble", inmueble);

		return datos;

	}

	@Transactional
	public Map<String, Object> actualizarInmueble(Inmueble inmueble) {

		Inmueble storedInmueble = null;
		Map<String, Object> response = new HashMap<>();
		Optional<Inmueble> optInmueble = inmuebleRepository.findById(inmueble.getId());

		if( optInmueble.isPresent() ) { 

			storedInmueble = optInmueble.get();

			storedInmueble.setArea( inmueble.getArea() );
			storedInmueble.setTitulo( inmueble.getTitulo() );
			storedInmueble.setPrecio( inmueble.getPrecio() );
			storedInmueble.setLocalidad( inmueble.getLocalidad() );
			storedInmueble.setDescripcion( inmueble.getDescripcion() );
			storedInmueble.setComercializado( inmueble.isComercializado() );
			storedInmueble.setAlquilado( !storedInmueble.isComercializado() );
			
			if( inmueble.getEmpleados() != null ) { 
				Empleado storedEmployee = empleadoRepository.findById(
					inmueble.getEmpleados().iterator().next().getId()).get();
				
				storedInmueble.getEmpleados().clear();
				storedInmueble.getEmpleados().add( storedEmployee );
				storedEmployee.getInmuebles().add(storedInmueble);
				empleadoRepository.save(storedEmployee);

			}

			inmuebleRepository.save(storedInmueble);
			
			response.put("inmueble", storedInmueble);

		}

		return response;

	}

	@Transactional
	public Map<String, Object> eliminarInmueble(int id) {

		Inmueble inmueble = null;
		Map<String, Object> response = new HashMap<>();
		Optional<Inmueble> storagedInmueble = inmuebleRepository.findById(id);

		if( storagedInmueble.isPresent() ) { 
		
			inmueble = storagedInmueble.get();
			inmueble.getFotos().forEach( foto -> {
				
				fbManager.deleteFile(foto.getNombre_foto());
				fotosRepository.deleteById(foto.getId());

			});

			inmuebleRepository.deleteById(id);
			response.put("deleted", true);
		
		}

		return response;

	}

}
