package cn.com.wushang.wspark.pos.eai.ccb;

public class WGPayment {
	private String RESULT;
	private String ORDERID;
	private String amount;
	private String TRACEID;
	private String ERRORCODE;
	private String ERRORMSG;
	private String SIGN;
	public String getRESULT() {
		return RESULT;
	}
	public void setRESULT(String rESULT) {
		RESULT = rESULT;
	}
	public String getORDERID() {
		return ORDERID;
	}
	public void setORDERID(String oRDERID) {
		ORDERID = oRDERID;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTRACEID() {
		return TRACEID;
	}
	public void setTRACEID(String tRACEID) {
		TRACEID = tRACEID;
	}
	public String getERRORCODE() {
		return ERRORCODE;
	}
	public void setERRORCODE(String eRRORCODE) {
		ERRORCODE = eRRORCODE;
	}
	public String getERRORMSG() {
		return ERRORMSG;
	}
	public void setERRORMSG(String eRRORMSG) {
		ERRORMSG = eRRORMSG;
	}
	public String getSIGN() {
		return SIGN;
	}
	public void setSIGN(String sIGN) {
		SIGN = sIGN;
	}
	
	
}
