package com.zcbspay.platform.instead.realtime.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.business.exception.QueryOrderException;
import com.zcbspay.platform.business.order.bean.OrderResultBean;
import com.zcbspay.platform.business.order.bean.ResultBean;
import com.zcbspay.platform.business.order.service.OrderQueryService;
import com.zcbspay.platform.instead.common.enums.ResponseTypeEnum;
import com.zcbspay.platform.instead.common.utils.BeanCopyUtil;
import com.zcbspay.platform.instead.common.utils.ValidateLocator;
import com.zcbspay.platform.instead.realtime.bean.RealTimeQueryReqBean;
import com.zcbspay.platform.instead.realtime.bean.RealTimeQueryResBean;
import com.zcbspay.platform.instead.realtime.bean.Reserved;
import com.zcbspay.platform.instead.realtime.service.CollectAndPayService;
import com.zcbspay.platform.support.signaturn.bean.MessageBean;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("realTimeQueryService")
public class RealTimeQueryServiceImpl implements CollectAndPayService {

	@Autowired
	private OrderQueryService realTimeTradeQuery;

	@Override
	public MessageBean invoke(MessageBean messageBean) {
		RealTimeQueryReqBean reqBean = (RealTimeQueryReqBean) JSONObject
				.toBean(JSONObject.fromObject(messageBean.getData()), RealTimeQueryReqBean.class);
		
		ResultBean resultBean = null;
		RealTimeQueryResBean realTimeQueryResBean = BeanCopyUtil.copyBean(RealTimeQueryResBean.class, reqBean);
		try {
			ValidateLocator.validateBeans(reqBean);
		} catch (Exception e) {
			e.printStackTrace();
			realTimeQueryResBean.setRespCode(ResponseTypeEnum.dataError.getCode());
			realTimeQueryResBean.setRespMsg(ResponseTypeEnum.dataError.getMessage());
		}
		try {
			if ("01".equals(reqBean.getOrderType())) {// 批量代收
				resultBean = realTimeTradeQuery.queryConcentrateCollectionOrder(reqBean.getTn());
			} else if ("02".equals(reqBean.getOrderType())) {// 批量代付
				resultBean = realTimeTradeQuery.queryConcentratePaymentOrder(reqBean.getTn());
			}
		} catch (QueryOrderException e) {
			e.printStackTrace();
			realTimeQueryResBean.setRespCode(ResponseTypeEnum.fail.getCode());
			realTimeQueryResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
		}

		if (!resultBean.isResultBool()) {
			realTimeQueryResBean.setRespCode(ResponseTypeEnum.fail.getCode());
			realTimeQueryResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
		} else {
			realTimeQueryResBean.setRespCode(ResponseTypeEnum.success.getCode());
			realTimeQueryResBean.setRespMsg(ResponseTypeEnum.success.getMessage());
			OrderResultBean orderResultBean = (OrderResultBean) resultBean.getResultObj();
			BeanCopyUtil.copyBean(realTimeQueryResBean, orderResultBean);
			Reserved reserved=BeanCopyUtil.copyBean(Reserved.class, orderResultBean);
			realTimeQueryResBean.setReserved(JSONObject.fromObject(reserved).toString());
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
