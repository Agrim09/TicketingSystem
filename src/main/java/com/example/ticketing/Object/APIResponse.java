package com.example.ticketing.Object;

public class APIResponse<T> {

	private HeaderResponse header;
	private T payload;
    
	public HeaderResponse getHeader() {
		return header;
	}
	public void setHeader(HeaderResponse header) {
		this.header = header;
	}
	public T getPayload() {
		return payload;
	}
	public void setPayload(T payload) {
		this.payload = payload;
	}
	@Override
	public String toString() {
		return "APIResponse [header=" + header + ", payload=" + payload + "]";
	}
    
}
