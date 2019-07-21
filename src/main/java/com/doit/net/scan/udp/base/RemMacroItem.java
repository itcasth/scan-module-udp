package com.doit.net.scan.udp.base;

import java.util.List;

/**
 * Created by wly on 2019/7/21.
 */
public class RemMacroItem {
	private String head;
	private String plmn;
	private String fcn;
	private String pci;
	private String ci;
	private String rsrp;
	private String rac;
	private String lac;
	private List<RemNeighbourItem> list;

	public String getPlmn() {
		return plmn;
	}

	public void setPlmn(String plmn) {
		this.plmn = plmn;
	}

	public String getFcn() {
		return fcn;
	}

	public void setFcn(String fcn) {
		this.fcn = fcn;
	}

	public String getPci() {
		return pci;
	}

	public void setPci(String pci) {
		this.pci = pci;
	}

	public String getCi() {
		return ci;
	}

	public void setCi(String ci) {
		this.ci = ci;
	}

	public String getRsrp() {
		return rsrp;
	}

	public void setRsrp(String rsrp) {
		this.rsrp = rsrp;
	}

	public String getRac() {
		return rac;
	}

	public void setRac(String rac) {
		this.rac = rac;
	}

	public String getLac() {
		return lac;
	}

	public void setLac(String lac) {
		this.lac = lac;
	}

	public List<RemNeighbourItem> getList() {
		return list;
	}

	public void setList(List<RemNeighbourItem> list) {
		this.list = list;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}
}
