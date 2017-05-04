package com.zcbspay.platform.instead.common.enums;

public enum BatchTxnTypeEnum {

	// 批量代收
	collect("01", "00","batchCollectService"),
	// 批量代付
	pay("02", "00","batchPayService"),
	// 交易查询
	query("03", "00","batchQueryService"),
	//批量导入
	batchImport("05", "00","batchImportService"),
	//合同查询
	contract("06", "00","contractQueryService"),
	;

	private String txnType; // 交易类型
	private String txnSubType; // 交易子类型

	private String className;//对应的业务方法

	private BatchTxnTypeEnum(String txnType, String txnSubType,String className) {
		this.txnType = txnType;
		this.txnSubType = txnSubType;
		this.className=className;
	}

	public String getTxnType() {
		return txnType;
	}

	public String getTxnSubType() {
		return txnSubType;
	}
	public String getClassName() {
		return className;
	}
	
	 // 普通方法  
    public static BatchTxnTypeEnum getTxnTypeEnum(String txnType) {  
        for (BatchTxnTypeEnum txnTypeEnum : BatchTxnTypeEnum.values()) {  
            if (txnTypeEnum.getTxnType().equals(txnType)) {  
                return txnTypeEnum;  
            }  
        }  
        return null;  
    }  
	
	
}
