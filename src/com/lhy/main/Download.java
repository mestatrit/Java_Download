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
	String path;
	String filename;
	long filelength;
	InputStream is;
	static long times;
	RandomAccessFile ra;
	HttpURLConnection con;
	Thread t1;
	Get_data gd;
	Download dl;

	public Download() {

	}

	public Download(HttpURLConnection con, URL url) {
		this.url = url;
		this.con = con;
		dl = new Download();
		path = "/Users/apple/Desktop/My_Download/";
		init();
	}

	private void init() {
		try {
			try {
				con = (HttpURLConnection) url.openConnection();
				con.setDoInput(true);
				con.connect();
				System.out.println();
				is = con.getInputStream();
				filelength = con.getContentLengthLong();
				file = new File(path + get_filename(con.toString()));
				ra = new RandomAccessFile(file, "rw");
				System.out.println("START GET DATA");
				t1 = new Thread(gd = new Get_data(ra, is, times, filelength,
						file.getName(), path));
				t1.start();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String get_filename(String ori_url) {
		ori_url = ori_url.replace(
				"sun.net.www.protocol.http.HttpURLConnection:", "");
		filename = ori_url.split("/")[ori_url.split("/").length - 1];
		return filename;
	}
}
