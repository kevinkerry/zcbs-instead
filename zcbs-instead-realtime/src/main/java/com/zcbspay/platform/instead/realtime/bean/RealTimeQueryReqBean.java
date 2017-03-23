package com.zcbspay.platform.instead.realtime.bean;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.zcbspay.platform.instead.common.bean.BaseBean;

/**
 * 实时代付应答bean
 * @author: zhangshd
 * @date:   2017年3月13日 下午1:17:46   
 * @version :v1.0
 */
public class RealTimeQueryReqBean extends BaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8985141679266491477L;
	@Length(max = 32,message="[受理订单号]长度不符合规范")
	@NotEmpty(message="[受理订单号]不能为空")
	private String 	tn	;//	受理订单号
	@Length(max = 2,message="[订单类型]长度不符合规范")
	@NotEmpty(message="[订单类型]不能为空")
	private String orderType;//订单类型 01实时代收,02实时代付
	
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getTn() {
		return tn;
	}

	public void setTn(String tn) {
		this.tn = tn;
	}

}
