package com.zcbspay.platform.instead.batch.service;

import com.zcbspay.platform.support.signaturn.bean.MessageBean;

public interface CollectAndPayService {
	public MessageBean invoke(MessageBean messageBean);

}
