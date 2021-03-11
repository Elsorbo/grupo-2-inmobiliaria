
package com.istb.app.services.dashboard;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import com.istb.app.entity.Arrendatario;
import com.istb.app.entity.Factura;
import com.istb.app.repository.ArrendatarioRepositoryI;
import com.istb.app.repository.DetalleFacturaRepositoryI;
import com.istb.app.repository.FacturaRepositoryI;
import com.istb.app.repository.ReciboPagoRepositoryI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class FacturaService {	

	@Autowired
	private FacturaRepositoryI billRepository;
		
	@Autowired
	private FacturaRepositoryI facturaRepository;
	@Autowired
	private ReciboPagoRepositoryI reciboRepository;
	
	@Autowired
	private DetalleFacturaRepositoryI detalleRepository;

	@Autowired
	private ArrendatarioRepositoryI arrendatarioRepository;
	
	@Transactional
	public Map<String, Object> addFactura(Factura bill) {
		
		Map<String, Object> data = new HashMap<>();
		
		Arrendatario storedTenant = arrendatarioRepository.findById(
			bill.getArrendatario().getId()).get();
		
		bill.setArrendatario(storedTenant);
		bill.getDetalles().forEach( detail -> detalleRepository.save(detail) );
		
		facturaRepository.save(bill);
		bill.getDetalles().forEach( (storedDetail) -> {
			
			storedDetail.setFactura(bill);
			detalleRepository.save(storedDetail);

		});

		data.put("factura", bill);

		return data;
		
	}
	
	@Transactional
	public Model setAdminAttributes(Model adminAttributes) {

		adminAttributes.addAttribute("arrendatarios", 
			arrendatarioRepository.findAll()
		);
				
		adminAttributes.addAttribute("recibos", 
			reciboRepository.findAllByFacturadoOrderByFechaCreacionDesc(
				false, 
				PageRequest.of(0, 6)
			)
		);

		return adminAttributes;

	}
	
	@Transactional
	public Model setEmployeeAttributes(
		String employee, Model employeeAttributes) {

		employeeAttributes.addAttribute("arrendatarios", 
			arrendatarioRepository.findAllByEmpleado_Usuario_Usuario(
				employee
			)
		);

		employeeAttributes.addAttribute("recibos", 
			reciboRepository.findByFacturadoAndArrendatario_Empleado_Usuario_UsuarioOrderByFechaCreacionDesc(
				false, 
				employee, 
				PageRequest.of(0, 6)
			)
		);

		return employeeAttributes;

	}

	@Transactional
	public Model setTenantAttributes(String tenant, Model tenantAttributes) {

		tenantAttributes.addAttribute("facturas", 
			billRepository.findByArrendatario_Usuario_UsuarioOrderByFechaAdmisionDesc(
				tenant
			)
		); 

		return tenantAttributes;
		
	}
	
}
