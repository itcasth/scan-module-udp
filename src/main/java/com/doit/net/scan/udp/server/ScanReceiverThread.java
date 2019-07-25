package com.doit.net.scan.udp.server;


import java.net.DatagramPacket;

import com.doit.net.scan.udp.base.SerialMessage;
/**
 * Created by wly on 2019/7/11.
 * 接收线程
 */
public class ScanReceiverThread extends Thread {

	private static final int BUFFER_SIZE = 1024;

	public ScanReceiverThread(){
		setName( "IPCELL-Receiver" );
	}

	@Override
	public void run(){
		init();
	}

	private void init() {
		while (ScanServerManager.isStarted){
			try {
				byte[] bytes = new byte[BUFFER_SIZE];
				DatagramPacket datagramPacket = new DatagramPacket( bytes,bytes.length );
				if(datagramPacket==null){
					continue;
				}
				ScanServerManager.receive( datagramPacket );
				if(datagramPacket==null){
					continue;
				}
				//添加到工作线程队列
				SerialMessage message = new SerialMessage(  );
				byte[] data = datagramPacket.getData();
			//	message = message.decode( data,message );
				ScanServerManager.CheckSendFinish( data );
				message.setSocketAddress( datagramPacket.getSocketAddress() );
				message.decodeByte( data );
			//	ScanWorkThread.push( item );
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
