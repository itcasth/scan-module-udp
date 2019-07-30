package com.doit.net.scan.udp.server;

import com.doit.net.scan.udp.base.SerialMessage;
import com.doit.net.scan.udp.constants.ScanFreqConstants;
import com.doit.net.scan.udp.message.ScanMessageCreator;
import com.doit.net.scan.udp.utils.StringUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by wly on 2019/7/11.
 * 发送线程
 */
public class ScanSenderThread extends Thread {

	private static BlockingQueue<SerialMessage> senderQueue = new LinkedBlockingQueue<SerialMessage>();

	public ScanSenderThread(){
		setName( "Scan-Sender" );
	}

	@Override
	public void run(){
		init();
	}

	private void init() {
		while (true)
		{
			try
			{
				if (ScanServerManager.SendMessageList.size() <= 0)
				{
					continue;
				}
				SerialMessage msg = ScanServerManager.SendMessageList.get( 0 );
				if (msg.isSend())
				{
					if (StringUtils.isBlank( msg.getReg() )) {
						ScanServerManager.SendMessageList.remove(0);
						continue;
					}
					if (msg.getWaitTimes() > 10)
					{
						msg.setSend( false );
						continue;
					}
					msg.waitTimes++;
					continue;
				}
				sendFirst();
			}
			finally
			{
				try {
					Thread.sleep(1*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 发送第一个消息
	 */
	private void sendFirst() {
		if (ScanServerManager.SendMessageList.size() <= 0)
		{
			return;
		}
		SerialMessage msg = ScanServerManager.SendMessageList.get( 0 );
		msg.setSend( true );
		msg.setWaitTimes( 0 );

		send(msg);
		try {
			Thread.sleep( 3000 );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 发送
	 * @param msg
	 */
	private void send(SerialMessage msg) {
		try {
			SocketAddress socketAddress = msg.getSocketAddress();
			byte[] bytes =msg.getMsg().getBytes();
			DatagramPacket packet = new DatagramPacket( bytes, bytes.length, socketAddress );
			DatagramSocket socket = getSocket();
			if(socket==null){
				System.out.println("not found socket");
			}
			socket.send( packet );
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static DatagramSocket getSocket(){
		return ScanServerManager.getDatagramSocket();
	}

	private static int PORT = 9201;
	private static DatagramSocket datagramSocket;
	private static final int TIME_OUT = 3*60*1000;

	private static DatagramSocket getDatagramSocket(){
		try{
			if(datagramSocket==null){
				datagramSocket = new DatagramSocket( PORT );
				datagramSocket.setSoTimeout( TIME_OUT );
				datagramSocket.setReceiveBufferSize( 1024*20000 );
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return datagramSocket;
	}


	public static void put(SerialMessage message){
		try {
			senderQueue.put( message );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		/*System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "trace");
		System.setProperty(SimpleLogger.SHOW_SHORT_LOG_NAME_KEY, "true");
		System.setProperty(SimpleLogger.LOG_FILE_KEY, "System.out");*/
		ScanServerManager.startListener();
		ScanMessageCreator.scanGsmFreq( ScanFreqConstants.IP,ScanFreqConstants.PORT,1 );
		Thread.sleep( 4000 );
		ScanMessageCreator.scanUmtsFreq( ScanFreqConstants.IP,ScanFreqConstants.PORT,0 );
		//ScanMessageCreator.scanGsmFreq( ScanFreqConstants.IP,ScanFreqConstants.PORT,0 );
		//IpcellServiceManager.addCallBack( String.valueOf( IpcellConstants.IPCELL_QUERY_ACK ),new BaseHandler() );

		//IpcellMessageCreator.setRedirect3G( IpcellConstants.IP,IpcellConstants.PORT,"10663");
		//IpcellMessageCreator.queryInitParam( IpcellConstants.IP,IpcellConstants.PORT );
		//IpcellMessageCreator.sendHeartBeat( IpcellConstants.IP,IpcellConstants.PORT );
		//IpcellMessageCreator.setParam( IpcellConstants.IP,IpcellConstants.PORT,"460","01","10688","158","0","321" );
		//IpcellMessageCreator.queryPsc( IpcellConstants.IP,IpcellConstants.PORT  );
		//运行状态 0x0084 0x0080 0x0081
		//IpcellMessageCreator.setParam( IpcellConstants.IP,IpcellConstants.PORT,"460","01","10688","158","0","321" );


	}
}
