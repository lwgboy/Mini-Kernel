package com.gcloud.common.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUtil {

	/**
	 * 验证签名是否正确
	 * 
	 * @param params
	 *            通知返回来的参数数组
	 * @return 验证结果
	 * @throws Exception
	 */
	public static boolean verify(String requestSign, String method, String url, Object body, Map<String, String> postParams, Map<String, String> headers, String accessKeySecret) throws Exception {
		String serverSign = buildRequestSign(method, url, body, postParams, headers, accessKeySecret);

		if (requestSign.equals(serverSign)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据反馈回来的信息，生成签名结果
	 * 
	 * @param Params
	 *            通知返回来的参数数组
	 * @param sign
	 *            比对的签名结果
	 * @return 生成的签名结果
	 * @throws Exception
	 */
	private static boolean getSignVerify(Map<String, String> Params, String sign, String key) throws Exception {
		// 过滤空值、sign与sign_type参数
		Map<String, String> sParaNew = SignCore.paraFilter(Params);
		// 获取待签名字符串
		String preSignStr = SignCore.createLinkString(sParaNew);
		// 获得签名验证结果
		return verify(preSignStr, sign, key, "utf-8");
	}

	/**
	 * 生成签名结果
	 * 
	 * @param sPara
	 *            要签名的数组
	 * @return 签名结果字符串
	 * @throws Exception
	 */
	public static String buildRequestSign(String method, String url, Object body, Map<String, String> params, Map<String, String> headers, String accessKeySecret) throws Exception {
		// 过滤空值、sign与sign_type参数
		Map<String, String> paramsNew = SignCore.paraFilter(params);
		String paramsStr = SignCore.createLinkString(paramsNew); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		
		Map<String, String> headersNew = SignCore.paraFilter(headers);
		String headersStr = SignCore.createLinkString(headersNew); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		
		String signContent = String.format("%s_%s_%s_%s_%s", method.toLowerCase(), url.toLowerCase(), paramsStr, headersStr, accessKeySecret);
		return sign(signContent, "utf-8");
	}

	/**
	 * 签名字符串
	 * 
	 * @param text
	 *            需要签名的字符串
	 * @param sign
	 *            签名结果
	 * @param key
	 *            密钥
	 * @param input_charset
	 *            编码格式
	 * @return 签名结果
	 * @throws Exception
	 */
	private static boolean verify(String text, String sign, String key, String input_charset) throws Exception {
		text = text + key;
		String mysign = MD5Util.encrypt(text);
		if (mysign.equals(sign)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 签名字符串
	 * 
	 * @param signContent
	 *            需要签名的字符串
	 * @param input_charset
	 *            编码格式
	 * @return 签名结果
	 * @throws Exception
	 */
	private static String sign(String signContent, String input_charset) throws Exception {
		return MD5Util.encrypt(signContent);
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> getAllParamAsString(Map<String, String[]> params) {
		Map<String, String> result = new HashMap<String, String>();
		if (params != null) {
			for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext();) {
				String name = iterator.next();
				String value = null;
	
				String[] values = params.get(name);
				if(values == null){
					continue;
				}

				if(values.length > 1){
					for (int i = 0; i < values.length; i++) {
						result.put(name + "[" + i + "]", values[i]);
					}
				}else if (values.length == 1 && !StringUtils.isEmpty(values[0])) {
					value = values[0];
					if (value == null) {
						continue;
					}
					result.put(name, value);
				}

			}
		}
		return result;
	}
	
	private static ParamNameType getPatternParamName(String name) {
		String regex = "((\\w|\\.)+)\\[\\d*\\]";
	
		String paramName = name;
		Boolean isArray = false;
	
		if (name.matches(regex)) {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(name);
			while (matcher.find()) {
				paramName = matcher.group(1);
				break;
			}
			isArray = true;
		}
		ParamNameType paramNameType = new ParamNameType();
		paramNameType.setParamName(paramName);
		paramNameType.setIsArray(isArray);
		return paramNameType;
	}
	
	private static class ParamNameType {
		private String paramName;
		private Boolean isArray;
	
		public String getParamName() {
			return paramName;
		}
	
		public void setParamName(String paramName) {
			this.paramName = paramName;
		}
	
		public Boolean getIsArray() {
			return isArray;
		}
	
		public void setIsArray(Boolean isArray) {
			this.isArray = isArray;
		}
	
	}
}
