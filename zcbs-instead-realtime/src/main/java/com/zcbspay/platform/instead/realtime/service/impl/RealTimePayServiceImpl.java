package com.zcbspay.platform.instead.realtime.service.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.business.concentrate.bean.RealtimePaymentBean;
import com.zcbspay.platform.business.concentrate.bean.ResultBean;
import com.zcbspay.platform.business.concentrate.realtime.service.RealtimePayment;
import com.zcbspay.platform.instead.batch.exception.DataErrorException;
import com.zcbspay.platform.instead.common.enums.ResponseTypeEnum;
import com.zcbspay.platform.instead.common.utils.BeanCopyUtil;
import com.zcbspay.platform.instead.common.utils.ExceptionUtil;
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
	private static final Logger logger = LoggerFactory.getLogger(RealTimePayServiceImpl.class); 
	@Autowired
	private EncryptAndDecryptService encryptAndDecryptService;
	@Autowired
	private RealtimePayment realtimePayment;
	
	@Override
	public MessageBean invoke(MessageBean messageBean) {
		RealTimePayResBean realTimePayResBean=new RealTimePayResBean();
		try {
			RealTimePayReqBean reqBean=(RealTimePayReqBean) JSONObject.toBean(JSONObject.fromObject(messageBean.getData()),RealTimePayReqBean.class);
			realTimePayResBean=BeanCopyUtil.copyBean(RealTimePayResBean.class,reqBean);
			ValidateLocator.validateBeans(reqBean);
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
				ResponseTypeEnum responseTypeEnum=ResponseTypeEnum.getTxnTypeEnumByInCode(resultBean.getErrCode());
				if (responseTypeEnum!=null) {
					realTimePayResBean.setRespCode(responseTypeEnum.getCode());
					realTimePayResBean.setRespMsg(resultBean.getErrMsg().toString());
				}else {
					realTimePayResBean.setRespCode(ResponseTypeEnum.fail.getCode());
					realTimePayResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
				}
			}else {
				realTimePayResBean.setRespCode(ResponseTypeEnum.success.getCode());
				realTimePayResBean.setRespMsg(ResponseTypeEnum.success.getMessage());
				realTimePayResBean.setTn(resultBean.getResultObj().toString());
			}
		} catch (DataErrorException e) {
			e.printStackTrace();
			logger.error(ExceptionUtil.getStackTrace(e));
			realTimePayResBean.setRespCode(ResponseTypeEnum.dataError.getCode());
			realTimePayResBean.setRespMsg(ResponseTypeEnum.dataError.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtil.getStackTrace(e));
			realTimePayResBean.setRespCode(ResponseTypeEnum.fail.getCode());
			realTimePayResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
		}
		messageBean.setData(JSONObject.fromObject(realTimePayResBean).toString());
		return messageBean;
	}

	
}
