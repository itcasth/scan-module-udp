package com.doit.net.scan.udp.base;

/**
 * Created by wly on 2019/7/21.
 * 串口消息
 */
public class SerialMessage extends BaseHeader{
	private String msg;
	private String reg;
	public 	int waitTimes;
	public boolean isSend;
	private String head;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getReg() {
		return reg;
	}

	public void setReg(String reg) {
		this.reg = reg;
	}

	public int getWaitTimes() {
		return waitTimes;
	}

	public void setWaitTimes(int waitTimes) {
		this.waitTimes = waitTimes;
	}

	public boolean isSend() {
		return isSend;
	}

	public void setSend(boolean send) {
		isSend = send;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	/**
	 * 解码
	 * @param data
	 * @param message
	 * @return
	 */
	public SerialMessage decode(byte[] data, SerialMessage message) {

		return null;
	}

	/**
	 * 解码
	 * @param data
	 * @return
	 */
	public RemMacroItem decodeRemMacroItem(byte[] data) {

		return null;
	}
}
