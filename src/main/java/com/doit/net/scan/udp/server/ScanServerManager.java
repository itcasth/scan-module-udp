package com.doit.net.scan.udp.server;

import com.doit.net.scan.udp.base.SerialMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wly on 2019/7/11.
 * 服务管理器
 */
public class ScanServerManager {
	private final static Logger log = LoggerFactory.getLogger( ScanServerManager.class);

	private static DatagramSocket datagramSocket;
	public static List<SerialMessage> SendMessageList = new ArrayList<SerialMessage>();

	private static int PORT = 6096;//控制台监听端口
	private static final int TIME_OUT = 3*60*1000;

	public static boolean isStarted = false;

	public static void startListener(int port){
		try {
			PORT = port;
			datagramSocket = new DatagramSocket( PORT );
			datagramSocket.setSoTimeout( TIME_OUT );
			datagramSocket.setReceiveBufferSize( 1024*20000 );
			isStarted = true;
			new ScanReceiverThread().start();
			new ScanWorkThread().start();
			new ScanSenderThread().start();
			log.info( "【IPCELL-Server】 started on port:{}",PORT );
		}catch (Exception e){
			log.error( "【IPCELL-Server】 启动异常，端口:{},异常原因：",port,e.getMessage() );
		}
	}

	public static void startListener(){
		System.out.println("开始监听");
		startListener( PORT );
	}

	private static DatagramSocket clientSocket = null;

	public static DatagramSocket getDatagramSocket() {
		return datagramSocket;
	}


	/**
	 * 接收数据包 该方法会造成线程阻塞
	 * @param packet
	 * @return
	 */
	public static DatagramPacket receive(DatagramPacket packet){
		try {
			datagramSocket.receive( packet );
			return packet;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void stop(){
		try {
			isStarted = false;
			if(datagramSocket.isClosed()==false){
				datagramSocket.close();
			}

		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 将响应数据发送给请求端
	 * @param packet
	 */
	public static void send(DatagramPacket packet){
		try {
			datagramSocket.send( packet );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检查消息是否发送成功
	 * @param data
	 */
	public static void CheckSendFinish(byte[] data)
	{
		if (SendMessageList.size() <= 0)
		{
			return;
		}

		String reg = new String( data );
		SerialMessage msg = SendMessageList.get( 0 );
		if (reg.contains(msg.getReg()))
		{
			log.info( "检查消息 包含："+msg.getReg() );
			SendMessageList.remove(0);
		}
	}
}
