package com.lhy.support;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Controller implements Runnable {
	Support s;
	boolean flag;
	private long pre;
	private long now;
	private long cut;
	String progress;
	String speed;

	public Controller(Support s) {
		flag = true;
		this.s = s;
	}

	@Override
	public void run() {
		while (flag) {
			pre = s.get_now_packet();
			sleep(1000);
			now = s.get_now_packet();
			cut = now - pre;
			progress = get_progress(((double) now / (double) s.get_filelength()) * 100);
			speed = get_speed_data((double) cut / 1024.0);
			System.out.println(progress + "   " + speed);
			if (progress.equals("100.00%"))
				break;
		}
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
