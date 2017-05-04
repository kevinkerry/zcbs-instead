package com.zcbspay.platform.instead.batch.bean;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.zcbspay.platform.instead.common.bean.BaseBean;

/**
 * 合同查询请求bean
 * 
 * @author: zhangshd
 * @date: 2017年3月13日 下午1:17:46
 * @version :v1.0
 */
public class ContractQueryResBean extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3215136100881828539L;
	@Length(message="[委托机构代码]长度不符合规范", max = 15)
	@NotEmpty(message="[委托机构代码]不能为空")
	private String merId;// 商户代码
	@Length(message="[合同编号]长度不符合规范", max = 15)
	@NotEmpty(message="[合同编号]不能为空")
	private String contractnum;//合同编号
	private String contractContent ;
	public String getContractContent() {
		return contractContent;
	}

	public void setContractContent(String contractContent) {
		this.contractContent = contractContent;
	}

	public String getContractnum() {
		return contractnum;
	}

	public void setContractnum(String contractnum) {
		this.contractnum = contractnum;
	}

	private String respCode;
	private String respMsg;

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

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}
}
