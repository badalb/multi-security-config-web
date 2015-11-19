package com.test.security.rest.filter;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService{

	@Override
	public SubscriptionResponse validate(String accessKey, String hashMessage,
			List<RestParameter> reqParams) {

		SubscriptionResponse response = new SubscriptionResponse();
		response.setAccessKey(accessKey);
		UserEnckey userEnckey = restUserService.validateSubscriber(accessKey);
		if (userEnckey == null) {
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR + "");
			response.setMessage(messagesUtil
					.getMessage("rest.validate.accesskeyfail"));
			return response;
		}
		String reqParamString = RequestParamUtils
				.getStringFromRequestParams(reqParams);
		boolean validationFlag = restUserService.validateMessageHash(
				userEnckey.getHmacKey(), hashMessage, reqParamString);
		if (validationFlag == false) {
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR + "");
			response.setMessage(messagesUtil
					.getMessage("rest.validate.failure"));
			return response;
		}
		response.setStatusCode(HttpStatus.OK + "");
		response.setMessage(messagesUtil.getMessage("rest.validate.success"));
		return response;
	}

}
