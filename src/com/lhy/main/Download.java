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
	File old_file;
	String path;
	String real_url;
	String filename;
	long filelength;
	InputStream is;
	static long times;
	RandomAccessFile ra;
	RandomAccessFile Lhy;
	HttpURLConnection con;
	Thread t1;
	Get_data gd;
	String suffix;
	long skip_data;

	public Download() {

	}

	public Download(URL url, long skip_data, long totle) {
		if (url != null) {
			this.url = url;
			path = "/Users/apple/Desktop/My_Download/";
			suffix = ".Lhy";
			this.skip_data = skip_data;
			init(skip_data, totle);
		} else
			System.out.println("网址无效");
	}

	public Download(URL url) {
		if (url != null) {
			this.url = url;
			path = "/Users/apple/Desktop/My_Download/";
			suffix = ".Lhy";
			init();
		} else
			System.out.println("网址无效");
	}

	private void init(long skip_data, long totle) {
		try {
			skip_to_connect(String.valueOf(skip_data), String.valueOf(totle));
			filelength = con.getContentLengthLong();
			file = new File(path + get_filename(con.toString()));
			ra = new RandomAccessFile(file, "rw");
			t1 = new Thread(gd = new Get_data(ra, is, times, filelength,
					file.getName(), path, skip_data, real_url));
			t1.setPriority(10);
			System.out.println("开始传输数据");
			t1.start();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		try {
			try {
				try_to_connect();
				filelength = con.getContentLengthLong();
				file = new File(path + get_filename(con.toString()));
				old_file = new File(path + filename + suffix);
				ra = new RandomAccessFile(file, "rw");
				if (old_file.exists()) {
					con.disconnect();
					int size = 0;
					int index;
					String data = "";
					Lhy = new RandomAccessFile(old_file, "rw");
					while ((index = Lhy.read()) > 0) {
						data += (char) index;
					}
					size = data.split("\r\n").length - 1;
					String totle_data = data.split("\r\n")[size - 1].trim();
					data = data.split("\r\n")[size].trim();
					skip_to_connect(data, totle_data);
					t1 = new Thread(gd = new Get_data(ra, is, times,
							filelength, file.getName(), path,
							Long.valueOf(data), real_url));
					t1.setPriority(10);
					System.out.println("开始传输数据");
					t1.start();
				} else {
					t1 = new Thread(gd = new Get_data(ra, is, times,
							filelength, file.getName(), path, real_url));
					System.out.println("开始传输数据");
					t1.start();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void skip_to_connect(String _data, String totle) {
		try {
			System.out.println("连接中");
			con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			con.setDoInput(true);
			con.setRequestProperty("Range", "bytes=" + _data + "-" + totle);
			con.connect();
			is = con.getInputStream();
			System.out.println("已连接至资源");
		} catch (IOException e) {
			System.out.println("连接超时，正在尝试重新连接");
			skip_to_connect(_data, totle);
		}
	}

	private void try_to_connect() {
		try {
			System.out.println("连接中");
			con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			con.setDoInput(true);
			con.connect();
			is = con.getInputStream();
			System.out.println("已连接至资源");
		} catch (IOException e) {
			System.out.println("连接超时，正在尝试重新连接");
			try_to_connect();
		}
	}

	private String get_filename(String ori_url) {
		real_url = ori_url.replace(
				"sun.net.www.protocol.http.HttpURLConnection:", "");
		filename = real_url.split("/")[ori_url.split("/").length - 1];
		return filename;
	}
}
