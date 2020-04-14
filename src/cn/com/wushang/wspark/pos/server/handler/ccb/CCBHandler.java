package cn.com.wushang.wspark.pos.server.handler.ccb;

import jxl.common.Logger;
import cn.com.wushang.wspark.Context;
import cn.com.wushang.wspark.pos.eai.ccb.CCBClient;
import cn.com.wushang.wspark.pos.server.handler.DefaultHandler;

public class CCBHandler extends DefaultHandler {

	static final Logger logger = Logger.getLogger(CCBHandler.class);

	public String handle(String request) {
		String response = "XXXXX:0:0";
		try {
			CCBClient client = (CCBClient) Context.getBean(CCBClient.class);
			response = client.invoke(request);
			return response;
		} catch (Exception ex) {
			logger.warn(ex);
		}
		return response;

	}
}
