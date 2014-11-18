package com.lhy.main;

public class Start {
	String url;

	Start() {
		init();
	}

	private void init() {
		url = "http://w.x.baidu.com/alading/anquan_soft_down_all/25677";
		new Thread(new DownloadStart(url)).start();
	}

	public static void main(String[] args) {
		new Start();
	}
}
