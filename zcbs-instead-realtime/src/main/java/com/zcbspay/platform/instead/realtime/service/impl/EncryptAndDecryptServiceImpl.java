package com.zcbspay.platform.instead.realtime.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.instead.common.bean.AdditBean;
import com.zcbspay.platform.instead.common.bean.UrlBean;
import com.zcbspay.platform.instead.common.constant.Constants;
import com.zcbspay.platform.instead.common.utils.HttpRequestParam;
import com.zcbspay.platform.instead.common.utils.HttpUtils;
import com.zcbspay.platform.instead.common.utils.RSAHelper;
import com.zcbspay.platform.instead.realtime.service.EncryptAndDecryptService;

import net.sf.json.JSONObject;
@Service
public class EncryptAndDecryptServiceImpl implements EncryptAndDecryptService {
	private static final Logger logger = LoggerFactory.getLogger(EncryptAndDecryptServiceImpl.class); 
	
	//@Autowired
	//private MerchMKService merchMKService;
	
	@Autowired
	private UrlBean urlBean;
	
	private String AccessType ="1";
	
	@Override
	public String decrypt(AdditBean additBean, String data) {
		RSAHelper rsa = null;
		if (AccessType.equals(additBean.getAccessType())) {
			rsa = getRsa(additBean);
		} else {
			logger.error("未知的接入方式");
			return null;
		}
		return rsa.decrypt(data);
	}

	

	@Override
	public String encrypt(AdditBean additBean, String data) {
		RSAHelper rsa = null;
		if (AccessType.equals(additBean.getAccessType())) {
			rsa = getRsa(additBean);
		} else {
			logger.error("未知的接入方式");
			return null;
		}
		return rsa.encrypt(data);
	}
	@SuppressWarnings("unchecked")
	private RSAHelper getRsa(AdditBean additBean) {
		RSAHelper rsa;
		HttpRequestParam httpRequestParam= new HttpRequestParam("data",additBean.getMerId());
		List<HttpRequestParam> list = new ArrayList<>();
		list.add(httpRequestParam);
		
		String url = urlBean.getMkUrl();
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.openConnection();
		String responseContent =null;
		try {
			responseContent= httpUtils.executeHttpPost(url,list,Constants.Encoding.UTF_8);
		} catch (HttpException e) {
			e.printStackTrace();
		}
		httpUtils.closeConnection();
		Map<String, Object> re=(Map<String, Object>) JSONObject.toBean(JSONObject.fromObject(responseContent),Map.class);
		
		rsa = new RSAHelper(re.get("localpub").toString(), re.get("localpri").toString());
		return rsa;
	}
}
