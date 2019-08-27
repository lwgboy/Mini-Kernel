package com.gcloud.common.util;

import org.apache.http.client.methods.HttpPut;

public class HttpPatch extends HttpPut {
	public HttpPatch(String url) {
		super(url);
	}

	@Override
	public String getMethod() {
		return "PATCH";
	}
}
