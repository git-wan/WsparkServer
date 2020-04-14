package cn.com.wushang.wspark.pos.server.handler.cmb;

import cn.com.wushang.wspark.Context;
import cn.com.wushang.wspark.pos.eai.cmb.CMBClient;
import cn.com.wushang.wspark.pos.server.handler.DefaultHandler;
import jxl.common.Logger;

public class CMBHandler extends DefaultHandler {

	static final Logger logger = Logger.getLogger(CMBHandler.class);

	public String handle(String request) {
		String response = "XXXXX:0:0";
		try {
			CMBClient client = (CMBClient) Context.getBean(CMBClient.class);
			response = client.invoke(request);
			return response;
		} catch (Exception ex) {
			logger.warn(ex);
		}
		return response;

	}
}
