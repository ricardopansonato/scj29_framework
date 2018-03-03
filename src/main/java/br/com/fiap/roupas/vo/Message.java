package br.com.fiap.roupas.vo;


public class Message {

	private Long orderId;
	
	public Message() {
		
	}
	
	public Message(Long orderId) {
		super();
		this.orderId = orderId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
}
