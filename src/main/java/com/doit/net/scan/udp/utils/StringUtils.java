package com.doit.net.scan.udp.utils;

/**
 * Created by wly on 2019/7/11.
 */
public class StringUtils {

	public static boolean isNotBlank(String str){
		if(str==null || "".equals( str )){
			return false;
		}
		return true;
	}
	public static boolean isBlank(String str){
		return !isNotBlank( str );
	}

}
