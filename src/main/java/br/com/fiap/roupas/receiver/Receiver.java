package br.com.fiap.roupas.receiver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import br.com.fiap.roupas.model.Pedido;
import br.com.fiap.roupas.model.PedidoItem;
import br.com.fiap.roupas.repository.PedidoRepository;
import br.com.fiap.roupas.vo.FileMessage;
import br.com.fiap.roupas.vo.Message;

@Component
public class Receiver {

	@Autowired
	private PedidoRepository repository;

	@Autowired
	private JmsTemplate jmsTemplate;
	
	@JmsListener(destination = "cupom", containerFactory = "myFactory")
    public void receiveMessage(Message message) throws DocumentException, NoSuchAlgorithmException, IOException {
    	Pedido pedido = repository.findById(message.getOrderId()).get();
		Document document = new Document();

		ClassPathResource path = new ClassPathResource("downloads/");
		File file = new File(path.getPath() + message.getOrderId() + ".pdf");
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();

		document.add(new Paragraph("Venus Turismo e CIA"));
		document.add(new Paragraph("Rua A"));
		document.add(new Paragraph("SAO PAULO/SP"));

		document.add(new Paragraph("CNPJ " + pedido.getCliente().getCnpj()));
		document.add(new Paragraph("----------------------------------------------"));
		document.add(new Paragraph(new SimpleDateFormat("dd/MM/yyyy").format(pedido.getCriacao())));

		document.add(new Paragraph("CUPOM FISCAL"));

		PedidoItem pedidoItem = pedido.getItems().get(0);

		document.add(new Paragraph(pedidoItem.getId() + "   -   " + pedidoItem.getProduto().getNome() + "   -   "
				+ pedidoItem.getQuantidade() + "  -  "
				+ (pedidoItem.getQuantidade() * pedidoItem.getProduto().getValor())));
		document.add(new Paragraph("Total: " + (pedidoItem.getQuantidade() * pedidoItem.getProduto().getValor())));
		
		final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.reset();
		messageDigest.update(message.getOrderId().toString().getBytes(Charset.forName("UTF8")));
		final byte[] resultByte = messageDigest.digest();
		final String result = new String(new String(resultByte));
		
		document.add(new Paragraph("Hash: " + result));

		document.close();

		FileMessage fileMessage = new FileMessage(encodeFileToBase64Binary(file));
        jmsTemplate.convertAndSend("file", fileMessage);
    }
	
	private static String encodeFileToBase64Binary(File file) throws IOException {
	    byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
	    return new String(encoded, StandardCharsets.US_ASCII);
	}

}