package com.gcloud.header.identity.user;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.common.RegExp;

public class CreateUserMsg extends ApiMessage {
	@ApiModel(description = "用户名", require = true)
	@NotBlank(message = "2010101")
	@Pattern(regexp="^[0-9a-zA-Z][0-9a-zA-Z_\\@\\.]*$", message="2010102::请输入正确的用户名，4-30位，字母或数字开头")
	private String loginName;
	
	@ApiModel(description = "密码", require = true)
	@NotBlank(message = "2010103")
	@Pattern(regexp=RegExp.REGEX_PASSWORD_STRONG, message="2010104::请输入8-20位的密码，必含字母数字及特殊字符，且以字母开头")
	private String password;//登录用密码，需外部传输，加密不可逆
	
	@ApiModel(description = "性别", require = true)
	@NotNull(message = "2010105")
	private Boolean gender;//false男true女
	
	@ApiModel(description = "邮箱", require = true)
	@NotBlank(message = "2010106")
	@Email(message="2010107::请输入正确的邮箱地址")
	private String email;
	
	@ApiModel(description = "手机号码", require = true)
	@NotBlank(message = "2010108")
	@Pattern(regexp=RegExp.REGEX_MOBILE_PHONE, message="2010109::请输入正确的手机号码")
	private String mobile;
	
	@ApiModel(description = "角色", require = true)
	@NotBlank(message = "2010110")
	private String roleId;//超级管理员、普通管理员
	
	@ApiModel(description = "是否禁用", require = true)
	@NotNull(message = "2010111")
	private Boolean disable;//true禁用false可用
	
	@ApiModel(description = "真实姓名", require = true)
	@NotBlank(message = "2010112")
	@Length(min=2, max=20, message="2010113")
	private String realName;
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Boolean getDisable() {
		return disable;
	}

	public void setDisable(Boolean disable) {
		this.disable = disable;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}

}
