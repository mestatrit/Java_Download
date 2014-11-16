package com.lhy.main;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Start {
	URL url;
	HttpURLConnection con;

	Start() {
		init();
	}

	private void init() {
		try {
			url = new URL(
//					"http://w.x.baidu.com/alading/anquan_soft_down_normal/12350");
			 "http://w.x.baidu.com/alading/anquan_soft_down_all/25677");
//			 "http://dlsw.baidu.com/sw-search-sp/soft/2a/25677/QQ_V3.1.2.1406167044.dmg");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		new Download(con, url);
	}

	public static void main(String[] args) {
		new Start();
	}
}
