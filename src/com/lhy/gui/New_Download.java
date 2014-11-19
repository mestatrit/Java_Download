package com.lhy.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.lhy.main.DownloadStart;

public class New_Download {
	JFrame f1;
	String url;
	JLabel jl1;
	JLabel jl2;
	JTextField tf;
	JTextField tf1;
	JTextField tf2;
	JButton jb;
	int i;
	JTable tb;
	Object[][] co;
	String filename;
	String filelength;
	HttpURLConnection con;

	public New_Download(final int i, final JTable tb, final Object[][] co) {
		this.i = i;
		this.co = co;
		this.tb = tb;
		init_jframe();
		init_jlabel();
		init_jtext();
		init_jbutton();
		init_add();
		f1.setVisible(true);
		jb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new DownloadStart(url, tb, i)).start();
				co[i][0] = filename;
				tb.setValueAt(filename, i, 0);
				f1.setVisible(false);
			}
		});
		tf.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				super.mouseExited(e);
				if (!tf.getText().equals("")) {
					url = tf.getText();
					get_con();
					tf1.setText(filename = get_filename());
					tf2.setText(filelength = con.getContentLengthLong() + "");
					con.disconnect();
				}
			}

		});
	}

	private String get_filename() {
		return con.toString()
				.replace("sun.net.www.protocol.http.HttpURLConnection:", "")
				.split("/")[con.toString().split("/").length - 1];
	}

	private void init_jbutton() {
		jb = new JButton("下载");
		jb.setBounds(10, 110, 280, 25);
	}

	private void init_add() {
		f1.add(tf);
		f1.add(jl1);
		f1.add(jl2);
		f1.add(tf1);
		f1.add(tf2);
		f1.add(jb);
	}

	private void init_jtext() {
		tf = new JTextField();
		tf1 = new JTextField();
		tf2 = new JTextField();
		tf.setBounds(20, 10, 270, 25);
		tf1.setBounds(100, 45, 180, 25);
		tf2.setBounds(100, 75, 180, 25);
		tf1.setEditable(false);
		tf2.setEditable(false);
	}

	private void init_jlabel() {
		jl1 = new JLabel("文件名");
		jl2 = new JLabel("文件长度");
		jl1.setBounds(30, 45, 100, 25);
		jl2.setBounds(30, 75, 100, 25);
	}

	private void init_jframe() {
		f1 = new JFrame();
		f1.setSize(300, 160);
		f1.setLocationRelativeTo(null);
		f1.setAlwaysOnTop(true);
		f1.setLayout(null);
	}

	private void get_con() {
		try {
			con = (HttpURLConnection) new URL(url).openConnection();
			con.setDoInput(true);
			con.connect();
			con.getInputStream();
		} catch (MalformedURLException e) {
			get_con();
		} catch (IOException e) {
			get_con();
		}
	}
}
