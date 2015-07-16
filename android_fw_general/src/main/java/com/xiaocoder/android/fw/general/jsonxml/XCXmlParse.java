package com.xiaocoder.android.fw.general.jsonxml;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;

public class XCXmlParse {
	/**
	 * 解析XML
	 * 
	 * @param is
	 *            xml字节流
	 * @param clazz
	 *            字节码 如：Object.class
	 * @param startName
	 *            开始位置
	 * @return 返回List列表
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getXmlList(InputStream is, Class<?> clazz, String startName) {
		List list = null;
		XmlPullParser parser = Xml.newPullParser();
		Object object = null;
		try {
			parser.setInput(is, "UTF-8");
			// 事件类型
			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					list = new ArrayList<Object>();
					break;
				case XmlPullParser.START_TAG:
					// 获得当前节点元素的名称
					String name = parser.getName();
					if (startName.equals(name)) {
						object = clazz.newInstance();
						// 判断标签里是否有属性，如果有，则全部解析出来
						int count = parser.getAttributeCount();
						for (int i = 0; i < count; i++)
							setXmlValue(object, parser.getAttributeName(i), parser.getAttributeValue(i));
					} else if (object != null) {
						setXmlValue(object, name, parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					if (startName.equals(parser.getName())) {
						list.add(object);
						object = null;
					}
					break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			Log.e("xml pull error", e.toString());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	/**
	 * 解析XML
	 * 
	 * @param is
	 *            xml字节流
	 * @param clazz
	 *            字节码 如：Object.class
	 * @return 返回Object
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object getXmlObject(InputStream is, Class<?> clazz) {
		XmlPullParser parser = Xml.newPullParser();
		Object object = null;
		List list = null;
		Object subObject = null;
		String subName = null;
		try {
			parser.setInput(is, "UTF-8");
			// 事件类型
			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					object = clazz.newInstance();
					break;
				case XmlPullParser.START_TAG:
					// 获得当前节点元素的名称
					String name = parser.getName();

					Field[] f = null;
					if (subObject == null) {
						f = object.getClass().getDeclaredFields();

						// 判断标签里是否有属性，如果有，则全部解析出来
						int count = parser.getAttributeCount();
						for (int j = 0; j < count; j++)
							setXmlValue(object, parser.getAttributeName(j), parser.getAttributeValue(j));
					} else {
						f = subObject.getClass().getDeclaredFields();
					}

					for (int i = 0; i < f.length; i++) {
						if (f[i].getName().equalsIgnoreCase(name)) {
							// 判断是不是List类型
							if (f[i].getType().getName().equals("java.util.List")) {
								Type type = f[i].getGenericType();
								if (type instanceof ParameterizedType) {
									// 获得泛型参数的实际类型
									Class<?> subClazz = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
									subObject = subClazz.newInstance();
									subName = f[i].getName();

									// 判断标签里是否有属性，如果有，则全部解析出来
									int count = parser.getAttributeCount();
									for (int j = 0; j < count; j++)
										setXmlValue(subObject, parser.getAttributeName(j), parser.getAttributeValue(j));

									if (list == null) {
										list = new ArrayList<Object>();
										f[i].setAccessible(true);
										f[i].set(object, list);
									}
								}
							} else { // 普通属性
								if (subObject != null) {
									setXmlValue(subObject, name, parser.nextText());
								} else {
									setXmlValue(object, name, parser.nextText());
								}
							}
							break;
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if (subObject != null && subName.equalsIgnoreCase(parser.getName())) {
						list.add(subObject);
						subObject = null;
						subName = null;
					}
					break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			Log.e("xml pull error", e.getMessage());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return object;
	}

	/**
	 * 把xml标签的值，转换成对象里属性的值
	 * 
	 * @param t
	 *            对象
	 * @param name
	 *            xml标签名
	 * @param value
	 *            xml标签名对应的值
	 */
	public static void setXmlValue(Object t, String name, String value) {
		try {
			Field[] f = t.getClass().getDeclaredFields();
			for (int i = 0; i < f.length; i++) {
				if (f[i].getName().equalsIgnoreCase(name)) {
					f[i].setAccessible(true);
					// 获得属性类型
					Class<?> fieldType = f[i].getType();
					if (fieldType == String.class) {
						f[i].set(t, value);
					} else if (fieldType == Integer.TYPE) {
						f[i].set(t, Integer.parseInt(value));
					} else if (fieldType == Float.TYPE) {
						f[i].set(t, Float.parseFloat(value));
					} else if (fieldType == Double.TYPE) {
						f[i].set(t, Double.parseDouble(value));
					} else if (fieldType == Long.TYPE) {
						f[i].set(t, Long.parseLong(value));
					} else if (fieldType == Short.TYPE) {
						f[i].set(t, Short.parseShort(value));
					} else if (fieldType == Boolean.TYPE) {
						f[i].set(t, Boolean.parseBoolean(value));
					} else {
						f[i].set(t, value);
					}
				}
			}
		} catch (Exception e) {
			Log.e("xml error", e.toString());
		}
	}

	// --------------------------------原始解析方式的demo----------------------------------------------------------------------
	private static final String BOOK = "book";
	private static final String BOOKS = "books";
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String PRICE = "price";

