package com.wmj.bean;

import java.io.Serializable;
/**
 * 封装用户评价的类，包括评价内容，商品名，评价时间，评价人
 */
public class Assess implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String assertConent;
	private String assertName;
	private String assertTime;
	private String assertShopName;

	public String getAssertConent() {
		return assertConent;
	}

	public void setAssertConent(String assertConent) {
		this.assertConent = assertConent;
	}

	public String getAssertName() {
		return assertName;
	}

	public void setAssertName(String assertName) {
		this.assertName = assertName;
	}

	public String getAssertTime() {
		return assertTime;
	}

	public void setAssertTime(String assertTime) {
		this.assertTime = assertTime;
	}

	public String getAssertShopName() {
		return assertShopName;
	}

	public void setAssertShopName(String assertShopName) {
		this.assertShopName = assertShopName;
	}

}
