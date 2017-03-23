package com.zcbspay.platform.instead.realtime.bean;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.zcbspay.platform.instead.common.bean.BaseBean;

/**
 * 实时代付应答bean
 * @author: zhangshd
 * @date:   2017年3月13日 下午1:17:46   
 * @version :v1.0
 */
public class RealTimeQueryResBean extends BaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4154811889380667196L;
	@Length(max = 32,message="[受理订单号]长度不符合规范")
	@NotEmpty(message="[受理订单号]不能为空")
	private String 	tn	;//	受理订单号
	@Length(max = 4,message="[响应码]长度不符合规范")
	@NotEmpty(message="[响应码]不能为空")
	private String 	respCode	;//	响应码
	@Length(max = 256,message="[应答信息]长度不符合规范")
	@NotEmpty(message="[应答信息]不能为空")
	private String 	respMsg	;//	应答信息
	@Length(max = 2,message="[订单类型]长度不符合规范")
	@NotEmpty(message="[订单类型]不能为空")
	private String busiType;//订单类型01:代收 ,02:代付
	public String getBusiType() {
		return busiType;
	}

	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

	private String 	reserved	;//	保留域
	public String getTn() {
		return tn;
	}
	public void setTn(String tn) {
		this.tn = tn;
	}
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
	public String getReserved() {
		return reserved;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

}
