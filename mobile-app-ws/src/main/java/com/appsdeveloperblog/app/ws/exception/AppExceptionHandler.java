package com.appsdeveloperblog.app.ws.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessageReturn;

@ControllerAdvice
public class AppExceptionHandler {
	@ExceptionHandler(value= {UserServiceException.class})
	public ResponseEntity<Object> handleUserServiceException(UserServiceException ex,
			WebRequest wr){
		ErrorMessageReturn emr = new ErrorMessageReturn(new Date(),ex.getMessage());
		return new ResponseEntity<>(emr, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@ExceptionHandler(value= {Exception.class})
	public ResponseEntity<Object> handleAnyOtherException(Exception ex,
			WebRequest wr){
		ErrorMessageReturn emr = new ErrorMessageReturn(new Date(),ex.getMessage());
		return new ResponseEntity<>(emr, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	
}
