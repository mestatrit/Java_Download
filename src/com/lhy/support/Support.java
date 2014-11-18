package com.lhy.support;

public class Support {
	long filelength;
	long now_packet;

	public Support() {
		now_packet = 0l;
	}

	public void set_filelength(long filelength) {
		this.filelength = filelength;
	}

	public void add_now_packet(long packets) {
		now_packet += packets;
	}

	public long get_filelength() {
		return filelength;
	}

	public long get_now_packet() {
		return now_packet;
	}

}
