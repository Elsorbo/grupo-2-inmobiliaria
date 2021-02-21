
package com.istb.app.services.dashboard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.istb.app.entity.Empleado;
import com.istb.app.entity.Inmueble;
import com.istb.app.repository.EmpleadoRepositoryI;
import com.istb.app.repository.FotosRepository;
import com.istb.app.repository.InmuebleRepositoryI;
import com.istb.app.repository.ServicioRepositoryI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InmuebleService {
	
	@Autowired
	private FotosRepository fotosRepository;

	@Autowired
	private EmpleadoRepositoryI empleadoRepository;

	@Autowired
	private ServicioRepositoryI servicioRepository;

	@Autowired
	private InmuebleRepositoryI inmuebleRepository;


	@Transactional
	public Map<String, Object> crearInmueble(Inmueble inmueble) {

		int idEmpleado;
		List<Integer> idFotos, idServices;

		idEmpleado = ((List<Empleado>) inmueble.getEmpleados()).get(0).getId();
		
		idServices = inmueble.getServicios().stream()
			.map( service -> service.getId() )
			.collect(Collectors.toList());
		
		idFotos = inmueble.getFotos().stream()
			.map( foto -> foto.getId() )
			.collect(Collectors.toList());

		
		inmueble.setFotos( fotosRepository.findAllById(idFotos) );
		inmueble.setServicios( servicioRepository.findAllById(idServices) );
		inmueble.setEmpleados(
			Arrays.asList( empleadoRepository.findById(idEmpleado).get() ));
		
		inmuebleRepository.save(inmueble);
		
		inmueble.getServicios().forEach( servicio -> {

			servicio.getInmuebles().add(inmueble);
			servicioRepository.save(servicio);
			
		});
		
		inmueble.getEmpleados().forEach(empleado -> { 
			
			empleado.getInmuebles().add(inmueble);
			empleadoRepository.save(empleado);
		
		});
		
		inmueble.getFotos().forEach( foto -> {

			foto.setInmueble(inmueble);
			fotosRepository.save(foto);

		});

		return new HashMap<>();

	}

}
