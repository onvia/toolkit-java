package com.vec2.editor.tool.textureunpacker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.vec2.editor.utils.Language;
import com.vec2.editor.utils.MessageKey;
import com.vec2.editor.utils.MessageListener;
import com.vec2.editor.utils.Messager;
import com.vec2.editor.utils.RuntimeModel;
import com.vec2.swing.utils.SwingUtils;
import com.vec2.swing.utils.SwingUtils.DropInAdapter;
import com.vec2.swing.widgets.Label;

public class TextureUnpackerUI extends DropInAdapter{
	
	RuntimeModel model;
	private JFrame frame;
	ArrayList<File> root = new ArrayList<File>();
	private static void init() {
		Language.load("/zn");
		SwingUtils.initWindowsLookAndFeel();
	}
	public static void main(String[] args) {
		init();
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				TextureUnpackerUI unTexturePacker = new TextureUnpackerUI(RuntimeModel.independent);
				SwingUtils.center(unTexturePacker.frame);
				unTexturePacker.frame.setVisible(true);
			}
		});
	}
	
	
	public TextureUnpackerUI(RuntimeModel model) {
		this.model = model;
		if(model == RuntimeModel.independent) {
			frame = new JFrame(Language.get("window.untexturepacker"));
			frame.setBounds(0, 0,640, 480);
			frame.setMinimumSize(new Dimension(640, 480));
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JPanel panel = initialize(frame);
			frame.setContentPane(panel);
		}		
	
	}
	public JPanel initialize(final JFrame frame) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		Label filePanel = new Label("text.drag_file","atlas");
		filePanel.setBackground(Color.WHITE);
		filePanel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(filePanel);
		
		final JLabel message_label = new JLabel("Ready");
		panel.add(message_label, BorderLayout.SOUTH);
		
		Messager.register(String.class, new MessageListener<String>() {
			@Override
			public void onMessage(String t, Object[] params) {
				if(is(MessageKey.ERROR, params)) {
					SwingUtils.warning(frame, Language.get("dialog.warning"), t);
				}else if(is(MessageKey.PROCESSING, params)) {
					message_label.setText(t);
				}else if (is(MessageKey.PROCESSING_END, params)) {
					message_label.setText(t);
					int result = JOptionPane.showConfirmDialog(
							frame,
	                        Language.get("menu.open_folder"),
	                        Language.get("dialog.prompt"),
	                        JOptionPane.OK_OPTION
	                );
	                System.out.println("选择结果: " + result);
	                if(result == 0) {
	                	openFolder();
	                }
				}
				frame.repaint();
			}
		});
		
		SwingUtils.addDropIn(panel,this);
		
		return panel;
	}
	
	private void openFolder() {
		for (File file : root) {
			try {
				Desktop.getDesktop().open(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void onDropIn(final List<File> files) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				for (File file : files) {					
					TextureUnpackerUI.this.onDropIn(file);
				}

				Messager.send("Processing End",MessageKey.PROCESSING_END);
			}
		}).start();
		
	}
	public void onDropIn(File file) {
		String fullpath = file.getAbsolutePath();
		if(fullpath.endsWith(".atlas") == false) {
			return;
		}

		if(root.contains(file.getParentFile()) == false) {
			root.add(file.getParentFile());	
		}
		
		String atlasFile = fullpath;
		String imageDir = file.getParent();
		String outputDir = atlasFile.replace(".atlas", "");
		TextureUnpacker.exec(atlasFile, imageDir, outputDir);

	}
	
}
