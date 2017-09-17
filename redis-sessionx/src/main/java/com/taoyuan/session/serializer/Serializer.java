package com.taoyuan.session.serializer;

import com.taoyuan.sesssion.serializer.impl.FastJsonSerializer;
import com.taoyuan.sesssion.serializer.impl.GsonSerializer;
import com.taoyuan.sesssion.serializer.impl.HessianSerializer;
import com.taoyuan.sesssion.serializer.impl.JacksonSerializer;
import com.taoyuan.sesssion.serializer.impl.JdkSerializer;
import com.taoyuan.sesssion.serializer.impl.ProtostuffSerializer;

/**
 * @author ll序列化抽象类
 *
 */
public abstract class Serializer {
	public abstract  <T> byte[] serialize(T obj);
	public abstract  <T> Object deserialize(byte[] bytes, Class<T> clazz);
	public enum SerializeEnum {
		HESSIAN(new HessianSerializer()),
		PROTOSTUFF(new ProtostuffSerializer()),
		JDK(new JdkSerializer()),
		JACKJSON(new JacksonSerializer()),
		FASTJSON(new FastJsonSerializer()),
		GSON(new GsonSerializer());

		public final Serializer serializer;
		private SerializeEnum (Serializer serializer) {
			this.serializer = serializer;
		}
		public static SerializeEnum match(String name, SerializeEnum defaultSerializer){
			for (SerializeEnum item : SerializeEnum.values()) {
				if (item.name().equals(name)) {
					return item;
				}
			}
			return defaultSerializer;
		}
	}
}
