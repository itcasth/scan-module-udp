package com.doit.net.scan.udp.service;

import com.doit.net.scan.udp.base.RemMacroItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wly on 2019/7/11.
 * 数据回调管理
 */
public class ScanServiceManager {
	private final static Logger log = LoggerFactory.getLogger( ScanServiceManager.class);

	private static Map<String,List<IHandlerFinish>> callList = new HashMap<String,List<IHandlerFinish>>();


	public static synchronized void addCallBack(String code,IHandlerFinish iHandlerFinish){
		log.info( "register listener :{},class:{}",code,iHandlerFinish.getClass().getName() );
		if(callList.containsKey( code )){
			callList.get( code ).add( iHandlerFinish );
		}else {
			ArrayList<IHandlerFinish> list = new ArrayList<IHandlerFinish>();
			list.add( iHandlerFinish );
			callList.put( code,list );
		}
	}



	public static synchronized  void removeCallBack(String header){
		log.info( "remove listener header:{}",header );
		callList.remove( header );
	}


	public static void handlerFinish(RemMacroItem item){
		log.info( "call handler finish code:{}",item.getHead() );
		if(callList.containsKey(item.getHead() )){
			for (IHandlerFinish i: callList.get( item.getHead() )){
				if(i==null){
					continue;
				}
				i.workFinish( item );
			}
		}
	}

}
