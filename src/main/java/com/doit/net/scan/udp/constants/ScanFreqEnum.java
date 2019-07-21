package com.doit.net.scan.udp.constants;

/**
 * Created by Administrator on 2019/7/21.
 */
public enum ScanFreqEnum {
	SCAN_CFUN0("AT+CFUN=0\\r\\n", "OK"),
	SCAN_CFUN1("AT+CFUN=1\\r\\n", "READY"),
	SCAN_CLEAR_OPERATOR( "AT*MRD_YYSLOCK=d\\r\\n", "OK"),//清除运营商
	SCAN_SET_OPERATOR( "AT*MRD_YYSLOCK=w,", "OK"),//指定运营商
	SCAN_EEMOPT("AT+EEMOPT=1\\r\\n","OK"),
	SCAN_GETCELL("AT+EEMOPT=1\\r\\n", "+EEMUMTSINTRA"),
	SCAN_BAND("AT*BAND=","OK"),
	SCAN_SET_BAND_0("AT*band=0\\r\\n","OK"),
	SCAN_SET_BAND_1("AT*band=1\\r\\n","OK"),
	SCAN_GET_BAND("AT*BAND?\\r\\n","BAND:"),
	SCAN_GET_GSM_CELL("AT+EEMGINFO?\\r\\n","+EEMGINFOBASIC"),
	SCAN_GET_UMTS_CELL("AT+EEMGINFO?\\r\\n","+EEMUMTSSVC"),

	;
	private String message;
	private String back;

	ScanFreqEnum(String message, String back) {
		this.message = message;
		this.back = back;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}
}
