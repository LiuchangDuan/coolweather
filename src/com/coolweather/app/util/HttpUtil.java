package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	
	public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
		// 开启线程来发起网络请求
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET"); // GET表示希望从服务器那里获取数据
					connection.setConnectTimeout(8000); // 设置连接超时的毫秒数
					connection.setReadTimeout(8000); // 设置读取超时的毫秒数
					InputStream in = connection.getInputStream(); // 获取服务器返回的输入流
					// 下面对获取到的输入流进行读取
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					if (listener != null) {
						// 回调onFinish()方法
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if (listener != null) {
						// 回调onError()方法
						listener.onError(e);
					}
				} finally {
					if (connection != null) {
						connection.disconnect(); // 将这个HTTP连接关闭掉
					}
				}
			}
		}).start();
	}
	
}
