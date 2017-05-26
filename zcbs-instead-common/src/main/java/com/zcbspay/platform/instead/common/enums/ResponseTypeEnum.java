package com.zcbspay.platform.instead.common.enums;

public enum ResponseTypeEnum {

	// 成功
	success("0000", "正常",""),
	// 处理失败(不能定位具体错误时候出现)
	fail("0009", "处理失败",""),
	//非法数据(数据长度和是否为空的校验)
	dataError("0040","非法数据",""),
	//验签解密错误
	decodeError("0020","验签解密失败","");
	
	private String code; // 交易类型
	private String message; // 交易子类型
	private String inCode;//内部错误码,服务传来
	
	public String getInCode() {
		return inCode;
	}

	public void setInCode(String inCode) {
		this.inCode = inCode;
	}

	private ResponseTypeEnum(String code, String message,String inCode) {
		this.code = code;
		this.message = message;
		this.inCode=inCode;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	 // 普通方法  
    public static ResponseTypeEnum getTxnTypeEnumByCode(String code) {  
        for (ResponseTypeEnum txnTypeEnum : ResponseTypeEnum.values()) {  
            if (txnTypeEnum.getCode().equals(code)) {  
                return txnTypeEnum;  
            }  
        }  
        return null;  
    }  
    
    public static ResponseTypeEnum getTxnTypeEnumByInCode(String inCode) {  
        for (ResponseTypeEnum txnTypeEnum : ResponseTypeEnum.values()) {  
            if (txnTypeEnum.getInCode().equals(inCode)) {  
                return txnTypeEnum;  
            }  
        }  
        return null;  
    } 
	
	
}
