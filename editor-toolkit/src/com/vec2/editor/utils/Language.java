package com.vec2.editor.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

//语言 (Language)
public class Language {
	private static final Properties PROPERTIES = new Properties();

	public static final void load(String file) {
		load(Language.class.getResourceAsStream(file));
	}

	public static final void load(File file) {
		try {
			load(new FileInputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void load(InputStream inputStream) {
		try {
			if(PROPERTIES == null) {
				System.out.println("the PROPERTIES is null");
			}
			if(inputStream == null) {
				System.out.println("inputStream is null");
			}
			PROPERTIES.load(inputStream);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void print() {
		Enumeration<?> enumeration = PROPERTIES.propertyNames();
		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			System.out.println(key + " : " + PROPERTIES.getProperty(key));
		}
	}

	public static final String get(String key) {
		String rt = PROPERTIES.getProperty(key.toLowerCase());
		if (rt != null) {
			return rt;
		} else {
			return key;
		}
	}
	public static final String[] getList(String... keys) {
		int length = keys.length;
		String strings [] = new String[length];
		for (int i = 0; i < length ; i++) {
			String key = keys[i];
			String rt = PROPERTIES.getProperty(key.toLowerCase());
			
			if (rt != null) {
				strings[i] = rt;
			} else {
				strings[i] = key;
			}
		}
	
		return strings;
	}
	
	public static final String get(String key,Object... args) {
		String rt = PROPERTIES.getProperty(key.toLowerCase());
		if (rt != null) {
			if(args.length > 0) {
				rt = MessageFormat.format(rt, args);	
			}			
			return rt;
		} else {
			return key;
		}
	}
	
	
}
