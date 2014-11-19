package com.lhy.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Start_GUI {
	JFrame f1;
	JTextField tf;
	JButton _new;
	JButton _pause;
	JButton _stop;
	JButton _offlinedownload;
	JTable tb;
	JScrollPane js;
	String[] Names = { "文件名", "进度", "下载速度", "文件状态" };
	Object[][] co = new Object[5][4];

	public Start_GUI() {
		init_jframe();
		init_jtable();
		init_jbutton();
		init_co();
		init_add();
		f1.setVisible(true);
		//http://w.x.baidu.com/alading/anquan_soft_down_normal/12350
		//http://w.x.baidu.com/alading/anquan_soft_down_all/25677
		_new.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < 5; i++) {
					if (co[i][0] == null) {
						new New_Download(i, tb,co);
						break;
					}
				}
			}

		});
	}

	private void init_co() {
		for(int i = 0 ; i < 5 ; i++){
			for(int j = 0 ; j < 4 ; j++){
				co[i][j] = null;
			}
		}
	}

	private void init_add() {
		f1.add(_new);
		f1.add(_pause);
		f1.add(_stop);
		f1.add(_offlinedownload);
		f1.add(js);
	}

	private void init_jbutton() {
		_new = new JButton("新建下载");
		_pause = new JButton("暂停");
		_stop = new JButton("停止");
		_offlinedownload = new JButton("离线下载");
		_new.setBounds(25, 15, 80, 25);
		_pause.setBounds(115, 15, 80, 25);
		_stop.setBounds(205, 15, 80, 25);
		_offlinedownload.setBounds(295, 15, 80, 25);
	}

	private void init_jframe() {
		f1 = new JFrame("下载器");
		f1.setSize(400, 180);
		f1.setLocationRelativeTo(null);
		f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f1.setResizable(false);
		f1.setLayout(null);
	}

	private void init_jtable() {
		tb = new JTable();
		js = new JScrollPane(tb);
		tb.setGridColor(Color.black);
		tb.setShowGrid(true);
		tb.setModel(new DefaultTableModel(co, Names));
		js.setBounds(15, 50, 370, 95);
	}

	public static void main(String[] args) {
		new Start_GUI();
	}
}
