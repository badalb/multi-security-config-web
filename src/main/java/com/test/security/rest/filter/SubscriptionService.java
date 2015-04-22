package com.test.security.rest.filter;


import java.util.List;

public interface SubscriptionService {


	public SubscriptionResponse validate(String accessKey, String hashMessage,
			List<RestParameter> reqParams);

}
