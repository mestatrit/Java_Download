package com.lhy.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import com.lhy.support.Support;

public class Get_data implements Runnable {
	RandomAccessFile ra;
	InputStream is;
	long filelength;
	String filename;
	String path;
	long times;
	Support s;
	int index;
	byte[] bt;

	public Get_data(RandomAccessFile ra, InputStream is, long times,
			long filelength, String filename, String path) {
		times = 0l;
		this.ra = ra;
		this.is = is;
		this.path = path;
		this.times = times;
		bt = new byte[1024];
		this.filename = filename;
		this.filelength = filelength;
	}

	@Override
	public void run() {
		new Thread(s = new Support(filelength, filename, path)).start();
		try {
			while ((index = is.read(bt, 0, bt.length)) > 0) {
				ra.write(bt, 0, index);
				times += index;
				s.set_point(times);
			}
			ra.close();
			s.set_flag_false();
			s.del_suffix();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
