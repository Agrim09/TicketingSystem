package com.example.ticketing.Object;

public class HeaderRequest {
    
    private String requestId;
	private String requestDS;
	private String uName;

    public String getRequestId() {
        return requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    public String getRequestDS() {
        return requestDS;
    }
    public void setRequestDS(String requestDS) {
        this.requestDS = requestDS;
    }
    public String getuName() {
        return uName;
    }
    public void setuName(String uName) {
        this.uName = uName;
    }
    
}
