package com.doit.net.scan.udp.server;

import com.doit.net.scan.udp.base.RemMacroItem;
import com.doit.net.scan.udp.service.ScanServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by wly on 2019/7/11.
 * 工作线程
 */
public class ScanWorkThread extends Thread{

	private static final Logger log = LoggerFactory.getLogger(ScanWorkThread.class);

	private static BlockingQueue<RemMacroItem> workQueue = new LinkedBlockingQueue<RemMacroItem>();


	public ScanWorkThread(){
		setName( "Scan-Work" );
	}

	@Override
	public void run(){
		init();
	}

	private void init() {
		log.info( "Scan udp server work thread started" );
		while (ScanServerManager.isStarted){
			try {
				RemMacroItem message = workQueue.take();
				ScanServiceManager.handlerFinish( message );
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	public static void push(RemMacroItem item){
		try {
			log.info( "add scan work queue header:{}",item.getHead() );
			workQueue.put( item );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
