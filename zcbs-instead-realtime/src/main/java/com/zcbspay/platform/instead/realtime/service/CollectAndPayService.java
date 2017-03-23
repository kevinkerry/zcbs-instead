package com.zcbspay.platform.instead.realtime.service;

import com.zcbspay.platform.support.signaturn.bean.MessageBean;

public interface CollectAndPayService {
	public MessageBean invoke(MessageBean messageBean);

}
