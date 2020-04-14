package cn.com.wushang.wspark;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Context
{
  protected static Logger logger = Logger.getLogger(Context.class);
  private static ApplicationContext applicationContext = null;
  
  private static void assertContextInjected() {}
  
  public static void initContext(String configLocation)
  {
    logger.info("Initializing Spring context.");
    applicationContext = new ClassPathXmlApplicationContext(configLocation);
    logger.info("Spring context initialized.");
  }
  
  public static ApplicationContext getContext()
  {
    assertContextInjected();
    return applicationContext;
  }
  
  public static <T> T getBean(String name)
  {
    assertContextInjected();
    return (T)applicationContext.getBean(name);
  }
  
  public static <T> T getBean(Class<T> requiredType)
  {
    assertContextInjected();
    return (T)applicationContext.getBean(requiredType);
  }
  
  public static void clearHolder()
  {
    applicationContext = null;
  }
}
