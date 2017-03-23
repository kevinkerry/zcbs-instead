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
public class RealTimePayResBean extends BaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8470006648631226036L;
	@Length(max = 40,message="[商户全称]长度不符合规范")
	private String 	merName	;//	商户全称
	@Length(max = 40,message="[商户简称]长度不符合规范")
	private String 	merAbbr	;//	商户简称
	
	private String reserved;
	public String getReserved() {
		return reserved;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	@Length(max = 4,message="[响应码]长度不符合规范")
	@NotEmpty(message="[响应码]不能为空")
	private String 	respCode	;//	响应码
	@Length(max =256,message="[应答信息]长度不符合规范")
	@NotEmpty(message="[应答信息]不能为空")
	private String 	respMsg	;//	应答信息
	private String 	tn	;//	受理订单号
	public String getMerName() {
		return merName;
	}
	public void setMerName(String merName) {
		this.merName = merName;
	}
	public String getMerAbbr() {
		return merAbbr;
	}
	public void setMerAbbr(String merAbbr) {
		this.merAbbr = merAbbr;
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
	public String getTn() {
		return tn;
	}
	public void setTn(String tn) {
		this.tn = tn;
	}


}
