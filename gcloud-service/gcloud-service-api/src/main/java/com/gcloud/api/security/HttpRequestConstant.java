package com.gcloud.api.security;

public class HttpRequestConstant {
	public static final String HEADER_TOKEN_ID = "X-Auth-Token";
    public static final String HEADER_USER_ID = "X-Auth-User-Id";

    public static final String ATTR_LOGIN_USER = "X-Login-User";
    public static final String INVALIDATE_ERRORCODE = "identity_token_auth_0001::token is not validate"; //统一没有权限的错误码
    public static final String ATTR_TASK_ID = "X-Task-Id";
    public static final String ATTR_REQUEST_ID = "x-request-id";

    public static final String SESSION_USER_INFO = "user-info";
}
