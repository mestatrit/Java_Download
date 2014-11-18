package com.lhy.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import com.lhy.support.Save_Info;
import com.lhy.support.Support;

public class Get_data implements Runnable {
	RandomAccessFile ra;
	InputStream is;
	Save_Info si;
	boolean flag;
	long filelength;
	String filename;
	long skip_data;
	String real_url;
	Get_data gd;
	String path;
	long times;
	Support s;
	int index;
	byte[] bt;
	Thread t1;
	Thread t2;

	public Get_data() {

	}

	public Get_data(RandomAccessFile ra, InputStream is, long times,
			long filelength, String filename, String path, String real_url) {
		times = 0l;
		this.ra = ra;
		this.is = is;
		flag = true;
		skip_data = 0l;
		this.path = path;
		this.times = times;
		bt = new byte[1024];
		gd = new Get_data();
		this.real_url = real_url;
		this.filename = filename;
		this.filelength = filelength;
		si = new Save_Info(real_url, filename, String.valueOf(filelength), null);
	}

	public Get_data(RandomAccessFile ra, InputStream is, long times,
			long filelength, String filename, String path, long skip_data,
			String real_url) {
		times = 0l;
		this.ra = ra;
		this.is = is;
		flag = true;
		this.path = path;
		this.times = times;
		bt = new byte[1024];
		gd = new Get_data();
		this.real_url = real_url;
		this.filename = filename;
		this.skip_data = skip_data;
		this.filelength = filelength;
		si = new Save_Info(real_url, filename, String.valueOf(filelength),
				String.valueOf(skip_data));
	}

	@Override
	public void run() {
		if (skip_data == 0l) {
			System.out.println("无需跳过，正常下载");
			t1 = new Thread(s = new Support(filelength, filename, path, gd, si));
			t1.start();
			try {
				while ((index = is.read(bt, 0, bt.length)) > 0 && flag) {
					ra.write(bt, 0, index);
					times += index;
					si.set_data(times + "");
					s.set_point(si.get_totle_info());
				}
				ra.close();
				s.del_suffix();
				s.set_flag_false();
			} catch (IOException e) {
				System.out.println("网络异常，该线程停止下载，等待网络恢复");
			}
		} else {
			System.out.println("跳过" + skip_data + "字节后开始下载");
			t2 = new Thread(s = new Support(filelength, filename, path, gd, si));
			try {
				ra.seek(skip_data);
				times = skip_data;
				t2.start();
				System.out.println("已跳过" + skip_data + "字节");
				while ((index = is.read(bt, 0, bt.length)) > 0 && flag) {
					ra.write(bt, 0, index);
					times += index;
					si.set_data(times + "");
					s.set_point(si.get_totle_info());
				}
				ra.close();
				s.del_suffix();
				s.set_flag_false();
			} catch (IOException e) {
				System.out.println("网络异常，该线程停止下载，等待网络恢复");
			}
		}
	}

	public void set_false() {
		flag = false;
	}
}
