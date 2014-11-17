package com.lhy.support;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Support implements Runnable {
	long pre;
	long now;
	int index;
	long times;
	String path;
	String data;
	boolean flag;
	String suffix;
	FileReader fr;
	String filename;
	long filelength;
	private File file;
	RandomAccessFile ra;

	public Support(long filelength, String filename, String path) {
		try {
			data = "";
			times = 0l;
			flag = true;
			suffix = ".Lhy";
			this.path = path;
			this.filename = filename;
			this.filelength = filelength;
			ra = new RandomAccessFile(file, "rw");
			file = new File(path + filename + suffix);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (flag) {
			get_speed();
			reset();
			display();
			reset();
		}
	}

	public void set_point(long length) {
		try {
			times++;
			ra.setLength(0);
			ra.write(String.valueOf(length).getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void set_flag_false() {
		flag = false;
	}

	public void del_suffix() {
		if (!file.delete())
			del_suffix();
	}

	private void get_speed() {
		pre = Long.valueOf(get_length());
		reset();
		sleep(1000);
		now = Long.valueOf(get_length());
	}

	private void display() {
		if (times != 0l)
			System.out.println(get_progress(((double) Integer
					.valueOf(get_length()) / (double) filelength) * 100)
					+ "  "
					+ get_speed_data((((double) now - (double) pre)) / 1024.0));
	}

	private String get_speed_data(double data) {
		BigDecimal bd = new BigDecimal(data);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.toString() + " KB/s";
	}

	private String get_progress(double data) {
		BigDecimal bd = new BigDecimal(data);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.toString() + "%";
	}

	private String get_length() {
		if (file.exists()) {
			try {
				fr = new FileReader(file);
				while ((index = fr.read()) > 0) {
					data += (char) index;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (data.equals("")) {
				reset();
				get_length();
			}
			return data;
		}
		return filelength + "";
	}

	private void reset() {
		try {
			data = "";
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
