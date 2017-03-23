package com.zcbspay.platform.instead.batch.bean;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class BatchQueryFileContent extends BatchCollectAndPayFileContent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6217480616899147129L;
	@Length(message="[响应码]长度不符合规范", max = 4)
	@NotEmpty(message="[响应码]不能为空")
	private String respCode;//	响应码
	@Length(message="[应答信息]长度不符合规范", max = 256)
	@NotEmpty(message="[应答信息]不能为空")
	private String respMsg;//	应答信息

	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespMsg() {
		return respMsg;
	}
	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}
}
