package com.lhy.main;

import java.io.File;

public class test {
	public static void main(String[] args) {
		// String data = "2000000";
		// System.out.println(data.matches("[0-9]{n}"));
		File file = new File("/Users/apple/Desktop/My_Download");
		System.out.println(file.exists());
	}
}
