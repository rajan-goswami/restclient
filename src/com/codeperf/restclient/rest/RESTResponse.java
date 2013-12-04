package com.codeperf.restclient.rest;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

public class RESTResponse {

	private HttpResponse httpResponse;
	private int responseCode = -1;
	private String stringData;

	public RESTResponse(String strData, int respCode, HttpResponse response) {
		stringData = strData;
		responseCode = respCode;
		this.httpResponse = response;
	}

	@SuppressWarnings("unused")
	public int getResponseCode() {
		return responseCode;
	}

	@SuppressWarnings("unused")
	public Header getHeader(String headerName) {
		return httpResponse == null ? null : httpResponse
				.getFirstHeader(headerName);
	}

	@SuppressWarnings("unused")
	public Header[] getHeaders(String headerName) {
		return httpResponse == null ? null : httpResponse
				.getHeaders(headerName);
	}

	@SuppressWarnings("unused")
	public HttpResponse getHttpResponse() {
		return httpResponse;
	}

	public String getResponseBodyInString() {
		return stringData;
	}
}
