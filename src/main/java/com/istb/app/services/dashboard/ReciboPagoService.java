
package com.istb.app.services.dashboard;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import com.istb.app.entity.Arrendatario;
import com.istb.app.entity.ReciboPago;
import com.istb.app.repository.ArrendatarioRepositoryI;
import com.istb.app.repository.ReciboPagoRepositoryI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ReciboPagoService {

	@Autowired
	private ReciboPagoRepositoryI receiptRepository;

	@Autowired
	private ArrendatarioRepositoryI tenantRepository;

	@Transactional
	public Map<String, Object> addReceipt(ReciboPago receipt) {

		Authentication account = null;
		Optional<Arrendatario> storedTenant = null;
		Map<String, Object> data = new HashMap<>();

		account = SecurityContextHolder.getContext().getAuthentication();
		storedTenant = tenantRepository.findByUsuario_Usuario(account.getName());
		
		receipt.setArrendatario( storedTenant.get() );

		receiptRepository.save(receipt);
		data.put("recibo", receipt);

		return data;

	}

}
