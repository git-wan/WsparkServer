package cn.com.wushang.wspark.pos.eai.ccb;

public class WGQuery {
	private String AUTHNO;
	private String AUTHSTATUS;
	private String TRACEID;
	private String ERRORCODE;
	private String ERRORMSG;
	private String SIGN;

	public String getAUTHNO() {
		return AUTHNO;
	}

	public void setAUTHNO(String aUTHNO) {
		AUTHNO = aUTHNO;
	}

	public String getAUTHSTATUS() {
		return AUTHSTATUS;
	}

	public void setAUTHSTATUS(String aUTHSTATUS) {
		AUTHSTATUS = aUTHSTATUS;
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
