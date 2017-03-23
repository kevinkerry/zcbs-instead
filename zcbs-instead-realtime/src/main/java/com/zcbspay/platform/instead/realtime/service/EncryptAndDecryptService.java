package com.zcbspay.platform.instead.realtime.service;

import com.zcbspay.platform.support.signaturn.bean.AdditBean;

public interface EncryptAndDecryptService {
	
	public String decrypt(AdditBean additBean,String data);
	
	public String encrypt(AdditBean additBean,String data);

}
