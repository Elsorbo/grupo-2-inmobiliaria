
package com.istb.app.services;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Map;

import com.istb.app.entity.Factura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Service
public class GeneratePDFService {
	
	@Autowired
    private TemplateEngine templateEngine;

    private final static String ROOT_URL = "https://inmobiliaria-mastarreno.herokuapp.com/";

    public ByteArrayOutputStream generateInvoicePDF(Factura invoice) throws Exception {

        Context ctx = new Context();
        ctx.setVariable("factura", invoice);

        ByteArrayOutputStream os = null;

        try {
           
            os = new ByteArrayOutputStream();
            ITextRenderer render = new ITextRenderer();
            String htmlTemplate = templateEngine.process("reports/invoice", ctx);

            render.setDocumentFromString(htmlTemplate, ROOT_URL);
            render.layout();
            render.createPDF(os, false);
            render.finishPDF();

        } catch (Exception e) {
            e.printStackTrace(); }
        

        return os;
        
    }

}
