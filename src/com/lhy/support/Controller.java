package com.lhy.support;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Controller implements Runnable {
	File file;
	String info;
	Support s;
	boolean flag;
	private long pre;
	private long now;
	private long cut;
	String progress;
	String speed;
	JSONObject jo;
	JSONArray _ja;
	JSONArray ja;
	String url;
	String filename;
	RandomAccessFile ra;

	public Controller(String url, String filename, Support s, File file) {
		flag = true;
		this.s = s;
		this.url = url;
		this.file = file;
		this.filename = filename;
		try {
			ra = new RandomAccessFile(file, "rw");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (flag) {
			pre = s.get_now_packet();
			sleep(1000);
			now = s.get_now_packet();
			cut = pre - now;
			if(cut > 0){
			progress = get_progress((((double) s.get_filelength() - (double) now) / (double) s
					.get_filelength()) * 100);
			speed = get_speed_data((double) cut / 1024.0);
			System.out.println(progress + "   " + speed);
			_write();
			if (progress.equals("100.00%")) {
				del_file();
				break;
			}
			}
		}
		try {
			ra.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void _write() {
		try {
			ra.setLength(0);
			ra.write(get_json().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String get_json() {
		jo = new JSONObject();
		ja = new JSONArray();
		for (int i = 0; i < 6; i++) {
			info = s.get_index()[i];
			if (info.indexOf("-") != -1) {
				try {
					jo.put(info.split("-")[0],
							(info.split("-")[2] + "-" + info.split("-")[3]));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			jo.put("totle", s.get_now_packet());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ja.put(url);
		ja.put(filename);
		ja.put(jo);
		return ja.toString();
	}

	public String get_json_data() {
		int index;
		byte[] bt = new byte[1024];
		String _index = "";
		try {
			while ((index = ra.read(bt, 0, bt.length)) > 0) {
				_index += new String(bt, 0, index);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return _index;
	}

	private void del_file() {
		file.delete();
	}

	private String get_progress(double data) {
		BigDecimal bd = new BigDecimal(data);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.toString() + "%";
	}

	private String get_speed_data(double data) {
		BigDecimal bd = new BigDecimal(data);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.toString() + " KB/s";
	}

	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
