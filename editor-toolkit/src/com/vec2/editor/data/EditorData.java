package com.vec2.editor.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;

import com.keyroy.util.json.Json;
import com.vec2.editor.utils.FileUtils;

public class EditorData {

	public static FontThinData fontThinData = new FontThinData();
	
	public static void saveFontThinData() {
		save(fontThinData);
	}
	
	public static void save() {
		save(new EditorData());
	}
	
	public static void save(Object object) {
		String filename = object.getClass().getSimpleName();
		try {
			Json json = new Json(object);
			String text = json.toString();
			FileUtils.save(new File(filename), text);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void load() {
		load(EditorData.class);
	}
	public static <T> T load(Class<T> clazz) {
		File file = new File(clazz.getSimpleName());
		if (file.exists()) {

			try {
				FileInputStream inputStream = new FileInputStream(file);
				Json json = new Json(inputStream);
				T t = json.toObject(clazz);
				inputStream.close();
				return t;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	public static void main(String[] args) throws IllegalAccessException {
		EditorData dataManager = new EditorData();
		for (Field field : EditorData.class.getDeclaredFields()) {
			System.out.println(field.getName());
			try {
				System.out.println(field.toGenericString());
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}
	}
}
