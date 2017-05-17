package com.zcbspay.platform.instead.common.bean;

import java.io.Serializable;

public class AdditBean implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4197123232264625963L;
	/**
	 * 	接入类型
	 */
	private String accessType;
	/**
	 * 	合作机构号
	 */
	private String coopInstiId;
	/**
	 * 	商户号
	 */
	private String merId;
	/**
	 * 	加密KEY
	 */
	private String encryKey;
	/**
	 * 	加密方法
	 */
	private String encryMethod;
	/**
	 * 	风控信息
	 */
	private String riskInfo;
	
	public String getAccessType() {
		return accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	public String getCoopInstiId() {
		return coopInstiId;
	}
	public void setCoopInstiId(String coopInstiId) {
		this.coopInstiId = coopInstiId;
	}
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public String getEncryKey() {
		return encryKey;
	}
	public void setEncryKey(String encryKey) {
		this.encryKey = encryKey;
	}
	public String getEncryMethod() {
		return encryMethod;
	}
	public void setEncryMethod(String encryMethod) {
		this.encryMethod = encryMethod;
	}
	public String getRiskInfo() {
		return riskInfo;
	}
	public void setRiskInfo(String riskInfo) {
		this.riskInfo = riskInfo;
	}
	
	
}
