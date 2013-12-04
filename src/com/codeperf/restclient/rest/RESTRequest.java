package com.codeperf.restclient.rest;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

public class RESTRequest {

	protected String body = null;
	protected String url = null;
	protected List<NameValuePair> params;
	protected List<Header> headers;

	protected int uniqueId;
	protected RESTAuthentication authentication;

	public RESTRequest() {
		params = new ArrayList<NameValuePair>();
		headers = new ArrayList<Header>();
	}

	public void addReqeustParam(String key, String value) {
		params.add(new BasicNameValuePair(key, value));
	}

	public void addRequestHeader(String key, String value) {
		headers.add(new BasicHeader(key, value));
	}

	public void setUri(String url) {
		this.url = url;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public HttpUriRequest constructHttpRequest(MethodType type) {

		if (url == null) {
			// Wrong
			return null;
		}
		HttpUriRequest request = null;

		String urlString = url;

		// Add uri params in request
		if (!params.isEmpty()) {
			if (!urlString.endsWith("?"))
				urlString += "?";
			urlString += "?";
			String paramString = URLEncodedUtils.format(params, "UTF-8");
			urlString += paramString;
		}

		// Form actual request and set entity(body) if required
		switch (type) {
		case GET:
			request = new HttpGet(urlString);
			break;
		case POST:
			request = new HttpPost(urlString);
			if (this.body != null && !this.body.isEmpty()) {
				try {
					((HttpPost) request).setEntity(new StringEntity(this.body,
							"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
			break;
		case PUT:
			request = new HttpPut(urlString);
			if (this.body != null && !this.body.isEmpty()) {
				try {
					((HttpPut) request).setEntity(new StringEntity(this.body,
							"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
			break;
		case DELETE:
			request = new HttpDelete(urlString);
			break;
		case HEAD:
			request = new HttpHead(urlString);
			break;
		case TRACE:
			request = new HttpTrace(urlString);
			break;
		case OPTIONS:
			request = new HttpOptions(urlString);
			break;
		default:
			return request;
		}

		// Add headers in request
		for (Header header : headers) {
			request.addHeader(header);
		}

		return request;
	}

}
