package com.zcbspay.platform.instead.common.bean;

import java.io.Serializable;

public class MessageBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3528899407714889383L;
	/**
	 * 签名字符串
	 */
	private String sign;
	/**
	 * 附加域
	 */
	private String addit;
	/**
	 * 业务报文字符串
	 */
	private String data;
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getAddit() {
		return addit;
	}
	public void setAddit(String addit) {
		this.addit = addit;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
}
