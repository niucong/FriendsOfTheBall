package com.fob.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Json解析工具类 枚举类型按字符串解析，map类型按对象解析，对象数组按List解析
 */
public class JsonParseTool {
	private final static String TAG = "JsonParseTool";
	private final static ArrayList<Class<?>> classList = new ArrayList<Class<?>>();

	/**
	 * 解析单一对象
	 * 
	 * @param str
	 * @return @
	 * @throws JSONException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws IllegalArgumentException
	 */
	public static Object dealSingleResult(String str, Class<?> ownerClass)
			throws IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, JSONException {
		// L.i(TAG, "dealSingleResult str = " + str);
		JSONObject jsonObject = new JSONObject(str);
		return jsonSingleParse(jsonObject, ownerClass);
	}

	/**
	 * json解析器
	 * 
	 * @param jsonObject
	 * @param ownerClass
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws JSONException
	 */
	private static Object jsonSingleParse(JSONObject jsonObject,
			Class<?> ownerClass) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, JSONException {
		ArrayList<Field> fields = new ArrayList<Field>();
		fields = getField(ownerClass, fields);

		// Field[] fields = ownerClass.getDeclaredFields();
		// int fieldsLength = fields.length;
		// L.i(TAG, "jsonParse fieldsLength = " + fieldsLength);

		Object obj = ownerClass.newInstance();
		for (Field field : fields) {
			try {
				String fieldsName = field.getName();
				Object msg = jsonObject.get(fieldsName);
				L.d(TAG, "jsonParse fields=" + fieldsName + ",msg=" + msg);
				// if (msg != null) {
				// String msgStr = msg.toString();
				// if (msgStr != null && !"".equals(msgStr)
				// && !"null".equals(msgStr)) {
				Class<?> fieldClass = field.getType();
				Method method = getMethod(ownerClass, fieldClass, fieldsName);
				method.invoke(obj, new Object[] { msg });
				// L.i(TAG, "jsonParse msg=" + msg);
				// }
				// }
			} catch (Exception e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
			}
		}
		return obj;
	}

	/**
	 * 解析对象嵌套对象
	 * 
	 * @param str
	 * @param ownerClass
	 *            需要解析的Bean类数组
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws JSONException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public static Object dealComplexResult(String str, Class<?> ownerClass)
			throws InstantiationException, IllegalAccessException,
			JSONException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException {
		// L.v(TAG, "dealComplexResult str = " + str);
		classList.add(String.class);
		classList.add(int.class);
		classList.add(boolean.class);
		classList.add(long.class);
		classList.add(double.class);

		JSONObject jsonObject = new JSONObject(str);
		return jsonComplexParse(jsonObject, ownerClass);
	}

	/**
	 * 解析列表对象
	 * 
	 * @param str
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws JSONException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public static ArrayList<Object> dealListResult(String str,
			Class<?> ownerClass) throws InstantiationException,
			IllegalAccessException, JSONException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException {
		// L.getLongLog(TAG, "dealListResult", str);
		// L.v(TAG, "dealListResult Class.getName=" + ownerClass.getName());

		JSONArray jsonArray = new JSONArray(str);
		ArrayList<Object> list = null;

		if (jsonArray != null) {
			int size = jsonArray.length();
			// L.i(TAG, "dealListResult size = " + size);
			if (size > 0) {
				classList.add(String.class);
				classList.add(int.class);
				classList.add(boolean.class);
				classList.add(long.class);
				classList.add(double.class);
				list = new ArrayList<Object>();
				for (int i = 0; i < size; i++) {
					// L.i(TAG, "dealListResult i = " + i);
					JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
					Object obj = jsonComplexParse(jsonObject, ownerClass);
					list.add(obj);
				}
			}
		}
		return list;
	}

	/**
	 * json解析器
	 * 
	 * @param jsonObject
	 * @param ownerClass
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws JSONException
	 */
	private static Object jsonComplexParse(JSONObject jsonObject,
			Class<?> ownerClass) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, JSONException {
		ArrayList<Field> fields = new ArrayList<Field>();
		fields = getField(ownerClass, fields);
		// Field[] fields = ownerClass.getDeclaredFields();
		// int fieldsLength = fields.length;
		// L.i(TAG, "jsonParse fieldsLength = " + fieldsLength);

		HashMap<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();
		for (Field field : fields) {
			Class<?> mClass = field.getType();
			if (!classList.contains(mClass)) {
				// L.v(TAG, "jsonParse mClass = " + mClass);
				map.put(mClass, mClass);
			}
		}

		Object obj = ownerClass.newInstance();
		for (Field field : fields) {
			try {
				String fieldsName = field.getName();
				Object msg = jsonObject.get(fieldsName);
				// L.d(TAG, "jsonParse field=" + fieldsName + "：" + msg);
				if (msg != null) {
					String msgStr = msg.toString();
					if (msgStr != null && !"".equals(msgStr)
							&& !"null".equals(msgStr)) {
						Class<?> fieldClass = field.getType();
						if (map.containsValue(fieldClass)) {
							if (fieldClass == List.class) {
								ParameterizedType pt = (ParameterizedType) field
										.getGenericType();
								@SuppressWarnings("rawtypes")
								Class clz = (Class) pt.getActualTypeArguments()[0];
								msg = dealListResult(msgStr, clz);
							} else {
								JSONObject jsonObject2 = new JSONObject(msgStr);
								msg = jsonComplexParse(jsonObject2, fieldClass);
							}
						}

						// L.i(TAG, "jsonParse ownerClass = " + ownerClass
						// + ",fieldClass=" + fieldClass);
						Method method = getMethod(ownerClass, fieldClass,
								fieldsName);

						// L.i(TAG, "jsonParse method = " + method);
						if (fieldClass == String.class) {
							method.invoke(obj, new Object[] { msgStr });
						} else {
							method.invoke(obj, new Object[] { msg });
						}
						// L.i(TAG, "jsonParse fieldClass = " + fieldClass);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {

			}
		}
		return obj;
	}

	private static Method getMethod(Class<?> ownerClass, Class<?> fieldClass,
			String fieldsName) {
		Method method = null;
		String classMethod = "set" + changeString(fieldsName);
		// L.i(TAG, "jsonParse classMethod = " + classMethod);

		try {
			method = ownerClass.getDeclaredMethod(classMethod,
					new Class[] { fieldClass });
		} catch (Exception e) {
			method = getMethod(ownerClass.getSuperclass(), fieldClass,
					fieldsName);
		}
		return method;
	}

	private static ArrayList<Field> getField(Class<?> ownerClass,
			ArrayList<Field> list) {
		Field[] fields = ownerClass.getDeclaredFields();
		int fieldsLength = fields.length;
		for (int i = 0; i < fieldsLength; i++) {
			list.add(fields[i]);
		}
		if (ownerClass.getSuperclass() != null
				&& ownerClass.getSuperclass() != Object.class) {
			list = getField(ownerClass.getSuperclass(), list);
		}
		return list;
	}

	/**
	 * @param src
	 *            需要变更的字符串 源字符串
	 * @return 字符串，将src的第一个字母转换为大写，src为空时返回null
	 */
	private static String changeString(String src) {
		if (src != null) {
			StringBuffer sb = new StringBuffer(src);
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
			return sb.toString();
		} else {
			return null;
		}
	}
}
