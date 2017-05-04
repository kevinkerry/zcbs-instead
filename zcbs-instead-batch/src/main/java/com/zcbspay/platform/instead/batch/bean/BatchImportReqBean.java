package com.zcbspay.platform.instead.batch.bean;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.zcbspay.platform.instead.common.bean.BaseBean;

/**
 * 批量导入请求bean
 * 
 * @author: zhangshd
 * @date: 2017年3月13日 下午1:17:46
 * @version :v1.0
 */
public class BatchImportReqBean extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3215136100881828539L;
	@Length(message="[商户代码]长度不符合规范", max = 15)
	@NotEmpty(message="[商户代码]不能为空")
	private String merId;// 商户代码
	@Length(message="[批次号]长度不符合规范", max = 32)
	@NotEmpty(message="[批次号]不能为空")
	private String batchNo;// 批次号
	@Length(message="[订单发送时间]长度不符合规范", max = 14)
	@NotEmpty(message="[订单发送时间]不能为空")
	private String txnTime;// 订单发送时间
	private String fileContent;// 文件内容

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


	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}


}
