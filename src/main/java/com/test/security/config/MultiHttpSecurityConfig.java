package com.test.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.test.security.rest.filter.RestAuthenticationEntryPoint;
import com.test.security.rest.filter.RestAuthenticationProvider;
import com.test.security.rest.filter.RestSecurityFilter;

@Configuration
@EnableWebSecurity

public class MultiHttpSecurityConfig {
   
   @Configuration
    @Order(1)                                                        
    public static class RestSecurityConfig extends WebSecurityConfigurerAdapter {

    	@Bean
    	public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
    		RestAuthenticationEntryPoint entryPoint = new RestAuthenticationEntryPoint();
    		entryPoint.setRealmName("Home Union");
    		return entryPoint;
    	}

    	@Bean
    	public RestAuthenticationProvider restAuthenticationProvider() {
    		RestAuthenticationProvider authProvider = new RestAuthenticationProvider();
    		return authProvider;
    	}

    @Bean
    	public RestSecurityFilter restSecurityFilter() {

    		RestSecurityFilter filter = null;
    		try {
    			filter = new RestSecurityFilter(authenticationManagerBean());
    		} catch (Exception e) {
    			e.printStackTrace();
    		}

    		return filter;
    	}

    	@Override
    	protected void configure(HttpSecurity http) throws Exception {
    		http.csrf().disable();
    		http
    				.antMatcher("/api/**")    
    				.sessionManagement()
    				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
    				//.addFilterBefore(restSecurityFilter(), BasicAuthenticationFilter.class)
    				.exceptionHandling()
    				.authenticationEntryPoint(restAuthenticationEntryPoint()).and()
    				.authorizeRequests()
    				.antMatchers("/api/**")
    				.authenticated().and().addFilterBefore(restSecurityFilter(), BasicAuthenticationFilter.class);
    		

    	}
    	
    	@Override
        protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
    		authManagerBuilder.authenticationProvider(restAuthenticationProvider());
        }
    }

    @Configuration
    //@Order(1)
    public static class HomeUnionSecurityConfig extends WebSecurityConfigurerAdapter {

    	@Autowired
    	@Qualifier("embraceDataSource")
    	DataSource dataSource;

    	@Autowired
    	private HomeUnionUserDetailsService customUserDetailsService;

    	@Autowired
    	CustomSecuritySuccessHandler customSecuritySuccessHandler;

    	@Autowired
    	CustomSecurityFailureHandler customSecurityFailureHandler;

    	@Autowired
    	private CommonPasswordEncoder homeUnionPasswordEncoder;
    	
    	@Autowired
    	private CustomAccessDeniedHandler customAccessDeniedHandler;
    	
    	@Autowired
    	private AjaxTimeoutRedirectFilter ajaxTimeoutRedirectFilter;
    	
    	@Override
    	public void configure(WebSecurity web) throws Exception {
    		web.ignoring().antMatchers("/resources/**");
    	}

    	protected void configure(HttpSecurity http) throws Exception {
    		http.csrf().disable();
    		http.authorizeRequests()
    				.antMatchers("/", "/login.html", "/app/**", "/assets/**", "/login","/failure","/register","/public/**", "/oauth/v1/**").permitAll().anyRequest().authenticated();
    		http.formLogin().loginPage("/login").failureUrl("/")
    				.successHandler(customSecuritySuccessHandler)
    				.failureHandler(customSecurityFailureHandler).permitAll().and()

    				.logout().logoutSuccessUrl("/login").permitAll().and()
    	            .rememberMe().and().exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);
    		http.addFilterAfter(ajaxTimeoutRedirectFilter, ExceptionTranslationFilter.class);
    		
    		return;
    	}

    	@Override
    	protected void configure(AuthenticationManagerBuilder authManagerBuilder)
    			throws Exception {
    		authManagerBuilder.userDetailsService(customUserDetailsService)
    				.passwordEncoder(homeUnionPasswordEncoder);
    	}
    	
    }

}