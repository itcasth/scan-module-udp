package com.doit.net.scan.udp.base;


import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 * Created by wly on 2019/7/11.
 *
 */
public class BaseHeader{

	private SocketAddress socketAddress;

	public SocketAddress getSocketAddress() {
		return socketAddress;
	}

	public InetSocketAddress getInetSocketAddress(){
		return (InetSocketAddress)socketAddress;
	}

	public void setSocketAddress(SocketAddress socketAddress) {
		this.socketAddress = socketAddress;
	}

	public void setSocketAddress(String ip,int port){
		try {
			socketAddress = new InetSocketAddress( InetAddress.getByName(ip),port);
		} catch (UnknownHostException e) {
		}
	}

	public String getRemoteAddr(){
		return getInetSocketAddress().getAddress().getHostAddress();
	}

	public int getRemotePort(){
		return getInetSocketAddress().getPort();
	}

}
