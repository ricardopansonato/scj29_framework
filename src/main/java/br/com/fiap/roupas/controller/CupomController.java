package br.com.fiap.roupas.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;

import br.com.fiap.roupas.repository.PedidoRepository;
import br.com.fiap.roupas.vo.Message;

@RestController
public class CupomController {

	@Autowired
	private PedidoRepository repository;
	
	@Autowired
	private JmsTemplate jmsTemplate;

	@RequestMapping(value = "/generateCupom", method = RequestMethod.GET)
	public String generateCupom(@RequestParam(name = "orderId", required = false) Long orderId) {
		if (orderId == null) {
			List<Message> collect = repository.findAll().stream().map(p -> new Message(p.getId())).collect(Collectors.toList());
			for (Message message: collect) {
				jmsTemplate.convertAndSend("cupom", message);
			}
			
			return "OK";
		}
		
		final Message message = new Message(orderId);
        jmsTemplate.convertAndSend("cupom", message);
        return "OK";
	}
	
	@RequestMapping(value = "/pdf/{orderId}", method = RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity<InputStreamResource> gerarCupom(@PathVariable(name = "orderId") Long orderId)
			throws FileNotFoundException, DocumentException, IOException {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/pdf"));
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Content-Disposition", "filename=" + orderId + ".pdf");
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		
		ClassPathResource path = new ClassPathResource("downloads/");
		File file = new File(path.getPath() + orderId + ".pdf");
		
		if (!file.exists()) {
			return ResponseEntity.notFound().build();
		}
		
		InputStream stream = new FileInputStream(file);
		InputStreamResource inputStreamResource = new InputStreamResource(stream);

		headers.setContentLength(file.length());
		ResponseEntity<InputStreamResource> response = new ResponseEntity<InputStreamResource>(inputStreamResource,
				headers, HttpStatus.OK);
		return response;
	}

}
