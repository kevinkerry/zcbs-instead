package com.zcbspay.platform.instead.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

// 响应基类
public class BeanTest implements Serializable{
	private List<BeanTest> list;
    public List<BeanTest> getList() {
		return list;
	}

	public void setList(List<BeanTest> list) {
		this.list = list;
	}

	protected String version="000003"; // 版本
	protected String encoding="000003"; // 编码方式
	private String txnType="000003"; // 交易类型
	private String txnSubType="000003"; // 交易子类
	private String bizType="000003"; // 产品类型
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getTxnSubType() {
		return txnSubType;
	}

	public void setTxnSubType(String txnSubType) {
		this.txnSubType = txnSubType;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		BeanTest test=new BeanTest();
		BeanTest test1=new BeanTest();
		BeanTest test2=new BeanTest();
		BeanTest test3=new BeanTest();
		BeanTest test4=new BeanTest();
		List<BeanTest> list =new ArrayList<>();
		list.add(test1);
		list.add(test2);
		list.add(test3);
		list.add(test4);
		test.setList(list);
		
		
		String  tString=JSONObject.fromObject(test).toString();
		System.out.println(tString);
		Map<String, Class> classT=new HashMap<>();
		
		classT.put("list", BeanTest.class);
		BeanTest beanTest=(BeanTest) JSONObject.toBean(JSONObject.fromObject(tString),BeanTest.class,classT);
		System.out.println(beanTest);
	}

}
