package com.doit.net.scan.udp.message;

import com.doit.net.scan.udp.base.SerialMessage;
import com.doit.net.scan.udp.constants.ScanFreqEnum;
import com.doit.net.scan.udp.server.ScanServerManager;


/**
 * Created by wly on 2019/7/21.
 * 扫频消息
 */
public class ScanMessageCreator {

	/**
	 * 读取物理层信息
	 * @param ip
	 * @param port
	 */
	public static void eemopt(String ip,int port)
	{
		send(ip,port, ScanFreqEnum.SCAN_EEMOPT.getMessage(), ScanFreqEnum.SCAN_EEMOPT.getBack());
	}

	/**
	 * CFUN0
	 * @param ip
	 * @param port
	 */
	public static void cfun0(String ip,int port)
	{
		send(ip,port, ScanFreqEnum.SCAN_CFUN0.getMessage(), ScanFreqEnum.SCAN_CFUN0.getBack());
	}

	/**
	 * CFUN1
	 * @param ip
	 * @param port
	 */
	public static void cfun1(String ip,int port)
	{
		send(ip,port, ScanFreqEnum.SCAN_CFUN1.getMessage(), ScanFreqEnum.SCAN_CFUN1.getBack());
	}


	/**
	 * 清除之前运营商
	 * @param ip
	 * @param port
	 */
	public static void clearPreOper(String ip,int port){
		send(ip,port, ScanFreqEnum.SCAN_CLEAR_OPERATOR.getMessage(), ScanFreqEnum.SCAN_CLEAR_OPERATOR.getBack());
	}


	/**
	 * 根据运营商扫频 0 不指定  1 移动  2联通  3电信
	 * @param s
	 */
	public static void scanByOper(String ip,int port,int s) {
		send(ip,port, ScanFreqEnum.SCAN_SET_OPERATOR.getMessage()+ s + "\\r\\n", ScanFreqEnum.SCAN_SET_OPERATOR.getBack());
	}

	/**
	 * 获取小区
	 * @param ip
	 * @param port
	 */
	public static void getCell(String ip,int port){
		send(ip,port, ScanFreqEnum.SCAN_GETCELL.getMessage(), ScanFreqEnum.SCAN_GETCELL.getBack());
	}

	/**
	 * 指定band扫频
	 * @param ip
	 * @param port
	 * @param band
	 */
	public static void setBand(String ip,int port,int band)
	{
		send(ip,port, ScanFreqEnum.SCAN_BAND.getMessage()+band+"\\r\\n", ScanFreqEnum.SCAN_BAND.getBack());
	}

	/**
	 * 指定band0扫频
	 * @param ip
	 * @param port
	 */
	public static void setBand0(String ip,int port)
	{
		send(ip,port, ScanFreqEnum.SCAN_SET_BAND_0.getMessage(), ScanFreqEnum.SCAN_SET_BAND_0.getBack());
	}

	/**
	 * 获取band
	 * @param ip
	 * @param port
	 */
	public static void getBand(String ip,int port)
	{
		send(ip,port, ScanFreqEnum.SCAN_GET_BAND.getMessage(), ScanFreqEnum.SCAN_GET_BAND.getBack());
	}

	/**
	 * 获取GSM小区
	 * @param ip
	 * @param port
	 */
	public static void getGSMCell(String ip,int port)
	{
		send(ip,port, ScanFreqEnum.SCAN_GET_GSM_CELL.getMessage(), ScanFreqEnum.SCAN_GET_GSM_CELL.getBack());
	}


	/**
	 * WCDMA band1 扫频
	 * @param ip
	 * @param port
	 */
	public static void setBand1Wcdma(String ip,int port)
	{
		send(ip,port, ScanFreqEnum.SCAN_SET_BAND_1.getMessage(), ScanFreqEnum.SCAN_SET_BAND_1.getBack());
	}

	/**
	 * 获取3G小区
	 * @param ip
	 * @param port
	 */
	public static void getUMTSCell(String ip,int port)
	{
		send(ip,port, ScanFreqEnum.SCAN_GET_UMTS_CELL.getMessage(), ScanFreqEnum.SCAN_GET_UMTS_CELL.getBack());
	}

	/**
	 * GSM扫频
	 * @param ip
	 * @param port
	 * @param operator  0 不指定  1 移动  2联通  3电信
	 */
	public static void scanUmtsFreq(String ip,int port,int operator){
		cfun0( ip,port );
		cfun1( ip,port );
		clearPreOper( ip,port );
		scanByOper( ip,port,operator );
		setBand1Wcdma( ip,port );
		getBand( ip,port );
		eemopt( ip,port );
		getUMTSCell( ip,port );
	}

	/**
	 * UMTS扫频
	 * @param ip
	 * @param port
	 * @param operator  0 不指定  1 移动  2联通  3电信
	 */
	public static void scanGsmFreq(String ip,int port,int operator){
		cfun0( ip,port );
		cfun1( ip,port );
		clearPreOper( ip,port );
		scanByOper( ip,port,operator );
		setBand0( ip,port );
		getBand( ip,port );
		eemopt( ip,port );
		getGSMCell( ip,port );
	}

	/**
	 * 通用send方法
	 * @param ip
	 * @param port
	 * @param msg
	 * @param reg
	 */
	public static void send(String ip,int port,String msg,String reg)
	{
		SerialMessage serialMsg = new SerialMessage();
		serialMsg.setSocketAddress( ip,port );
		serialMsg.setMsg( msg );
		serialMsg.setReg( reg );
		ScanServerManager.SendMessageList.add( serialMsg );
	}
}
