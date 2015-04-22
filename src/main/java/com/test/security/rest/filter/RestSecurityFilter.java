package com.test.security.rest.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.GenericFilterBean;

import com.google.gson.Gson;

public class RestSecurityFilter extends GenericFilterBean {

    /*private static final Set<String> METHOD_HAS_CONTENT = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER) {
        private static final long serialVersionUID = 1L; 
        { add("PUT"); add("POST"); }
    }; */
    
    private AuthenticationManager authenticationManager;
    private AuthenticationEntryPoint authenticationEntryPoint;

    

    public RestSecurityFilter(AuthenticationManager authenticationManager) {
        this(authenticationManager, new RestAuthenticationEntryPoint());
        ((RestAuthenticationEntryPoint)this.authenticationEntryPoint).setRealmName("Secure Realm");
    }

    public RestSecurityFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationManager = authenticationManager;
        this.authenticationEntryPoint = authenticationEntryPoint;

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
    	
    	if(!((HttpServletRequest) req).getServletPath().contains("/api/v1")){
    		chain.doFilter(req, resp);
    	}else{
    	
        AuthenticationRequestWrapper request = new AuthenticationRequestWrapper((HttpServletRequest) req);
        HttpServletResponse response = (HttpServletResponse) resp;
        
        String credentials = request.getHeader("Authorization");

        if(!request.getServletPath().contains("/api/v1")){
            chain.doFilter(request, response);
            return;
        }

      
        String auth[] = credentials.split(":");

        RestRequest reqObject =  new Gson().fromJson(request.getPayload(), RestRequest.class);
        
      
       
        RestCredentials restCredential = new RestCredentials(reqObject.getReqParams(), auth[1]);
        Authentication authentication = new RestToken(auth[0], restCredential, null);

        try {
          
            Authentication successfulAuthentication = authenticationManager.authenticate(authentication);          
            SecurityContextHolder.getContext().setAuthentication(successfulAuthentication);
            chain.doFilter(request, response);
            
        } catch (AuthenticationException authenticationException) {
          
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, authenticationException);
        }
    }
    }
}