package com.lhy.support;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.lhy.main.Get_data;

public class Support implements Runnable {
	static long pre;
	static long now;
	int index;
	long times;
	Support s;
	String path;
	String data;
	Get_data gd;
	static boolean flag;
	static boolean c_flag;
	String suffix;
	FileReader fr;
	String filename;
	long filelength;
	File file;
	Save_Info si;
	Thread t1;
	Controller ct;
	RandomAccessFile ra;

	public Support(String new_path) {
		data = "";
		file = new File(new_path);
	}

	public Support(long filelength, String filename, String path, Get_data gd,
			Save_Info si) {
		try {
			data = "";
			times = 0l;
			flag = true;
			c_flag = true;
			this.gd = gd;
			suffix = ".Lhy";
			this.si = si;
			this.path = path;
			s = new Support(path + filename + suffix);
			this.filename = filename;
			this.filelength = filelength;
			file = new File(path + filename + suffix);
			ra = new RandomAccessFile(file, "rw");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		start_controller();
		while (flag) {
			get_speed();
			reset();
			display();
			reset();
		}
	}

	private void start_controller() {
		t1 = new Thread(ct = new Controller(gd, s, si, c_flag));
		t1.start();
	}

	public void set_point(String str) {
		try {
			times++;
			ra.setLength(0);
			ra.write(str.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void set_flag_false() {
		flag = false;
	}

	public void del_suffix() {
		try {
			ct.set_false();
			fr.close();
			ra.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public long exam_speed() {
		long _data = Long.valueOf(get_length());
		reset();
		return _data;
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
			int size = 0;
			try {
				fr = new FileReader(file);
				while ((index = fr.read()) > 0) {
					data += (char) index;
				}
				size = data.split("\r\n").length - 1;
				data = data.split("\r\n")[size].trim();
			} catch (IOException e) {
				reset();
				get_length();
			}
			if (data.equals("") || size == 4) {
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
