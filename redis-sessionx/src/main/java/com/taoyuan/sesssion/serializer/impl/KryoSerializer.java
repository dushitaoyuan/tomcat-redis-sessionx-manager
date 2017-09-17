package com.taoyuan.sesssion.serializer.impl;

import java.io.ByteArrayOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.taoyuan.session.serializer.Serializer;

public class KryoSerializer extends Serializer {
	private static final Kryo kryo = new Kryo();

	@Override
	public <T> byte[] serialize(T obj) {
		try (Output output = new Output(new ByteArrayOutputStream())) {
			kryo.writeObject(output, obj);
			byte[] bytes = output.toBytes();
			return bytes;
		}

	}

	@Override
	public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
		try (Input input = new Input(bytes)) {
			return kryo.readObject(input, clazz);
		}
	}
}
