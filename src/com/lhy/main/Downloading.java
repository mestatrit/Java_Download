package com.lhy.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.lhy.support.Support;

public class Downloading implements Runnable {
	Support s;
	byte[] bt;
	String url;
	String path;
	String range;
	long data_pac;
	String filename;
	InputStream is;
	RandomAccessFile ra;
	HttpURLConnection con;

	public Downloading(String url, String path, String filename, String range,
			Support s) {
		bt = new byte[1024];
		data_pac = 0l;
		this.s = s;
		this.url = url;
		this.path = path;
		this.range = range;
		this.filename = filename;
	}

	@Override
	public void run() {
		get_con(range);
		set_randomaccessfile(Long.valueOf(range.split("-")[0]));
		worker();
	}

	private void worker() {
		try {
			int index;
			while ((index = is.read(bt, 0, bt.length)) > 0) {
				ra.write(bt, 0, index);
				data_pac += index;
				s.add_now_packet(index);
			}
			if (data_pac < Long.valueOf(range.split("-")[1])
					- Long.valueOf(range.split("-")[0])) {
				try {
					ra.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				get_con(String
						.valueOf((Long.valueOf(range.split("-")[0]) + data_pac))
						+ "-" + range.split("-")[1]);
				set_randomaccessfile((Long.valueOf(range.split("-")[0]) + data_pac));
				worker();
			}
		} catch (IOException e) {
			if (data_pac < Long.valueOf(range.split("-")[1])
					- Long.valueOf(range.split("-")[0])) {
				try {
					ra.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				get_con(String
						.valueOf((Long.valueOf(range.split("-")[0]) + data_pac))
						+ "-" + range.split("-")[1]);
				set_randomaccessfile((Long.valueOf(range.split("-")[0]) + data_pac));
				worker();
			}
		}
	}

	private void set_randomaccessfile(long seek) {
		try {
			ra = new RandomAccessFile(new File(path + filename), "rw");
			ra.seek(seek);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void get_con(String new_range) {
		try {
			con = (HttpURLConnection) new URL(url).openConnection();
			con.setRequestProperty("Range", "bytes=" + new_range);
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			con.setDoInput(true);
			con.connect();
			is = con.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			get_con(new_range);
		}
	}
}
