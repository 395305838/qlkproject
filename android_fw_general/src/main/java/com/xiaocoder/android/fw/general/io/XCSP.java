package com.xiaocoder.android.fw.general.io;

import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author xiaocoder
 * 2015-2-28 上午11:30:18
 */
public class XCSP {
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;

	/**
	 * @param context
	 * @param fileName
	 *            不要加 ".xml"的后缀名
	 * @param mode
	 *            Context.Mode 下有四种模式常量,一般都用私有模式
	 */
	public XCSP(Context context, String fileName, int mode) {
		super();
		this.sharedPreferences = context.getSharedPreferences(fileName, mode);
	}

	// =============================sharedPreferences存数据======================================================
	public boolean putString(String key, String value) {
		editor = sharedPreferences.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	public boolean putInt(String key, int value) {
		editor = sharedPreferences.edit();
		editor.putInt(key, value);
		return editor.commit();
	}

	public boolean putLong(String key, long value) {
		editor = sharedPreferences.edit();
		editor.putLong(key, value);
		return editor.commit();
	}

	public boolean putFloat(String key, float value) {
		editor = sharedPreferences.edit();
		editor.putFloat(key, value);
		return editor.commit();
	}

	public boolean putBoolean(String key, boolean value) {
		editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}


	public boolean putStringSet(String key, Set<String> values) {
		  editor = sharedPreferences.edit();
		  editor.putStringSet(key, values);
		  return editor.commit();
	}


	// =============================sharedPreferences取数据======================================================
	public String getString(String key, String defaultValue) {
		return sharedPreferences.getString(key, defaultValue);
	}

	/**
	 * 获取String,未指定默认值,则自动设置为null
	 * 
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return getString(key, null);
	}

	public int getInt(String key, int defaultValue) {
		return sharedPreferences.getInt(key, defaultValue);
	}

	/**
	 * 获取int,未指定默认值,则自动设置为-1
	 * 
	 * @param key
	 * @return
	 */
	public int getInt(String key) {
		return getInt(key, -1);
	}

	public long getLong(String key, long defaultValue) {
		return sharedPreferences.getLong(key, defaultValue);
	}

	/**
	 * 获取long,未指定默认值,则自动设置为-1
	 * 
	 * @param key
	 * @return
	 */
	public long getLong(String key) {
		return getLong(key, -1);
	}

	public float getFloat(String key, float defaultValue) {
		return sharedPreferences.getFloat(key, defaultValue);
	}

	/**
	 * 获取float,未指定默认值,则自动设置为-1
	 * 
	 * @param key
	 * @return
	 */
	public float getFloat(String key) {
		return getFloat(key, -1);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return sharedPreferences.getBoolean(key, defaultValue);
	}

	/**
	 * 获取boolean,未指定默认值,则自动设置为false
	 * 
	 * @param key
	 * @return
	 */
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public Map<String, ?> getAll() {
		return sharedPreferences.getAll();
	}


	public Set<String> getStringSet(String key , Set<String> defValues) {
		return sharedPreferences.getStringSet(key, defValues);
	}

}