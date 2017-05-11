package com.zcbspay.platform.instead.batch.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.business.concentrate.batch.service.BatchCollection;
import com.zcbspay.platform.business.concentrate.bean.BatchCollectionBean;
import com.zcbspay.platform.business.concentrate.bean.FileContentBean;
import com.zcbspay.platform.business.concentrate.bean.ResultBean;
import com.zcbspay.platform.instead.batch.bean.BatchCollectAndPayFileContent;
import com.zcbspay.platform.instead.batch.bean.BatchCollectReqBean;
import com.zcbspay.platform.instead.batch.bean.BatchCollectResBean;
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
 * 批量代收服务
 * @author: zhangshd
 * @date:   2017年5月3日 上午9:47:43   
 * @version :v1.0
 */
@Service("batchCollectService")
public class BatchCollectServiceImpl implements CollectAndPayService {
	private static final Logger logger = LoggerFactory.getLogger(BatchCollectServiceImpl.class);
	@Autowired
	private BatchCollection BatchCollection;

	@SuppressWarnings({ "unchecked", "deprecation", "static-access" })
	@Override
	public MessageBean invoke(MessageBean messageBean) {
		BatchCollectResBean batchCollectResBean = new BatchCollectResBean();
		try {
			BatchCollectReqBean reqBean = (BatchCollectReqBean) JSONObject
					.toBean(JSONObject.fromObject(messageBean.getData()), BatchCollectReqBean.class);
			batchCollectResBean = BeanCopyUtil.copyBean(BatchCollectResBean.class, reqBean);
			ValidateLocator.validateBeans(reqBean);
			BatchCollectionBean batchCollectionBean = BeanCopyUtil.copyBean(BatchCollectionBean.class, reqBean);
			List<FileContentBean> fileContents = new ArrayList<>();
			if (StringUtils.isNotEmpty(reqBean.getFileContent())) {
				JSONArray data = JSONArray.fromObject(FlaterUtils.inflater(reqBean.getFileContent()));
				List<BatchCollectAndPayFileContent> batchCollectAndPayFileContents = data.toList(data,
						BatchCollectAndPayFileContent.class);
				for (BatchCollectAndPayFileContent batchCollectAndPayFileContent : batchCollectAndPayFileContents) {
					ValidateLocator.validateBeans(batchCollectAndPayFileContent);
					FileContentBean fileContentBean = BeanCopyUtil.copyBean(FileContentBean.class,
							batchCollectAndPayFileContent);
					fileContents.add(fileContentBean);
				}

			}
			batchCollectionBean.setFileContent(fileContents);
			ResultBean resultBean = BatchCollection.pay(batchCollectionBean);
			if (!resultBean.isResultBool()) {
				ResponseTypeEnum responseTypeEnum=ResponseTypeEnum.getTxnTypeEnumByInCode(resultBean.getErrCode());
				if (responseTypeEnum!=null) {
					batchCollectResBean.setRespCode(responseTypeEnum.getCode());
					batchCollectResBean.setRespMsg(resultBean.getErrMsg().toString());
				}else {
					batchCollectResBean.setRespCode(ResponseTypeEnum.fail.getCode());
					batchCollectResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
				}
			} else {
				batchCollectResBean.setTn(resultBean.getResultObj().toString());
				batchCollectResBean.setRespCode(ResponseTypeEnum.success.getCode());
				batchCollectResBean.setRespMsg(ResponseTypeEnum.success.getMessage());
			}
		} catch (DataErrorException e) {
			batchCollectResBean.setRespCode(ResponseTypeEnum.dataError.getCode());
			batchCollectResBean.setRespMsg(ResponseTypeEnum.dataError.getMessage());
			e.printStackTrace();
			logger.error(ExceptionUtil.getStackTrace(e));
		} catch (Exception e) {
			batchCollectResBean.setRespCode(ResponseTypeEnum.fail.getCode());
			batchCollectResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
			e.printStackTrace();
			logger.error(ExceptionUtil.getStackTrace(e));
		}
		messageBean.setData(JSONObject.fromObject(batchCollectResBean).toString());
		return messageBean;
	}
}
