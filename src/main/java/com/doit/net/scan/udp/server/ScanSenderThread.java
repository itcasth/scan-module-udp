package com.doit.net.scan.udp.server;

import com.doit.net.scan.udp.base.SerialMessage;
import com.doit.net.scan.udp.server.ScanServerManager;
import com.doit.net.scan.udp.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private final static Logger log = LoggerFactory.getLogger( ScanSenderThread.class);

	private static BlockingQueue<SerialMessage> senderQueue = new LinkedBlockingQueue<SerialMessage>();
	public ScanSenderThread(){
		setName( "Scan-Sender" );
	}

	@Override
	public void run(){
		init();
	}

	private void init() {
		log.info( "Scan udp sender thread started " );
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
					if (StringUtils.isNotBlank( msg.getReg() )) {
						ScanServerManager.SendMessageList.remove(0);
						continue;
					}
					if (msg.getWaitTimes() > 10)
					{
						msg.setSend( false );
						continue;
					}
					log.info("Serial wait msg:{0}", msg.getMsg());
					log.info("SendMessageList 长度:{0}", ScanServerManager.SendMessageList.size());
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

		log.info("Send {0} to {1}", msg.getMsg(), msg.getInetSocketAddress());
		send(msg);
	}


	/**
	 * 发送
	 * @param msg
	 */
	private void send(SerialMessage msg) {
		log.info( "scan udp sender thread started " );
		while (true){
			try {
				SocketAddress socketAddress = msg.getSocketAddress();
				byte[] bytes =msg.getMsg().getBytes();
				log.info( "send code:{}",msg.getHead() );
				DatagramPacket packet = new DatagramPacket( bytes, bytes.length, socketAddress );
				DatagramSocket socket = getSocket();
				if(socket==null){
					log.warn( "Not found scan socket:{}",msg.getInetSocketAddress().getPort() );
				}
				socket.send( packet );
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	public static DatagramSocket getSocket(){
		return getDatagramSocket();
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
			log.info( "添加消息 code:{}到发送队列",message.getHead() );
			senderQueue.put( message );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ScanServerManager.startListener();

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
