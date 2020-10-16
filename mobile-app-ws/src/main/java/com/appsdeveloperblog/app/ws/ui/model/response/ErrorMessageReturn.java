package com.appsdeveloperblog.app.ws.ui.model.response;

import java.util.Date;

public class ErrorMessageReturn {
	private Date date;
	private String errorMessage;

	public ErrorMessageReturn() {
	}

	public ErrorMessageReturn(Date date, String errorMessage) {
		this.date = date;
		this.errorMessage = errorMessage;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
