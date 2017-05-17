
package com.zcbspay.platform.instead.common.utils;

/** 
 * @author ZhangDM(Mingly)
 * @date 2012-9-5
 * @description：请求参数
 */

public class HttpRequestParam {
	
private String paraName;
    
    private String paraValue;

    public String getParaName() {
        return paraName;
    }

    public void setParaName(String paraName) {
        this.paraName = paraName;
    }

    public String getParaValue() {
        return paraValue;
    }

    public void setParaValue(String paraValue) {
        this.paraValue = paraValue;
    }

    public HttpRequestParam(String paraName, String paraValue) {
        super();
        this.paraName = paraName;
        this.paraValue = paraValue;
    }

    public HttpRequestParam() {
        super();
        // TODO Auto-generated constructor stub
    }
    
}
