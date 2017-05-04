package com.zcbspay.platform.instead.batch.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.business.merch.bean.ResultBean;
import com.zcbspay.platform.business.merch.service.ContractBizService;
import com.zcbspay.platform.instead.batch.bean.BatchImportFileContent;
import com.zcbspay.platform.instead.batch.bean.BatchImportReqBean;
import com.zcbspay.platform.instead.batch.bean.BatchImportResBean;
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
/**
 * 批量查询服务
 * @author: zhangshd
 * @date:   2017年5月3日 上午9:48:19   
 * @version :v1.0
 */
@Service("batchImportService")
public class BatchImportServiceImpl implements CollectAndPayService {
	private static final Logger logger = LoggerFactory.getLogger(BatchImportServiceImpl.class);
	@Autowired
	private ContractBizService contractService;

	@SuppressWarnings("all")
	@Override
	public MessageBean invoke(MessageBean messageBean) {
		BatchImportResBean batchImportResBean=new BatchImportResBean();
		try {
			BatchImportReqBean reqBean=(BatchImportReqBean) JSONObject.toBean(JSONObject.fromObject(messageBean.getData()),BatchImportReqBean.class);
			batchImportResBean=BeanCopyUtil.copyBean(BatchImportResBean.class, reqBean);
			ValidateLocator.validateBeans(reqBean);
			List<com.zcbspay.platform.business.merch.bean.BatchImportFileContent> batchImportFileContent=new ArrayList<>();
			
			if (StringUtils.isNotEmpty(reqBean.getFileContent())) {
				JSONArray data = JSONArray.fromObject(FlaterUtils.inflater(reqBean.getFileContent()));	
				List<BatchImportFileContent> batchCollectAndPayFileContents =data.toList(data, BatchImportFileContent.class);
				for (BatchImportFileContent fileContent : batchCollectAndPayFileContents) {
					ValidateLocator.validateBeans(fileContent);
					com.zcbspay.platform.business.merch.bean.BatchImportFileContent fileContentBean = BeanCopyUtil.copyBean(com.zcbspay.platform.business.merch.bean.BatchImportFileContent.class, fileContent);
					batchImportFileContent.add(fileContentBean);
				}
			}
			com.zcbspay.platform.business.merch.bean.BatchImportReqBean batch=new com.zcbspay.platform.business.merch.bean.BatchImportReqBean();
			batch=BeanCopyUtil.copyBean(com.zcbspay.platform.business.merch.bean.BatchImportReqBean.class, batchImportResBean);
			batch.setFileContents(batchImportFileContent);
			ResultBean resultBean =contractService.importBatchContract(batch);
			if (!resultBean.isResultBool()) {
				ResponseTypeEnum responseTypeEnum=ResponseTypeEnum.getTxnTypeEnumByInCode(resultBean.getErrCode());
				if (responseTypeEnum!=null) {
					batchImportResBean.setRespCode(responseTypeEnum.getCode());
					batchImportResBean.setRespMsg(resultBean.getErrMsg().toString());
				}else {
					batchImportResBean.setRespCode(ResponseTypeEnum.fail.getCode());
					batchImportResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
				}
			}else {
				batchImportResBean.setRespCode(ResponseTypeEnum.success.getCode());
				batchImportResBean.setRespMsg(ResponseTypeEnum.success.getMessage());
			}
		} catch (DataErrorException e) {
			e.printStackTrace();
			logger.error(ExceptionUtil.getStackTrace(e));
			batchImportResBean.setRespCode(ResponseTypeEnum.dataError.getCode());
			batchImportResBean.setRespMsg(ResponseTypeEnum.dataError.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtil.getStackTrace(e));
			batchImportResBean.setRespCode(ResponseTypeEnum.fail.getCode());
			batchImportResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
		}
		
		messageBean.setData(JSONObject.fromObject(batchImportResBean).toString());
		return messageBean;
	}

}
