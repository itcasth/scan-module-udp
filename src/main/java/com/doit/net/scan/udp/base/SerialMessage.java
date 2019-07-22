package com.doit.net.scan.udp.base;

import com.doit.net.scan.udp.constants.ScanFreqConstants;
import com.doit.net.scan.udp.server.ScanServerManager;
import com.doit.net.scan.udp.server.ScanWorkThread;
import com.doit.net.scan.udp.utils.StringUtils;
import io.netty.handler.codec.json.JsonObjectDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wly on 2019/7/21.
 * 串口消息
 */
public class SerialMessage extends BaseHeader{
	private final static Logger log = LoggerFactory.getLogger( SerialMessage.class);
	private String msg;
	private String reg;
	public 	int waitTimes;
	public boolean isSend;
	private String head;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getReg() {
		return reg;
	}

	public void setReg(String reg) {
		this.reg = reg;
	}

	public int getWaitTimes() {
		return waitTimes;
	}

	public void setWaitTimes(int waitTimes) {
		this.waitTimes = waitTimes;
	}

	public boolean isSend() {
		return isSend;
	}

	public void setSend(boolean send) {
		isSend = send;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	/**
	 * 解码
	 * @param data
	 * @param message
	 * @return
	 */
	public SerialMessage decode(byte[] data, SerialMessage message) {

		return null;
	}


	private static String dataStr = "";
	private static RemMacroItem item = null;
	private static List<RemNeighbourItem> list = new ArrayList(  );
	/**
	 * 码流解码
	 * @param data
	 */
	public static void  decodeByte(byte[] data){
		String content = new String( data );
		log.info("==================接收的数据：{}",content);
		if(content.startsWith( ScanFreqConstants.PRE_EEMGINFO)){
			String[] arr = content.split( "\r\n" );
			for (String str : arr){
				if(str.startsWith( ScanFreqConstants.POST_EEMGINFOSVC )){
					//GSM当前小区解码
					pushInfoSvcToWorkThread(str,ScanFreqConstants.POST_EEMGINFOSVC );
				}else if(str.startsWith( ScanFreqConstants.POST_EEMUMTSSVC )){
					//UMTS本小区
					String[] val = content.replace("+EEMLTESVC:", "").split(",");
					pushUmtsToWorkThread(val,ScanFreqConstants.POST_EEMUMTSSVC);
				}
			}
		}else if(content.startsWith( ScanFreqConstants.POST_EEMGINFONC )){
			dataStr = content;

		}else if(content.contains( ScanFreqConstants.POST_EEMGINFONC )){
			dataStr = dataStr + content;
			System.out.println("合并后的数据："+dataStr);
			String[] arr = dataStr.split( "\r\n" );
			//GSM邻区信息解码
			for (String str : arr){
				if(str.startsWith( ScanFreqConstants.POST_EEMGINFONC )){
					pushInfocToWorkThread(str,ScanFreqConstants.POST_EEMGINFONC );
				}else if(str.startsWith( ScanFreqConstants.POST_EEMGINBFTM)){
					pushinBfTmToWorkThread(str,ScanFreqConstants.POST_EEMGINBFTM );
				}
			}

		}else if(content.contains( ScanFreqConstants.POST_EEMUMTSINTRA )){
			//UMTS邻区
			String[] arr = content.split( "\r\n" );
			for (String str : arr){
				if(str.startsWith( ScanFreqConstants.POST_EEMUMTSINTRA )){
					String[] val = str.replace("+EEMUMTSINTRA:", "").split(",");
					if (val.length < 10)
					{
						continue;
					}
					pushUmtsIntraToWorkThread(val,ScanFreqConstants.POST_EEMUMTSINTRA );
				}
			}

		}


		//将扫频结果添加到工作线程
		if(item!=null && StringUtils.isNotBlank( item.getHead() )){
			item.setList( list );
			ScanWorkThread.push( item );
			System.out.println(item.getList().size());
			dataStr = "";
			list.clear();
			item = null;
		}
	}


	/**
	 * 解码
	 * @param data
	 * @return
	 */
	public void decodeRemMacroItem(byte[] data) {
		String content = new String( data );
		log.info("==================接收的数据：{}",content);
		if(content.startsWith( ScanFreqConstants.PRE_EEMGINFO)){
			String[] arr = content.split( "\r\n" );
			for (String str : arr){
				if(str.startsWith( ScanFreqConstants.POST_EEMLTESVC )){
					//LTE
					pushLTEtoWorkThread(str,ScanFreqConstants.POST_EEMLTESVC );
				}else if(str.startsWith( ScanFreqConstants.POST_EEMUMTSSVC )){
					//UMTS
					String[] val = str.replace("+EEMLTESVC:", "").split(",");
					if (val.length < 10)
					{
						continue;
					}
					pushUmtsToWorkThread(val,ScanFreqConstants.POST_EEMUMTSSVC);
				}else if(str.startsWith( ScanFreqConstants.POST_EEMGINFOSVC )){
					//添加到工作线程
					pushInfoSvcToWorkThread(str,ScanFreqConstants.POST_EEMGINFOSVC );
				}else if(str.startsWith( ScanFreqConstants.POST_EEMGINBFTM )){
					//添加到工作线程
					pushinBfTmToWorkThread(str,ScanFreqConstants.POST_EEMGINBFTM );
				}else if(str.startsWith( ScanFreqConstants.POST_EEMGINFONC )){
					//添加到工作线程
					pushInfocToWorkThread(str,ScanFreqConstants.POST_EEMGINFONC );
				}
			}
		}else if(content.startsWith( ScanFreqConstants.PRE_EEMLTEINTER )
				|| content.startsWith( ScanFreqConstants.PRE_EEMLTEINTRA )
				|| content.startsWith( ScanFreqConstants.PRE_EEMUMTSINTER )
				|| content.startsWith( ScanFreqConstants.PRE_EEMUMTSINTRA )){
			String[] arr = content.split( "\r\n" );
			for (String str : arr){
				if(str.startsWith( ScanFreqConstants.POST_EEMLTEINTER )){
					//添加到工作线程
					pushLTEInterToWorkThread(str,ScanFreqConstants.POST_EEMLTEINTER );
				}else if(str.startsWith( ScanFreqConstants.POST_EEMLTEINTRA )){
					//添加到工作线程
					pushLTEIntraToWorkThread(str,ScanFreqConstants.POST_EEMLTEINTRA );
				}else if(str.startsWith( ScanFreqConstants.POST_EEMUMTSINTER )){
					//添加到工作线程
					pushUmtsInterToWorkThread(str,ScanFreqConstants.POST_EEMUMTSINTER );
				}else if(str.startsWith( ScanFreqConstants.POST_EEMUMTSINTRA )){
					//添加到工作线程
					String[] val = str.replace("+EEMUMTSINTRA:", "").split(",");
					if (val.length < 10)
					{
						continue;
					}
					pushUmtsIntraToWorkThread(val,ScanFreqConstants.POST_EEMUMTSINTRA );
				}
			}
		}

	}


	/**
	 * 添加到工作线程
	 * @param val
	 * @param head
	 */
	private static void pushUmtsIntraToWorkThread(String[] val, String head) {
		//+EEMUMTSINTER:\ 0,\ -32768,\ 0,\ -115,\ 0,\ 0,\ 65534,\ 0,\ 10713,\ 29\r\n\r\n\+EEMUMTSINTER:\ 1,\ -32768,\ 0,\ -115,\ 0,\ 0,\ 65534,\ 1,\ 10713,\ 327\r\n\r\n\+EEMUMTSINTER:\ 2,\ -32768,\ 0,\ -115,\ 0,\ 0,\ 65534,\ 2,\ 10713,\ 182\r\n\r\n\+EEMUMTSINTER:\ 3,\ -32768,\ 0,\ -115,\ 0,\ 0,\ 65534,\ 3,\ 10713,\ 336\r\n\r\n\+EEMUMTSINTER:\ 4,\ -32768,\ 0,\ -115,\ 0,\ 0,\ 65534,\ 4,\ 10713,\ 230\r\n\r\n\+EEMUMTSINTER:\ 5,\ -32768,\ 0,\ -115,\ 0,\ 0,\ 65534,\ 5,\ 10713,\ 72\r\n\r\n\+EEMUMTSINTER:\ 6,\ -32768,\ 0,\ -115,\ 0,\ 0,\ 65534,\ 6,\ 10713,\ 221\r\n\r\n\+EEMUMTSINTER:\ 7,\ -32768,\ 0,\ -115,\ 0,\ 0,\ 65534,\ 7,\ 10713,\ 378\r\n\r\n\+EEMUMTSINTRA:\ 0,\ -32768,\ -1,\ -115,\ 0,\ 0,\ 65534,\ 1,\ 10663,\ 478\r\n
		//+EEMUMTSINTER: 0, -32768, 0, -115, 0, 0, 65534, 0, 10713, 29
		//pccpchRSCP,utraRssi,sRxLev,mcc,mnc,lac,ci,arfcn,cellParameterId
		//+EEMUMTSINTRA:\ 0,\ -32768,\ -1,\ -115,\ 0,\ 0,\ 65534,\ 1,\ 10663,\ 478\r\n
		System.out.println("===pushUmtsIntraToWorkThread===");
		String rxLevel = val[3].trim();
		String lac = val[6].trim();
		String ci = val[7];
		String mcc = Integer.toHexString( Integer.parseInt( val[4].trim() ) );
		String _mnc = Integer.toHexString( Integer.parseInt( val[5].trim() ) );
		String mnc = _mnc.length() == 2 ? _mnc : "0" + _mnc;
		String plmn = mcc+mnc;
		if (StringUtils.isNotBlank( ci ) && !"0".equals( ci ))
		{
			ci = ci.trim();
		}
		String arfcn = val[8].trim();
		String psc = val[9].trim();
		log.info("rxLevel:{},lac:{},ci:{},arfcn:{},psc:{}", rxLevel, lac, ci, arfcn,psc);

		RemNeighbourItem ritem = new RemNeighbourItem();
		ritem.setCi( ci );
		ritem.setPci( psc );
		ritem.setFcn( arfcn );
		ritem.setLac( lac );
		ritem.setRxLevel( rxLevel );
		ritem.setPlmn( plmn );
		if(!ritem.getPlmn().startsWith( "0" )){
			list.add( ritem );
		}

	}


	/**
	 * 添加到工作线程
	 * @param l
	 * @param head
	 */
	private void pushUmtsInterToWorkThread(String l, String head) {
		//+EEMUMTSINTER:\ 0,\ -32768,\ 0,\ -115,\ 0,\ 0,\ 65534,\ 0,\ 10713,\ 29\r\n\r\n\+EEMUMTSINTER:\ 1,\ -32768,\ 0,\ -115,\ 0,\ 0,\ 65534,\ 1,\ 10713,\ 327\r\n\r\n\+EEMUMTSINTER:\ 2,\ -32768,\ 0,\ -115,\ 0,\ 0,\ 65534,\ 2,\ 10713,\ 182\r\n\r\n\+EEMUMTSINTER:\ 3,\ -32768,\ 0,\ -115,\ 0,\ 0,\ 65534,\ 3,\ 10713,\ 336\r\n\r\n\+EEMUMTSINTER:\ 4,\ -32768,\ 0,\ -115,\ 0,\ 0,\ 65534,\ 4,\ 10713,\ 230\r\n\r\n\+EEMUMTSINTER:\ 5,\ -32768,\ 0,\ -115,\ 0,\ 0,\ 65534,\ 5,\ 10713,\ 72\r\n\r\n\+EEMUMTSINTER:\ 6,\ -32768,\ 0,\ -115,\ 0,\ 0,\ 65534,\ 6,\ 10713,\ 221\r\n\r\n\+EEMUMTSINTER:\ 7,\ -32768,\ 0,\ -115,\ 0,\ 0,\ 65534,\ 7,\ 10713,\ 378\r\n\r\n\+EEMUMTSINTRA:\ 0,\ -32768,\ -1,\ -115,\ 0,\ 0,\ 65534,\ 1,\ 10663,\ 478\r\n
		//+EEMUMTSINTER: 0, -32768, 0, -115, 0, 0, 65534, 0, 10713, 29
		//pccpchRSCP,utraRssi,sRxLev,mcc,mnc,lac,ci,arfcn,cellParameterId
		//+EEMUMTSINTRA:\ 0,\ -32768,\ -1,\ -115,\ 0,\ 0,\ 65534,\ 1,\ 10663,\ 478\r\n
		String[] val = l.replace("+EEMUMTSINTER:", "").split(",");
		String rxLevel = val[3].trim();
		String lac = val[6].trim();
		String ci = val[7];
		if (StringUtils.isNotBlank( ci ) && !"0".equals( ci ) ) {
			ci = ci.trim();
		}
		String arfcn = val[8];
		if (StringUtils.isNotBlank( arfcn ) && !"0".equals( arfcn ) ) {
			arfcn = arfcn.trim();
		}
		String psc= val[9];
		if (StringUtils.isNotBlank( psc ) && !"0".equals( psc ))
		{
			psc = psc.trim();
		}
		log.info("rxLevel:{},lac:{},ci:{},arfcn:{},psc:{}", rxLevel,lac,ci,arfcn,psc);

		RemMacroItem item = new RemMacroItem();
		item.setHead( head );
		RemNeighbourItem ritem = new RemNeighbourItem();
		ritem.setCi( ci );
		ritem.setPci( psc );
		ritem.setFcn( arfcn );
		ritem.setLac( lac );
		ritem.setRxLevel( rxLevel );
		List<RemNeighbourItem> list = new ArrayList(  );
		list.add( ritem );
		item.setList( list );
		System.out.println("===pushUmtsInterToWorkThread===");
		ScanWorkThread.push( item );
	}

	/**
	 * 添加到工作线程
	 * @param l
	 * @param head
	 */
	private void pushLTEIntraToWorkThread(String l, String head) {
		String[] val = l.replace("+EEMLTEINTER:", "").split(",");
		String pci = val[1].trim();
		String fcn = val[2].trim();
		log.info("neighbour PCI:{},FCN:{}", pci, fcn);
		RemMacroItem item = new RemMacroItem();
		item.setHead( head );
		RemNeighbourItem ritem = new RemNeighbourItem();
		ritem.setPci( pci );
		ritem.setFcn( fcn );
		List<RemNeighbourItem> list = new ArrayList(  );
		list.add( ritem );
		item.setList( list );
		System.out.println("===pushLTEIntraToWorkThread===");
		ScanWorkThread.push( item );
	}

	/**
	 * 添加到工作线程
	 * @param l
	 * @param head
	 */
	private void pushLTEInterToWorkThread(String l, String head) {
		String[] val = l.replace("+EEMLTEINTER:", "").split(",");
		String pci = val[1].trim();
		String fcn = val[2].trim();
		log.info("neighbour PCI:{},FCN:{}",pci,fcn);
		RemMacroItem item = new RemMacroItem();
		item.setHead( head );
		RemNeighbourItem ritem = new RemNeighbourItem();
		ritem.setPci( pci );
		ritem.setFcn( fcn );
		List<RemNeighbourItem> list = new ArrayList(  );
		list.add( ritem );
		item.setList( list );
		ScanWorkThread.push( item );
		System.out.println("===pushLTEInterToWorkThread===");
	}

	/**
	 * 添加到工作线程
	 * @param l
	 * @param head
	 */
	private static void pushInfocToWorkThread(String l, String head) {
		System.out.println("===pushInfocToWorkThread===");
		//+EEMGINFONC:\ 1,\ 0,\ 0,\ 0,\ 0,\ 0,20,\ 255,\ 0,\ 0,\ 99,\ 0,\ 0\r\n\r\n\
		String[] val = l.replace("+EEMGINFONC:", "").split(",");
		RemNeighbourItem ritem = new RemNeighbourItem();
		String mcc = Integer.toHexString( Integer.parseInt( val[1].trim() ) );
		String ss = val[2];
		String _mnc = Integer.toHexString( Integer.parseInt( val[2].trim() ) );
		String mnc = _mnc.length() == 2 ? _mnc : "0" + _mnc;
		String tac = val[3].trim();
		String ci = val[5].trim();
		String rx_lev = Integer.toHexString( Integer.parseInt( val[6].trim() ) );
		String fcn = val[10].trim();
		String fcn2= Integer.toHexString( Integer.parseInt( val[10].trim() ) );
		ritem.setPlmn( mcc + mnc );
		ritem.setLac( tac );
		ritem.setCi( ci );
		ritem.setRxLevel( rx_lev );
		ritem.setFcn( fcn );
		log.info("EEMGINFONC - rxLevel:{},lac:{},ci:{},arfcn:{},plmn:{}", rx_lev,tac,ci,fcn, mcc + mnc );
		if(item==null){
			item =  new RemMacroItem();
		}
		if(!ritem.getPlmn().startsWith( "0" )){
			list.add( ritem );
		}



	}


	/**
	 * 添加到工作线程
	 * @param l
	 * @param head
	 */
	private static void pushinBfTmToWorkThread(String l, String head) {
		System.out.println("===pushinBfTmToWorkThread===");
		//+EEMGINBFTM:\ 1,\ 1120,\ 1,\ 4188,\ 52884,\ 0,\ 17,\ 7,\ 0,\ 0,\ 37,\ 0,\ 0,0,\ 0,\ 0,\ 0,\ 0,\ 0\r\n
		String[] val = l.replace("+EEMGINBFTM:", "").split(",");
		String mcc =  Integer.toHexString( Integer.parseInt( val[1].trim() ) );
		String _mnc =  Integer.toHexString( Integer.parseInt( val[2].trim() ) );
		String mnc = _mnc.length() == 2 ? _mnc : "0" + _mnc;
		String tac = Integer.toHexString( Integer.parseInt( val[3].trim() ) );
		String ci =  Integer.toHexString( Integer.parseInt( val[4].trim() ) );
		String rx_lev = Integer.toHexString( Integer.parseInt( val[5].trim() ) );
		String dlEuarfcn = val[15].trim();

		log.info("EEMGINBFTM - SVC MCC:{},MNC:{},TAC:{},dl fcn:{},ci:{}", mcc, mnc, tac, dlEuarfcn, ci);
		RemNeighbourItem ritem = new RemNeighbourItem();
		ritem.setFcn( dlEuarfcn );
		ritem.setCi( ci );
		ritem.setLac( tac );
		ritem.setRxLevel( rx_lev );
		ritem.setPlmn( mcc+mnc );
		if(item==null){
			item = new RemMacroItem();
		}
		if(!ritem.getPlmn().startsWith( "0" )){
			list.add( ritem );
		}

	}

	/**
	 * GSM当前小区工程信息
	 * @param l
	 * @param head
	 */
	private static void pushInfoSvcToWorkThread(String l, String head) {
		System.out.println("===pushInfoSvcToWorkThread===");
		//+EEMGINFOSVC:\ 1120,\ 1,\ 55064,\ 8,\ 0,\ 0,\ 25,\ 38,\ 58,\ 0,\ 0,\ \ 38,\ 0,\ 0,\ 0,\ 0,\ \ 0,\ 0,\ 0,\ 0,\ 0,\ 0,\ 100,\ \ 2,\ 100,\ 36,\ 6,\ 54,\ 0,\ 0,\ \ 0,\ 0,\ 0,\ 0,\ 0,\ 0\r\n\r\n\
		String[] val = l.replace("+EEMGINFOSVC:", "").split(",");
		String mcc =  Integer.toHexString( Integer.parseInt( val[0].trim() ) );
		String _mnc = Integer.toHexString( Integer.parseInt( val[1].trim() ) );
		String mnc = _mnc.length() == 2 ? _mnc : "0" + _mnc;
		String tac = Integer.toHexString( Integer.parseInt( val[2].trim() ) );
		String ci = Integer.toHexString( Integer.parseInt( val[3].trim() ) );
		String dlEuarfcn = val[4].trim();

		log.info("SVC MCC:{},MNC:{},TAC:{},dl fcn:{},ci:{}", mcc, mnc, tac, dlEuarfcn, ci);
		item = new RemMacroItem();
		item.setPlmn( mcc + mnc );
		item.setFcn( dlEuarfcn );
		item.setCi( val[3].trim() );
		item.setLac( val[2].trim() );
		item.setHead( ScanFreqConstants.GSM_SCAN_RESULT );
	}

	/**
	 * 把UMTS消息添加到工作线程
	 * @param val
	 */
	private static void pushUmtsToWorkThread(String[] val,String head) {
		//+EEMUMTSSVC:3, 1, 1, 1,-88, 16, -3, -19, -115, -32768, 43, 1, 1120, 1, 43051, 14005560, 43, 480, 10663, 60, 0, 0, 1, 128, 128, 65535, 0, 0, 2, 255, 65535, -1, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, -1, -1, 1, 1,28672, 255, 194, 24, 0, 65535, 0, 0, 0
		//+EEMUMTSSVC:3,\ 1,\ 1,\ 1,-78,\ 27,\ -3,\ -19,\ -115,\ -32768,\ 43,\ 1,\ 1120,\ 1,\ 43051,\ 14005560,\ 43,\ 480,\ 10663,\ 60,\ 0,\ 0,\ 1,\ 128,\ 128,\ 65535,\ 0,\ 0,\ 2,\ 255,\ 65535,\ -1,\ 0,\ 0,\ 0,\ 0,\ 0,\ 0,0,\ 0,\ 0,\ 0,\ -1,\ -1,\ 1,\ 1,28672,\ 255,\ 194,\ 24,\ 0,\ 65535,\ 0,\ 0,\ 0\r\n

		System.out.println("===pushUmtsToWorkThread===");
		String rxLevel = "";
		String ci = "";
		String arfcn = "";
		String rac = "";
		String mcc =  "";
		String _mnc = "";
		String mnc = "";
		String lac = "";
		String psc = "";

		mcc = Integer.toHexString( Integer.parseInt( val[13].trim() ) );
		_mnc = Integer.toHexString( Integer.parseInt( val[14].trim() ) );
		mnc = _mnc.length() == 2 ? _mnc : "0" + _mnc;
		lac = val[15].trim();
		psc = val[18].trim();
		arfcn = val[19].trim();
		ci = val[16].trim();
		rac = val[11].trim();
		rxLevel = val[9].trim();
		log.info("===========Engineering Mode=============");
		log.info("SVC plmn{},rxLevel{},ci:{},arfcn:{},rac:{},psc:{}", (mcc + mnc),  rxLevel,  ci,  arfcn, rac, psc);
		item = new RemMacroItem();
		item.setPlmn( mcc + mnc );
		item.setPci(psc);
		item.setFcn( arfcn );
		item.setCi( ci );
		item.setRac( rac );
		item.setLac( lac );
		item.setRsrp( rxLevel );
		item.setHead( ScanFreqConstants.UMTS_SCAN_RESULT );

	}

	/**l
	 * 把LTE消息添加到工作线程
	 * @param l
	 */
	private void pushLTEtoWorkThread(String l,String head) {

		//+EEMLTESVC:1120, 2, 17, 23079, 70, 100, 18100, 1, 5, 94497282, 0, 50, 14, 1, 51, 20, 20, 127, 68, 0, 0, 0, 1, 24, 2, 1, 0, 65535, 255, -1
		String[] val = l.replace("+EEMLTESVC:", "").split(",");
		String mcc = Integer.toHexString( Integer.parseInt( val[0].trim() ) );
		int lenMnc = Integer.parseInt( val[1].trim() );
		String _mnc = Integer.toHexString( Integer.parseInt( val[2].trim() ) );
		String mnc = _mnc.length() == 2 ? _mnc : "0" + _mnc;
		String tac =val[3].trim();
		String pci = val[4].trim();
		String dlEuarfcn = val[5].trim();
		String ulEuarfcn = val[6].trim();
		String band = Integer.toHexString( Integer.parseInt( val[7].trim() ) );
		String dlbandWidth = Integer.toHexString( Integer.parseInt( val[8].trim() ) );
		String ci = Integer.toHexString( Integer.parseInt( val[9].trim() ) );
		String rsrp = Integer.toHexString( Integer.parseInt( val[10].trim() ) );
		String rsrq = Integer.toHexString( Integer.parseInt( val[11].trim() ) );
		log.info("===========Debug Mode=============");
		log.info("SVC MCC:{},MNC:{},TAC:{},PCI:{},dl fcn:{},ul fcn:{},band:{},band width:{},ci:{},rsrp:{},rsrq:{}", mcc, mnc, tac, pci, dlEuarfcn, ulEuarfcn, band, dlbandWidth, ci, rsrp, rsrq);
		RemMacroItem item = new RemMacroItem();
		item.setPlmn(  mcc + mnc );
		item.setPci( pci );
		item.setFcn(dlEuarfcn);
		item.setCi( val[9].trim());
		item.setLac( tac );
		item.setRsrp( rsrp );
		item.setHead( head );
		ScanWorkThread.push( item );
		System.out.println("===pushLTEtoWorkThread===");
	}
}
