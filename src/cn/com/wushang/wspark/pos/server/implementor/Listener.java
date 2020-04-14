package cn.com.wushang.wspark.pos.server.implementor;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.springframework.core.task.TaskExecutor;

import cn.com.wushang.wspark.Context;

public class Listener
  extends Thread
{
  private static Logger logger = Logger.getLogger(Listener.class);
  private String host = "0.0.0.0";
  private int port = 9004;
  private int backlog = 10;
  private int sotimeout = 300000;
  
  public String getHost()
  {
    return this.host;
  }
  
  public void setHost(String host)
  {
    this.host = host;
  }
  
  public int getPort()
  {
    return this.port;
  }
  
  public void setPort(int port)
  {
    this.port = port;
  }
  
  public int getBacklog()
  {
    return this.backlog;
  }
  
  public void setBacklog(int backlog)
  {
    this.backlog = backlog;
  }
  
  public int getSotimeout()
  {
    return this.sotimeout;
  }
  
  public void setSotimeout(int sotimeout)
  {
    this.sotimeout = sotimeout;
  }
  
  public void run()
  {
    try
    {
      logger.info("listen: " + this.host + ":" + this.port);
      InetAddress address = InetAddress.getByName(this.host);
      ServerSocket s = new ServerSocket(this.port, this.backlog, address);
      //死循环
      for (;;)
      {
        Socket socket = s.accept();
        String clientAddress = socket.getRemoteSocketAddress().toString();
        logger.debug(clientAddress);
        socket.setSoTimeout(this.sotimeout);
        
        Dispatcher dispatcher = new Dispatcher(socket);
        TaskExecutor executor = (TaskExecutor)Context.getBean(TaskExecutor.class);
        executor.execute(dispatcher);
      }
    }
    catch (Exception e)
    {
      logger.warn("", e);
    }
  }
}
