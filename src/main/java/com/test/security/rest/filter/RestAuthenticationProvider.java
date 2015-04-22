package com.test.security.rest.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class RestAuthenticationProvider implements AuthenticationProvider {

	/*@Autowired
	private UserService service;*/
	
	@Autowired
	private SubscriptionService subscriptionService;

	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		RestToken restToken = (RestToken) authentication;

		// api key (aka username)
		String apiKey = restToken.getPrincipal();
		// hashed blob
		RestCredentials credentials = restToken.getCredentials();

		// get secret access key from api key
		//String secret = service.loadSecretByUsername(apiKey);

		// if that username does not exist, throw exception
		//if (secret == null) {
		//	throw new BadCredentialsException("Invalid username or password.");
		//}

		// calculate the hmac of content with secret key
		//String hmac = calculateHMAC(secret, credentials.getRequestData());
		SubscriptionResponse res = subscriptionService.validate(apiKey, credentials.getSignature(), credentials.getRequestData());
		// check if signatures match
		if (!res.getStatusCode().equalsIgnoreCase(HttpStatus.OK.toString())) {
			throw new BadCredentialsException("Invalid username or password.");
		}
		
		return restToken;
	}

	public boolean supports(Class<?> authentication) {
		return RestToken.class.equals(authentication);
	}

}