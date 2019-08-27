package com.gcloud.api.security;

import com.gcloud.header.identity.user.GetUserReplyMsg;

public interface IApiIdentity {
	TokenUser checkToken(String token);
	SignUser getUserByAccessKey(String accessKey);
	GetUserReplyMsg getUserById(String userId);
}
