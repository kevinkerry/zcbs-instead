package com.zcbspay.platform.instead.common.bean;

import java.io.Serializable;



public class ValidateResultBean implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8907255757029910430L;
	private String errCode;
	private String errMsg;
	private boolean resultBool;
	private Object resultObj;
	
	public ValidateResultBean(){
		this.resultBool=false;
	}
	
	public ValidateResultBean(String errCode,String errMsg){
		this.resultBool=false;
		this.errCode=errCode;
		this.errMsg=errMsg;
	}
	
	public ValidateResultBean(Object resultObj){
		this.resultObj=resultObj;
		this.resultBool=true;
	}
	
	
	
	public void setSuccessResultDto(Object resultObj){
		this.resultObj=resultObj;
		this.resultBool=true;
	}
	
	public void setErrorResultDto(String errMsg){
		this.resultBool=false;
		this.errCode="9999";
		this.errMsg=errMsg;
	}
	
	public void setErrorResultDto(String errCode,String errMsg){
		this.resultBool=false;
		this.errCode=errCode;
		this.errMsg=errMsg;
	}
	
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public boolean isResultBool() {
		return resultBool;
	}
	public void setResultBool(boolean resultBool) {
		this.resultBool = resultBool;
	}

	public Object getResultObj() {
		return resultObj;
	}

	public void setResultObj(Object resultObj) {
		this.resultObj = resultObj;
	}
}
