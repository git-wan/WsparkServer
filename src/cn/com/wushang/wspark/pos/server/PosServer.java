package cn.com.wushang.wspark.pos.server;

import org.apache.log4j.Logger;

import cn.com.wushang.wspark.Context;
import cn.com.wushang.wspark.pos.server.implementor.Listener;
import cn.com.wushang.wspark.util.RtUtils;

/*
 * 2019-01-23 加入无感支付查询结果
 */
public class PosServer {
	static Logger logger = Logger.getLogger(PosServer.class);

	public static void main(String[] args) {
		try {
			logger.info("武商停车场服务(19.01.23.1)");
			RtUtils.dump();

			String springContextLocation = "/context.xml";
			Context.initContext(springContextLocation);

			Listener service = (Listener) Context.getBean(Listener.class);
			new Thread(service).start();
		} catch (Exception e) {
			logger.warn("", e);
		}
	}
}
