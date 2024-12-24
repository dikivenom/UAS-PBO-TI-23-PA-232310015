package com.ibik.pbo.applications;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;


public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton loginAdmin = new JButton("Login sebagai admin");
		loginAdmin.setBackground(new Color(255, 255, 128));
		loginAdmin.setBounds(145, 48, 197, 37);
		contentPane.add(loginAdmin);
		
		JButton loginSiswa = new JButton("Login sebagai siswa");
		loginSiswa.setBackground(new Color(128, 255, 128));
		loginSiswa.setBounds(145, 121, 197, 37);
		contentPane.add(loginSiswa);
		
		loginAdmin.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        LoginPage loginPage = new LoginPage(); 
		        loginPage.setVisible(true);
		        dispose(); 
		    }
		});
		
		loginSiswa.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        LoginPage loginPage = new LoginPage(); 
		        loginPage.setVisible(true);
		        dispose(); 
		    }
		});
	}
}
