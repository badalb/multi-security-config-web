package com.test.security.rest.filter;

import java.util.List;

public class RestRequest {

	private String accesskey; 
	
	private String hashMessage; 
	
	private List<RestParameter> reqParams;

	public String getAccesskey() {
		return accesskey;
	}

	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}

	public String getHashMessage() {
		return hashMessage;
	}

	public void setHashMessage(String hashMessage) {
		this.hashMessage = hashMessage;
	}

	public List<RestParameter> getReqParams() {
		return reqParams;
	}

	public void setReqParams(List<RestParameter> reqParams) {
		this.reqParams = reqParams;
	}
	
	
}