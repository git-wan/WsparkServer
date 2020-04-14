package cn.com.wushang.wspark.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;

public class JsonUtils
{
  public static <T> T readValue(String json, Class<T> clazz)
    throws Exception
  {
    T v = ((ObjectMapper)Tls.mapper.get()).readValue(json, clazz);
    return v;
  }
  
  public static <T> T readValue(InputStream stream, Class<T> clazz)
    throws Exception
  {
    T v = ((ObjectMapper)Tls.mapper.get()).readValue(stream, clazz);
    return v;
  }
  
  public static String writeValue(Object object)
    throws Exception
  {
    String v = ((ObjectMapper)Tls.mapper.get()).writeValueAsString(object);
    return v;
  }
  
  public static String writeValueQuietly(Object object)
  {
    try
    {
      return ((ObjectMapper)Tls.mapper.get()).writeValueAsString(object);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      if (object == null) {
        return "null";
      }
    }
    return object.toString();
  }
}
