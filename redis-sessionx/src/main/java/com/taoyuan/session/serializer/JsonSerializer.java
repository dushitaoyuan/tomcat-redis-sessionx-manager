package com.taoyuan.session.serializer;


public interface JsonSerializer {
	public <T> Object convertValue(Object fromValue, Class<T>  toValueType);
}
