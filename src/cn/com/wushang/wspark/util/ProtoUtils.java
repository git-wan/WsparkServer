package cn.com.wushang.wspark.util;

import java.io.IOException;
import java.io.OutputStream;

public class ProtoUtils
{
  public static void sendInteger(OutputStream os, int data)
    throws IOException
  {
    byte[] buffer = new byte[4];
    buffer[3] = ((byte)(data >>> 24));
    buffer[2] = ((byte)(data >>> 16));
    buffer[1] = ((byte)(data >>> 8));
    buffer[0] = ((byte)(data >>> 0));
    os.write(buffer);
  }
  
  public static void sendString(OutputStream os, String text)
    throws Exception
  {
    if (text == null) {
      text = "";
    }
    byte[] bytes = text.getBytes("GBK");
    int length = bytes.length + 1;
    
    int lb = length % 256;
    os.write(lb);
    
    int hb = length / 256 % 256;
    os.write(hb);
    
    os.write(bytes);
    os.write(0);
  }
  
  public static void sendBlock(OutputStream os, byte[] data, int offset, int count)
    throws Exception
  {
    os.write(data, offset, count);
  }
}
