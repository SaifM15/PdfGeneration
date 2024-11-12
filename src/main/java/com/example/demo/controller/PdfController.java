package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.pojo.Invoice;
import com.example.demo.service.PdfService;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

	@Autowired
	@Qualifier("pdfService")
	private PdfService pdfService;

 @PostMapping(value = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE)
 public ResponseEntity<byte[]> generatePdf(@RequestBody Invoice invoice) throws IOException {
	 System.out.println(invoice.getSeller());
     ByteArrayInputStream pdfStream = pdfService.generatePdf(invoice);

     byte[] pdfBytes = pdfStream.readAllBytes();
     return ResponseEntity.ok()
    	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf")
    	        .contentType(MediaType.APPLICATION_PDF)
    	        .body(pdfBytes);

 }
}
