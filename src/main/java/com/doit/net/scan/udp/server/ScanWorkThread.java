package com.doit.net.scan.udp.server;

import com.doit.net.scan.udp.base.RemMacroItem;
import com.doit.net.scan.udp.service.ScanServiceManager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by wly on 2019/7/11.
 * 工作线程
 */
public class ScanWorkThread extends Thread{


	private static BlockingQueue<RemMacroItem> workQueue = new LinkedBlockingQueue<RemMacroItem>();

	public ScanWorkThread(){
		setName( "Scan-Work" );
	}

	@Override
	public void run(){
		init();
	}

	private void init() {
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
			workQueue.put( item );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
