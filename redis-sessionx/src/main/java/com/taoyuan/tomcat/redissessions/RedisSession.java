package com.taoyuan.tomcat.redissessions;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.catalina.Manager;
import org.apache.catalina.SessionListener;
import org.apache.catalina.session.StandardSession;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import com.taoyuan.session.serializer.JsonSerializer;
import com.taoyuan.session.serializer.Serializer;



public class RedisSession extends StandardSession {

  private final Log log = LogFactory.getLog(RedisSession.class);
 

  protected static Boolean manualDirtyTrackingSupportEnabled = false;
  protected  Boolean isJson = false;//是否为json序列化
  protected  Serializer serializer=null;
  public static void setManualDirtyTrackingSupportEnabled(Boolean enabled) {
    manualDirtyTrackingSupportEnabled = enabled;
  }

  protected static String manualDirtyTrackingAttributeKey = "__changed__";

  public static void setManualDirtyTrackingAttributeKey(String key) {
    manualDirtyTrackingAttributeKey = key;
  }


  protected HashMap<String, Object> changedAttributes;
  protected Boolean dirty;
  protected SessionMap sessionMap;
  

  public SessionMap getSessionMap() {
	return sessionMap;
}
public void setSessionMap(SessionMap sessionMap) {
	this.sessionMap = sessionMap;
}

public RedisSession(Manager manager,Serializer serializer) {
    super(manager);
    resetDirtyTracking();
    this.isJson=serializer instanceof JsonSerializer;
    this.serializer=serializer;
  }

  public Boolean isDirty() {
    return dirty || !changedAttributes.isEmpty();
  }

  public HashMap<String, Object> getChangedAttributes() {
    return changedAttributes;
  }

  public void resetDirtyTracking() {
    changedAttributes = new HashMap<>();
    dirty = false;
  }

  @Override
  public void setAttribute(String key, Object value) {
    if (manualDirtyTrackingSupportEnabled && manualDirtyTrackingAttributeKey.equals(key)) {
      dirty = true;
      return;
    }

    Object oldValue = getAttribute(key);
    super.setAttribute(key, value);
    if ( (value != null || oldValue != null)
         && ( value == null && oldValue != null
              || oldValue == null && value != null
              || !value.getClass().isInstance(oldValue)
              || !value.equals(oldValue) ) ) {
      if (this.manager instanceof RedisSessionManager
          && ((RedisSessionManager)this.manager).getSaveOnChange()) {
        try {
          ((RedisSessionManager)this.manager).save(this, true);
          
        } catch (IOException ex) {
          log.error("Error saving session on setAttribute (triggered by saveOnChange=true): " + ex.getMessage());
        }
      } else {
        changedAttributes.put(key, value);
      }
    }
  }
  @Override
  public void removeAttribute(String name) {
    super.removeAttribute(name);
    if (this.manager instanceof RedisSessionManager
        && ((RedisSessionManager)this.manager).getSaveOnChange()) {
      try {
        ((RedisSessionManager)this.manager).save(this, true);
      } catch (IOException ex) {
        log.error("Error saving session on setAttribute (triggered by saveOnChange=true): " + ex.getMessage());
      }
    } else {
      dirty = true;
    }
  }

  @Override
  public void setId(String id) {
    // Specifically do not call super(): it's implementation does unexpected things
    // like calling manager.remove(session.id) and manager.add(session).

    this.id = id;
  }

  @Override
  public void setPrincipal(Principal principal) {
    dirty = true;
    super.setPrincipal(principal);
  }

  @Override
  public void writeObjectData(java.io.ObjectOutputStream out) throws IOException {
	  if(sessionMap==null)sessionMap=new SessionMap();
	  sessionMap.setCreationTime(creationTime);
	  sessionMap.setLastAccessedTime(lastAccessedTime);
	  sessionMap.setMaxInactiveInterval(maxInactiveInterval);
	  sessionMap.setIsNew(isNew);
	  sessionMap.setIsValid(isValid);
	  sessionMap.setThisAccessedTime(thisAccessedTime);
	  sessionMap.setId(id);
     String keys[] = keys();
     ArrayList<String> saveNames = new ArrayList<String>();
     ArrayList<Object> saveValues = new ArrayList<Object>();
     ArrayList<Class> saveValuesType=null;
     if(isJson){//如果是json序列化，保存类型
    	 saveValuesType=new ArrayList<Class>();
    	 sessionMap.setSaveValuesType(saveValuesType);
     }
     for (int i = 0; i < keys.length; i++) {
        Object value = attributes.get(keys[i]);
        if (value == null)
            continue;
        else if ( (value instanceof Serializable)
                && (!exclude(keys[i]) )) {
            saveNames.add(keys[i]);
            saveValues.add(value);
            if(isJson)saveValuesType.add(value.getClass());
            
        } else {
            removeAttributeInternal(keys[i], true);
        }
    }
    int n = saveNames.size();
    sessionMap.setAttrCount(n);
    sessionMap.setSaveNames(saveNames);
    sessionMap.setSaveValues(saveValues);
  }

  @Override
  public void readObjectData(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
	  creationTime=sessionMap.getCreationTime();
	  lastAccessedTime=sessionMap.getLastAccessedTime();
	  maxInactiveInterval=sessionMap.getMaxInactiveInterval();
	  isNew=sessionMap.getIsNew();
	  isValid=sessionMap.getIsValid();
	  thisAccessedTime=sessionMap.getThisAccessedTime();
	  if (attributes == null)
          attributes = new ConcurrentHashMap<String, Object>();
	  int n =sessionMap.getAttrCount();
	  List<String> saveNames = sessionMap.getSaveNames();
	  List<Object> saveValues = sessionMap.getSaveValues();
	  List<Class> saveValuesType = sessionMap.getSaveValuesType();
	  boolean isValidSave = isValid;
      isValid = true;
      for (int i = 0; i < n; i++) {
          String name = saveNames.get(i);
          Object value = saveValues.get(i);
          if ((value instanceof String) && (value.equals(NOT_SERIALIZED)))
              continue;
          if (manager.getContainer().getLogger().isDebugEnabled())
              manager.getContainer().getLogger().debug("  loading attribute '" + name +
                  "' with value '" + value + "'");
          if(isJson)
        	  attributes.put(name, ((JsonSerializer)serializer).convertValue(value, saveValuesType.get(i)));
          else
          attributes.put(name, value);
      }
      isValid = isValidSave;

      if (listeners == null) {
          listeners = new ArrayList<SessionListener>();
      }

      if (notes == null) {
          notes = new Hashtable<String, Object>();
      }
  }
  

}
