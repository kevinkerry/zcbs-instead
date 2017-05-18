package com.zcbspay.platform.instead.realtime.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zcbspay.platform.instead.common.bean.AdditBean;
import com.zcbspay.platform.instead.common.bean.BaseBean;
import com.zcbspay.platform.instead.common.bean.MessageBean;
import com.zcbspay.platform.instead.common.bean.ResponseBaseBean;
import com.zcbspay.platform.instead.common.bean.UrlBean;
import com.zcbspay.platform.instead.common.constant.Constants;
import com.zcbspay.platform.instead.common.enums.RealTimeTxnTypeEnum;
import com.zcbspay.platform.instead.common.enums.ResponseTypeEnum;
import com.zcbspay.platform.instead.common.utils.AESUtil;
import com.zcbspay.platform.instead.common.utils.DateUtils;
import com.zcbspay.platform.instead.common.utils.HttpRequestParam;
import com.zcbspay.platform.instead.common.utils.HttpUtils;
import com.zcbspay.platform.instead.common.utils.RiskInfoUtils;
import com.zcbspay.platform.instead.realtime.bean.EncryptData;
import com.zcbspay.platform.instead.realtime.bean.RealTimeCollectReqBean;
import com.zcbspay.platform.instead.realtime.bean.RealTimePayReqBean;
import com.zcbspay.platform.instead.realtime.bean.RealTimeQueryReqBean;
import com.zcbspay.platform.instead.realtime.helper.SpringContextHelper;
import com.zcbspay.platform.instead.realtime.service.CollectAndPayService;
import com.zcbspay.platform.instead.realtime.service.EncryptAndDecryptService;

import net.sf.json.JSONObject;

/**
 * 实时代收付API
 * 
 * @author: zhangshd
 * @date: 2017年3月13日 下午2:35:16
 * @version :v1.0
 */
@Controller
@RequestMapping("/realtime/")
public class RealTimeController {
	private CollectAndPayService collectAndPaySerivce;
	//@Autowired
	//private MessageDecodeService messageDecodeService;
	//@Autowired
	//private MessageEncryptService messageEncryptService;

	@Autowired
	private UrlBean urlBean;
	
	@Autowired
	private EncryptAndDecryptService encryptAndDecryptService;

	private static final String MER_ID = "200000000000610";
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
		MessageBean requestBean = null;
		ResponseBaseBean responseBaseBean = new ResponseBaseBean();
		HttpUtils httpUtils = new HttpUtils();
		try {
			// 验签,解密
			//requestBean = messageDecodeService.decodeAndVerify(messageBean);
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
			return encrypt(responseBaseBean, messageBean);
		}finally {
			httpUtils.closeConnection();
		}
		try {
			// 获取基础信息
			BaseBean baseBean = (BaseBean) JSONObject.toBean(JSONObject.fromObject(requestBean.getData()),
					BaseBean.class);
			// 确定处理类
			collectAndPaySerivce = (CollectAndPayService) SpringContextHelper
					.getBean(RealTimeTxnTypeEnum.getTxnTypeEnum(baseBean.getTxnType()).getClassName());
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
			requestBean=(MessageBean) JSONObject.toBean(JSONObject.fromObject(responseContent),MessageBean.class);
			return requestBean;
			/*return messageEncryptService.encryptAndSigntrue(requestBean.getData(),
					prepareAdditbean(((AdditBean) JSONObject.toBean(JSONObject.fromObject(messageBean.getAddit()), AdditBean.class)).getMerId()));*/
		} catch (Exception e) {
			e.printStackTrace();
			responseBaseBean.setRespCode(ResponseTypeEnum.fail.getCode());
			responseBaseBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
			return encrypt(responseBaseBean, messageBean);
		}finally {
			httpUtils.closeConnection();
		}
	}

	private MessageBean encrypt(ResponseBaseBean responseBaseBean, MessageBean messageBean) {
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
		}finally {
			httpUtils.closeConnection();
		}
	}

	@RequestMapping("i")
	public String index() {
		return "index";
	}

	@ResponseBody
	@RequestMapping("paysubmit")
	public MessageBean paysubmit(RealTimePayReqBean realTimePayReqBean, EncryptData encryptData) {
		AdditBean additBean = new AdditBean();
		additBean.setMerId(MER_ID);
		additBean.setAccessType("1");
		realTimePayReqBean.setEncryptData(
				encryptAndDecryptService.encrypt(additBean, JSONObject.fromObject(encryptData).toString()));
		return encryptData(realTimePayReqBean);

	}

	@ResponseBody
	@RequestMapping("collectsubmit")
	public MessageBean collectsubmit(RealTimeCollectReqBean realTimeCollectReqBean, EncryptData encryptData) {

		AdditBean additBean = new AdditBean();
		additBean.setMerId(MER_ID);
		additBean.setAccessType("1");
		realTimeCollectReqBean.setEncryptData(
				encryptAndDecryptService.encrypt(additBean, JSONObject.fromObject(encryptData).toString()));
		return encryptData(realTimeCollectReqBean);
	}

	@ResponseBody
	@RequestMapping("tradequery")
	public MessageBean tradequery(RealTimeQueryReqBean realTimeQueryReqBean) {
		// 确定处理类
		collectAndPaySerivce = (CollectAndPayService) SpringContextHelper
				.getBean(RealTimeTxnTypeEnum.getTxnTypeEnum(realTimeQueryReqBean.getTxnType()).getClassName());
		MessageBean messageBean =new MessageBean();
		messageBean.setData(JSONObject.fromObject(realTimeQueryReqBean).toString());
		collectAndPaySerivce.invoke(messageBean);
		return encryptData(realTimeQueryReqBean);
	}

	private MessageBean encryptData(Object object) {
		MessageBean requestBean=null;
		AdditBean additBean = new AdditBean();
		additBean.setAccessType("1");
		additBean.setMerId(MER_ID);
		additBean.setEncryMethod("01");
		HttpUtils httpUtils = new HttpUtils();
		try {
			Map<String, Object> riskInfo = new TreeMap<String, Object>();
			riskInfo.put("random", RiskInfoUtils.randomInt(32));
			riskInfo.put("timestamp", DateUtils.geCurrentDateTimeStr());
			riskInfo.put("deviceID", RiskInfoUtils.getMacAddress());
			additBean.setRiskInfo(JSONObject.fromObject(riskInfo).toString());
			
			HttpRequestParam httpRequestParam1= new HttpRequestParam("enData",JSONObject.fromObject(object).toString());
			HttpRequestParam httpRequestParam2= new HttpRequestParam("additBean",JSONObject.fromObject(additBean).toString());
			List<HttpRequestParam> listen = new ArrayList<>();
			listen.add(httpRequestParam1);
			listen.add(httpRequestParam2);
			String url =urlBean.getEncryptUrl();
			
			httpUtils.openConnection();
			String responseContent = httpUtils.executeHttpPost(url,listen,Constants.Encoding.UTF_8);
			
			requestBean=(MessageBean) JSONObject.toBean(JSONObject.fromObject(responseContent),MessageBean.class);
			return requestBean;
			
			/*return messageEncryptService.encryptAndSigntrue(JSONObject.fromObject(object).toString(), additBean);*/
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			httpUtils.closeConnection();
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
