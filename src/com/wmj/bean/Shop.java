package com.wmj.bean;

import java.io.Serializable;
/**
 * ��װ��Ʒ��Ϣ����
 */
public class Shop  implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;   
	private String shopname;  //��Ʒ��
	private String decripes;   //��Ʒ����
	private String category;   //��Ʒ����
	private String picture_url; //ͼƬ
	private double price;    //�۸�
	private String userName;  //������
	private String phone;     //�����ߵ绰
	private String time;      //����ʱ��

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getDecripes() {
		return decripes;
	}

	public void setDecripes(String decripes) {
		this.decripes = decripes;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPicture_url() {
		return picture_url;
	}

	public void setPicture_url(String picture_url) {
		this.picture_url = picture_url;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
