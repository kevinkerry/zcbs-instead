package com.zcbspay.platform.instead.common.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.zcbspay.platform.instead.batch.exception.DataErrorException;
import com.zcbspay.platform.instead.common.bean.ValidateResultBean;

public class ValidateLocator {

	private static Validator validator;
	public static final String SYSTEM_ERROR = "订单信息错误,请重新提交订单";
	public static <T> ValidateResultBean validateBeans(T obj) {
		ValidateResultBean resultBean = null;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
		String message = SYSTEM_ERROR;
		for (ConstraintViolation<T> constraintViolation : constraintViolations) {
			message = constraintViolation.getMessage() == null ? SYSTEM_ERROR : constraintViolation.getMessage();
			resultBean = new ValidateResultBean(constraintViolation.getPropertyPath().toString(), message);
			if (!resultBean.isResultBool()) {// 非空，长度检查出现异常，非法数据
				throw new DataErrorException("非法数据:字段" + resultBean.getErrMsg());
			}
			return resultBean;
		}
		resultBean = new ValidateResultBean(obj);
		return resultBean;
	}

}
