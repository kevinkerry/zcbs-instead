package com.zcbspay.platform.instead.batch.bean;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.zcbspay.platform.instead.common.bean.BaseBean;

/**
 * 批量代付应答bean
 * 
 * @author: zhangshd
 * @date: 2017年3月13日 下午1:17:46
 * @version :v1.0
 */
public class BatchQueryResBean extends BaseBean {
	private static final long serialVersionUID = -2502602537806161588L;
	@Length(message = "[商户代码]长度不符合规范", max = 15)
	@NotEmpty(message = "[商户代码]不能为空")

	private String merId;// 商户代码
	@Length(message = "[批次号]长度不符合规范", max = 32)
	@NotEmpty(message = "[批次号]不能为空")

	private String batchNo;// 批次号
	@Length(message = "[订单发送时间]长度不符合规范", max = 14)
	@NotEmpty(message = "[订单发送时间]不能为空")

	private String txnTime;// 订单发送时间
	@Length(message = "[响应码]长度不符合规范", max = 4)
	@NotEmpty(message = "[响应码]不能为空")

	private String respCode;// 响应码
	
	
	@Length(message = "[原批次交易日期]长度不符合规范", max = 8)
	@NotEmpty(message = "[原批次交易日期]不能为空")
	private String txnDate;	//原批次交易日期;
	public String getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}

	@Length(message = "[应答信息]长度不符合规范", max = 256)
	@NotEmpty(message = "[应答信息]不能为空")
	private String respMsg;// 应答信息
	private String fileContent;// 文件内容
	private String totalQty;// 总笔数
	private String totalAmt;// 总金额
	private String waitTotalQty;// 处理中总笔数
	private String waitTotalAmt;// 处理中总金额
	private String succTotalQty;// 成功总笔数
	private String succTotalAmt;// 成功总金额
	private String failTotalQty;// 失败总笔数
	private String failTotalAmt;// 失败总金额

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
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

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
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

	public String getWaitTotalQty() {
		return waitTotalQty;
	}

	public void setWaitTotalQty(String waitTotalQty) {
		this.waitTotalQty = waitTotalQty;
	}

	public String getWaitTotalAmt() {
		return waitTotalAmt;
	}

	public void setWaitTotalAmt(String waitTotalAmt) {
		this.waitTotalAmt = waitTotalAmt;
	}

	public String getSuccTotalQty() {
		return succTotalQty;
	}

	public void setSuccTotalQty(String succTotalQty) {
		this.succTotalQty = succTotalQty;
	}

	public String getSuccTotalAmt() {
		return succTotalAmt;
	}

	public void setSuccTotalAmt(String succTotalAmt) {
		this.succTotalAmt = succTotalAmt;
	}

	public String getFailTotalQty() {
		return failTotalQty;
	}

	public void setFailTotalQty(String failTotalQty) {
		this.failTotalQty = failTotalQty;
	}

	public String getFailTotalAmt() {
		return failTotalAmt;
	}

	public void setFailTotalAmt(String failTotalAmt) {
		this.failTotalAmt = failTotalAmt;
	}

}
