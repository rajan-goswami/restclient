package com.codeperf.restclient.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class RESTEngine {

	private static final String TAG = RESTEngine.class.getSimpleName();

	private static volatile RESTEngine engineInstance = new RESTEngine();

	private HttpParams httpParams = null;

	private static HttpClient httpClient = null;

	private RESTEngine() {
	}

	public static RESTEngine getInstance() {
		return engineInstance;
	}

	public void setHttpParams(HttpParams params) {
		httpParams = params;
	}

	public RESTResponse execute(HttpUriRequest httpRequest) {

		if (httpRequest == null)
			return null;

		if (httpClient == null)
			httpClient = new DefaultHttpClient(httpParams);

		RESTResponse respStructure = null;

		try {
			Log.d(TAG, "executeHttpRequest: Executing Http Request >>  "
					+ httpRequest.getRequestLine());
			HttpResponse httpResponse = httpClient.execute(httpRequest);

			if (httpResponse != null && httpResponse.getStatusLine() != null) {

				int statusCode = httpResponse.getStatusLine().getStatusCode();
				Log.d(TAG, "executeHttpRequest: Received Response : Code - "
						+ statusCode);

				switch (statusCode) {
				case HttpStatus.SC_ACCEPTED:
				case HttpStatus.SC_CREATED:
				case HttpStatus.SC_OK:

					StringBuffer sb = new StringBuffer();
					InputStream inputStream = httpResponse.getEntity()
							.getContent();
					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(inputStream,
									Charset.forName("utf-8")));
					String line = null;
					while ((line = buffer.readLine()) != null) {
						sb.append(line);
					}
					respStructure = new RESTResponse(sb.toString(), statusCode,
							httpResponse);

					Log.d(TAG,
							"======= Response body >> "
									+ respStructure.getResponseBodyInString()
									+ " =======");
					break;
				default:
					respStructure = new RESTResponse(null, statusCode, null);
					break;
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return respStructure;
	}

	public void abortRequest(HttpUriRequest request) {
		if (request != null)
			request.abort();
	}
}
