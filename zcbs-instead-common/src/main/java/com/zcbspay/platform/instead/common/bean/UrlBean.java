package com.zcbspay.platform.instead.common.bean;

import java.io.Serializable;

// url地址
public class UrlBean implements Serializable{
	//实时代收代付地址
	// 实时代收
	private String singleCollectUrl;
	// 实时代付
	private String singlePayUrl;
	// 交易查询
	private String singleQueryCollectUrl;
	
	private String singleQueryPayUrl;
	
	//批量代收代付地址
	// 批量代收
	private String batchCollectUrl;
	// 批量代付
	private String batchPayUrl;
	// 交易查询
	private String batchQueryCollectUrl;
	
	private String batchQueryPayUrl;
	public String getBatchQueryCollectUrl() {
		return batchQueryCollectUrl;
	}
	public void setBatchQueryCollectUrl(String batchQueryCollectUrl) {
		this.batchQueryCollectUrl = batchQueryCollectUrl;
	}
	public String getBatchQueryPayUrl() {
		return batchQueryPayUrl;
	}
	public void setBatchQueryPayUrl(String batchQueryPayUrl) {
		this.batchQueryPayUrl = batchQueryPayUrl;
	}
	//批量导入
	private String batchImportUrl;
	//合同查询
	private String batchContractUrl;
	
	private String decodeUrl;
	
	private String encryptUrl;
	
	private String mkUrl;
	
	public String getMkUrl() {
		return mkUrl;
	}
	public void setMkUrl(String mkUrl) {
		this.mkUrl = mkUrl;
	}
	public String getDecodeUrl() {
		return decodeUrl;
	}
	public void setDecodeUrl(String decodeUrl) {
		this.decodeUrl = decodeUrl;
	}
	public String getEncryptUrl() {
		return encryptUrl;
	}
	public void setEncryptUrl(String encryptUrl) {
		this.encryptUrl = encryptUrl;
	}
	public String getBatchImportUrl() {
		return batchImportUrl;
	}
	public void setBatchImportUrl(String batchImportUrl) {
		this.batchImportUrl = batchImportUrl;
	}
	
	public String getSingleCollectUrl() {
		return singleCollectUrl;
	}
	public void setSingleCollectUrl(String singleCollectUrl) {
		this.singleCollectUrl = singleCollectUrl;
	}
	public String getSinglePayUrl() {
		return singlePayUrl;
	}
	public void setSinglePayUrl(String singlePayUrl) {
		this.singlePayUrl = singlePayUrl;
	}
	public String getBatchCollectUrl() {
		return batchCollectUrl;
	}
	public void setBatchCollectUrl(String batchCollectUrl) {
		this.batchCollectUrl = batchCollectUrl;
	}
	public String getBatchPayUrl() {
		return batchPayUrl;
	}
	public void setBatchPayUrl(String batchPayUrl) {
		this.batchPayUrl = batchPayUrl;
	}
	public String getBatchContractUrl() {
		return batchContractUrl;
	}
	public void setBatchContractUrl(String batchContractUrl) {
		this.batchContractUrl = batchContractUrl;
	}
	public String getSingleQueryCollectUrl() {
		return singleQueryCollectUrl;
	}
	public void setSingleQueryCollectUrl(String singleQueryCollectUrl) {
		this.singleQueryCollectUrl = singleQueryCollectUrl;
	}
	public String getSingleQueryPayUrl() {
		return singleQueryPayUrl;
	}
	public void setSingleQueryPayUrl(String singleQueryPayUrl) {
		this.singleQueryPayUrl = singleQueryPayUrl;
	}
}
