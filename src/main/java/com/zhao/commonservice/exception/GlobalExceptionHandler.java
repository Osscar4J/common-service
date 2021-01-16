package com.zhao.commonservice.exception;


import com.zhao.common.exception.BusinessException;
import com.zhao.common.respvo.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private HttpServletRequest request;

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public BaseResponse<?> jsonErrorHandler(Exception e){
		BaseResponse<?> resp = BaseResponse.getError();
		resp.setMsg(e.getMessage());
		if (e instanceof BusinessException) {
			resp.setCode(((BusinessException)e).getCode());
		} else {
			e.printStackTrace();
			resp.setCode(500);
		}
		logger.error("==================== Exception ====================");
		logger.error("request URI: {}", request.getRequestURI());
		logger.error("error: {}", e.getLocalizedMessage());
		logger.error("================== Exception End ==================");
		return resp;
	}
}
