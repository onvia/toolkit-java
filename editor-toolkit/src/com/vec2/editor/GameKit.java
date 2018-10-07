package com.vec2.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.MessageFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.vec2.editor.utils.Language;
import com.vec2.swing.utils.SwingUtils;
import com.vec2.swing.widgets.Label;



public class GameKit {
	private JFrame frame;
	
	private static void init() {
		Language.load("/zn");
		
		SwingUtils.initWindowsLookAndFeel();

	}
	public static void main(String[] args) {
		
		try {
			init();
			
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						GameKit window = new GameKit();
						SwingUtils.center(window.frame);
						window.frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e1) {
			e1.printStackTrace();
//			JOptionPane.showMessageDialog(null, L.get("error.init_data_failed"), L.get("Error"),
//					JOptionPane.ERROR_MESSAGE);
		}		 
	}
	
	public GameKit() {
		initialize();
	}
	
	private void initialize() {
		
		frame = new JFrame();
		frame.setTitle("Kit");
		frame.setBounds(0, 0,1280, 720);
		frame.setMinimumSize(new Dimension(1280, 720));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JList<String> list = new JList<String>();
		   // 设置一下首选大小
        list.setPreferredSize(new Dimension(320, 720));
        // 允许可间断的多选
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // 设置选项数据（内部将自动封装成 ListModel ）
        String[] liststrs = Language.getList("menu.game_data_edit","menu.clean_same_char","menu.untexturepacker","menu.font_thin");
        list.setListData(liststrs);
        list.setSelectedIndex(0);
        list.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		JList<String> jList = (JList<String>) (e.getSource());
        		  int[] indices = jList.getSelectedIndices();
                  // 获取选项数据的 ListModel
                  ListModel<String> listModel = jList.getModel();
                  // 输出选中的选项
                  for (int index : indices) {
                      System.out.println("选中: " + index + " = " + listModel.getElementAt(index));
                  }
                  System.out.println();
        	}
		});
        // 添加选项选中状态被改变的监听器
//        list.addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//            	if(e.getValueIsAdjusting()) {
//            		 // 获取所有被选中的选项索引
//                    int[] indices = list.getSelectedIndices();
//                    // 获取选项数据的 ListModel
//                    ListModel<String> listModel = list.getModel();
//                    // 输出选中的选项
//                    for (int index : indices) {
//                        System.out.println("选中: " + index + " = " + listModel.getElementAt(index));
//                    }
//                    System.out.println();
//            	}
//               
//            }
//        });
        
        
		JPanel panel_left = new JPanel();
		panel_left.setPreferredSize(new Dimension(320, 720));
		panel_left.setLayout(new BorderLayout());
		frame.getContentPane().add(panel_left, BorderLayout.WEST);
		panel_left.add(list);
		
		
		
//		
//		CleanSameCharsTable panel_right= new CleanSameCharsTable();
//		frame.getContentPane().add(panel_right, BorderLayout.CENTER);
	}
	
	
	
	
}
