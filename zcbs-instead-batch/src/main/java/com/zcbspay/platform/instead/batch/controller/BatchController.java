package com.zcbspay.platform.instead.batch.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zcbspay.platform.instead.batch.helper.SpringContextHelper;
import com.zcbspay.platform.instead.batch.service.CollectAndPayService;
import com.zcbspay.platform.instead.common.bean.AdditBean;
import com.zcbspay.platform.instead.common.bean.BaseBean;
import com.zcbspay.platform.instead.common.bean.MessageBean;
import com.zcbspay.platform.instead.common.bean.ResponseBaseBean;
import com.zcbspay.platform.instead.common.bean.UrlBean;
import com.zcbspay.platform.instead.common.constant.Constants;
import com.zcbspay.platform.instead.common.enums.BatchTxnTypeEnum;
import com.zcbspay.platform.instead.common.enums.ResponseTypeEnum;
import com.zcbspay.platform.instead.common.utils.AESUtil;
import com.zcbspay.platform.instead.common.utils.DateUtils;
import com.zcbspay.platform.instead.common.utils.HttpRequestParam;
import com.zcbspay.platform.instead.common.utils.HttpUtils;
import com.zcbspay.platform.instead.common.utils.LogHelper;
import com.zcbspay.platform.instead.common.utils.RiskInfoUtils;

import net.sf.json.JSONObject;

/**
 * 批量代收付接口
 * 
 * @author: zhangshd
 * @date: 2017年2月24日 上午10:04:26
 * @version :1.0
 */
@Controller
@RequestMapping("/batch/")
public class BatchController {

	private Logger log = LoggerFactory.getLogger(LogHelper.class);
	
	private CollectAndPayService collectAndPaySerivce;

	@Autowired
	private UrlBean urlBean;
	
	
	private final String accessType="1";
	
	private final String encryMethod="01";
	/**
	 * 批量代收付Api
	 * 
	 * @author: zhangshd
	 * @return Map<String,Object>
	 * @date: 2017年3月13日 下午2:37:11
	 * @version v1.0
	 */
	@ResponseBody
	@RequestMapping("payAndCollectApi")
	public MessageBean payAndCollectApi(MessageBean messageBean) {
		log.info(JSONObject.fromObject(messageBean).toString());
		MessageBean requestBean=null;
		ResponseBaseBean responseBaseBean = new ResponseBaseBean();
		HttpUtils httpUtils = new HttpUtils();
		try {
			// 验签,解密
			HttpRequestParam httpRequestParam= new HttpRequestParam("data",JSONObject.fromObject(messageBean).toString());
			List<HttpRequestParam> list = new ArrayList<>();
			list.add(httpRequestParam);
			String url =urlBean.getDecodeUrl();//"http://localhost:9911/fe/sign/decode";
			
			httpUtils.openConnection();
			String responseContent = httpUtils.executeHttpPost(url,list,Constants.Encoding.UTF_8);
			requestBean=(MessageBean) JSONObject.toBean(JSONObject.fromObject(responseContent),MessageBean.class);
			
		} catch (Exception e) {
			e.printStackTrace();
			responseBaseBean.setRespCode(ResponseTypeEnum.decodeError.getCode());
			responseBaseBean.setRespMsg(ResponseTypeEnum.decodeError.getMessage());
			return encrypt(responseBaseBean,messageBean);
		}finally{
			httpUtils.closeConnection();
		}
		
		try {
			// 获取基础信息
			BaseBean baseBean = (BaseBean) JSONObject.toBean(JSONObject.fromObject(requestBean.getData()),
					BaseBean.class);
			// 确定处理类
			collectAndPaySerivce = (CollectAndPayService) SpringContextHelper
					.getBean(BatchTxnTypeEnum.getTxnTypeEnum(baseBean.getTxnType()).getClassName());
			// 真正处理
			requestBean = collectAndPaySerivce.invoke(requestBean);

			HttpRequestParam httpRequestParam1= new HttpRequestParam("enData",requestBean.getData());
			HttpRequestParam httpRequestParam2= new HttpRequestParam("additBean",JSONObject.fromObject(prepareAdditbean(((AdditBean) JSONObject.toBean(JSONObject.fromObject(messageBean.getAddit()), AdditBean.class)).getMerId())).toString());
			List<HttpRequestParam> listen = new ArrayList<>();
			listen.add(httpRequestParam1);
			listen.add(httpRequestParam2);
			String url =urlBean.getEncryptUrl();
			httpUtils.openConnection();
			String responseContent = httpUtils.executeHttpPost(url,listen,Constants.Encoding.UTF_8);
			httpUtils.closeConnection();
			requestBean=(MessageBean) JSONObject.toBean(JSONObject.fromObject(responseContent),MessageBean.class);
			return requestBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBaseBean.setRespCode(ResponseTypeEnum.fail.getCode());
			responseBaseBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
			return encrypt(responseBaseBean,messageBean);
		}
		finally{
			httpUtils.closeConnection();
		}
	}

