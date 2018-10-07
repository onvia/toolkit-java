package com.vec2.editor.tool.fontthin;

/**
 * https://github.com/GameBuildingBlocks/FontPruner
 * */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.Spring;
import javax.swing.SpringLayout;

import com.google.typography.font.tools.sfnttool.SfntTool;
import com.vec2.editor.data.EditorData;
import com.vec2.editor.tool.ToolAdapter;
import com.vec2.editor.tool.fontthin.fontextract.ExtractionOperationUtil;
import com.vec2.editor.tool.fontthin.fontextract.FontExtract;
import com.vec2.editor.utils.FileUtils;
import com.vec2.editor.utils.Language;
import com.vec2.editor.utils.MessageKey;
import com.vec2.editor.utils.MessageListener;
import com.vec2.editor.utils.Messager;
import com.vec2.editor.utils.RuntimeModel;
import com.vec2.swing.utils.SwingUtils;
import com.vec2.swing.utils.SwingUtils.DropInAdapter;
import com.vec2.swing.widgets.Label;


public class FontThin extends ToolAdapter{
	private static final String InputTxtFilelist = "input_txt_filelist.txt";
	private static final String InputTtfFilelist = "input_ttf_filelist.txt";
	private static final String IntermediateFolder = "intermediate";
	private static final String OutputFolder = "outputFont";
	private static final String ChineseOutPut = "ChineseOutPut.txt";
	private static final String UnChineseOutPut = "unChineseOutPut.txt";
	
