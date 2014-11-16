package com.lhy.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

public class Download {
	URL url;
	File file;
	int index;
	byte[] bt;
	int times;
	String path;
	InputStream is;
	RandomAccessFile ra;
	HttpURLConnection con;

	public Download(HttpURLConnection con, URL url) {
		times = 0;
		this.url = url;
		this.con = con;
		bt = new byte[1024];
		path = "/Users/apple/Desktop/My_Download/";
		init();
	}

	private void init() {
		try {
			try {
				con = (HttpURLConnection) url.openConnection();
				con.setDoInput(true);
				con.connect();
				is = con.getInputStream();
				file = new File(path + get_filename(con.toString()));
				ra = new RandomAccessFile(file, "rw");
				getdata();
				ra.close();
				con.disconnect();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getdata() {
		try {
			while ((index = is.read(bt, 0, bt.length)) > 0) {
				ra.write(bt, 0, index);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String get_filename(String ori_url) {
		ori_url = ori_url.replace(
				"sun.net.www.protocol.http.HttpURLConnection:", "");
		return ori_url.split("/")[ori_url.split("/").length - 1];
	}
}
