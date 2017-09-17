package com.taoyuan.sesssion.serializer.impl;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.taoyuan.tomcat.redissessions.SessionMap;

public class Test {
    public static void main(String[] args) throws Exception  {
	testGson();
		 
	}
    public static void testJack() throws Exception{
    	SessionMap se=new SessionMap();
		se.getSaveValues().add(new User("lei", "123"));
		 ObjectMapper obj = new ObjectMapper();
		 byte[] writeValueAsBytes = obj.writeValueAsBytes(se);
		 SessionMap readValue = obj.readValue(writeValueAsBytes, SessionMap.class);
		 System.out.println(readValue.getSaveValues().get(0) instanceof String);
    }
    public static void testGson() throws Exception{
    	 ObjectMapper obj = new ObjectMapper();
     	SessionMap se=new SessionMap();
        se.getSaveValues().add(new User("lei", "123"));
		Gson gson=new GsonBuilder().create();
    	String json = gson.toJson(se);
    	SessionMap fromJson = gson.fromJson(json, SessionMap.class);
    	System.out.println(obj.convertValue(fromJson.getSaveValues().get(0), User.class));
    	
    }
}
