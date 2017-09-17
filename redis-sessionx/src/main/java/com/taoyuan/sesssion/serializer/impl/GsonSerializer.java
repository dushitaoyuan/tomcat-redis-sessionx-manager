package com.taoyuan.sesssion.serializer.impl;

import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.taoyuan.session.serializer.JsonSerializer;
import com.taoyuan.session.serializer.Serializer;

public class GsonSerializer extends Serializer implements JsonSerializer {
	private final static Gson gson=new Gson();
    private final static ObjectMapper objectMapper = new ObjectMapper();
	    
	@Override
	public <T> Object convertValue(Object fromValue, Class<T> toValueType) {
		return objectMapper.convertValue(fromValue, toValueType);
	}

	@Override
	public <T> byte[] serialize(T obj) {
		try {
			return gson.toJson(obj).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
		try {
			return  gson.fromJson(new String(bytes,"UTF-8"), clazz);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
