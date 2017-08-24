package yue.wu.testSwing;

import java.awt.*;
import javax.swing.*;

public class WindowWelcome {

	private JFrame frmWelcome;

	/**
	 * Launch the application.
	 */
	public static void notmain(String args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WindowWelcome window = new WindowWelcome();
					window.frmWelcome.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WindowWelcome() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWelcome = new JFrame();
		frmWelcome.setTitle("Welcome");
		frmWelcome.setBounds(720, 350, 499, 230);
		frmWelcome.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmWelcome.getContentPane().setLayout(null);
		
		JLabel lblCopyright = new JLabel("<html><body>Simple Bank System<br><br>"
				+ "Version: &nbsp;1.0<br>"
				+ "Author: &nbsp;yuewu<br>"
				+ "Date: &nbsp;20170830<br>"
				+ "Copyright: &nbsp;yue.w.wu@oracle.com. All rights reserved.</body></html>");
		lblCopyright.setBounds(163, 10, 314, 156);
		frmWelcome.getContentPane().add(lblCopyright);
		
		JLabel lblIcon = new JLabel();
		ImageIcon img = new ImageIcon("./OracleBank2.jpg");
		lblIcon.setIcon(img);
		lblIcon.setBounds(10, 10, 150, 150);
		frmWelcome.getContentPane().add(lblIcon);
		
		JSeparator separatorUp = new JSeparator();
		separatorUp.setBounds(155, 35, 315, 2);
		frmWelcome.getContentPane().add(separatorUp);
		
		JSeparator separatorDown = new JSeparator();
		separatorDown.setBounds(155, 140, 315, 2);
		frmWelcome.getContentPane().add(separatorDown);
		
		JSeparator separatorLeft = new JSeparator();
		separatorLeft.setOrientation(SwingConstants.VERTICAL);
		separatorLeft.setBounds(155, 35, 2, 105);
		frmWelcome.getContentPane().add(separatorLeft);
		
		JSeparator separatorRight = new JSeparator();
		separatorRight.setOrientation(SwingConstants.VERTICAL);
		separatorRight.setBounds(470, 35, 2, 105);
		frmWelcome.getContentPane().add(separatorRight);
	}
}
