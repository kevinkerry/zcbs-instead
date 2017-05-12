package com.zcbspay.platform.instead.batch.bean;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 合同查询导入内容类
 * 
 * @author: zhangshd
 * @date: 2017年3月13日 下午1:17:46
 * @version :v1.0
 */
public class ContractQueryFileContent implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3722664259558757762L;
	@Length(max = 60,message="[合同编号]长度不符合规范")
	@NotEmpty(message="[合同编号]不能为空")
	private String	contractnum	;//	合同编号
	@Length(max = 4,message="[合同类型]长度不符合规范")
	@NotEmpty(message="[合同类型]不能为空")
	@Pattern(regexp="^(CT0)([0-4])$",message="[合同类型]不符合规范")
	private String	contracttype	;//	合同类型
	@Length(max = 60,message="[付款人名称]长度不符合规范")
	@NotEmpty(message="[付款人名称]不能为空")
	private String	debtorname	;//	付款人名称
	@Length(max = 32,message="[付款人账号]长度不符合规范")
	@NotEmpty(message="[付款人账号]不能为空")
	private String	debtoraccountno	;//	付款人账号
	@Length(max = 14,message="[付款人账号]长度不符合规范")
	@NotEmpty(message="[付款人账号]不能为空")
	private String	debtorbranchcode	;//	付款行行号
	//@Length(max = 12,message="[付款单笔金额上限]长度不符合规范")
	private String	debtoramountlimit	;//	付款单笔金额上限
	//@Length(max = 2,message="[付款金额限制类型]长度不符合规范")
	private String	debtortransamtlimittype	;//	付款金额限制类型
	//@Length(max = 12,message="[付款累计金额上限]长度不符合规范")
	private String	debtoraccuamountlimit	;//	付款累计金额上限
	//@Length(max = 2,message="[付款次数限制类型]长度不符合规范")
	private String	debtortransnumlimittype	;//	付款次数限制类型
	//@Length(max = 12,message="[付款次数限制]长度不符合规范")
	private String	debtortranslimit	;//	付款次数限制
	@Length(max = 60,message="[收款人名称]长度不符合规范")
	@NotEmpty(message="[收款人名称]不能为空")
	private String	creditorname	;//	收款人名称
	@Length(max = 32,message="[收款人账号]长度不符合规范")
	@NotEmpty(message="[收款人账号]不能为空")
	private String	creditoraccountno	;//	收款人账号
	@Length(max = 32,message="[收款行行号]长度不符合规范")
	@NotEmpty(message="[收款行行号]不能为空")
	private String	creditorbranchcode	;//	收款行行号
	//@Length(max = 12,message="[收款单笔金额上限]长度不符合规范")
	private String	creditoramountlimit	;//	收款单笔金额上限
	//@Length(max = 2,message="[收款金额限制类型]长度不符合规范")
	private String	creditortransamtlimittype	;//	收款金额限制类型
	//@Length(max = 12,message="[收款累计金额上限]长度不符合规范")
	private String	creditoraccuamountlimit	;//	收款累计金额上限
	//@Length(max = 2,message="[收款次数限制类型]长度不符合规范")
	private String	creditortransnumlimittype	;//	收款次数限制类型
	//@Length(max = 12,message="[收款次数限制]长度不符合规范")
	private String	creditortranslimit	;//	收款次数限制
	@Length(max = 8,message="[合约开始日期]长度不符合规范")
	@NotEmpty(message="[合约开始日期]不能为空")
	private String	signdate	;//	合约开始日期
	@Length(max = 8,message="[合约终止日期]长度不符合规范")
	@NotEmpty(message="[合约终止日期]不能为空")
	private String	expirydate	;//	合约终止日期
	@Length(max = 128,message="[合同附件名称]长度不符合规范")
	@NotEmpty(message="[合同附件名称]不能为空")
	private String	fileaddress	;//	合同附件名称
	//@Length(max = 12,message="[业务类型]长度不符合规范")
	//@NotEmpty(message="[业务类型]不能为空")
	private String	categorypurpose	;//	业务类型
	//@Length(max = 12,message="[业务种类]长度不符合规范")
	//@NotEmpty(message="[业务种类]不能为空")
	private String	proprietary	;//	业务种类
	@Length(max = 2,message="[业务种类]长度不符合规范")
	@NotEmpty(message="[业务种类]不能为空")
	private String status;
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getContractnum() {
		return contractnum;
	}
	public void setContractnum(String contractnum) {
		this.contractnum = contractnum;
	}
	public String getContracttype() {
		return contracttype;
	}
	public void setContracttype(String contracttype) {
		this.contracttype = contracttype;
	}
	public String getDebtorname() {
		return debtorname;
	}
	public void setDebtorname(String debtorname) {
		this.debtorname = debtorname;
	}
	public String getDebtoraccountno() {
		return debtoraccountno;
	}
	public void setDebtoraccountno(String debtoraccountno) {
		this.debtoraccountno = debtoraccountno;
	}
	public String getDebtorbranchcode() {
		return debtorbranchcode;
	}
	public void setDebtorbranchcode(String debtorbranchcode) {
		this.debtorbranchcode = debtorbranchcode;
	}
	public String getDebtoramountlimit() {
		return debtoramountlimit;
	}
	public void setDebtoramountlimit(String debtoramountlimit) {
		this.debtoramountlimit = debtoramountlimit;
	}
	public String getDebtortransamtlimittype() {
		return debtortransamtlimittype;
	}
	public void setDebtortransamtlimittype(String debtortransamtlimittype) {
		this.debtortransamtlimittype = debtortransamtlimittype;
	}
	public String getDebtoraccuamountlimit() {
		return debtoraccuamountlimit;
	}
	public void setDebtoraccuamountlimit(String debtoraccuamountlimit) {
		this.debtoraccuamountlimit = debtoraccuamountlimit;
	}
	public String getDebtortransnumlimittype() {
		return debtortransnumlimittype;
	}
	public void setDebtortransnumlimittype(String debtortransnumlimittype) {
		this.debtortransnumlimittype = debtortransnumlimittype;
	}
	public String getDebtortranslimit() {
		return debtortranslimit;
	}
	public void setDebtortranslimit(String debtortranslimit) {
		this.debtortranslimit = debtortranslimit;
	}
	public String getCreditorname() {
		return creditorname;
	}
	public void setCreditorname(String creditorname) {
		this.creditorname = creditorname;
	}
	public String getCreditoraccountno() {
		return creditoraccountno;
	}
	public void setCreditoraccountno(String creditoraccountno) {
		this.creditoraccountno = creditoraccountno;
	}
	public String getCreditorbranchcode() {
		return creditorbranchcode;
	}
	public void setCreditorbranchcode(String creditorbranchcode) {
		this.creditorbranchcode = creditorbranchcode;
	}
	public String getCreditoramountlimit() {
		return creditoramountlimit;
	}
	public void setCreditoramountlimit(String creditoramountlimit) {
		this.creditoramountlimit = creditoramountlimit;
	}
	public String getCreditortransamtlimittype() {
		return creditortransamtlimittype;
	}
	public void setCreditortransamtlimittype(String creditortransamtlimittype) {
		this.creditortransamtlimittype = creditortransamtlimittype;
	}
	public String getCreditoraccuamountlimit() {
		return creditoraccuamountlimit;
	}
	public void setCreditoraccuamountlimit(String creditoraccuamountlimit) {
		this.creditoraccuamountlimit = creditoraccuamountlimit;
	}
	public String getCreditortransnumlimittype() {
		return creditortransnumlimittype;
	}
	public void setCreditortransnumlimittype(String creditortransnumlimittype) {
		this.creditortransnumlimittype = creditortransnumlimittype;
	}
	public String getCreditortranslimit() {
		return creditortranslimit;
	}
	public void setCreditortranslimit(String creditortranslimit) {
		this.creditortranslimit = creditortranslimit;
	}
	public String getSigndate() {
		return signdate;
	}
	public void setSigndate(String signdate) {
		this.signdate = signdate;
	}
	public String getExpirydate() {
		return expirydate;
	}
	public void setExpirydate(String expirydate) {
		this.expirydate = expirydate;
	}
	public String getFileaddress() {
		return fileaddress;
	}
	public void setFileaddress(String fileaddress) {
		this.fileaddress = fileaddress;
	}
	public String getCategorypurpose() {
		return categorypurpose;
	}
	public void setCategorypurpose(String categorypurpose) {
		this.categorypurpose = categorypurpose;
	}
	public String getProprietary() {
		return proprietary;
	}
	public void setProprietary(String proprietary) {
		this.proprietary = proprietary;
	}


}
