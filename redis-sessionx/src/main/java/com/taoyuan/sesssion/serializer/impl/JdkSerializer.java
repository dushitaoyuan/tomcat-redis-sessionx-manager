package com.taoyuan.sesssion.serializer.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.catalina.util.CustomObjectInputStream;

import com.taoyuan.session.serializer.Serializer;


public class JdkSerializer extends Serializer{

	@Override
	public <T> byte[] serialize(T obj) {
		
		try {
			ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
			ObjectOutputStream out=new ObjectOutputStream(byteArr);
			out.writeObject(obj);
			out.flush();
			return byteArr.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
		try {
			ObjectInputStream input=new CustomObjectInputStream(new ByteArrayInputStream(bytes), Thread.currentThread().getContextClassLoader());
			return input.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
