package com.keyroy.util.json;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.m.JSONArray;
import org.json.m.JSONObject;
import org.json.m.JSONTokener;

public class Json {
	private static final String CLASS_KEY = "class";
	// json 的源
	private JSONObject source;

	public Json() {
		source = new JSONObject();
	}

	public Json(String string) {
		source = new JSONObject(string);
	}

	public Json(InputStream inputStream) {
		try {
			this.source = new JSONObject(new JSONTokener(inputStream));
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Json(InputStream inputStream, Charset charset) {
		try {
			this.source = new JSONObject(new JSONTokener(inputStream, charset));
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Json(Object object) {
		try {
			this.source = encode(object, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public final <T> T toObject(Class<T> clazz) throws Exception {
		if (source != null) {
			return decode(clazz, source);
		} else {
			return null;
		}
	}

	public final JSONObject getSource() {
		return source;
	}

	@Override
	public String toString() {
		if (source != null) {
			return source.toString();
		}
		return super.toString();
	}

	// ------------------------------------------------------------------------------
	@SuppressWarnings("rawtypes")
	/***
	 * 初始化对象到 JSONObject
	 * @param source 目标对象
	 * @param template
	 * @return
	 * @throws Exception
	 */
	private static final JSONObject encode(Object source, Class<?> template) throws Exception {
		if (source != null) {
			if (source instanceof JSONObject) {
				return (JSONObject) source;
			} else {
				JSONObject jsonObject = null;
				Class<?> clazz = ReflectTools.getClass(source);
				if (ReflectTools.isBaseType(clazz) && ReflectTools.isDefaultValue(source) == false) {// 常量
					jsonObject = new JSONObject();
					jsonObject.append(clazz.getSimpleName(), String.valueOf(source));
				} else if (ReflectTools.isArray(source)) { // 数组
					Class<?> arrayClass = clazz.getComponentType();
					JSONArray jsonArray = new JSONArray();
					Object[] objects = (Object[]) source;
					for (Object object : objects) {
						if (object != null) {
							if (ReflectTools.isBaseType(arrayClass)) { // 基础类型
								jsonArray.put(object);
							} else { // 对象类型
								JSONObject childJsonObject = encode(object, null);
								jsonArray.put(childJsonObject);
							}
						}
					}
					//
					jsonObject = jsonArray;
				} else if (List.class.isInstance(source)) { // 列表
					JSONArray jsonArray = new JSONArray();
					List list = (List) source;
					for (Object object : list) {
						if (object != null) {
							if (ReflectTools.isBaseType(object.getClass())) { // 基础类型
								jsonArray.put(object);
							} else { // 对象类型
								JSONObject childJsonObject = encode(object, template);
								if (template != null && template.equals(object.getClass()) == false) {
									childJsonObject.append(CLASS_KEY, object.getClass().getName());
								}
								jsonArray.put(childJsonObject);
							}
						}
					}
					//
					jsonObject = jsonArray;
				} else if (Map.class.isInstance(source)) { // 对应表
					jsonObject = new JSONObject();
					Map<?, ?> map = (Map<?, ?>) source;
					Set<?> set = map.keySet();
					Iterator<?> iterator = set.iterator();
					while (iterator.hasNext()) {
						Object key = (Object) iterator.next();
						Object value = map.get(key);
						if (ReflectTools.isBaseType(value.getClass())) { // 基础类型
							jsonObject.append(String.valueOf(key), value);
						} else { // 对象类型
							JSONObject childJsonObject = encode(value, null);
							jsonObject.append(String.valueOf(key), childJsonObject);
						}
					}
				} else if (source instanceof Class<?>) {
				} else {
					jsonObject = new JSONObject();
					List<Field> fields = ReflectTools.getFields(clazz);
					for (Field field : fields) {
						Object value = field.get(source);
						String fieldName = getFieldName(field);
						if (value != null) {
							if (ReflectTools.isBaseType(value.getClass())) { // 基础类型
								if (ReflectTools.isDefaultValue(value)) {
									// 默认值
								} else {
									jsonObject.append(fieldName, value);
								}
							} else if (ReflectTools.isSubClass(field, List.class)) {
								JSONObject childJsonObject = encode(value,
										ReflectTools.getClass(field.getGenericType()));
								jsonObject.append(fieldName, childJsonObject);
							} else if (field.getType().equals(clazz) == false) { // 对象类型
								JSONObject childJsonObject = encode(value, null);
								// 查看对象 和 声明类型是否相同
								Class<?> valueClass = value.getClass();
								if (valueClass.equals(template)) { // 数据类型和模板类型相同

								} else if (value.getClass().equals(field.getType())) {// 数据类型和声明类型相同

								} else {
									childJsonObject.append(CLASS_KEY, value.getClass().getName());
								}
								jsonObject.append(fieldName, childJsonObject);
							}
						}
					}
				}
				return jsonObject;
			}
		}
		return null;
	}

	public static final void fill(Object t, InputStream inputStream) throws Exception {
		fill(t, new JSONObject(new JSONTokener(inputStream)));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static final void fill(Object t, JSONObject source) throws Exception {
		if (t instanceof Class<?>) {
			throw new IllegalArgumentException("object can not been Class");
		} else {
			List<Field> fields = ReflectTools.getFields(t.getClass());
			for (Field field : fields) {
				String fieldName = getFieldName(field);
				if (source.has(fieldName)) {
					Object value = source.get(fieldName);
					if (ReflectTools.isBaseType(field.getType()) && ReflectTools.isBaseType(value.getClass())) { // 基础类型
						if (value.getClass().equals(field.getType())) { // 基本类型匹配
							field.set(t, value);
						} else {
							field.set(t, ReflectTools.parser(String.valueOf(value), field.getType()));
						}
					} else if (ReflectTools.isArray(field) && value instanceof JSONArray) { // 数组
						Class<?> arrayClass = ReflectTools.getTemplate(field);
						JSONArray jsonArray = (JSONArray) value;
						Object array = Array.newInstance(arrayClass, jsonArray.length());
						field.set(t, array);
						if (ReflectTools.isBaseType(arrayClass)) { // 基础类型数组
							for (int i = 0; i < jsonArray.length(); i++) {
								Array.set(array, i, jsonArray.get(i));
							}
						} else { // 对象类型数组
							for (int i = 0; i < jsonArray.length(); i++) {
								Object jsonElement = jsonArray.get(i);
								if (jsonElement instanceof JSONObject) {
									Object element = decode(arrayClass, (JSONObject) jsonElement);
									Array.set(array, i, element);
								}
							}
						}

					} else if (ReflectTools.isSubClass(field, List.class) && value instanceof JSONArray) { // 列表
						JSONArray jsonArray = (JSONArray) value;
						Class<?> arrayClass = ReflectTools.getTemplate(field);
						List list = null;
						if (field.getType().equals(List.class)) {
							list = new ArrayList<Object>(jsonArray.length());
						} else {
							list = (List) field.getType().newInstance();
						}
						field.set(t, list);
						if (ReflectTools.isBaseType(arrayClass)) { // 基础类型数组
							for (int i = 0; i < jsonArray.length(); i++) {
								list.add(jsonArray.get(i));
							}
						} else { // 对象类型数组
							for (int i = 0; i < jsonArray.length(); i++) {
								Object jsonElement = jsonArray.get(i);
								if (jsonElement instanceof JSONObject) {
									JSONObject jsonObject = (JSONObject) jsonElement;
									if (jsonObject.has(CLASS_KEY)) {
										Class<?> elementClass = Class.forName(jsonObject.getString(CLASS_KEY));
										list.add(decode(elementClass, jsonObject));
									} else {
										list.add(decode(arrayClass, jsonObject));
									}
								}
							}
						}
					} else if (ReflectTools.isSubClass(field, Map.class) && value instanceof JSONObject) {// 对应表
						JSONObject jsonObject = (JSONObject) value;
						Class<?>[] classes = ReflectTools.getTemplates(field);

						Class<?> keyClass = classes[0];
						Class<?> valClass = classes[1];

						Map map = null;
						if (field.getType().equals(Map.class)) {
							map = new HashMap(jsonObject.length());
						} else {
							map = (Map) field.getType().newInstance();
						}
						field.set(t, map);
						String[] keies = JSONObject.getNames(jsonObject);
						for (String key : keies) {
							Object mapKey = key;
							Object mapValue = jsonObject.get(key);
							if (keyClass != null) {
								mapKey = ReflectTools.parser(key, keyClass);
							}
							if (mapValue != null && ReflectTools.isBaseType(mapValue.getClass())) {
								map.put(mapKey, mapValue);
							} else if (mapValue instanceof JSONObject && valClass != null) {
								Object object = decode(valClass, (JSONObject) mapValue);
								map.put(mapKey, object);
							}
						}
					} else if (value instanceof JSONObject) { // 对象类型
						JSONObject jsonObject = (JSONObject) value;
						Object object = null;
						if (jsonObject.has(CLASS_KEY)) {
							Class<?> elementClass = Class.forName(jsonObject.getString(CLASS_KEY));
							object = decode(elementClass, jsonObject);
						} else {
							object = decode(field.getType(), jsonObject);
						}
						field.set(t, object);
					}
				}
			}
		}

	}

	private static final <T> T decode(Class<T> clazz, JSONObject source) throws Exception {
		T t = clazz.newInstance();
		fill(t, source);
		return t;
	}

	private static final String getFieldName(Field field) {
		JsonAn jsonAn = field.getAnnotation(JsonAn.class);
		if (jsonAn != null && jsonAn.value().length() > 0) {
			return jsonAn.value();
		}
		return field.getName();
	}

}