	JTextField mexport_TextField;//导出路径
	public static void main(String[] args) {
		EditorData.load(EditorData.class);
		run(FontThin.class);
	}
	public FontThin(RuntimeModel model) {
		super(model);
		
		frame = new JFrame(Language.get("window.font_thin"));
		frame.setBounds(0, 0,640, 480);
		frame.setMinimumSize(new Dimension(640, 480));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = initialize(frame);
		frame.setContentPane(panel);
	
	}
	@Override
	public JPanel initialize(JFrame frame) {
		
		SpringLayout springLayout = new SpringLayout();
		JPanel panel = new JPanel(springLayout);//主面板
		
		//左侧文件列表
		// 创建根节点
		FontThinTxtFileTree fileTree = new FontThinTxtFileTree();
		JScrollPane scrollPane = new JScrollPane(fileTree);
		JPanel file_tree = new JPanel();
		file_tree.setPreferredSize(new Dimension(240,320));
		file_tree.setLayout(new BorderLayout(0, 0));
		file_tree.add(scrollPane,BorderLayout.CENTER);
		file_tree.setBorder(BorderFactory.createTitledBorder(Language.get("text.drag_file","ttf/txt")));  
		
		JPanel dragPanel= new JPanel(new BorderLayout());
		dragPanel.setBorder(BorderFactory.createTitledBorder(Language.get("label.preview")));  
//		JPanel topPanel= new JPanel(new BorderLayout());
		  // 创建分隔面板
        JSplitPane splitPane = new JSplitPane();

        // 设置左右两边显示的组件
        splitPane.setLeftComponent(new JButton("左边按钮"));
        splitPane.setRightComponent(new JButton("右边按钮"));

        // 分隔条上显示快速 折叠/展开 两边组件的小按钮
        splitPane.setOneTouchExpandable(true);

        // 拖动分隔条时连续重绘组件
        splitPane.setContinuousLayout(true);

        // 设置分隔条的初始位置
        splitPane.setDividerLocation(240);
		
		mexport_TextField = new JTextField();
		JButton disposeBtn = new JButton(Language.get("text.dispose"));
		JButton select_exports_btn = new JButton(Language.get("text.select"));
		
		Label file_txt = new Label("text.pack_folder");
		mexport_TextField.addInputMethodListener(new InputMethodListener() {
			@Override
			public void inputMethodTextChanged(InputMethodEvent event) {
				System.out.println(event.getText());
			}
			
			@Override
			public void caretPositionChanged(InputMethodEvent event) {
				
			}
		});
		mexport_TextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		mexport_TextField.setText(EditorData.fontThinData.getExportPath());
		splitPane.setLeftComponent(file_tree);
		splitPane.setRightComponent(dragPanel);
		panel.add(splitPane);
		panel.add(file_txt);
	    panel.add(mexport_TextField);
	    panel.add(select_exports_btn);
	    panel.add(disposeBtn);
	    
		//文本文件预览面板
		JTextPane previewText = new JTextPane();
		previewText.setEditable(false);		
		previewText.setText(Language.get("text.drag_file","ttf/txt"));
		JScrollPane previewScroll = new JScrollPane(previewText);
		dragPanel.add(previewScroll);
		
	    int distance = 10;
	    
	    //拖拽面板约束
	    SpringLayout.Constraints dropPanelCons = springLayout.getConstraints(splitPane);
	    dropPanelCons.setX(Spring.constant(distance));
	    dropPanelCons.setY(Spring.constant(distance));
	    
	    SpringLayout.Constraints file_txtCons = springLayout.getConstraints(file_txt);
	    file_txtCons.setX(Spring.constant(distance));
	    file_txtCons.setY(Spring.sum(dropPanelCons.getConstraint(SpringLayout.SOUTH),Spring.constant(distance)));
	    
	    //txt地址栏约束
	    SpringLayout.Constraints exportsFieldCons = springLayout.getConstraints(mexport_TextField);
	    exportsFieldCons.setX(Spring.sum(file_txtCons.getConstraint(SpringLayout.EAST),Spring.constant(distance)));
	    exportsFieldCons.setY(file_txtCons.getY());
	    exportsFieldCons.setHeight(Spring.constant(21));
	    
	    
	    //导出路径 选择按钮约束
	    SpringLayout.Constraints select_exports_btn_Cons = springLayout.getConstraints(select_exports_btn);
	    select_exports_btn_Cons.setY(exportsFieldCons.getY());
	    
	    
	    //<处理>按钮约束
	    SpringLayout.Constraints disposeBtnCons = springLayout.getConstraints(disposeBtn);
	    disposeBtnCons.setY(exportsFieldCons.getY());
	    
	    //面板约束
	    SpringLayout.Constraints panelCons = springLayout.getConstraints(panel);  // 获取容器的约束对象
	    panelCons.setConstraint(SpringLayout.EAST, Spring.sum(dropPanelCons.getConstraint(SpringLayout.EAST), Spring.constant(distance)));	    
	    panelCons.setConstraint(SpringLayout.SOUTH, Spring.sum(file_txtCons.getConstraint(SpringLayout.SOUTH), Spring.constant(distance)));
	    
	    disposeBtnCons.setConstraint(SpringLayout.EAST, Spring.sum(panelCons.getConstraint(SpringLayout.EAST), Spring.constant(-distance)));
	    select_exports_btn_Cons.setConstraint(SpringLayout.EAST, Spring.sum(disposeBtnCons.getConstraint(SpringLayout.WEST), Spring.constant(-distance)));
	    exportsFieldCons.setConstraint(SpringLayout.EAST, Spring.sum(select_exports_btn_Cons.getConstraint(SpringLayout.WEST), Spring.constant(-distance)));
		
	    
		SwingUtils.addDropIn(previewText, new DropInAdapter() {
			@Override
			public void onDropIn(File file) {
			}
			@Override
			public void onDropIn(List<File> files) {
				super.onDropIn(files);
				fileTree.onDropIn(files);
			}
		});
		
		SwingUtils.addDropIn(mexport_TextField, new DropInAdapter() {
			@Override
			public void onDropIn(File file) {
				if(file.isDirectory()) {
					mexport_TextField.setText(file.getPath());
					updateExportPath();
				}
			}
		});
		
		
		disposeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("click dispose");
				updateExportPath();
				exec();
			}
		});
		

		select_exports_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File folder = FileUtils.selecDirectories(panel);
				if (folder != null) {
					mexport_TextField.setText(folder.getPath());
				}
			}
		});
		
		
		Messager.register(String.class, new MessageListener<String>() {
			@Override
			public void onMessage(String t, Object[] params) {
				if(is(MessageKey.PREVIEW, params)){
					previewText.setText(t);
				}
			}
		});
		
		return panel;
	}
	
	private void updateExportPath() {
		EditorData.fontThinData.setExportPath(mexport_TextField.getText());
		EditorData.save();
	}
	
	public void exec() {
		ArrayList<String> txtpaths = EditorData.fontThinData.getTxtPaths();
		ArrayList<String> ttfpaths = EditorData.fontThinData.getTtfPaths();
		String exportpath =  EditorData.fontThinData.getExportPath();
		if (txtpaths.isEmpty() || ttfpaths.isEmpty()) {
			SwingUtils.warning(null, Language.get("dialog.warning"), Language.get("error.ttf_txt_paths_error"));
			return;
		}
		if(exportpath == null || exportpath.equals("") || exportpath == "") {
			SwingUtils.warning(null, Language.get("dialog.warning"), Language.get("error.export_paths_error"));
			return;
		}
		
		String input_txt_file = exportpath+"/"+InputTxtFilelist;
		String input_ttf_file = exportpath+"/"+InputTtfFilelist;
		GenFileList.gen(txtpaths,input_txt_file);
		GenFileList.gen(ttfpaths,input_ttf_file);
		
		String extractFileStringOutput = exportpath+"/"+IntermediateFolder;
		FontExtract.exec(input_txt_file, extractFileStringOutput);
		
		String chineseoutput = extractFileStringOutput+"/"+ChineseOutPut;
		String unchineseoutput = extractFileStringOutput+"/"+UnChineseOutPut;
		String outputfontfolder = exportpath+"/"+OutputFolder;
		
		FileUtils.mkdirs(outputfontfolder);
		
		//ttf 文件
		File file = new File(input_ttf_file);
		List<String> filePathList =ExtractionOperationUtil.ExtractStringListFromFile(file);
		for(String filePath :filePathList)
		{
			System.out.println(filePath);
			File f = new File(filePath);
			if(!f.exists() ||!f.isFile()){
				System.out.println("TTF不存在或者无效"+f.toString());
				continue;
			}
			
			try {
				String outputfont = outputfontfolder+"/"+f.getName();
				
				SfntTool.main(new String[]{"-c",chineseoutput,unchineseoutput,filePath,outputfont});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		int result = JOptionPane.showConfirmDialog(
				frame,
                Language.get("menu.open_folder"),
                Language.get("dialog.prompt"),
                JOptionPane.OK_OPTION
        );
        System.out.println("选择结果: " + result);
        if(result == 0) {
        	try {
				FileUtils.openFile(new File(outputfontfolder));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}

}
