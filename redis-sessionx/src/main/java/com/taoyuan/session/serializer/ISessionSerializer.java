package com.taoyuan.session.serializer;

import java.io.IOException;
import com.taoyuan.tomcat.redissessions.RedisSession;
import com.taoyuan.tomcat.redissessions.SessionSerializationMetadata;
/**
 * @author taoyuan
 *session序列化
 */
public interface ISessionSerializer {
	   Serializer getSerializer();
	   byte[] attributesHashFrom(RedisSession session) throws IOException;
	   byte[] serializeFrom(RedisSession session, SessionSerializationMetadata metadata) throws IOException;
	   void deserializeInto(byte[] data, RedisSession session, SessionSerializationMetadata metadata) throws IOException, ClassNotFoundException;
}
