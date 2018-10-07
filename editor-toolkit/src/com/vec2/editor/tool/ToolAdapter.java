package com.vec2.editor.tool;

import java.awt.EventQueue;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.vec2.editor.utils.Language;
import com.vec2.editor.utils.RuntimeModel;
import com.vec2.swing.utils.SwingUtils;

public abstract class ToolAdapter {
	protected RuntimeModel model;
	protected JFrame frame;
	protected static void init() {
		Language.load("/zn");
		SwingUtils.initWindowsLookAndFeel();
	}
	public static <T extends ToolAdapter> void run(Class<T> clazz) {
		init();
		
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("rawtypes")
			@Override
			public void run() {
				try {
					Constructor con = clazz.getConstructor(RuntimeModel.class);
					ToolAdapter toolAdapter = (ToolAdapter) con.newInstance(RuntimeModel.independent);
					SwingUtils.center(toolAdapter.frame);
					toolAdapter.frame.setVisible(true);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	public ToolAdapter(RuntimeModel model) {
		this.model = model;
	}
	
	public abstract JPanel initialize(JFrame frame);
}
