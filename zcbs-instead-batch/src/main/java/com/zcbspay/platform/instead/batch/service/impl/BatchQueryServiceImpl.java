package com.zcbspay.platform.instead.batch.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.business.order.bean.BatchResultBean;
import com.zcbspay.platform.business.order.bean.ResultBean;
import com.zcbspay.platform.business.order.service.OrderQueryService;
import com.zcbspay.platform.instead.batch.bean.BatchQueryReqBean;
import com.zcbspay.platform.instead.batch.bean.BatchQueryResBean;
import com.zcbspay.platform.instead.batch.service.CollectAndPayService;
import com.zcbspay.platform.instead.common.enums.ResponseTypeEnum;
import com.zcbspay.platform.instead.common.utils.BeanCopyUtil;
import com.zcbspay.platform.instead.common.utils.FlaterUtils;
import com.zcbspay.platform.instead.common.utils.ValidateLocator;
import com.zcbspay.platform.support.signaturn.bean.MessageBean;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service("batchQueryService")
public class BatchQueryServiceImpl implements CollectAndPayService{
	@Autowired
	private OrderQueryService batchTradeQuery;
	@Override
	public MessageBean invoke(MessageBean messageBean) {
		BatchQueryReqBean reqBean=(BatchQueryReqBean) JSONObject.toBean(JSONObject.fromObject(messageBean.getData()),BatchQueryReqBean.class);
		BatchQueryResBean batchPayResBean=BeanCopyUtil.copyBean(BatchQueryResBean.class, reqBean);
		try {
			ValidateLocator.validateBeans(reqBean);
		} catch (Exception e) {
			e.printStackTrace();
			batchPayResBean.setRespCode(ResponseTypeEnum.dataError.getCode());
			batchPayResBean.setRespMsg(ResponseTypeEnum.dataError.getMessage());
		}
		ResultBean resultBean =null;
		if ("01".equals(reqBean.getBusiType())) {//批量代收
			resultBean=batchTradeQuery.queryConcentrateCollectionBatch(reqBean.getMerId(), reqBean.getBatchNo(), reqBean.getTxnDate());
		} else if ("02".equals(reqBean.getBusiType())) {//批量代付
			resultBean=batchTradeQuery.queryConcentratePaymentBatch(reqBean.getMerId(), reqBean.getBatchNo(), reqBean.getTxnDate());
		}
		if (!resultBean.isResultBool()) {
			batchPayResBean.setRespCode(ResponseTypeEnum.fail.getCode());
			batchPayResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
		}else {
			batchPayResBean.setRespCode(ResponseTypeEnum.success.getCode());
			batchPayResBean.setRespMsg(ResponseTypeEnum.success.getMessage());
			BatchResultBean batchResultBean=(BatchResultBean)resultBean.getResultObj();
			BeanCopyUtil.copyBean(batchPayResBean, batchResultBean);
			batchPayResBean.setFileContent(FlaterUtils.inflater(JSONArray.fromObject(batchResultBean.getFileContentList()).toString()));
		}
		messageBean.setData(JSONObject.fromObject(batchPayResBean).toString());
		return messageBean;
	}


}
