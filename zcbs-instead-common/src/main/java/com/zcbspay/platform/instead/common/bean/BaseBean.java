package com.zcbspay.platform.instead.common.bean;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

// 响应基类
public class BaseBean implements Serializable{

	/**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7554538268683124107L;
    @Length(message = "[版本]长度不符合规范",max=6)
    @NotEmpty(message = "[版本]不能为空")
    
    protected String version; // 版本
    @Length(message = "[编码方式]长度不符合规范",max=2)
    @NotEmpty(message = "[编码方式]不能为空")
    
	protected String encoding; // 编码方式
    @Length(message = "[交易类型]长度不符合规范",max=2)
    @NotEmpty(message = "[交易类型]不能为空")
    
	private String txnType; // 交易类型
    @Length(message = "[交易子类]长度不符合规范",max=2)
    @NotEmpty(message = "[交易子类]不能为空")
    
	private String txnSubType; // 交易子类
    @Length(message = "[产品类型]长度不符合规范",max=6)
    @NotEmpty(message = "[产品类型]不能为空")
    
	private String bizType; // 产品类型

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getTxnSubType() {
		return txnSubType;
	}

	public void setTxnSubType(String txnSubType) {
		this.txnSubType = txnSubType;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
}
