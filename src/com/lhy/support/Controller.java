package com.lhy.support;

import com.lhy.main.Download;
import com.lhy.main.Get_data;

public class Controller implements Runnable {
	boolean flag;
	Get_data gd;
	Save_Info si;
	Support s;
	long cut;
	long pre;
	long now;

	public Controller(Get_data gd, Support s, Save_Info si, boolean flag) {
		this.gd = gd;
		this.s = s;
		this.si = si;
		this.flag = flag;
	}

	@Override
	public void run() {
		while (flag) {
			pre = s.exam_speed();
			sleep(1000);
			now = s.exam_speed();
			cut = now - pre;
			if (cut == 0l && flag) {
				System.out.println("网络连接异常，需要重新尝试");
				gd.set_false();
				s.set_flag_false();
				this.flag = false;
				new Download(si.get_url(), si.get_data());
			} else {
				System.out.println("网络连接监测中: 连接正常");
			}
		}
	}

	public void set_false() {
		flag = false;
	}

	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