	/**
	 * 通过pull解析xml文件
	 * 
	 * @param is
	 *            ：inputStream
	 * @return ：ArrayList<HashMap<String, Object>>
	 */
	public static ArrayList<HashMap<String, Object>> parseXml(InputStream is) throws Exception {
		ArrayList<HashMap<String, Object>> data = null;
		HashMap<String, Object> map = null;
		// 解析XML文件
		XmlPullParser parser = Xml.newPullParser();// 构建一个解析器
		// 解析器解析的内容，以及输入的字符集
		parser.setInput(is, "UTF-8");
		int eventType = parser.getEventType();// 取得解析出的事件类型
		boolean isdone = false;// 是否完成
		while (eventType != XmlPullParser.END_DOCUMENT && !isdone) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:// 开始文档事件类型
				data = new ArrayList<HashMap<String, Object>>();
				break;
			case XmlPullParser.START_TAG:// 开始标记事件类型
				String tag = parser.getName();// 取得标记名
				if (tag.equals(BOOK)) {
					map = new HashMap<String, Object>();
					map.put(ID, Integer.valueOf(parser.getAttributeValue(0)));// 取得book标记的id属性值
				} else if (NAME.equals(tag) || PRICE.equals(tag)) {
					map.put(tag, parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:// 结束标记事件类型
				if (parser.getName().equals(BOOK)) {
					data.add(map);// 把map数据添加到list中
					if (map.get(NAME).toString().equals("牡丹")) {
						// isdone=true;//已经完成
					}
				}
			default:
				break;
			}
			eventType = parser.next();// 手动指向下一个节点，继续解析
		}
		return data;
	}

	/**
	 * 输出数据以xml的形式 输出到文件中
	 * 
	 * @param data
	 *            ：ArrayList<HashMap<String, Object>>
	 * @param fos
	 *            ：FileOutputStream
	 */
	public static void exportXml(ArrayList<HashMap<String, Object>> data, FileOutputStream fos) throws Exception {
		// <?xml version=1.0 encoding="utf-8"?>- START_DOCUMENT :文档的开始事件
		// 调用XMLPullParser.next() 指向下一个节点继续解析
		// <books>-START_TAG
		// <book id="1">
		// <name>菊花</name>
		// <price>3</price>
		// </book>-END_TAG
		// <book id="6">
		// <name>牡丹</name>
		// <price>2</price>
		// </book>
		// </books>
		XmlSerializer serializer = Xml.newSerializer();// 取得一个序列号器，用于写xml数据
		serializer.setOutput(fos, "UTF-8");// 设置要输出的内容 以及编码集
		serializer.startDocument("UTF-8", true);// standalone :表示该文件是否单独可以存储
		/**
		 * 写开始标记 namespace：命名空间
		 */
		serializer.startTag(null, BOOKS);
		for (HashMap<String, Object> item : data) {
			// 写book开始标记
			serializer.startTag(null, BOOK);
			serializer.attribute(null, ID, item.get(ID).toString());// 写book开始标记的属性
			serializer.startTag(null, NAME);// 写name开始标记
			serializer.text(item.get(NAME).toString());// 写文本
			serializer.endTag(null, NAME);
			serializer.startTag(null, PRICE);// 写price开始标记
			serializer.text(item.get(PRICE).toString());// 写文本
			serializer.endTag(null, PRICE);
			serializer.endTag(null, BOOK);
		}
		serializer.endTag(null, BOOKS);
		serializer.endDocument();// 写文档结尾
	}
}

/**
 * 对pull解析xml进行了封装 1、支持简单的列表解析 <?xml version="1.0" encoding="UTF-8"?> <list>
 * <user id="1"> <userName>张三</userName> <email>zhangsan@xxx.com</email> </user>
 * <user> <id>2</id> <userName>李四</userName> <email>lisi@xxx.com</email> </user>
 * <user> <id>3</id> <userName>王五</userName> <email>wangwu@xxx.com</email>
 * </user> </list>
 * 
 * public class User { private int id; private String userName; private String
 * email;
 * 
 * public int getId() { return id; } public void setId(int id) { this.id = id; }
 * public String getUserName() { return userName; } public void
 * setUserName(String userName) { this.userName = userName; } public String
 * getEmail() { return email; } public void setEmail(String email) { this.email
 * = email; } }
 * 
 * 2、支持简单对象解析 <?xml version="1.0" encoding="UTF-8"?> <menu id="1" code="1002">
 * <name>测试</name> </menu>
 * 
 * public class Menu { private int id; private int code; private String name;
 * 
 * public int getId() { return id; } public void setId(int id) { this.id = id; }
 * public int getCode() { return code; } public void setCode(int code) {
 * this.code = code; } public String getName() { return name; } public void
 * setName(String name) { this.name = name; }
 * 
 * }
 * 
 * 3、支持对象中包含List列表的解析，如： <?xml version="1.0" encoding="UTF-8"?> <parent>
 * <total>33</total> <list> <user> <id>1</id> <userName>张三</userName>
 * <email>zhangsan@xxx.com</email> </user> <user id="2"> <userName>李四</userName>
 * <email>lisi@xxx.com</email> </user> <user id="3" userName="王五">
 * <email>wangwu@xxx.com</email> </user> </list> </parent>
 * 
 * public class UserList { private int total; private List<User> user; public
 * int getTotal() { return total; } public void setTotal(int total) { this.total
 * = total; } public List<User> getUser() { return user; } public void
 * setUser(List<User> user) { this.user = user; } }
 */
