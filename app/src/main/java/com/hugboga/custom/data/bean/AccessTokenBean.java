package com.hugboga.custom.data.bean;

import java.io.Serializable;

/**
 * 请求AccessToken返回的bean
 */
public class AccessTokenBean implements Serializable {

	public static final long serialVersionUID = 1L;

	public String access_token ;

	public int expires_in ;

	public String refresh_token ;

	public String openid ;

	public String scope ;

	@Override
	public String toString() {
		return "AccessTokenBean [access_token=" + access_token + ", expires_in="
				+ expires_in + ", refresh_token=" + refresh_token + ", openid="
				+ openid + ", scope=" + scope + "]";
	}

}