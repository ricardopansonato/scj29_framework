package br.com.fiap.roupas.vo;

import java.io.Serializable;

public class FileMessage implements Serializable {
	
	private static final long serialVersionUID = 5317760058784367314L;
	
	private String file;

	public FileMessage() {
		super();
	}

	public FileMessage(String file) {
		super();
		this.file = file;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}
