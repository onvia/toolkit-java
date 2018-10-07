package com.vec2.editor.tool.cleanchars;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.Spring;
import javax.swing.SpringLayout;

import com.vec2.editor.utils.Language;
import com.vec2.editor.utils.RuntimeModel;
import com.vec2.swing.utils.SwingUtils;
import com.vec2.swing.utils.SwingUtils.DropInAdapter;

/**
 * 清理相同字符串
 * */
public class CleanSameCharsTool{
	
	RuntimeModel model;
	private JFrame frame;
	private FileDialog mFileDialog;
	private FileReader mFileReader;
	private BufferedReader mBufferedReader;
	private JTextField mTextField;
	private JTextArea mTextArea;
	
	
	private static void init() {
		Language.load("/zn");
		SwingUtils.initWindowsLookAndFeel();
	}
	public static void main(String[] args) {
		init();
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				CleanSameCharsTool charsTool = new CleanSameCharsTool(RuntimeModel.independent);
				SwingUtils.center(charsTool.frame);
				charsTool.frame.setVisible(true);
			}
		});
	}
	
	public CleanSameCharsTool(RuntimeModel model) {
		this.model = model;
		if(model == RuntimeModel.independent) {
			frame = new JFrame(Language.get("window.clean_same_char"));
			frame.setBounds(0, 0,640, 480);
			frame.setMinimumSize(new Dimension(640, 480));
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JPanel panel = initialize(frame);
			frame.setContentPane(panel);
		}		
	}
	public JPanel initialize(JFrame frame) {
	
		mFileDialog = new FileDialog(frame, Language.get("text.choose_file"));
		mFileDialog.setMode(FileDialog.LOAD);
		
		
		SpringLayout springLayout = new SpringLayout();
		JPanel panel = new JPanel(springLayout);
		
		mTextField = new JTextField();
		JButton openBtn = new JButton(Language.get("text.select"));
		JButton disposeBtn = new JButton(Language.get("text.dispose"));
		mTextArea = new JTextArea();
	    JScrollPane scroll = new JScrollPane(mTextArea);
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);	

	    mTextArea.setLineWrap(true);
	    mTextArea.setWrapStyleWord(true);

	    panel.add(scroll);
	    panel.add(mTextField);
	    panel.add(openBtn);
	    panel.add(disposeBtn);
	    
	    int distance = 10;
	    //文本约束
	    SpringLayout.Constraints scrollCons = springLayout.getConstraints(scroll);
	    scrollCons.setX(Spring.constant(distance));
	    scrollCons.setY(Spring.constant(distance));
	    
	    //地址栏约束
	    SpringLayout.Constraints textFieldCons = springLayout.getConstraints(mTextField);
	    textFieldCons.setX(Spring.constant(distance));	    
	    textFieldCons.setY(Spring.sum(scrollCons.getConstraint(SpringLayout.SOUTH),Spring.constant(distance)));
	    textFieldCons.setHeight(Spring.constant(21));
	    //<打开>按钮约束
	    SpringLayout.Constraints openBtnCons = springLayout.getConstraints(openBtn);
	    openBtnCons.setY(Spring.sum(scrollCons.getConstraint(SpringLayout.SOUTH),Spring.constant(distance)));
	    
	    //<处理>按钮约束
	    SpringLayout.Constraints disposeBtnCons = springLayout.getConstraints(disposeBtn);
	    disposeBtnCons.setY(Spring.sum(scrollCons.getConstraint(SpringLayout.SOUTH),Spring.constant(distance)));
	    
	    //面板约束
	    SpringLayout.Constraints panelCons = springLayout.getConstraints(panel);  // 获取容器的约束对象
	    panelCons.setConstraint(SpringLayout.EAST, Spring.sum(scrollCons.getConstraint(SpringLayout.EAST), Spring.constant(distance)));	    
	    panelCons.setConstraint(SpringLayout.SOUTH, Spring.sum(textFieldCons.getConstraint(SpringLayout.SOUTH), Spring.constant(distance)));
	    
	    

	    disposeBtnCons.setConstraint(SpringLayout.EAST, Spring.sum(panelCons.getConstraint(SpringLayout.EAST), Spring.constant(-distance)));
	    openBtnCons.setConstraint(SpringLayout.EAST, Spring.sum(disposeBtnCons.getConstraint(SpringLayout.WEST), Spring.constant(-distance)));
	    textFieldCons.setConstraint(SpringLayout.EAST, Spring.sum(openBtnCons.getConstraint(SpringLayout.WEST), Spring.constant(-distance)));
	
	
	
		//逻辑
	    openBtn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				mFileDialog.setFile("*.txt");
		        mFileDialog.setVisible(true);
		       
		        mTextArea.setText(null);
		        if(mFileDialog.getFile() != null) {
		        	String fullpath = mFileDialog.getDirectory()+mFileDialog.getFile();
		        	File file = new File(fullpath);
		        	openFile(file);
		        	
		        }
			}
		});
	
	    disposeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				  String currentString = mTextArea.getText();
			        if (currentString.equals("")) {
			          return;
			        }
			        String newString = cleanTheSameChar(currentString);
			        mTextArea.setText(newString);
			}
		});
	    
	    SwingUtils.addDropIn(mTextArea, new DropInAdapter() {
	    	@Override
	    	public void onDropIn(File file) {
	    		openFile(file);
	    	}
	    });
	    SwingUtils.addDropIn(mTextField, new DropInAdapter() {
	    	@Override
	    	public void onDropIn(File file) {
	    		openFile(file);
	    	}
	    });
	    
	    return panel;
	}
	private void openFile(File file) {
		System.out.println(file.getPath());
		mTextField.setText(file.getPath());
	   	try {
	   		String s = null;
			mFileReader = new FileReader(file);
        	mBufferedReader = new BufferedReader(mFileReader);
        	 while ((s = mBufferedReader.readLine()) != null) {
                 mTextArea.append(s + "\n");
               }
               mBufferedReader.close();
               mFileReader.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.out.println("FileNotFoundException:"+e1.getMessage());
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("IOException"+e1.getMessage());
		}
	}
	

	public static String cleanTheSameChar(String str) {
		StringBuffer result = new StringBuffer();
		List<Character> list = new ArrayList<Character>();
		char[] cs = str.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			if (!list.contains(cs[i])) {
				result.append(cs[i]);
				list.add(cs[i]);
			}
		}
		return result.toString();
	}

}
