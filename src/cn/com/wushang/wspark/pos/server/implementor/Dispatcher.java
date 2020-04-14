package cn.com.wushang.wspark.pos.server.implementor;

import cn.com.wushang.wspark.pos.server.handler.DefaultHandler;
import cn.com.wushang.wspark.pos.server.handler.cmb.CMBHandler;
import cn.com.wushang.wspark.pos.server.handler.ccb.CCBHandler;
import cn.com.wushang.wspark.util.SocketUtils;

import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

public class Dispatcher implements Runnable {
	private static Logger logger = Logger.getLogger(Dispatcher.class);
	String charset = "gbk";
	Socket socket;

	public Dispatcher(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			initialize();

			String request = receive(this.socket);
			logger.debug("request: " + request);

			String command = getCommand(request);
			String parameter = request.substring(command.length() + 1);

			DefaultHandler handler = getCommandHandler(command);
			String response = handler.handle(parameter);
			logger.debug("response: " + response);
			this.sendInt(this.socket, response.getBytes(this.charset).length);
			this.socket.getOutputStream().write(response.getBytes(this.charset));
		} catch (Exception ex) {
			logger.warn("", ex);
		} finally {
			terminate();
		}
	}

	void initialize() {
		InetSocketAddress address = (InetSocketAddress) this.socket.getRemoteSocketAddress();
		String host = address.getHostString();
		MDC.put("pos", host);
	}

	void terminate() {
		SocketUtils.closeQuietly(this.socket);

		MDC.remove("pos");
	}

	String receive(Socket socket) throws Exception {
		int size = SocketUtils.recvInt(socket);
		byte[] buffer = new byte[size];
		//标记
		/*System.out.println("qqqqqqqqqqqq:"+size);*/
		logger.debug(size);
		SocketUtils.recvFully(socket, buffer, 0, size);
		return new String(buffer, this.charset);
	}

	public void sendInt(Socket socket, int n) throws Exception {
		byte[] data = new byte[4];
		data[0] = ((byte) (n & 0xFF));
		data[1] = ((byte) (n >>> 8));
		data[2] = ((byte) (n >>> 16));
		data[3] = ((byte) (n >>> 24));
		socket.getOutputStream().write(data);
	}

	String getCommand(String request) {
		int index = request.indexOf('$');
		if (index >= 0) {
			String command = request.substring(0, index);
			return command;
		}
		return "";
	}

	DefaultHandler getCommandHandler(String command) throws Exception {
		if (StringUtils.equalsIgnoreCase(command, "ccb")) {
			return new CCBHandler();
		}
		if (StringUtils.equalsIgnoreCase(command, "cmb")) {
			return new CMBHandler();
		}

		throw new Exception("不支持的POS命令:" + command);
	}
}
