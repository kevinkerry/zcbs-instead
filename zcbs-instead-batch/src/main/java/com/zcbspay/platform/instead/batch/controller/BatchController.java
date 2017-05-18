package com.zcbspay.platform.instead.batch.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zcbspay.platform.instead.batch.bean.BatchCollectAndPayFileContent;
import com.zcbspay.platform.instead.batch.bean.BatchCollectReqBean;
import com.zcbspay.platform.instead.batch.bean.BatchImportFileContent;
import com.zcbspay.platform.instead.batch.bean.BatchImportReqBean;
import com.zcbspay.platform.instead.batch.bean.BatchPayReqBean;
import com.zcbspay.platform.instead.batch.bean.BatchQueryReqBean;
import com.zcbspay.platform.instead.batch.bean.ContractQueryReqBean;
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
import com.zcbspay.platform.instead.common.utils.BeanCopyUtil;
import com.zcbspay.platform.instead.common.utils.DateUtils;
import com.zcbspay.platform.instead.common.utils.FlaterUtils;
import com.zcbspay.platform.instead.common.utils.HttpRequestParam;
import com.zcbspay.platform.instead.common.utils.HttpUtils;
import com.zcbspay.platform.instead.common.utils.RiskInfoUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 扣率相关的类
 * 
 * @author: zhangshd
 * @date: 2017年2月24日 上午10:04:26
 * @version :1.0
 */
@Controller
@RequestMapping("/batch/")
public class BatchController {

	private CollectAndPayService collectAndPaySerivce;
	//@Autowired
	//private MessageDecodeService messageDecodeService;
	//@Autowired
	//private MessageEncryptService messageEncryptService;

	private static final String MER_ID = "200000000000610";

	@Autowired
	private UrlBean urlBean;
	
	/**
	 * 实时代收付接口
	 * 
	 * @author: zhangshd
	 * @return Map<String,Object>
	 * @date: 2017年3月13日 下午2:37:11
	 * @version v1.0
	 */
	@ResponseBody
	@RequestMapping("payAndCollectApi")
	public MessageBean payAndCollectApi(MessageBean messageBean) {
		MessageBean requestBean=null;
		ResponseBaseBean responseBaseBean = new ResponseBaseBean();
		HttpUtils httpUtils = new HttpUtils();
		try {
			// 验签,解密
			//requestBean= messageDecodeService.decodeAndVerify(messageBean);
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
			
			/*return messageEncryptService.encryptAndSigntrue(requestBean.getData(),
					prepareAdditbean(((AdditBean) JSONObject.toBean(JSONObject.fromObject(messageBean.getAddit()), AdditBean.class)).getMerId()));*/
			
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
			/*return messageEncryptService.encryptAndSigntrue(JSONObject.fromObject(responseBaseBean).toString(),
					prepareAdditbean(((AdditBean) JSONObject.toBean(JSONObject.fromObject(messageBean.getAddit()), AdditBean.class)).getMerId()));*/
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			httpUtils.closeConnection();
		}
	}
	@RequestMapping("i")
	public String index() {
		return "index";
	}

	@ResponseBody
	@RequestMapping("paysubmit")
	public MessageBean paysubmit(BatchPayReqBean realTimePayReqBean,
			BatchCollectAndPayFileContent batchCollectAndPayFileContent) {
		List<BatchCollectAndPayFileContent> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			list.add(batchCollectAndPayFileContent);
		}
		realTimePayReqBean.setFileContent(FlaterUtils.deflater(JSONArray.fromObject(list).toString()));
		
		/*// 确定处理类
		collectAndPaySerivce = (CollectAndPayService) SpringContextHelper
							.getBean(BatchTxnTypeEnum.getTxnTypeEnum(realTimePayReqBean.getTxnType()).getClassName());
		MessageBean messageBean=new MessageBean();
		messageBean.setData(JSONObject.fromObject(realTimePayReqBean).toString());
		collectAndPaySerivce.invoke(messageBean);*/
		
		
		return encryptData(realTimePayReqBean);

	}

	@ResponseBody
	@RequestMapping("collectsubmit")
	public MessageBean collectsubmit(BatchCollectReqBean realTimeCollectReqBean,
			BatchCollectAndPayFileContent batchCollectAndPayFileContent) {
		List<BatchCollectAndPayFileContent> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			list.add(batchCollectAndPayFileContent);
		}
		realTimeCollectReqBean.setFileContent(FlaterUtils.deflater(JSONArray.fromObject(list).toString()));
		return encryptData(realTimeCollectReqBean);
	}
	@ResponseBody
	@RequestMapping("batchimportsubmit")
	public MessageBean batchimportsubmit(BatchImportReqBean batchImportReqBean,
			BatchImportFileContent batchCollectAndPayFileContent) {
		List<BatchImportFileContent> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			BatchImportFileContent batchImportFileContent=new BatchImportFileContent();
			batchImportFileContent=BeanCopyUtil.copyBean(BatchImportFileContent.class, batchCollectAndPayFileContent);
			batchImportFileContent.setContractnum("00000"+i);
			//batchCollectAndPayFileContent.setContractnum("00000");
			list.add(batchImportFileContent);
		}
		batchImportReqBean.setFileContent(FlaterUtils.deflater(JSONArray.fromObject(list).toString()));
		return encryptData(batchImportReqBean);
	}
	@ResponseBody
	@RequestMapping("contractsubmit")
	public MessageBean contractsubmit(ContractQueryReqBean contractQueryReqBean) {
		return encryptData(contractQueryReqBean);
	}
	@ResponseBody
	@RequestMapping("tradequery")
	public MessageBean tradequery(BatchQueryReqBean realTimeQueryReqBean) {
		return encryptData(realTimeQueryReqBean);
	}

	private MessageBean encryptData(Object object) {
		MessageBean requestBean=null;
		AdditBean additBean = new AdditBean();
		additBean.setAccessType("1");
		additBean.setMerId(MER_ID);
		additBean.setEncryMethod("02");
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
			//return messageEncryptService.encryptAndSigntrue(JSONObject.fromObject(object).toString(), additBean);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	private AdditBean prepareAdditbean(String merid){
		AdditBean additBean = new AdditBean();
		try {
			additBean.setEncryKey(AESUtil.getAESKey());
			additBean.setAccessType("1");
			additBean.setMerId(merid);
			additBean.setEncryMethod("01");
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
}
