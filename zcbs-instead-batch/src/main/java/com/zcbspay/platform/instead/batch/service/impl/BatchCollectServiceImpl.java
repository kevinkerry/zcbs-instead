package com.zcbspay.platform.instead.batch.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.business.concentrate.batch.service.BatchCollection;
import com.zcbspay.platform.business.concentrate.bean.BatchCollectionBean;
import com.zcbspay.platform.business.concentrate.bean.FileContentBean;
import com.zcbspay.platform.business.concentrate.bean.ResultBean;
import com.zcbspay.platform.instead.batch.bean.BatchCollectAndPayFileContent;
import com.zcbspay.platform.instead.batch.bean.BatchCollectReqBean;
import com.zcbspay.platform.instead.batch.bean.BatchCollectResBean;
import com.zcbspay.platform.instead.batch.service.CollectAndPayService;
import com.zcbspay.platform.instead.common.enums.ResponseTypeEnum;
import com.zcbspay.platform.instead.common.utils.BeanCopyUtil;
import com.zcbspay.platform.instead.common.utils.FlaterUtils;
import com.zcbspay.platform.instead.common.utils.ValidateLocator;
import com.zcbspay.platform.support.signaturn.bean.MessageBean;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service("batchCollectService")
public class BatchCollectServiceImpl implements CollectAndPayService{
	@Autowired
	private BatchCollection BatchCollection;
	@SuppressWarnings({ "unchecked", "deprecation", "static-access" })
	@Override
	public MessageBean invoke(MessageBean messageBean) {
		BatchCollectReqBean reqBean=(BatchCollectReqBean) JSONObject.toBean(JSONObject.fromObject(messageBean.getData()),BatchCollectReqBean.class);
		BatchCollectResBean batchCollectResBean=BeanCopyUtil.copyBean(BatchCollectResBean.class, reqBean);
		try {
			ValidateLocator.validateBeans(reqBean);
		} catch (Exception e) {
			e.printStackTrace();
			batchCollectResBean.setRespCode(ResponseTypeEnum.dataError.getCode());
			batchCollectResBean.setRespMsg(ResponseTypeEnum.dataError.getMessage());
		}
		BatchCollectionBean batchCollectionBean=BeanCopyUtil.copyBean(BatchCollectionBean.class, reqBean);
		List<FileContentBean> fileContents=new ArrayList<>();
		if (StringUtils.isNotEmpty(reqBean.getFileContent())) {
			JSONArray data = JSONArray.fromObject(FlaterUtils.inflater(reqBean.getFileContent()));	
			List<BatchCollectAndPayFileContent> batchCollectAndPayFileContents =data.toList(data, BatchCollectAndPayFileContent.class);
			for (BatchCollectAndPayFileContent batchCollectAndPayFileContent : batchCollectAndPayFileContents) {
				ValidateLocator.validateBeans(batchCollectAndPayFileContent);
				FileContentBean fileContentBean = BeanCopyUtil.copyBean(FileContentBean.class, batchCollectAndPayFileContent);
				fileContents.add(fileContentBean);
			}
			
		}
		batchCollectionBean.setFileContent(fileContents);
		ResultBean resultBean =BatchCollection.pay(batchCollectionBean);
		if (!resultBean.isResultBool()) {
			batchCollectResBean.setRespCode(ResponseTypeEnum.fail.getCode());
			batchCollectionBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
		}else {
			batchCollectResBean.setRespCode(ResponseTypeEnum.success.getCode());
			batchCollectResBean.setRespMsg(ResponseTypeEnum.success.getMessage());
		}
		messageBean.setData(JSONObject.fromObject(batchCollectResBean).toString());
		return messageBean;
	}
}
