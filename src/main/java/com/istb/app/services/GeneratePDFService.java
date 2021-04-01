
package com.istb.app.services;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.istb.app.entity.Arrendatario;
import com.istb.app.entity.Empleado;
import com.istb.app.entity.Factura;
import com.istb.app.entity.Inmueble;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Service
public class GeneratePDFService {
	
	@Autowired
    private TemplateEngine templateEngine;

    private final static String ROOT_URL = "http://localhost:8000";
	private List<String> meses = Arrays.asList( 
		"enero", "febrero", "marzo",
		"abril", "mayo", "junio",
		"julio", "agosto", "septiembre",
		"octubre", "noviembre", "diciembre");

    public ByteArrayOutputStream generateInvoicePDF(Factura invoice) throws Exception {
		
		String template = "reports/invoice";
		Map<String, Object> ctxData = new HashMap<>();

		ctxData.put("factura", invoice);

        return generateReportFromTemplate(template, ctxData);
        
    }
    
	public ByteArrayOutputStream generateEmployeeListPDF(
		List<Empleado> entities) throws Exception {
		
		String template = "reports/listEmployees";
		Map<String, Object> ctxData = new HashMap<>();

		ctxData.put("employees", entities);

        return generateReportFromTemplate(template, ctxData);
        
    }

	public ByteArrayOutputStream generateTenantListPDF(
		List<Arrendatario> entities) throws Exception {
		
		String template = "reports/listTenants";
		Map<String, Object> ctxData = new HashMap<>();

		ctxData.put("tenants", entities);

        return generateReportFromTemplate(template, ctxData);
        
    }

	public ByteArrayOutputStream generatePropertieListPDF(
		List<Inmueble> entities) throws Exception {
		
		String template = "reports/listProperties";
		Map<String, Object> ctxData = new HashMap<>();

		ctxData.put("properties", entities);

        return generateReportFromTemplate(template, ctxData);
        
    }

	private ByteArrayOutputStream generateReportFromTemplate(
		String template, Map<String, Object> data) throws Exception {

		Context ctx = new Context();
		ctx.setVariable("fechaGeneracion", LocalDate.now());
		for(String key : data.keySet() ) {
			ctx.setVariable(key, data.get(key)); }

        ByteArrayOutputStream os = null;

        try {
           
            os = new ByteArrayOutputStream();
            ITextRenderer render = new ITextRenderer();
            String htmlTemplate = templateEngine.process(template, ctx);

            render.setDocumentFromString(htmlTemplate, ROOT_URL);
            render.layout();
            render.createPDF(os, false);
            render.finishPDF();

        } catch (Exception e) {
            e.printStackTrace(); }

		return os;

	}

}
