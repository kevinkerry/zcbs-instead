package com.zcbspay.platform.instead.common.enums;

public enum RealTimeTxnTypeEnum {

	// 实时代收
	collect("01", "00","realTimeCollectService"),
	// 实时代付
	pay("02", "00","realTimePayService"),
	// 交易查询
	query("03", "00","realTimeQueryService"),
	// 交易查询
	;

	private String txnType; // 交易类型
	private String txnSubType; // 交易子类型
	private String className; //对应的业务类

	private RealTimeTxnTypeEnum(String txnType, String txnSubType,String className) {
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
    public static RealTimeTxnTypeEnum getTxnTypeEnum(String txnType) {  
        for (RealTimeTxnTypeEnum txnTypeEnum : RealTimeTxnTypeEnum.values()) {  
            if (txnTypeEnum.getTxnType().equals(txnType)) {  
                return txnTypeEnum;  
            }  
        }  
        return null;  
    }  
	
	
}
