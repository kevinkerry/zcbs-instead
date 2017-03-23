package com.zcbspay.platform.instead.realtime.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.business.concentrate.bean.RealtimePaymentBean;
import com.zcbspay.platform.business.concentrate.bean.ResultBean;
import com.zcbspay.platform.business.concentrate.realtime.service.RealtimePayment;
import com.zcbspay.platform.instead.common.enums.ResponseTypeEnum;
import com.zcbspay.platform.instead.common.utils.BeanCopyUtil;
import com.zcbspay.platform.instead.common.utils.ValidateLocator;
import com.zcbspay.platform.instead.realtime.bean.EncryptData;
import com.zcbspay.platform.instead.realtime.bean.RealTimePayReqBean;
import com.zcbspay.platform.instead.realtime.bean.RealTimePayResBean;
import com.zcbspay.platform.instead.realtime.service.CollectAndPayService;
import com.zcbspay.platform.instead.realtime.service.EncryptAndDecryptService;
import com.zcbspay.platform.support.signaturn.bean.AdditBean;
import com.zcbspay.platform.support.signaturn.bean.MessageBean;

import net.sf.json.JSONObject;
@Service("realTimePayService")
public class RealTimePayServiceImpl implements CollectAndPayService{

	@Autowired
	private EncryptAndDecryptService encryptAndDecryptService;
	@Autowired
	private RealtimePayment realtimePayment;
	
	@Override
	public MessageBean invoke(MessageBean messageBean) {
		RealTimePayReqBean reqBean=(RealTimePayReqBean) JSONObject.toBean(JSONObject.fromObject(messageBean.getData()),RealTimePayReqBean.class);
		RealTimePayResBean realTimePayResBean=BeanCopyUtil.copyBean(RealTimePayResBean.class,reqBean);
		try {
			ValidateLocator.validateBeans(reqBean);
		} catch (Exception e) {
			e.printStackTrace();
			realTimePayResBean.setRespCode(ResponseTypeEnum.dataError.getCode());
			realTimePayResBean.setRespMsg(ResponseTypeEnum.dataError.getMessage());
		}
		EncryptData encryptData= null;
		if (StringUtils.isNotEmpty(reqBean.getEncryptData())) {
			AdditBean additBean = (AdditBean) JSONObject.toBean(JSONObject.fromObject(messageBean.getAddit()),AdditBean.class);
			String data = encryptAndDecryptService.decrypt(additBean, reqBean.getEncryptData());
			encryptData=(EncryptData) JSONObject.toBean(JSONObject.fromObject(data),EncryptData.class);
			ValidateLocator.validateBeans(encryptData);
		}
		RealtimePaymentBean realtimePaymentBean=new RealtimePaymentBean();
		BeanCopyUtil.copyBean(realtimePaymentBean,reqBean);
		BeanCopyUtil.copyBean(realtimePaymentBean,encryptData);
		ResultBean resultBean= realtimePayment.pay(realtimePaymentBean);
		
		if (!resultBean.isResultBool()) {
			realTimePayResBean.setRespCode(ResponseTypeEnum.fail.getCode());
			realTimePayResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
		}else {
			realTimePayResBean.setRespCode(ResponseTypeEnum.success.getCode());
			realTimePayResBean.setRespMsg(ResponseTypeEnum.success.getMessage());
			realTimePayResBean.setTn(resultBean.getResultObj().toString());
		}
		messageBean.setData(JSONObject.fromObject(realTimePayResBean).toString());
		return messageBean;
	}

	
}
