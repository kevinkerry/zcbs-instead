package com.zcbspay.platform.instead.realtime.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.business.concentrate.bean.RealtimePaymentBean;
import com.zcbspay.platform.business.concentrate.bean.ResultBean;
import com.zcbspay.platform.instead.common.bean.AdditBean;
import com.zcbspay.platform.instead.common.bean.MessageBean;
import com.zcbspay.platform.instead.common.bean.UrlBean;
import com.zcbspay.platform.instead.common.constant.Constants;
import com.zcbspay.platform.instead.common.enums.ResponseTypeEnum;
import com.zcbspay.platform.instead.common.exception.DataErrorException;
import com.zcbspay.platform.instead.common.utils.BeanCopyUtil;
import com.zcbspay.platform.instead.common.utils.ExceptionUtil;
import com.zcbspay.platform.instead.common.utils.HttpRequestParam;
import com.zcbspay.platform.instead.common.utils.HttpUtils;
import com.zcbspay.platform.instead.common.utils.ValidateLocator;
import com.zcbspay.platform.instead.realtime.bean.EncryptData;
import com.zcbspay.platform.instead.realtime.bean.RealTimePayReqBean;
import com.zcbspay.platform.instead.realtime.bean.RealTimePayResBean;
import com.zcbspay.platform.instead.realtime.service.CollectAndPayService;
import com.zcbspay.platform.instead.realtime.service.EncryptAndDecryptService;

import net.sf.json.JSONObject;
@Service("realTimePayService")
public class RealTimePayServiceImpl implements CollectAndPayService{
	private static final Logger logger = LoggerFactory.getLogger(RealTimePayServiceImpl.class); 
	@Autowired
	private EncryptAndDecryptService encryptAndDecryptService;
	
	@Autowired
	private UrlBean urlBean;
	@Override
	public MessageBean invoke(MessageBean messageBean) {
		RealTimePayResBean realTimePayResBean=new RealTimePayResBean();
		HttpUtils httpUtils = new HttpUtils();
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
			HttpRequestParam httpRequestParam= new HttpRequestParam("data",JSONObject.fromObject(realtimePaymentBean).toString());
			List<HttpRequestParam> list = new ArrayList<>();
			list.add(httpRequestParam);
			String url = urlBean.getSinglePayUrl();
			
			httpUtils.openConnection();
			String responseContent = httpUtils.executeHttpPost(url,list,Constants.Encoding.UTF_8);
			logger.info("实时代付处理结果:"+responseContent);
			ResultBean resultBean=(ResultBean) JSONObject.toBean(JSONObject.fromObject(responseContent), ResultBean.class);
			//ResultBean resultBean= realtimePayment.pay(realtimePaymentBean);
			
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
		}finally {
			httpUtils.closeConnection();
		}
		messageBean.setData(JSONObject.fromObject(realTimePayResBean).toString());
		return messageBean;
	}

	
}
