package cn.com.wushang.wspark.socket;

public class SocketConfig
{
  private String host = "127.0.0.1";
  private int port = 5000;
  private int cotimeout = 1000;
  private int sotimeout = 5000;
  
  public SocketConfig() {}
  
  public SocketConfig(String host, int port)
  {
    this.host = host;
    this.port = port;
  }
  
  public SocketConfig(String host, int port, int cotimeout, int sotimeout)
  {
    this.host = host;
    this.port = port;
    this.cotimeout = cotimeout;
    this.sotimeout = sotimeout;
  }
  
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
  
  public int getCotimeout()
  {
    return this.cotimeout;
  }
  
  public void setCotimeout(int cotimeout)
  {
    this.cotimeout = cotimeout;
  }
  
  public int getSotimeout()
  {
    return this.sotimeout;
  }
  
  public void setSotimeout(int sotimeout)
  {
    this.sotimeout = sotimeout;
  }
}
