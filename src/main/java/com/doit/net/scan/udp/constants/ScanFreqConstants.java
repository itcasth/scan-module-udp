package com.doit.net.scan.udp.constants;

/**
 * Created by wly on 2019/7/21.
 */
public class ScanFreqConstants {

	public static final String PRE_EEMGINFO = "AT+EEMGINFO?\\r\\n+EEMGINFO :";

	public static final String PRE_EEMLTEINTER = "\\r\\n+EEMLTEINTER:";
	public static final String PRE_EEMLTEINTRA = "\\r\\n+EEMLTEINTRA:";
	public static final String PRE_EEMUMTSINTER = "\\r\\n+EEMUMTSINTER:";
	public static final String PRE_EEMUMTSINTRA = "\\r\\n+EEMUMTSINTRA:";

	public static final String PRE_EEMGINFO_ERROR = "AT+EEMGINFO?\\r\\n+CME ERROR:";


	public static final String POST_EEMLTESVC = "+EEMLTESVC:";
	public static final String POST_EEMUMTSSVC = "+EEMUMTSSVC:";
	public static final String POST_EEMGINFOSVC = "+EEMGINFOSVC:";
	public static final String POST_EEMGINBFTM = "+EEMGINBFTM:";
	public static final String POST_EEMGINFONC = "+EEMGINFONC:";


	public static final String POST_EEMLTEINTER = "+EEMLTEINTER:";
	public static final String POST_EEMLTEINTRA = "+EEMLTEINTRA:";
	public static final String POST_EEMUMTSINTER = "+EEMUMTSINTER:";
	public static final String POST_EEMUMTSINTRA = "+EEMUMTSINTRA:";

}
