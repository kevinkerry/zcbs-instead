package com.zcbspay.platform.instead.batch.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.business.concentrate.bean.FileContentBean;
import com.zcbspay.platform.business.merch.bean.ContractQueryFileContent;
import com.zcbspay.platform.business.merch.bean.ResultBean;
import com.zcbspay.platform.business.merch.service.ContractBizService;
import com.zcbspay.platform.instead.batch.bean.ContractQueryReqBean;
import com.zcbspay.platform.instead.batch.bean.ContractQueryResBean;
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
@Service("contractQueryService")
public class ContractQueryServiceImpl implements CollectAndPayService {
	private static final Logger logger = LoggerFactory.getLogger(ContractQueryServiceImpl.class);
	@Autowired
	private ContractBizService contractService;

	@Override
	public MessageBean invoke(MessageBean messageBean) {
		ContractQueryResBean contractQueryResBean=new ContractQueryResBean();
		try {
			ContractQueryReqBean reqBean = (ContractQueryReqBean) JSONObject.toBean(JSONObject.fromObject(messageBean.getData()),
					ContractQueryReqBean.class);
			contractQueryResBean = BeanCopyUtil.copyBean(ContractQueryResBean.class, reqBean);
			ValidateLocator.validateBeans(reqBean);
			ResultBean resultBean = null;
			resultBean=contractService.findByCode(contractQueryResBean.getContractnum());
			if (!resultBean.isResultBool()) {
				ResponseTypeEnum responseTypeEnum=ResponseTypeEnum.getTxnTypeEnumByInCode(resultBean.getErrCode());
				if (responseTypeEnum!=null) {
					contractQueryResBean.setRespCode(responseTypeEnum.getCode());
					contractQueryResBean.setRespMsg(resultBean.getErrMsg().toString());
				}else {
					contractQueryResBean.setRespCode(ResponseTypeEnum.fail.getCode());
					contractQueryResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
				}
			} else {
				contractQueryResBean.setRespCode(ResponseTypeEnum.success.getCode());
				contractQueryResBean.setRespMsg(ResponseTypeEnum.success.getMessage());
				
				ContractQueryFileContent contractQueryFileContent = (ContractQueryFileContent)resultBean.getResultObj();
				contractQueryResBean.setContractContent(
						FlaterUtils.deflater(JSONObject.fromObject(contractQueryFileContent).toString()));
			}
		} catch (DataErrorException e) {
			e.printStackTrace();
			logger.error(ExceptionUtil.getStackTrace(e));
			contractQueryResBean.setRespCode(ResponseTypeEnum.dataError.getCode());
			contractQueryResBean.setRespMsg(ResponseTypeEnum.dataError.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtil.getStackTrace(e));
			contractQueryResBean.setRespCode(ResponseTypeEnum.fail.getCode());
			contractQueryResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
		}
		messageBean.setData(JSONObject.fromObject(contractQueryResBean).toString());
		return messageBean;
	}

}
