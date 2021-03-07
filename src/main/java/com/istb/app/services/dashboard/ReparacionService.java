
package com.istb.app.services.dashboard;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import com.istb.app.entity.Arrendatario;
import com.istb.app.entity.Reparacion;
import com.istb.app.entity.Usuario;
import com.istb.app.repository.ArrendatarioRepositoryI;
import com.istb.app.repository.ReparacionRepositoryI;
import com.istb.app.repository.UsuarioRepositoryI;
import com.istb.app.util.enums.EstadoReparacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReparacionService {

	@Autowired
	private UsuarioRepositoryI userRepository;

	@Autowired
	private ReparacionRepositoryI repairRepository;

	@Autowired
	private ArrendatarioRepositoryI arrendatarioRepository;

	@Transactional
	public Map<String, Object> addRepair(Reparacion repair) {

		Map<String, Object> data = new HashMap<>();
		Arrendatario storedTenant = arrendatarioRepository.findByUsuario_Usuario(
			repair.getArrendatario().getUsuario().getUsuario() ).get();
		
		repair.setArrendatario(storedTenant);
		repair.setEstado(EstadoReparacion.SOLICITADA);

		repairRepository.save(repair);		
		data.put("reparacion", repair);
		
		return data;

	}

	@Transactional
	public Map<String, Object> updateRepair(Reparacion repair) {

		Reparacion storedRepair = null;
		Map<String, Object> data = new HashMap<>();
		Optional<Reparacion> repairOpt = repairRepository.findById(repair.getId());
		
		if( repairOpt.isPresent() ) {
		
			storedRepair = repairOpt.get();
			storedRepair.setEstado( repair.getEstado() );
			
			repairRepository.save(storedRepair);
			data.put("reparacion", storedRepair);
		
		} else { 
			data.put("error", "No se pudo cambiar el estado de la petición"); }

		return data;

	}
	
	@Transactional
	public Map<String, Object> acceptRepair(int id) {

		Reparacion storedRepair = null;
		Map<String, Object> data = new HashMap<>();
		Optional<Reparacion> repairOpt = repairRepository.findById(id);
		
		if( repairOpt.isPresent() ) {
		
			storedRepair = repairOpt.get();
			storedRepair.setEstado( EstadoReparacion.ACEPTADA );
			
			repairRepository.save(storedRepair);
			data.put("reparacion", storedRepair);
		
		} else { 
			data.put("error", "No se pudo cambiar el estado de la petición"); }

		return data;

	}
	
}
