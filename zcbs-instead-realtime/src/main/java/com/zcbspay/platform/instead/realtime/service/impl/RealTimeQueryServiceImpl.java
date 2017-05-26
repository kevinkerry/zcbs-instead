package com.zcbspay.platform.instead.realtime.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.business.order.bean.OrderResultBean;
import com.zcbspay.platform.business.order.bean.ResultBean;
import com.zcbspay.platform.instead.common.bean.MessageBean;
import com.zcbspay.platform.instead.common.bean.UrlBean;
import com.zcbspay.platform.instead.common.constant.Constants;
import com.zcbspay.platform.instead.common.enums.ResponseTypeEnum;
import com.zcbspay.platform.instead.common.exception.DataErrorException;
import com.zcbspay.platform.instead.common.utils.BeanCopyUtil;
import com.zcbspay.platform.instead.common.utils.ExceptionUtil;
import com.zcbspay.platform.instead.common.utils.HttpUtils;
import com.zcbspay.platform.instead.common.utils.ValidateLocator;
import com.zcbspay.platform.instead.realtime.bean.RealTimeQueryReqBean;
import com.zcbspay.platform.instead.realtime.bean.RealTimeQueryResBean;
import com.zcbspay.platform.instead.realtime.bean.Reserved;
import com.zcbspay.platform.instead.realtime.service.CollectAndPayService;

import net.sf.json.JSONObject;

@Service("realTimeQueryService")
public class RealTimeQueryServiceImpl implements CollectAndPayService {
	private static final Logger logger = LoggerFactory.getLogger(RealTimeQueryServiceImpl.class); 
	@Autowired
	private UrlBean urlBean;
	
	private final String collectFlag="01";
	
	private final String payFlag="02";
	
	@Override
	public MessageBean invoke(MessageBean messageBean) {
		RealTimeQueryResBean realTimeQueryResBean=new RealTimeQueryResBean();
		HttpUtils httpUtils = new HttpUtils();
		try {
			RealTimeQueryReqBean reqBean = (RealTimeQueryReqBean) JSONObject
					.toBean(JSONObject.fromObject(messageBean.getData()), RealTimeQueryReqBean.class);
			
			ResultBean resultBean = null;
			realTimeQueryResBean = BeanCopyUtil.copyBean(RealTimeQueryResBean.class, reqBean);
			ValidateLocator.validateBeans(reqBean);
			String url =null;// 
			
			if (collectFlag.equals(reqBean.getOrderType())) {// 实时代收
				url=urlBean.getSingleQueryCollectUrl();
			} else if (payFlag.equals(reqBean.getOrderType())) {// 实时代付
				url=urlBean.getSingleQueryPayUrl();
			}
			
			
			httpUtils.openConnection();
			String responseContent = httpUtils.executeHttpGet(url+"/"+reqBean.getTn(),Constants.Encoding.UTF_8);
			logger.info("交易查询结果:"+responseContent);
			resultBean=(ResultBean) JSONObject.toBean(JSONObject.fromObject(responseContent), ResultBean.class);
			
			if (!resultBean.isResultBool()) {
				/*ResponseTypeEnum responseTypeEnum=ResponseTypeEnum.getTxnTypeEnumByInCode(resultBean.getErrCode());
				if (responseTypeEnum!=null) {
					realTimeQueryResBean.setRespCode(responseTypeEnum.getCode());
					realTimeQueryResBean.setRespMsg(resultBean.getErrMsg().toString());
				}else {
					realTimeQueryResBean.setRespCode(ResponseTypeEnum.fail.getCode());
					realTimeQueryResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
				}*/
				realTimeQueryResBean.setRespCode(resultBean.getErrCode().toString());
				realTimeQueryResBean.setRespMsg(resultBean.getErrMsg().toString());
			} else {
				realTimeQueryResBean.setRespCode(ResponseTypeEnum.success.getCode());
				realTimeQueryResBean.setRespMsg(ResponseTypeEnum.success.getMessage());
				Map<String, Class> mapClass=new HashMap<>();
				mapClass.put("resultObj", OrderResultBean.class);
				resultBean=(ResultBean) JSONObject.toBean(JSONObject.fromObject(responseContent), ResultBean.class,mapClass);
				OrderResultBean orderResultBean = (OrderResultBean) resultBean.getResultObj();
				BeanCopyUtil.copyBean(realTimeQueryResBean, orderResultBean);
				Reserved reserved=BeanCopyUtil.copyBean(Reserved.class, orderResultBean);
				realTimeQueryResBean.setReserved(JSONObject.fromObject(reserved).toString());
			}
		} catch (DataErrorException e) {
			e.printStackTrace();
			logger.error(ExceptionUtil.getStackTrace(e));
			realTimeQueryResBean.setRespCode(ResponseTypeEnum.dataError.getCode());
			realTimeQueryResBean.setRespMsg(ResponseTypeEnum.dataError.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtil.getStackTrace(e));
			realTimeQueryResBean.setRespCode(ResponseTypeEnum.fail.getCode());
			realTimeQueryResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
		}finally{
			httpUtils.closeConnection();
		}
		
		messageBean.setData(JSONObject.fromObject(realTimeQueryResBean).toString());
		return messageBean;
	}
}