	private MessageBean encrypt(ResponseBaseBean responseBaseBean,MessageBean messageBean){
		MessageBean requestBean=null;
		HttpUtils httpUtils = new HttpUtils();
		try {
			HttpRequestParam httpRequestParam1= new HttpRequestParam("enData",JSONObject.fromObject(responseBaseBean).toString());
			HttpRequestParam httpRequestParam2= new HttpRequestParam("additBean",JSONObject.fromObject(prepareAdditbean(((AdditBean) JSONObject.toBean(JSONObject.fromObject(messageBean.getAddit()), AdditBean.class)).getMerId())).toString());
			List<HttpRequestParam> listen = new ArrayList<>();
			listen.add(httpRequestParam1);
			listen.add(httpRequestParam2);
			String url =urlBean.getEncryptUrl();
			httpUtils.openConnection();
			String responseContent = httpUtils.executeHttpPost(url,listen,Constants.Encoding.UTF_8);
			requestBean=(MessageBean) JSONObject.toBean(JSONObject.fromObject(responseContent),MessageBean.class);
			return requestBean;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			httpUtils.closeConnection();
		}
	}
	
	
	private AdditBean prepareAdditbean(String merid){
		AdditBean additBean = new AdditBean();
		try {
			additBean.setEncryKey(AESUtil.getAESKey());
			additBean.setAccessType(accessType);
			additBean.setMerId(merid);
			additBean.setEncryMethod(encryMethod);
			Map<String, Object> riskInfo = new TreeMap<String, Object>();
			riskInfo.put("random", RiskInfoUtils.randomInt(32));
			riskInfo.put("timestamp", DateUtils.geCurrentDateTimeStr());
			riskInfo.put("deviceID", RiskInfoUtils.getMacAddress());
			additBean.setRiskInfo(JSONObject.fromObject(riskInfo).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return additBean;
	}
	
	
	/*@RequestMapping("i")
	public String index() {
		return "index";
	}*/

	/*@ResponseBody
	@RequestMapping("paysubmit")
	public MessageBean paysubmit(BatchPayReqBean realTimePayReqBean,
			BatchCollectAndPayFileContent batchCollectAndPayFileContent) {
		List<BatchCollectAndPayFileContent> list = new ArrayList<>();
			BatchCollectAndPayFileContent b1=BeanCopyUtil.copyBean(BatchCollectAndPayFileContent.class, batchCollectAndPayFileContent);
			b1.setOrderId(String.valueOf(System.currentTimeMillis()));
			BatchCollectAndPayFileContent b2=BeanCopyUtil.copyBean(BatchCollectAndPayFileContent.class, batchCollectAndPayFileContent);
			b2.setOrderId(String.valueOf(System.currentTimeMillis()-3));
			list.add(b1);
			list.add(b2);
		
		realTimePayReqBean.setFileContent(FlaterUtils.deflater(JSONArray.fromObject(list).toString()));
		return encryptData(realTimePayReqBean,realTimePayReqBean.getMerId());

	}*/

	/*@ResponseBody
	@RequestMapping("collectsubmit")
	public MessageBean collectsubmit(BatchCollectReqBean realTimeCollectReqBean,
			BatchCollectAndPayFileContent batchCollectAndPayFileContent) {
		List<BatchCollectAndPayFileContent> list = new ArrayList<>();
		
		BatchCollectAndPayFileContent b1=BeanCopyUtil.copyBean(BatchCollectAndPayFileContent.class, batchCollectAndPayFileContent);
		b1.setOrderId(String.valueOf(System.currentTimeMillis()));
		BatchCollectAndPayFileContent b2=BeanCopyUtil.copyBean(BatchCollectAndPayFileContent.class, batchCollectAndPayFileContent);
		b2.setOrderId(String.valueOf(System.currentTimeMillis()-3));
		list.add(b1);
		list.add(b2);
		
		realTimeCollectReqBean.setFileContent(FlaterUtils.deflater(JSONArray.fromObject(list).toString()));
		return encryptData(realTimeCollectReqBean,realTimeCollectReqBean.getMerId());
	}*/
	/*@ResponseBody
	@RequestMapping("batchimportsubmit")
	public MessageBean batchimportsubmit(BatchImportReqBean batchImportReqBean,
			BatchImportFileContent batchCollectAndPayFileContent) {
		List<BatchImportFileContent> list = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			BatchImportFileContent batchImportFileContent=new BatchImportFileContent();
			batchImportFileContent=BeanCopyUtil.copyBean(BatchImportFileContent.class, batchCollectAndPayFileContent);
			batchImportFileContent.setContractnum(String.valueOf(System.currentTimeMillis()+i));
			System.out.println(System.currentTimeMillis()+i);
			list.add(batchImportFileContent);
		}
		batchImportReqBean.setFileContent(FlaterUtils.deflater(JSONArray.fromObject(list).toString()));
		return encryptData(batchImportReqBean,batchImportReqBean.getMerId());
	}*/
	/*@ResponseBody
	@RequestMapping("contractsubmit")
	public MessageBean contractsubmit(ContractQueryReqBean contractQueryReqBean) {
		return encryptData(contractQueryReqBean,contractQueryReqBean.getMerId());
	}*/
	/*@ResponseBody
	@RequestMapping("tradequery")
	public MessageBean tradequery(BatchQueryReqBean realTimeQueryReqBean) {
		return encryptData(realTimeQueryReqBean,realTimeQueryReqBean.getMerId());
	}*/

	/*private MessageBean encryptData(Object object,String memberId) {
		MessageBean requestBean=null;
		AdditBean additBean = new AdditBean();
		additBean.setAccessType(accessType);
		additBean.setMerId(memberId);
		additBean.setEncryMethod(encryMethod);
		try {
			Map<String, Object> riskInfo = new TreeMap<String, Object>();
			riskInfo.put("random", RiskInfoUtils.randomInt(32));
			riskInfo.put("timestamp", DateUtils.getCurrentDateString());
			riskInfo.put("deviceID", RiskInfoUtils.getMacAddress());
			additBean.setRiskInfo(JSONObject.fromObject(riskInfo).toString());
			
			HttpRequestParam httpRequestParam1= new HttpRequestParam("enData",JSONObject.fromObject(object).toString());
			HttpRequestParam httpRequestParam2= new HttpRequestParam("additBean",JSONObject.fromObject(additBean).toString());
			List<HttpRequestParam> listen = new ArrayList<>();
			listen.add(httpRequestParam1);
			listen.add(httpRequestParam2);
			String url =urlBean.getEncryptUrl();//"http://localhost:9911/fe/sign/encrypt";
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.openConnection();
			String responseContent = httpUtils.executeHttpPost(url,listen,Constants.Encoding.UTF_8);
			httpUtils.closeConnection();
			requestBean=(MessageBean) JSONObject.toBean(JSONObject.fromObject(responseContent),MessageBean.class);
			return requestBean;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}*/
	
	
	
}
