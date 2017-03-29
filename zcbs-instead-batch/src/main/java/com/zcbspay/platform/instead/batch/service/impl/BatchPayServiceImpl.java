package com.zcbspay.platform.instead.batch.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.business.concentrate.batch.service.BatchPayment;
import com.zcbspay.platform.business.concentrate.bean.BatchPaymentBean;
import com.zcbspay.platform.business.concentrate.bean.FileContentBean;
import com.zcbspay.platform.business.concentrate.bean.ResultBean;
import com.zcbspay.platform.instead.batch.bean.BatchCollectAndPayFileContent;
import com.zcbspay.platform.instead.batch.bean.BatchPayReqBean;
import com.zcbspay.platform.instead.batch.bean.BatchPayResBean;
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
@Service("batchPayService")
public class BatchPayServiceImpl implements CollectAndPayService{
	private static final Logger logger = LoggerFactory.getLogger(BatchPayServiceImpl.class); 
	@Autowired
	private BatchPayment batchPayment;
	
	@SuppressWarnings({ "unchecked", "deprecation", "static-access" })
	@Override
	public MessageBean invoke(MessageBean messageBean) {
		BatchPayResBean batchPayResBean=new BatchPayResBean();
		try {
			BatchPayReqBean reqBean=(BatchPayReqBean) JSONObject.toBean(JSONObject.fromObject(messageBean.getData()),BatchPayReqBean.class);
			batchPayResBean=BeanCopyUtil.copyBean(BatchPayResBean.class, reqBean);
			BatchPaymentBean batchPaymentBean=BeanCopyUtil.copyBean(BatchPaymentBean.class, reqBean);
			ValidateLocator.validateBeans(reqBean);
			List<FileContentBean> fileContentBeans=new ArrayList<>();
			
			if (StringUtils.isNotEmpty(reqBean.getFileContent())) {
				JSONArray data = JSONArray.fromObject(FlaterUtils.inflater(reqBean.getFileContent()));	
				List<BatchCollectAndPayFileContent> batchCollectAndPayFileContents =data.toList(data, BatchCollectAndPayFileContent.class);
				for (BatchCollectAndPayFileContent batchCollectAndPayFileContent : batchCollectAndPayFileContents) {
					ValidateLocator.validateBeans(batchCollectAndPayFileContent);
					FileContentBean fileContentBean = BeanCopyUtil.copyBean(FileContentBean.class, batchCollectAndPayFileContent);
					fileContentBeans.add(fileContentBean);
				}
			}
			batchPaymentBean.setFileContent(fileContentBeans);;
			ResultBean resultBean =batchPayment.pay(batchPaymentBean);
			if (!resultBean.isResultBool()) {
				ResponseTypeEnum responseTypeEnum=ResponseTypeEnum.getTxnTypeEnumByInCode(resultBean.getErrCode());
				if (responseTypeEnum!=null) {
					batchPayResBean.setRespCode(responseTypeEnum.getCode());
					batchPayResBean.setRespMsg(resultBean.getResultObj().toString());
				}else {
					batchPayResBean.setRespCode(ResponseTypeEnum.fail.getCode());
					batchPayResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
				}
			}else {
				batchPayResBean.setRespCode(ResponseTypeEnum.success.getCode());
				batchPayResBean.setRespMsg(ResponseTypeEnum.success.getMessage());
			}
		} catch (DataErrorException e) {
			e.printStackTrace();
			logger.error(ExceptionUtil.getStackTrace(e));
			batchPayResBean.setRespCode(ResponseTypeEnum.dataError.getCode());
			batchPayResBean.setRespMsg(ResponseTypeEnum.dataError.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtil.getStackTrace(e));
			batchPayResBean.setRespCode(ResponseTypeEnum.fail.getCode());
			batchPayResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
		}
		
		messageBean.setData(JSONObject.fromObject(batchPayResBean).toString());
		return messageBean;
	}

	
}
