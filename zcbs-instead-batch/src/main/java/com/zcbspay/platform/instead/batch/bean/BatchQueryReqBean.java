package com.zcbspay.platform.instead.batch.bean;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.zcbspay.platform.instead.common.bean.BaseBean;
import com.zcbspay.platform.instead.common.utils.ValidateLocator;

/**
 * 批量代付应答bean
 * 
 * @author: zhangshd
 * @date: 2017年3月13日 下午1:17:46
 * @version :v1.0
 */
public class BatchQueryReqBean extends BaseBean {
	public static void main(String[] args) {
		BatchQueryReqBean batchQueryReqBean = new BatchQueryReqBean();
		ValidateLocator.validateBeans(batchQueryReqBean);
	}
	private static final long serialVersionUID = 5332858638346984082L;
	@Length(message="[商户代码]长度不符合规范", max = 15)
	@NotEmpty(message="[商户代码]不能为空")
	private String merId;// 商户代码
	@Length(message="[批次号]长度不符合规范", max = 32)
	@NotEmpty(message="[批次号]不能为空")
	private String batchNo;// 批次号
	@Length(message="[订单发送时间]长度不符合规范", max = 14)
	@NotEmpty(message="[订单发送时间]不能为空")
	private String txnTime;// 订单发送时间
	@Length(max = 2,message="[订单类型]长度不符合规范")
	@NotEmpty(message="[订单类型]不能为空")
	private String busiType;//订单类型01:代收 ,02:代付
	@Length(message = "[原批次交易日期]长度不符合规范", max = 8)
	@NotEmpty(message = "[原批次交易日期]不能为空")
	private String txnDate;	//原批次交易日期;
	public String getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}
	public String getBusiType() {
		return busiType;
	}

	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

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

}
