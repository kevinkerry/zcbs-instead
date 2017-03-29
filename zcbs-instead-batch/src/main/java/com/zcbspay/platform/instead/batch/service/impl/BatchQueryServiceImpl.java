package com.zcbspay.platform.instead.batch.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.business.order.bean.BatchResultBean;
import com.zcbspay.platform.business.order.bean.ResultBean;
import com.zcbspay.platform.business.order.service.OrderQueryService;
import com.zcbspay.platform.instead.batch.bean.BatchQueryReqBean;
import com.zcbspay.platform.instead.batch.bean.BatchQueryResBean;
import com.zcbspay.platform.instead.batch.exception.DataErrorException;
import com.zcbspay.platform.instead.batch.service.CollectAndPayService;
import com.zcbspay.platform.instead.common.enums.ResponseTypeEnum;
import com.zcbspay.platform.instead.common.utils.BeanCopyUtil;
import com.zcbspay.platform.instead.common.utils.ExceptionUtil;
import com.zcbspay.platform.instead.common.utils.FlaterUtils;
import com.zcbspay.platform.instead.common.utils.ValidateLocator;
import com.zcbspay.platform.support.signaturn.bean.MessageBean;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("batchQueryService")
public class BatchQueryServiceImpl implements CollectAndPayService {
	private static final Logger logger = LoggerFactory.getLogger(BatchQueryServiceImpl.class);
	@Autowired
	private OrderQueryService batchTradeQuery;

	@Override
	public MessageBean invoke(MessageBean messageBean) {
		BatchQueryResBean batchQueryResBean=new BatchQueryResBean();
		try {
			BatchQueryReqBean reqBean = (BatchQueryReqBean) JSONObject.toBean(JSONObject.fromObject(messageBean.getData()),
					BatchQueryReqBean.class);
			batchQueryResBean = BeanCopyUtil.copyBean(BatchQueryResBean.class, reqBean);
			ValidateLocator.validateBeans(reqBean);
			ResultBean resultBean = null;
			if ("01".equals(reqBean.getBusiType())) {// 批量代收
				resultBean = batchTradeQuery.queryConcentrateCollectionBatch(reqBean.getMerId(), reqBean.getBatchNo(),
						reqBean.getTxnDate());
			} else if ("02".equals(reqBean.getBusiType())) {// 批量代付
				resultBean = batchTradeQuery.queryConcentratePaymentBatch(reqBean.getMerId(), reqBean.getBatchNo(),
						reqBean.getTxnDate());
			}
			if (!resultBean.isResultBool()) {
				ResponseTypeEnum responseTypeEnum=ResponseTypeEnum.getTxnTypeEnumByInCode(resultBean.getErrCode());
				if (responseTypeEnum!=null) {
					batchQueryResBean.setRespCode(responseTypeEnum.getCode());
					batchQueryResBean.setRespMsg(resultBean.getResultObj().toString());
				}else {
					batchQueryResBean.setRespCode(ResponseTypeEnum.fail.getCode());
					batchQueryResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
				}
				
			} else {
				batchQueryResBean.setRespCode(ResponseTypeEnum.success.getCode());
				batchQueryResBean.setRespMsg(ResponseTypeEnum.success.getMessage());
				BatchResultBean batchResultBean = (BatchResultBean) resultBean.getResultObj();
				BeanCopyUtil.copyBean(batchQueryResBean, batchResultBean);
				batchQueryResBean.setFileContent(
						FlaterUtils.inflater(JSONArray.fromObject(batchResultBean.getFileContentList()).toString()));
			}
		} catch (DataErrorException e) {
			e.printStackTrace();
			logger.error(ExceptionUtil.getStackTrace(e));
			batchQueryResBean.setRespCode(ResponseTypeEnum.dataError.getCode());
			batchQueryResBean.setRespMsg(ResponseTypeEnum.dataError.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtil.getStackTrace(e));
			batchQueryResBean.setRespCode(ResponseTypeEnum.fail.getCode());
			batchQueryResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
		}
		messageBean.setData(JSONObject.fromObject(batchQueryResBean).toString());
		return messageBean;
	}

}
