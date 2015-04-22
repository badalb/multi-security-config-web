package com.test.security.rest.filter;

import java.util.List;

public final class RestCredentials {

    private List<RestParameter> requestData;
    private String signature;
	
    public RestCredentials(List<RestParameter> requestData, String signature) {
		super();
		this.requestData = requestData;
		this.signature = signature;
	}

	public List<RestParameter> getRequestData() {
		return requestData;
	}

	public void setRequestData(List<RestParameter> requestData) {
		this.requestData = requestData;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
}