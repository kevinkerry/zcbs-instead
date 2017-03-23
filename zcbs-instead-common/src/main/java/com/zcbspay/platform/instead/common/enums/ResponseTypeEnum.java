package com.zcbspay.platform.instead.common.enums;

public enum ResponseTypeEnum {

	// 正常
	success("0000", "正常"),
	// 处理失败
	fail("0009", "处理失败"),
	
	dataError("0040","非法数据")
	;
	
	

	private String code; // 交易类型
	private String message; // 交易子类型
	private ResponseTypeEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	 // 普通方法  
    public static ResponseTypeEnum getTxnTypeEnum(String code) {  
        for (ResponseTypeEnum txnTypeEnum : ResponseTypeEnum.values()) {  
            if (txnTypeEnum.getCode().equals(code)) {  
                return txnTypeEnum;  
            }  
        }  
        return null;  
    }  
	
	
}
