package com.wmj.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.wmj.task.Constant;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/**
 * 发送请求给服务器
 * @author Administrator
 *
 */
public class HttpUtil{
	public static String sendHttpRequest(String address){
		HttpURLConnection connection=null;
		StringBuilder response=null;
		try{
			URL url=new URL(address);
			connection=(HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setReadTimeout(8000);
			connection.setConnectTimeout(8000);
			connection.setRequestProperty("charset", "UTF-8");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			InputStream in=connection.getInputStream();
			BufferedReader reader=new BufferedReader(new InputStreamReader(in));
			response=new StringBuilder();
			String line;
			while((line=reader.readLine())!=null){
				response.append(line);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(connection!=null){
			connection.disconnect();
			}
		}
		return response.toString();
	 }
	public static Bitmap getPicture(String imageUrl){
		URL url=null;
		Bitmap bitmap=null;
		HttpURLConnection conn=null;
		try{
			url=new URL(imageUrl);
			conn=(HttpURLConnection)url.openConnection();
			conn.connect();
			InputStream in=conn.getInputStream();
			bitmap=BitmapFactory.decodeStream(in);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null){
				conn.disconnect();
			}
		}
		return bitmap;
	}
	public static String sendHttpRequests(final String datastr) {
		String urlString = Constant.URL;
		HttpURLConnection conn = null;
		StringBuilder response = null;
		try {
			URL url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Charset", "UTF-8"); // 设置编码字符集
			byte[] data = datastr.getBytes();
			DataOutputStream outputStream = new DataOutputStream(
					conn.getOutputStream());
			outputStream.write(data);
			outputStream.flush(); // 刷新
			outputStream.close(); // 关闭
			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			response = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}

		} catch (Exception e) {

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		if(response!=null)
		  return response.toString();
		else 
			return null;
	}
	/*
	 * 在开始用HttpURLConnection对象的setRequestProperty()设置,就是生成HTML文件头
	 */
	public static String sendHttpRequest2(final String datastr, String filePath) {
		// 服务器端地址
		String urlString = Constant.URL;
		// String urlString =
		// "http://172.23.226.73:8080/SecondHandServer/servlet/ServletService";
		 String boundary ="*****";
		HttpURLConnection conn = null;
		StringBuilder response = null;
		try {
			URL url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Connection", "Keep-Alive");// 设置维持长连接
			conn.setRequestProperty("Charset", "UTF-8"); // 设置编码字符集
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);// 设置传输文件类型
			byte[] data = datastr.getBytes();
			DataOutputStream outputStream = new DataOutputStream(
					conn.getOutputStream());
			outputStream.write(data);

			FileInputStream filein = new FileInputStream(filePath);
			int bufSize = 1024;
			byte[] buffer = new byte[bufSize];

			int length = -1;
			while ((length = filein.read(buffer)) != -1) {
				outputStream.write(buffer, 0, length);
			}
			filein.close();
			outputStream.flush(); // 刷新
			outputStream.close(); // 关闭

			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			response = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}

		} catch (Exception e) {

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return response.toString();
	}
	
}
