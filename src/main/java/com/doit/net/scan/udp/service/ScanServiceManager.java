package com.doit.net.scan.udp.service;

import com.doit.net.scan.udp.base.RemMacroItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wly on 2019/7/11.
 * 数据回调管理
 */
public class ScanServiceManager {

	private static Map<String,List<IHandlerFinish>> callList = new HashMap<String,List<IHandlerFinish>>();


	public static synchronized void addCallBack(String code,IHandlerFinish iHandlerFinish){
		if(callList.containsKey( code )){
			callList.get( code ).add( iHandlerFinish );
		}else {
			ArrayList<IHandlerFinish> list = new ArrayList<IHandlerFinish>();
			list.add( iHandlerFinish );
			callList.put( code,list );
		}
	}



	public static synchronized  void removeCallBack(String header){
		callList.remove( header );
	}


	public static void handlerFinish(RemMacroItem item){
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
