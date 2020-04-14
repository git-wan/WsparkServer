package cn.com.wushang.wspark.socket;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class SocketFactory
{
  private String name;
  private LinkedList<SocketConfig> configs = new LinkedList();
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public List<SocketConfig> getConfigs()
  {
    return this.configs;
  }
  
  public void setConfigs(List<SocketConfig> configs)
  {
    this.configs.clear();
    if (configs != null) {
      this.configs.addAll(configs);
    }
  }
  
  public Socket connnect()
    throws Exception
  {
    int size = this.configs.size();
    while (size > 0)
    {
      size--;
      SocketConfig config = (SocketConfig)this.configs.getFirst();
      try
      {
        return connect(config);
      }
      catch (Exception ex)
      {
        this.configs.removeFirst();
        this.configs.addLast(config);
      }
    }
    throw new IOException("连接" + this.name + "失败");
  }
  
  Socket connect(SocketConfig server)
    throws Exception
  {
    String host = server.getHost();
    int port = server.getPort();
    int cotimeout = server.getCotimeout();
    int sotimeout = server.getSotimeout();
    
    StringBuilder message = new StringBuilder();
    message.append("connect to : host=").append(host);
    message.append(", port=").append(port);
    message.append(", cotimeout=").append(cotimeout);
    message.append(", sotimeout=").append(sotimeout);
    System.out.println(message.toString());
    
    Socket socket = new Socket();
    InetSocketAddress socketAddress = new InetSocketAddress(host, port);
    socket.connect(socketAddress, cotimeout);
    socket.setSoTimeout(sotimeout);
    return socket;
  }
}
