package cn.com.wushang.wspark.pos.eai.ccb;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import CCBSign.RSASig;
import COM.CCB.EnDecryptAlgorithm.MCipherEncryptor;
import cn.com.wushang.wspark.pos.eai.ccb.WGQuery;
import cn.com.wushang.wspark.pos.server.handler.ccb.CCBHandler;
import cn.com.wushang.wspark.util.Tls;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class CCBClient {
	static final Logger logger = Logger.getLogger(CCBClient.class);
	private String charset = "utf-8";
	private String key;
	private String mchid;
	private String branchid;
	private String posid;
	private String url;

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMchid() {
		return this.mchid;
	}

	public void setMchid(String mchid) {
		this.mchid = mchid;
	}

	public void setBranchid(String branchid) {
		this.branchid = branchid;
	}

	public String getBranchid() {
		return this.branchid;
	}

	public String getRrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPosid() {
		return this.posid;
	}

	public void setPosid(String posid) {
		this.posid = posid;
	}

	public String invoke(String request) throws Exception {
		CloseableHttpResponse response = null;
		CloseableHttpClient client = null;

		String[] ccbRequest = request.split(":");
		String ccbResponse = ccbRequest[0] + ":0:0";

		String strKey = key.substring(key.length() - 31, key.length() - 1);
		Gson gson = new Gson();
		// String strSrcParas =
		// "MERFLAG=1&MERCHANTID=105000000000000&POSID=000000000&TERMNO1=&TERMNO2=&BRANCHID=110000000&ORDERID=105000000000000123456&AUTHNO=CCB9991234567&AMOUNT=0.01&ORDERID=201802281539001&TXCODE=WGZF00";
		String strSrcParas = "MERFLAG=1&MERCHANTID=" + mchid;
		strSrcParas = strSrcParas + "&POSID=" + posid;
		strSrcParas = strSrcParas + "&BRANCHID=" + branchid;
		strSrcParas = strSrcParas + "&AUTHNO=" + ccbRequest[0];
		strSrcParas = strSrcParas + "&TXCODE=WGZF01";
		logger.debug("车牌查询参数：" + strSrcParas);
		COM.CCB.EnDecryptAlgorithm.MCipherEncryptor ccbEncryptor = new MCipherEncryptor(strKey);
		try {
			String ccbParam = ccbEncryptor.doEncrypt(strSrcParas);
			ccbParam = strSrcParas + "&ccbParam=" + ccbParam;
			logger.debug("车牌查询URL：" + url + ccbParam);

			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpUriRequest httppost = new HttpPost(url + ccbParam);
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			String body = "";
			if (entity != null) {
				// 按指定编码转换结果实体为String类型
				body = EntityUtils.toString(entity, "UTF-8");
				logger.debug("车牌查询返回：" + body.trim());
				if (body.trim().startsWith("{\"")) {

					WGQuery wgQuery = gson.fromJson(body, WGQuery.class);
					logger.debug("无感支付查询结果: " + wgQuery.getAUTHSTATUS());

					// 已绑定车牌
					if (wgQuery.getAUTHSTATUS().equals("1")) {
						logger.debug("无感支付开始");
						strSrcParas = null;
						httppost = null;
						body = null;
						ccbParam = null;
						strSrcParas = "MERFLAG=1&MERCHANTID=" + mchid;
						strSrcParas = strSrcParas + "&POSID=" + posid;
						strSrcParas = strSrcParas + "&BRANCHID=" + branchid;
						strSrcParas = strSrcParas + "&ORDERID=" + ccbRequest[1];
						strSrcParas = strSrcParas + "&AUTHNO=" + ccbRequest[0];
						strSrcParas = strSrcParas + "&AMOUNT=" + ccbRequest[2];
						strSrcParas = strSrcParas + "&TXCODE=WGZF00";

						logger.debug("无感支付参数：" + strSrcParas);
						ccbParam = ccbEncryptor.doEncrypt(strSrcParas);
						ccbParam = strSrcParas + "&ccbParam=" + ccbParam;
						logger.debug("无感支付URL：" + url + ccbParam);
						httppost = new HttpPost(url + ccbParam);
						response = httpclient.execute(httppost);
						entity = response.getEntity();
						if (entity != null) {
							// 按指定编码转换结果实体为String类型
							body = EntityUtils.toString(entity, "UTF-8");
							logger.debug("无感支付返回：" + body.trim());

							if (body.trim().startsWith("{\"")) {
								WGPayment wgPayment = gson.fromJson(body, WGPayment.class);

								if (wgPayment.getRESULT().equals("Y")) {
									logger.debug("无感支付成功：" + body);
									ccbResponse = ccbRequest[0] + ":1:" + wgPayment.getTRACEID();
								} else {
									logger.debug("无感支付失败：" + body);
									ccbResponse = ccbRequest[0] + ":0:0";
								}
							}
						}

						EntityUtils.consume(entity);
						response.close();
						logger.debug("无感支付结束");
					} else {
						logger.debug("未绑定车牌：" + wgQuery.getAUTHNO() + ":" + wgQuery.getAUTHSTATUS() + ":" + wgQuery.getTRACEID());
						ccbResponse = wgQuery.getAUTHNO() + ":0:0";
					}
					// 返回签名验证
					// RSASig rsasig = new RSASig();
					// rsasig.setPublicKey("69d6bda43aba033654b69cb1020113");
					// String strSrc = "AUTHNO=" + wgQuery.getAUTHNO() +
					// "&AUTHSTATUS="
					// + wgQuery.getAUTHSTATUS() + "&TRACEID=" +
					// wgQuery.getTRACEID();
					// System.out.println(wgQuery.getSIGN());
					// System.out.println(strSrc);
					// System.out.println(rsasig.verifySigature(wgQuery.getSIGN(),
					// strSrcParas));
				}
			}
			EntityUtils.consume(entity);
			response.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ccbResponse;
	}
}
