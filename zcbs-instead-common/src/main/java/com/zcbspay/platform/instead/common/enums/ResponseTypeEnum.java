package com.zcbspay.platform.instead.common.enums;

public enum ResponseTypeEnum {

	// 成功
	success("0000", "正常",""),
	// 处理失败(不能定位具体错误时候出现)
	fail("0009", "处理失败",""),
	//非法数据(数据长度和是否为空的校验)
	dataError("0040","非法数据",""),
	//验签解密错误
	decodeError("0020","验签解密失败",""),
	exception1("0061","支付信息错误 ","PC001"),
	exception2("0062","银行卡信息错误","PC002"),
	exception3("0063","银行卡类型错误","PC003"),
	exception4("0064","订单不存在","PC004"),
	exception5("0065","订单支付中","PC005"),
	exception6("0066","订单失效","PC006"),
	exception7("0067","交易金额与订单交易金额不一致","PC007"),
	exception8("0068","交易日志不存在","PC008"),
	exception9("0069","无法获取交易路由","PC009"),
	exception10("0070","无可用交易渠道","PC010"),
	exception11("0071","无法获取风控数据","PC011"),
	exception12("0072","交易被风控系统拒绝","PC012"),
	exception13("0073","订单服务异常","PC013"),
	exception14("0074","代付订单生成失败","PC014"),
	exception15("0075","代付订单不存在","PC015"),
	exception16("0076","代付订单正在付款中","PC016"),
	exception17("0077","代付订单失效","PC017"),
	exception18("0078","代付金额不一致","PC018"),
	exception19("0079","交易超时，请稍后查询交易结果","PC019"),
	exception20("0080","交易序列号和受理订单号不能同时为空","PC020"),
	exception21("0081","会员号不能为空","PC021"),
	exception22("0082","订单支付成功，请不要重复支付","PC022"),
	exception23("0083","会员不存在","PC023"),
	exception24("0084","提现订单生成失败","PC024"),
	exception25("0085","退款订单生成失败","PC025"),
	exception26("0086","充值订单生成失败","PC026"),
	exception27("0087","消费订单生成失败","PC027"),
	exception28("0088","交易失败，商户参数错误","PC028"),
	exception29("0089","参数不能为空","BC0000"),
	exception30("0090","合同信息不存在","BC0001"),
	exception31("0091","无法获取合同信息","BC0002"),
	exception32("0092","批量代收异常","BP001"),
	exception33("0093","批量代付异常","BP002"),
	exception34("0094","实时代收异常","BP003"),
	exception35("0095","实时代付异常","BP004"),
	;
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
