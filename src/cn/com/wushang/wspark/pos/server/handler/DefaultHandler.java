package cn.com.wushang.wspark.pos.server.handler;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public abstract class DefaultHandler
{
  static final Logger logger = Logger.getLogger(DefaultHandler.class);
  protected final String EMPTY_JSON_OBJECT = "{}";
  
  public abstract String handle(String paramString);
  
  public String errorMessage(Throwable ex)
  {
    String message = ex.getMessage();
    if (StringUtils.isEmpty(message)) {
      message = ex.getClass().getCanonicalName();
    }
    return message;
  }
}
