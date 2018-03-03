package br.com.fiap.roupas.receiver;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.itextpdf.text.DocumentException;

import br.com.fiap.roupas.vo.FileMessage;

@Component
public class FileReceiver {
	
	@JmsListener(destination = "file", containerFactory = "myFactory")
    public void receiveMessage(FileMessage message) throws FileNotFoundException, DocumentException, NoSuchAlgorithmException {
		System.out.println(message.getFile());
	}

}