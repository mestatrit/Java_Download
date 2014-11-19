package com.lhy.support;

public class Support {
	int thread_num;
	long filelength;
	long now_packet;
	String[] index;

	public Support(int thread_num) {
		this.thread_num = thread_num;
		init();
	}

	private void init() {
		index = new String[thread_num];
		for (int i = 0; i < thread_num; i++) {
			index[i] = "";
		}
	}

	public void set_filelength(long filelength) {
		this.filelength = filelength;
	}

	public String[] get_index() {
		return index;
	}

	synchronized public void add_now_packet(String packets) {
		index[Integer.valueOf(packets.split("-")[0])] = packets;
	}

	public long get_filelength() {
		return filelength;
	}

	public long get_now_packet() {
		now_packet = 0;
		for (int i = 0; i < thread_num; i++) {
			if(index[i].indexOf("-") != -1)
			now_packet += Long.valueOf(index[i].split("-")[3])-Long.valueOf(index[i].split("-")[2]);
		}
		return now_packet;
	}

}
