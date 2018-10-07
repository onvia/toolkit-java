package com.vec2.swing.widgets;


import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import com.vec2.editor.utils.Language;

@SuppressWarnings("serial")
public class Label extends JLabel {
	public Label(String key) {
		super(Language.get(key));
		setBorder(new EmptyBorder(8, 8, 8, 8));
	}
	public Label(String key,Object... args) {
		super(Language.get(key,args));
		setBorder(new EmptyBorder(8, 8, 8, 8));
	}
}
