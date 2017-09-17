package com.taoyuan.tomcat.redissessions;

import java.io.*;


public class SessionSerializationMetadata implements Serializable {

  private byte[] sessionAttributesHash;

  public SessionSerializationMetadata() {
    this.sessionAttributesHash = new byte[0];
  }

  public byte[] getSessionAttributesHash() {
    return sessionAttributesHash;
  }

  public void setSessionAttributesHash(byte[] sessionAttributesHash) {
    this.sessionAttributesHash = sessionAttributesHash;
  }

  public void copyFieldsFrom(SessionSerializationMetadata metadata) {
    this.setSessionAttributesHash(metadata.getSessionAttributesHash());
  }







}
