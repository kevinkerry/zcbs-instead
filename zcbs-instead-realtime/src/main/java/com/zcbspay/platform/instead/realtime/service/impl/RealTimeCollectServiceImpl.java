package com.zcbspay.platform.instead.realtime.service.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.business.concentrate.bean.RealtimeCollectionBean;
import com.zcbspay.platform.business.concentrate.bean.ResultBean;
import com.zcbspay.platform.business.concentrate.realtime.service.RealtimeCollection;
import com.zcbspay.platform.instead.batch.exception.DataErrorException;
import com.zcbspay.platform.instead.common.enums.ResponseTypeEnum;
import com.zcbspay.platform.instead.common.utils.BeanCopyUtil;
import com.zcbspay.platform.instead.common.utils.ExceptionUtil;
import com.zcbspay.platform.instead.common.utils.ValidateLocator;
import com.zcbspay.platform.instead.realtime.bean.EncryptData;
import com.zcbspay.platform.instead.realtime.bean.RealTimeCollectReqBean;
import com.zcbspay.platform.instead.realtime.bean.RealTimeCollectResBean;
import com.zcbspay.platform.instead.realtime.service.CollectAndPayService;
import com.zcbspay.platform.instead.realtime.service.EncryptAndDecryptService;
import com.zcbspay.platform.support.signaturn.bean.AdditBean;
import com.zcbspay.platform.support.signaturn.bean.MessageBean;

import net.sf.json.JSONObject;
@Service("realTimeCollectService")
public class RealTimeCollectServiceImpl implements CollectAndPayService{
	private static final Logger logger = LoggerFactory.getLogger(RealTimeCollectServiceImpl.class); 
	@Autowired
	private EncryptAndDecryptService encryptAndDecryptService;
	@Autowired
	private RealtimeCollection realtimeCollection;

	@Override
	public MessageBean invoke(MessageBean messageBean) {
		RealTimeCollectResBean realTimeCollectResBean=new RealTimeCollectResBean();
		try {
			//解析请求参数到实体类
			RealTimeCollectReqBean reqBean=(RealTimeCollectReqBean) JSONObject.toBean(JSONObject.fromObject(messageBean.getData()),RealTimeCollectReqBean.class);
			//将请求参数赋值给应答实体
			realTimeCollectResBean=BeanCopyUtil.copyBean(RealTimeCollectResBean.class,reqBean);
			//校验数据是否合法
			ValidateLocator.validateBeans(reqBean);
			//解析加密信息域(rsa加密)
			EncryptData encryptData= null;
			if (StringUtils.isNotEmpty(reqBean.getEncryptData())) {
				//解析附加信息
				AdditBean additBean = (AdditBean) JSONObject.toBean(JSONObject.fromObject(messageBean.getAddit()),AdditBean.class);
				//解密加密信息域
				String data = encryptAndDecryptService.decrypt(additBean, reqBean.getEncryptData());
				encryptData=(EncryptData) JSONObject.toBean(JSONObject.fromObject(data),EncryptData.class);
				ValidateLocator.validateBeans(encryptData);
			}
			
			RealtimeCollectionBean realtimeCollectionBean=new RealtimeCollectionBean();
			BeanCopyUtil.copyBean(realtimeCollectionBean,reqBean);
			BeanCopyUtil.copyBean(realtimeCollectionBean,encryptData);
			ResultBean resultBean= realtimeCollection.pay(realtimeCollectionBean);
			
			if (!resultBean.isResultBool()) {
				ResponseTypeEnum responseTypeEnum=ResponseTypeEnum.getTxnTypeEnumByInCode(resultBean.getErrCode());
				if (responseTypeEnum!=null) {
					realTimeCollectResBean.setRespCode(responseTypeEnum.getCode());
					realTimeCollectResBean.setRespMsg(resultBean.getResultObj().toString());
				}else {
					realTimeCollectResBean.setRespCode(ResponseTypeEnum.fail.getCode());
					realTimeCollectResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
				}
			}else {
				realTimeCollectResBean.setRespCode(ResponseTypeEnum.success.getCode());
				realTimeCollectResBean.setRespMsg(ResponseTypeEnum.success.getMessage());
				realTimeCollectResBean.setTn(resultBean.getResultObj().toString());
			}
		} catch (DataErrorException e) {
			e.printStackTrace();
			logger.error(ExceptionUtil.getStackTrace(e));
			realTimeCollectResBean.setRespCode(ResponseTypeEnum.dataError.getCode());
			realTimeCollectResBean.setRespMsg(ResponseTypeEnum.dataError.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtil.getStackTrace(e));
			realTimeCollectResBean.setRespCode(ResponseTypeEnum.fail.getCode());
			realTimeCollectResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
		}
		messageBean.setData(JSONObject.fromObject(realTimeCollectResBean).toString());
		return messageBean;
	}
	
}
