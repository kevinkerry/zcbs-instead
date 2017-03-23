package com.zcbspay.platform.instead.realtime.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.instead.common.utils.RSAHelper;
import com.zcbspay.platform.instead.realtime.service.EncryptAndDecryptService;
import com.zcbspay.platform.member.merchant.bean.MerchMK;
import com.zcbspay.platform.member.merchant.service.MerchMKService;
import com.zcbspay.platform.support.signaturn.bean.AdditBean;
@Service
public class EncryptAndDecryptServiceImpl implements EncryptAndDecryptService {
	private static final Logger logger = LoggerFactory.getLogger(EncryptAndDecryptServiceImpl.class); 
	
	@Autowired
	private MerchMKService merchMKService;
	
	
	@Override
	public String decrypt(AdditBean additBean, String data) {
		RSAHelper rsa = null;
		if ("1".equals(additBean.getAccessType())) {
			MerchMK merchMk = merchMKService.get(additBean.getMerId());
			rsa = new RSAHelper(merchMk.getLocalPubKey(), merchMk.getLocalPriKey());
		} else {
			logger.error("未知的接入方式");
			return null;
		}
		return rsa.decrypt(data);
	}

	@Override
	public String encrypt(AdditBean additBean, String data) {
		RSAHelper rsa = null;
		if ("1".equals(additBean.getAccessType())) {
			MerchMK merchMk = merchMKService.get(additBean.getMerId());
			rsa = new RSAHelper(merchMk.getLocalPubKey(), merchMk.getLocalPriKey());
		} else {
			logger.error("未知的接入方式");
			return null;
		}
		return rsa.encrypt(data);
	}

}
