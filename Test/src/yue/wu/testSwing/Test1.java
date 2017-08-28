package yue.wu.testSwing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Test1 {

	private JFrame frame;
	private JTable table;
	private JPopupMenu m_popupMenu;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Test1 window = new Test1();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Test1() {
		initialize();
	}

    private void createPopupMenu() {  
        m_popupMenu = new JPopupMenu();
        JMenuItem delMenItem = new JMenuItem();  
        delMenItem.setText("打印本行第一列的数据");  
        delMenItem.addActionListener(new java.awt.event.ActionListener() {  
            public void actionPerformed(java.awt.event.ActionEvent evt) {  
            	System.out.println(table.getValueAt(table.getSelectedRow(), 0));
            }
        });
        m_popupMenu.add(delMenItem);
    }
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		String[] columnNames = {"第一列", "第二列"};
		Object[][] cellData = {{"11", "12"},{"21", "22"}};
		
		table = new JTable(cellData, columnNames);
		frame.getContentPane().add(table, BorderLayout.NORTH);
		
	    table.addMouseListener(new MouseAdapter() {  
            public void mouseClicked(MouseEvent evt) {  
            	mouseRightButtonClick(evt);
            }  
         });
	    createPopupMenu();
	}
	
    private void mouseRightButtonClick(MouseEvent evt) {  
        if (evt.getButton() == MouseEvent.BUTTON3) {
            if (-1 == table.rowAtPoint(evt.getPoint())) {
                return;  
            }
            m_popupMenu.show(table, evt.getX(), evt.getY());
        }
    }

}
