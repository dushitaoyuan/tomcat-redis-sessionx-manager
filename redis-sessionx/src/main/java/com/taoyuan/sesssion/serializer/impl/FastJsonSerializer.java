package com.taoyuan.sesssion.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taoyuan.session.serializer.JsonSerializer;
import com.taoyuan.session.serializer.Serializer;

public class FastJsonSerializer extends Serializer implements JsonSerializer {
    private final static ObjectMapper objectMapper = new ObjectMapper();
	    
	@Override
	public <T> Object convertValue(Object fromValue, Class<T> toValueType) {
		return objectMapper.convertValue(fromValue, toValueType);
	}

	@Override
	public <T> byte[] serialize(T obj) {
		return JSON.toJSONBytes(obj);
	}

	@Override
	public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
		return  JSON.parseObject(bytes, clazz);
	}

}
