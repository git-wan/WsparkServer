package cn.com.wushang.wspark.util;

import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.apache.log4j.Logger;

public class SocketUtils {
	private static Logger logger = Logger.getLogger(SocketUtils.class);

	public static Socket connect(String host, int port, int cotimeout, int sotimeout) throws Exception {
		Socket socket = null;
		try {
			StringBuilder message = new StringBuilder();
			message.append("connect");
			message.append(": host=").append(host);
			message.append(", port=").append(port);
			message.append(", cotimeoutt=").append(cotimeout);
			message.append(", sotimeoutt=").append(sotimeout);
			logger.debug(message);

			socket = new Socket();
			InetSocketAddress socketAddress = new InetSocketAddress(host, port);
			socket.connect(socketAddress, cotimeout);
			socket.setSoTimeout(sotimeout);
			return socket;
		} catch (Exception ex) {
			closeQuietly(socket);
			throw ex;
		}
	}

	public static void recvFully(Socket socket, byte[] data, int offset, int count) throws Exception {
		InputStream is = socket.getInputStream();
		do {
			int n = is.read(data, offset, count);
			if (n <= 0) {
				throw new EOFException();
			}
			count -= n;
			offset += n;
		} while (count > 0);
	}

	public static void sendInt(Socket socket, int n) throws Exception {
		byte[] data = new byte[4];
		data[0] = ((byte) (n & 0xFF));
		data[1] = ((byte) (n >>> 8));
		data[2] = ((byte) (n >>> 16));
		data[3] = ((byte) (n >>> 24));
		socket.getOutputStream().write(data);
	}

	public static int recvInt(Socket socket) throws Exception {
		byte[] data = new byte[4];
		recvFully(socket, data, 0, 4);
		int n = 0;
		n += (data[0] & 0xFF);
		n += ((data[1] & 0xFF) << 8);
		n += ((data[2] & 0xFF) << 16);
		n += ((data[3] & 0xFF) << 24);
		return n;
	}

	public static void sendShort(Socket socket, int n) throws Exception {
		byte[] data = new byte[2];
		data[0] = ((byte) (n & 0xFF));
		data[1] = ((byte) (n >>> 8));
		socket.getOutputStream().write(data);
	}

	public static int recvShort(Socket socket) throws Exception {
		byte[] data = new byte[2];
		recvFully(socket, data, 0, 2);

		int n = 0;
		n += (data[0] & 0xFF);
		n += ((data[1] & 0xFF) << 8);

		return n;
	}

	public static void sendString(Socket socket, String s) throws Exception {
		if (s == null) {
			s = "";
		}
		byte[] data = s.getBytes("GBK");
		sendShort(socket, data.length + 1);

		socket.getOutputStream().write(data);
		socket.getOutputStream().write(0);
	}

	public static void sendStringX(Socket socket, String s) throws Exception {
		sendString(socket, s);

		byte[] data = new byte[1];
		recvFully(socket, data, 0, 1);
	}

	public static String recvString(Socket socket) throws Exception {
		int size = recvShort(socket);
		if (size <= 0) {
			logger.warn("size:" + size);
			throw new RuntimeException("协议异常");
		}
		byte[] data = new byte[size];
		recvFully(socket, data, 0, size);

		String s = new String(data, 0, size - 1, "GBK");

		return s;
	}

	public static String recvStringX(Socket socket) throws Exception {
		String s = recvString(socket);
		byte[] data = new byte[1];
		socket.getOutputStream().write(data);
		return s;
	}

	public static int recvLength(Socket socket) throws Exception {
		byte[] buffer = new byte[2];
		recvFully(socket, buffer, 0, 2);
		int value = ((buffer[1] & 0xFF) << 8) + ((buffer[0] & 0xFF) << 0);
		return value;
	}

	public static String recvStringFromServer(Socket socket) throws Exception {
		int length = recvLength(socket);
		byte[] buffer = new byte[length];
		recvFully(socket, buffer, 0, length);
		String data = new String(buffer, 0, length - 1);
		return data;
	}

	public static void closeQuietly(Socket socket) {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
