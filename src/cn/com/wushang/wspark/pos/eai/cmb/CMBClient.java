package cn.com.wushang.wspark.pos.eai.cmb;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class CMBClient {
    static final Logger logger = Logger.getLogger(CMBClient.class);
    private String charset;
    private String secret_key;
    private String url;
    private String pay_status_txCode;
    private String version;
    private String signType;
    private String branchNo;
    private String merchantNo;
    private String trnAbs;
    private String rspCode_success;
    private String pay_txCode;
    private String noticeUrl;
    private String currency;
    private String rspCode_status;
    private String rspCode_cancel;
    private long sleepTime;

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }
    public void setRspCode_cancel(String rspCode_cancel) {
        this.rspCode_cancel = rspCode_cancel;
    }

    public void setRspCode_status(String rspCode_status) {
        this.rspCode_status = rspCode_status;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }

    public void setPay_status_txCode(String pay_status_txCode) {
        this.pay_status_txCode = pay_status_txCode;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public void setBranchNo(String branchNo) {
        this.branchNo = branchNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public void setTrnAbs(String trnAbs) {
        this.trnAbs = trnAbs;
    }

    public void setRspCode_success(String rspCode_success) {
        this.rspCode_success = rspCode_success;
    }

    public void setPay_txCode(String pay_txCode) {
        this.pay_txCode = pay_txCode;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //访问收银接口
    //收银接口返回成功，调用交易支付接口
    //支付接口成功返回停车场
    public String invoke(String request) throws Exception {
        JSONObject jsonObject = null;
        String origRspCode = null;
        //生成收款参数
        Map<String, String> payMap = payParams(request);
        String payParams = buildParam(payMap);
        //生成交易状态查询参数
        String payStatusParams = buildParam(payStatusParams(payMap));
        //建立http连接
            String result = Util.uploadParam(payParams, url, charset);
            logger.debug("收款返回参数:" + result);
            jsonObject = new JSONObject(result);
            JSONObject rspData = (JSONObject) jsonObject.get("rspData");
            String rspCode = rspData.get("rspCode") + "";
            String bankSerialNo = rspData.get("bankSerialNo") + "";
            if (StringUtils.isEmpty(rspCode) || StringUtils.isEmpty(bankSerialNo)) {
                return payMap.get("agrNo") + ":0:0";
            }
            if (rspCode.equals(rspCode_cancel)) {
                return payMap.get("agrNo") + ":9:0";
            }

            if (rspCode.equals(rspCode_status)||rspCode.equals(rspCode_success)) {
                for (int j = 1; j <= 5; j++) {
                    result = Util.uploadParam(payStatusParams, url, charset);
                    logger.debug("查询订单返回参数:" + result);
                    jsonObject = new JSONObject(result);
                    rspData = (JSONObject) jsonObject.get("rspData");
                    rspCode = rspData.get("rspCode") + "";
                    origRspCode = rspData.get("origRspCode") + "";
                    if (rspCode.equals(rspCode_success) && origRspCode.equals(rspCode_success)) {
                        return payMap.get("agrNo") + ":1:" + bankSerialNo;
                    }
                    Thread.sleep(sleepTime);
                }
            }

        return payMap.get("agrNo") + ":0:0";
    }

    /*
     * 收款参数
     * */
    public Map<String, String> payParams(String request) {
        Map<String, String> reqData = new HashMap<>();
        //从request获取，(签约号:流水号:金额)字符串
        //得到签约号
        String agrNo = request.split(":")[0];
        //得到流水号
        String merchantSerialNo = request.split(":")[1];
        //得到金额
        String amount = request.split(":")[2];
        amount = Integer.valueOf(amount)*100+"";
        //请求时间
        reqData.put("dateTime", Util.getNowTime());
        //商户分行号
        reqData.put("branchNo", branchNo);
        //商户号
        reqData.put("merchantNo", merchantNo);
        //交易码
        reqData.put("txCode", pay_txCode);
        //签约号
        reqData.put("agrNo", agrNo);
        //交易金额
        reqData.put("amount", amount);
        //币种
        reqData.put("currency", currency);
        //结果异步通知URL，用于银行异步发送交易结果
        reqData.put("noticeUrl", noticeUrl);
        //交易摘要，简要描述交易的关键信息
        reqData.put("trnAbs", trnAbs);
        //商户交易请求流水号
        reqData.put("merchantSerialNo", merchantSerialNo);
        return reqData;
    }

    /*
     * 交易状态参数
     * */
    public Map<String, String> payStatusParams(Map<String, String> reqData) {
        String origDate = reqData.get("dateTime").substring(0, 8);
        reqData.put("txCode", pay_status_txCode);
        reqData.put("origDate", origDate);
        reqData.put("origMerchantSerialNo", reqData.get("merchantSerialNo") + "");
        reqData.put("dateTime", Util.getNowTime());
        reqData.remove("merchantSerialNo");
        reqData.remove("currency");
        reqData.remove("amount");
        reqData.remove("trnAbs");
        reqData.remove("noticeUrl");
        return reqData;
    }

    /*
     * 将所有的参数整合成json字符串
     * */
    public String buildParam(Map<String, String> reqDataMap) {
        JSONObject jsonRequestData = new JSONObject();
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("version", version);
            jsonParam.put("sign", Util.sign(reqDataMap, secret_key));
            jsonParam.put("signType", signType);
            jsonParam.put("reqData", reqDataMap);
            jsonRequestData.put("charset", charset);// 支持GBK和UTF-8两种编码
            jsonRequestData.put("jsonRequestData", jsonParam);
        } catch (JSONException e) {
            logger.debug("参数转json字符串错误");
            e.printStackTrace();
        }
        return jsonParam.toString();
    }
}
