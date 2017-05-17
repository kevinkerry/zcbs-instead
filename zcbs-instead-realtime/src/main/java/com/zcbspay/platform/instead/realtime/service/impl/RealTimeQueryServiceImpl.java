package com.zcbspay.platform.instead.realtime.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.zcbspay.platform.instead.common.utils.HttpRequestParam;
import com.zcbspay.platform.instead.common.utils.HttpUtils;
import com.zcbspay.platform.instead.common.utils.ValidateLocator;
import com.zcbspay.platform.instead.realtime.bean.RealTimeQueryReqBean;
import com.zcbspay.platform.instead.realtime.bean.RealTimeQueryResBean;
import com.zcbspay.platform.instead.realtime.bean.Reserved;
import com.zcbspay.platform.instead.realtime.service.CollectAndPayService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("realTimeQueryService")
public class RealTimeQueryServiceImpl implements CollectAndPayService {
	private static final Logger logger = LoggerFactory.getLogger(RealTimeQueryServiceImpl.class); 
//	@Autowired
//	private OrderQueryService realTimeTradeQuery;
	
	@Autowired
	private UrlBean urlBean;
	
	@Override
	public MessageBean invoke(MessageBean messageBean) {
		RealTimeQueryResBean realTimeQueryResBean=new RealTimeQueryResBean();
		try {
			RealTimeQueryReqBean reqBean = (RealTimeQueryReqBean) JSONObject
					.toBean(JSONObject.fromObject(messageBean.getData()), RealTimeQueryReqBean.class);
			
			ResultBean resultBean = null;
			realTimeQueryResBean = BeanCopyUtil.copyBean(RealTimeQueryResBean.class, reqBean);
			ValidateLocator.validateBeans(reqBean);
			//TODO:
			String url =null;// 
			
			
			if ("01".equals(reqBean.getOrderType())) {// 实时代收
				url=urlBean.getSingleQueryCollectUrl();
				//resultBean = realTimeTradeQuery.queryConcentrateCollectionOrder(reqBean.getTn());
			} else if ("02".equals(reqBean.getOrderType())) {// 实时代付
				url=urlBean.getSinglePayUrl();
				//resultBean = realTimeTradeQuery.queryConcentratePaymentOrder(reqBean.getTn());
			}
			
			HttpRequestParam httpRequestParam= new HttpRequestParam("data",reqBean.getTn());
			List<HttpRequestParam> list = new ArrayList<>();
			list.add(httpRequestParam);
			
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.openConnection();
			String responseContent = httpUtils.executeHttpGet(url,list,Constants.Encoding.UTF_8);
			httpUtils.closeConnection();
			resultBean=(ResultBean) JSONObject.toBean(JSONObject.fromObject(responseContent), ResultBean.class);
			
			if (!resultBean.isResultBool()) {
				ResponseTypeEnum responseTypeEnum=ResponseTypeEnum.getTxnTypeEnumByInCode(resultBean.getErrCode());
				if (responseTypeEnum!=null) {
					realTimeQueryResBean.setRespCode(responseTypeEnum.getCode());
					realTimeQueryResBean.setRespMsg(resultBean.getErrMsg().toString());
				}else {
					realTimeQueryResBean.setRespCode(ResponseTypeEnum.fail.getCode());
					realTimeQueryResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
				}
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
		}
		
		messageBean.setData(JSONObject.fromObject(realTimeQueryResBean).toString());
		return messageBean;
	}
	
	public static void main(String[] args) {
		List<RealTimeQueryReqBean> list=new ArrayList<>();
		RealTimeQueryReqBean realTimeQueryReqBean1=new RealTimeQueryReqBean();
		realTimeQueryReqBean1.setBizType("bizType1");
		realTimeQueryReqBean1.setEncoding("encoding1");
		
		RealTimeQueryReqBean realTimeQueryReqBean2=new RealTimeQueryReqBean();
		realTimeQueryReqBean2.setBizType("bizType2");
		realTimeQueryReqBean2.setEncoding("encoding2");
		
		list.add(realTimeQueryReqBean1);
		list.add(realTimeQueryReqBean2);
		
		String reString= JSONArray.fromObject(list).toString();
		System.out.println("array:"+reString);
		String mtring=JSONObject.fromObject(list).toString();
		System.out.println("array:"+mtring);
	}

}
