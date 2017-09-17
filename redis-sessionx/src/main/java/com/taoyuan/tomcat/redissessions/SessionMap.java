package com.taoyuan.tomcat.redissessions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author taoyuan
 * 目前 gson序列化,会有问题,抽个时间,我会排查一下问题所在
 * 
 *
 */
public class SessionMap implements Serializable {
	private Long creationTime;
	private Long lastAccessedTime;
	private Integer maxInactiveInterval;
	private Boolean isNew;
	private Boolean isValid;
	private Long thisAccessedTime;
	private String id;
	private Integer attrCount;
	private List<String> saveNames=new ArrayList<>();
	private List<Object> saveValues = new ArrayList<Object>();
	private List<Class> saveValuesType=null;//用来保存value类型，只有json序列化时，才有用
	
	public List<Class> getSaveValuesType() {
		return saveValuesType;
	}
	public void setSaveValuesType(List<Class> saveValuesType) {
		this.saveValuesType = saveValuesType;
	}
	private SessionSerializationMetadata metadata;
	
	public SessionSerializationMetadata getMetadata() {
		return metadata;
	}
	public void setMetadata(SessionSerializationMetadata metadata) {
		this.metadata = metadata;
	}
	public Integer getAttrCount() {
		return attrCount;
	}
	public void setAttrCount(Integer attrCount) {
		this.attrCount = attrCount;
	}
	public List<String> getSaveNames() {
		return saveNames;
	}
	public List<Object> getSaveValues() {
		return saveValues;
	}
	public void setSaveNames(List<String> saveNames) {
		this.saveNames = saveNames;
	}
	public void setSaveValues(List<Object> saveValues) {
		this.saveValues = saveValues;
	}
	public Long getCreationTime() {
		return creationTime;
	}
	public Long getLastAccessedTime() {
		return lastAccessedTime;
	}
	public Integer getMaxInactiveInterval() {
		return maxInactiveInterval;
	}
	public Boolean getIsNew() {
		return isNew;
	}
	public Boolean getIsValid() {
		return isValid;
	}
	public Long getThisAccessedTime() {
		return thisAccessedTime;
	}
	public String getId() {
		return id;
	}

	public void setCreationTime(Long creationTime) {
		this.creationTime = creationTime;
	}
	public void setLastAccessedTime(Long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}
	public void setMaxInactiveInterval(Integer maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}
	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}
	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}
	public void setThisAccessedTime(Long thisAccessedTime) {
		this.thisAccessedTime = thisAccessedTime;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "SessionMap [creationTime=" + creationTime + ", lastAccessedTime=" + lastAccessedTime
				+ ", maxInactiveInterval=" + maxInactiveInterval + ", isNew=" + isNew + ", isValid=" + isValid
				+ ", thisAccessedTime=" + thisAccessedTime + ", id=" + id + ", attrCount=" + attrCount + ", saveNames="
				+ saveNames + ", saveValues=" + saveValues + "]";
	}
	
}
