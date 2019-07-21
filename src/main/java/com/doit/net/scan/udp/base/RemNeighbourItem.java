package com.doit.net.scan.udp.base;

/**
 * Created by wly on 2019/7/21.
 */
public class RemNeighbourItem {

	private String fcn;
	private String pci;
	private String ci;
	private String lac;
	private String rxLevel;
	private String plmn;

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

	public String getLac() {
		return lac;
	}

	public void setLac(String lac) {
		this.lac = lac;
	}

	public String getRxLevel() {
		return rxLevel;
	}

	public void setRxLevel(String rxLevel) {
		this.rxLevel = rxLevel;
	}

	public String getPlmn() {
		return plmn;
	}

	public void setPlmn(String plmn) {
		this.plmn = plmn;
	}
}
