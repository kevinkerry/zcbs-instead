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
	exception1("0701","支付信息错误 ","PC001"),
	exception2("0702","银行卡信息错误","PC002"),
	exception3("0703","银行卡类型错误","PC003"),
	exception4("0704","订单不存在","PC004"),
	exception5("0705","订单支付中","PC005"),
	exception6("0706","订单失效","PC006"),
	exception7("0707","交易金额与订单交易金额不一致","PC007"),
	exception8("0708","交易日志不存在","PC008"),
	exception9("0709","无法获取交易路由","PC009"),
	exception10("0710","无可用交易渠道","PC010"),
	exception11("0711","无法获取风控数据","PC011"),
	exception12("0712","交易被风控系统拒绝","PC012"),
	exception13("0713","订单服务异常","PC013"),
	exception14("0714","代付订单生成失败（%1$s）","PC014"),
	exception15("0715","代付订单不存在","PC015"),
	exception16("0716","代付订单正在付款中","PC016"),
	exception17("0717","代付订单失效","PC017"),
	exception18("0718","代付金额不一致","PC018"),
	exception19("0719","交易超时，请稍后查询交易结果","PC019"),
	exception20("0720","交易序列号和受理订单号不能同时为空","PC020"),
	exception21("0721","会员号不能为空","PC021"),
	exception22("0722","订单支付成功，请不要重复支付","PC022"),
	exception23("0723","会员不存在","PC023"),
	exception24("0724","提现订单生成失败（%1$s）","PC024"),
	exception25("0725","退款订单生成失败（%1$s）","PC025"),
	exception26("0726","充值订单生成失败（%1$s）","PC026"),
	exception27("0727","消费订单生成失败（%1$s）","PC027"),
	exception28("0728","交易失败，商户参数错误","PC028"),
	exception29("0801","合同信息校验失败","BC001"),
	exception30("0802","批量的校验结果","BC002"),
	exception31("0803","通过","CT00"),
	exception32("0804","付款人名称不符","CT01"),
	exception33("0805","付款人账号不符","CT02"),
	exception34("0806","收款人名称不符","CT03"),
	exception35("0807","收款人账号不符","CT04"),
	exception36("0808","合同已失效","CT05"),
	exception37("0809","交易金额超出合同限制","CT06"),
	exception38("0810","交易金额超出合同累计代收金额限制","CT07"),
	exception39("0811","交易金额超出合同累计代付金额限制","CT08"),
	exception40("0812","交易金额超出合同累计代付笔数限制","CT09"),
	exception41("0813","交易金额超出合同累计代付笔数限制","CT10"),
	exception42("0814","未知","CT98"),
	exception43("0815","合同不存在","CT99"),
	exception44("0901","批量代收异常","BP001"),
	exception45("0902","批量代付异常","BP002"),
	exception46("0903","实时代收异常","BP003"),
	exception47("0904","实时代付异常","BP004");
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
