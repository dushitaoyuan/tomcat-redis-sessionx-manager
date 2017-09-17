package com.taoyuan.session.serializer;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashMap;


import com.taoyuan.tomcat.redissessions.RedisSession;
import com.taoyuan.tomcat.redissessions.SessionMap;
import com.taoyuan.tomcat.redissessions.SessionSerializationMetadata;

public class SessionSerializer implements ISessionSerializer {
	private Serializer serializer;
	
  public SessionSerializer() {
		super();
	}
  
 public Serializer getSerializer() {
	return serializer;
}
public SessionSerializer(Serializer serializer) {
		super();
		this.serializer = serializer;
	}
public  byte[] attributesHashFrom(RedisSession session) throws IOException{
	  HashMap<String,Object> attributes = new HashMap<String,Object>();
	    for (Enumeration<String> enumerator = session.getAttributeNames(); enumerator.hasMoreElements();) {
	      String key = enumerator.nextElement();
	      attributes.put(key, session.getAttribute(key));
	    }

	    byte[] serialized = serializer.serialize(attributes);
	    MessageDigest digester = null;
	    try {
	      digester = MessageDigest.getInstance("MD5");
	    } catch (NoSuchAlgorithmException e) {
	    }
	    return digester.digest(serialized);
  };
  public byte[] serializeFrom(RedisSession session, SessionSerializationMetadata metadata) throws IOException{
	  session.writeObjectData(null);
	  session.getSessionMap().setMetadata(metadata);
      return serializer.serialize(session.getSessionMap());	  
  };
  public void deserializeInto(byte[] data, RedisSession session, SessionSerializationMetadata metadata) throws IOException, ClassNotFoundException{
	  SessionMap sessionMap = (SessionMap) serializer.deserialize(data, SessionMap.class);
      SessionSerializationMetadata serializedMetadata = sessionMap.getMetadata();
      metadata.copyFieldsFrom(serializedMetadata);
      session.setSessionMap(sessionMap);
      session.readObjectData(null);
  };

}
