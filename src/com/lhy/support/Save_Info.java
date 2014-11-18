package com.lhy.support;

import java.net.MalformedURLException;
import java.net.URL;

public class Save_Info {
	String url;
	String filename;
	String finish_data;

	public Save_Info(String url, String filename, String finish_data) {
		this.url = url;
		this.filename = filename;
		this.finish_data = finish_data;
	}

	public void set_data(String finish_data) {
		this.finish_data = finish_data;
	}

	public String get_totle_info() {
		return url + "\r\n" + filename + "\r\n" + finish_data;
	}

	public URL get_url() {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String get_filename() {
		return filename;
	}

	public long get_data() {
		return Long.valueOf(finish_data);
	}
	
}
