package com.zcbspay.platform.instead.batch.bean;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.zcbspay.platform.instead.common.bean.BaseBean;

/**
 * 批量代收应答bean
 * 
 * @author: zhangshd
 * @date: 2017年3月13日 下午1:17:46
 * @version :v1.0
 */
public class BatchCollectResBean extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 44998144207577926L;
	@Length(message="[商户代码]长度不符合规范",max = 15)
	@NotEmpty(message="[商户代码]不能为空")
	
	private String merId;// 商户代码
	@Length(message="[通知地址]长度不符合规范",max = 128)
	@NotEmpty(message="[通知地址]不能为空")
	private String backUrl;// 通知地址
	@Length(message="[批次号]长度不符合规范",max = 32)
	@NotEmpty(message="[批次号]不能为空")
	
	private String batchNo;// 批次号
	@Length(message="[订单发送时间]长度不符合规范",max = 14)
	@NotEmpty(message="[订单发送时间]不能为空")
	
	private String txnTime;// 订单发送时间
	@NotEmpty(message="[总笔数]不能为空")
	
	private String totalQty;// 总笔数
	@NotEmpty(message="[总金额]不能为空")
	
	private String totalAmt;// 总金额
	private String reserved;// 保留域
	@Length(message="[响应码]长度不符合规范",max = 4)
	@NotEmpty(message="[响应码]不能为空")
	
	private String respCode;// 响应码
	@Length(message="[应答信息]长度不符合规范",max = 256)
	@NotEmpty(message="[应答信息]不能为空")
	
	private String respMsg;// 应答信息

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getTxnTime() {
		return txnTime;
	}

	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}

	public String getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(String totalQty) {
		this.totalQty = totalQty;
	}

	public String getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(String totalAmt) {
		this.totalAmt = totalAmt;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
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

}
