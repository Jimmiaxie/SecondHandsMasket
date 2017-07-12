package com.wmj.bean;

import java.io.Serializable;

/*
 * 封装帖子（商品）信息类，包括商品名称、商品类别、商品价钱、发帖用户名、发帖用户联系方式、发布时间
 */
@SuppressWarnings("serial")
public class PostInfomation implements Serializable {
	private String name, descripe, category, price, username, phone, time;
	private int id;

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescripe(String descripe) {
		this.descripe = descripe;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setUserName(String username) {
		this.username = username;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescripe() {
		return descripe;
	}

	public String getCategory() {
		return category;
	}

	public String getPrice() {
		return price;
	}

	public String getUserName() {
		return username;
	}

	public String getPhone() {
		return phone;
	}

	public String getTime() {
		return time;
	}
}
