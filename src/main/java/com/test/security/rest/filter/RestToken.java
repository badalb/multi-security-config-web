package com.test.security.rest.filter;

import java.util.Date;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class RestToken extends UsernamePasswordAuthenticationToken {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8260824152927489851L;
	private Date timestamp;

    public RestToken(String principal, RestCredentials credentials, Date timestamp) {
        super(principal, credentials, null);
        this.timestamp = timestamp;
    }

    @Override
    public String getPrincipal() {
        return (String) super.getPrincipal();
    }
    
    @Override
    public RestCredentials getCredentials() {
        return (RestCredentials) super.getCredentials();
    }
    
    public Date getTimestamp() {
        return timestamp;
    }

}
