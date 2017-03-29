package com.zcbspay.platform.instead.realtime.controller;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zcbspay.platform.instead.common.bean.BaseBean;
import com.zcbspay.platform.instead.common.bean.ResponseBaseBean;
import com.zcbspay.platform.instead.common.enums.RealTimeTxnTypeEnum;
import com.zcbspay.platform.instead.common.enums.ResponseTypeEnum;
import com.zcbspay.platform.instead.common.utils.AESUtil;
import com.zcbspay.platform.instead.realtime.bean.EncryptData;
import com.zcbspay.platform.instead.realtime.bean.RealTimeCollectReqBean;
import com.zcbspay.platform.instead.realtime.bean.RealTimePayReqBean;
import com.zcbspay.platform.instead.realtime.bean.RealTimeQueryReqBean;
import com.zcbspay.platform.instead.realtime.helper.SpringContextHelper;
import com.zcbspay.platform.instead.realtime.service.CollectAndPayService;
import com.zcbspay.platform.instead.realtime.service.EncryptAndDecryptService;
import com.zcbspay.platform.support.signaturn.bean.AdditBean;
import com.zcbspay.platform.support.signaturn.bean.MessageBean;
import com.zcbspay.platform.support.signaturn.service.MessageDecodeService;
import com.zcbspay.platform.support.signaturn.service.MessageEncryptService;

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
	@Autowired
	private MessageDecodeService messageDecodeService;
	@Autowired
	private MessageEncryptService messageEncryptService;

	@Autowired
	private EncryptAndDecryptService encryptAndDecryptService;

	private static final String COOP_INSTI_CODE = "300000000000004"; // test
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
		try {
			// 验签,解密
			requestBean = messageDecodeService.decodeAndVerify(messageBean);
		} catch (Exception e) {
			e.printStackTrace();
			responseBaseBean.setRespCode(ResponseTypeEnum.decodeError.getCode());
			responseBaseBean.setRespMsg(ResponseTypeEnum.decodeError.getMessage());
			return encrypt(responseBaseBean, messageBean);
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
			return messageEncryptService.encryptAndSigntrue(requestBean.getData(),
					(AdditBean) JSONObject.toBean(JSONObject.fromObject(requestBean.getAddit()), AdditBean.class));
		} catch (Exception e) {
			e.printStackTrace();
			responseBaseBean.setRespCode(ResponseTypeEnum.fail.getCode());
			responseBaseBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
			return encrypt(responseBaseBean, messageBean);
		}
	}

	private MessageBean encrypt(ResponseBaseBean responseBaseBean, MessageBean messageBean) {
		try {
			return messageEncryptService.encryptAndSigntrue(JSONObject.fromObject(responseBaseBean).toString(),
					(AdditBean) JSONObject.toBean(JSONObject.fromObject(messageBean.getAddit()), AdditBean.class));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
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
//		collectAndPaySerivce.invoke(messageBean);

		return encryptData(realTimeQueryReqBean);
	}

	private MessageBean encryptData(Object object) {
		AdditBean additBean = new AdditBean();
		additBean.setAccessType("1");
		additBean.setCoopInstiId(COOP_INSTI_CODE);
		additBean.setMerId(MER_ID);
		additBean.setEncryMethod("02");
		try {
			Map<String, Object> riskInfo = new TreeMap<String, Object>();
			riskInfo.put("random", AESUtil.getAESKey());
			riskInfo.put("os", "browser");
			riskInfo.put("timestamp", System.currentTimeMillis());
			riskInfo.put("deviceID", "000000");
			additBean.setRiskInfo(JSONObject.fromObject(riskInfo).toString());
			return messageEncryptService.encryptAndSigntrue(JSONObject.fromObject(object).toString(), additBean);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
