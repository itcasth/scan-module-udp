package com.doit.net.ipcell.udp.utils;


/**
 * Created by wiker on 2016/3/15.
 */
public class ByteUtils {


    /**
     * 转换short为byte
     *
     * @param b
     * @param s
     *            需要转换的short
     * @param index
     */
    public static void putShort(byte b[], short s, int index) {
        b[index + 1] = (byte) (s >> 8);
        b[index + 0] = (byte) (s >> 0);
    }

    /**
     * 通过byte数组取到short
     *
     * @param b
     * @param index
     *            第几位开始取
     * @return
     */
    public static short getShort(byte[] b, int index) {
        return (short) (((b[index + 1] << 8) | b[index + 0] & 0xff));
    }

    public static int byte2Int(byte b){
        return b & 0xFF;
    }

    public static byte intToByte(int x) {
        return (byte) x;
    }
    /**
     * 转换int为byte数组
     *
     * @param bb
     * @param x
     * @param index
     */
    public static void putInt(byte[] bb, int x, int index) {
        bb[index + 3] = (byte) (x >> 24);
        bb[index + 2] = (byte) (x >> 16);
        bb[index + 1] = (byte) (x >> 8);
        bb[index + 0] = (byte) (x >> 0);
    }

    /**
     * 通过byte数组取到int
     *
     * @param bb
     * @param index
     *            第几位开始
     * @return
     */
    public static int getInt(byte[] bb, int index) {
        return (int) ((((bb[index + 3] & 0xff) << 24)
                | ((bb[index + 2] & 0xff) << 16)
                | ((bb[index + 1] & 0xff) << 8) | ((bb[index + 0] & 0xff) << 0)));
    }

    /**
     * 转换long型为byte数组
     *
     * @param bb
     * @param x
     * @param index
     */
    public static void putLong(byte[] bb, long x, int index) {
        bb[index + 7] = (byte) (x >> 56);
        bb[index + 6] = (byte) (x >> 48);
        bb[index + 5] = (byte) (x >> 40);
        bb[index + 4] = (byte) (x >> 32);
        bb[index + 3] = (byte) (x >> 24);
        bb[index + 2] = (byte) (x >> 16);
        bb[index + 1] = (byte) (x >> 8);
        bb[index + 0] = (byte) (x >> 0);
    }
    /**
     * 将byte转换为一个长度为8的byte数组，数组每个值代表bit
     */
    public static byte[] getBinaryArray(byte b) {
        byte[] array = new byte[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte)(b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }

    /**
     * 通过byte数组取到long
     *
     * @param bb
     * @param index
     * @return
     */
    public static long getLong(byte[] bb, int index) {
        return ((((long) bb[index + 7] & 0xff) << 56)
                | (((long) bb[index + 6] & 0xff) << 48)
                | (((long) bb[index + 5] & 0xff) << 40)
                | (((long) bb[index + 4] & 0xff) << 32)
                | (((long) bb[index + 3] & 0xff) << 24)
                | (((long) bb[index + 2] & 0xff) << 16)
                | (((long) bb[index + 1] & 0xff) << 8) | (((long) bb[index + 0] & 0xff) << 0));
    }

    /**
     * 字符到字节转换
     *
     * @param ch
     * @return
     */
    public static void putChar(byte[] bb, char ch, int index) {
        int temp = (int) ch;
        // byte[] b = new byte[2];
        for (int i = 0; i < 2; i ++ ) {
            bb[index + i] = new Integer(temp & 0xff).byteValue(); // 将最高位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
    }

    /**
     * 字节到字符转换
     *
     * @param b
     * @return
     */
    public static char getChar(byte[] b, int index) {
        int s = 0;
        if (b[index + 1] > 0)
            s += b[index + 1];
        else
            s += 256 + b[index + 0];
        s *= 256;
        if (b[index + 0] > 0)
            s += b[index + 1];
        else
            s += 256 + b[index + 0];
        char ch = (char) s;
        return ch;
    }

    /**
     * float转换byte
     *
     * @param bb
     * @param x
     * @param index
     */
    public static void putFloat(byte[] bb, float x, int index) {
        // byte[] b = new byte[4];
        int l = Float.floatToIntBits(x);
        for (int i = 0; i < 4; i++) {
            bb[index + i] = new Integer(l).byteValue();
            l = l >> 8;
        }
    }

    /**
     * 通过byte数组取得float
     *
     * @param b
     * @param index
     * @return
     */
    public static float getFloat(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    /**
     * double转换byte
     *
     * @param bb
     * @param x
     * @param index
     */
    public static void putDouble(byte[] bb, double x, int index) {
        // byte[] b = new byte[8];
        long l = Double.doubleToLongBits(x);
        for (int i = 0; i < 8; i++) {
            bb[index + i] = new Long(l).byteValue();
            l = l >> 8;
        }
    }

    /**
     * 通过byte数组取得double
     *
     * @param b
     * @param index
     * @return
     */
    public static double getDouble(byte[] b, int index) {
        long l;
        l = b[0];
        l &= 0xff;
        l |= ((long) b[1] << 8);
        l &= 0xffff;
        l |= ((long) b[2] << 16);
        l &= 0xffffff;
        l |= ((long) b[3] << 24);
        l &= 0xffffffffl;
        l |= ((long) b[4] << 32);
        l &= 0xffffffffffl;
        l |= ((long) b[5] << 40);
        l &= 0xffffffffffffl;
        l |= ((long) b[6] << 48);
        l &= 0xffffffffffffffl;
        l |= ((long) b[7] << 56);
        return Double.longBitsToDouble(l);
    }

    public static void putHexString(byte[] bb, String s, int index){
        for (int i = 0; i < s.length(); i+=2) {
            byte c= (byte)( charToByte(s.charAt(i))<<4 | charToByte(s.charAt(i+1)));
            bb[index+(i>>1)] = c;
        }
    }
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String getHexString(byte[] b, int index, int count){
        StringBuilder stringBuilder = new StringBuilder("");
        if (b == null || index < 0 || b.length < index + count ) {
            return null;
        }
        for (int i = index; i < count + index; i++) {
            int v = b[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String getBinaryString(int d,int length){
        StringBuilder stringBuilder = new StringBuilder("");
        String hv = Integer.toBinaryString(d);
        for (int j = 0; j < length - hv.length(); j++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(hv);
        return stringBuilder.toString();
    }

    public static String getBinaryReverseString(int d,int length){
        StringBuilder stringBuilder = new StringBuilder("");
        String hv = Integer.toBinaryString(d);
        for (int j = 0; j < length - hv.length(); j++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(hv);
        stringBuilder.reverse();

        return stringBuilder.toString();
    }


    //网络字节逆序
    public static byte[] ReversEndian(byte b[],int count, boolean big)
    {
        byte by;
        byte data[] = new byte[count];
        for(int i=0;i<count;i++)
        {
            data[i] = b[i];
        }
        if(big==false)
        {
            for(int i=0;i<count;i++)
            {
                by = b[i];
                data[count-i-1] = by;
            }
        }
        return data;
    }
    public static short htons(short s){
        short rslt = 0;
        byte [] bs1 = new byte[2];
        putShort(bs1, s, 0);
        byte[] bs2 = ReversEndian(bs1, 2, false);
        rslt = getShort(bs2, 0);
        return rslt;
    }
    public static int htonl(int d){
        int rslt = 0;
        byte [] bs1 = new byte[4];
        putInt(bs1, d, 0);
        byte[] bs2 = ReversEndian(bs1, 4, false);
        rslt = getInt(bs2, 0);
        return rslt;
    }

    public static byte[] sub(byte[] arr,int start){
        return sub(arr,start,arr.length-start);
    }

    public static byte[] sub(byte[] arr,int start,int len){
        try {
            byte[] b = new byte[len];
            System.arraycopy(arr,start,b,0,b.length);
            return b;
        } catch (Exception e) {
        }
        return null;
    }

    public static byte[] int2byte(int res) {
        byte[] targets = new byte[4];

        targets[0] = (byte) (res & 0xff);// 最低位
        targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
        targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
        return targets;
    }
    public static int byte2int(byte[] res) {
// 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000

        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
                | ((res[2] << 24) >>> 8) | (res[3] << 24);
        return targets;
    }

    /**
     * Byte转Bit
     */
    public static String byteToBit(byte b) {
        return "" +(byte)((b >> 7) & 0x1) +
                (byte)((b >> 6) & 0x1) +
                (byte)((b >> 5) & 0x1) +
                (byte)((b >> 4) & 0x1) +
                (byte)((b >> 3) & 0x1) +
                (byte)((b >> 2) & 0x1) +
                (byte)((b >> 1) & 0x1) +
                (byte)((b >> 0) & 0x1);
    }
    public static String byteToBit(byte b,int len) {
        String s = "";
        for(int i=len;i>0;i--){
            s += (byte)((b >> i-1) & 0x1);
        }
        return s;
    }

    /**
     * Bit转Byte
     */
    public static byte bitToByte(String byteStr) {
        int re, len;
        if (null == byteStr) {
            return 0;
        }
        len = byteStr.length();
        if (len != 4 && len != 8) {
            return 0;
        }
        if (len == 8) {// 8 bit处理
            if (byteStr.charAt(0) == '0') {// 正数
                re = Integer.parseInt(byteStr, 2);
            } else {// 负数
                re = Integer.parseInt(byteStr, 2) - 256;
            }
        } else {//4 bit处理
            re = Integer.parseInt(byteStr, 2);
        }
        return (byte) re;
    }

    public static int bitToInt(String bit){
    	byte b = bitToByte(bit);
    	return byte2Int(b);
    }
    
    public static void main(String[] args) {
        byte[] b = new byte[]{0x01};
        //1d  == -85
//        System.out.println(Arrays.toString(getBooleanArray(b[0])));
        System.out.println(byteToBit(b[0],4));
        System.out.println(bitToByte("0001"));
//        PrintUtils.printHex(int2byte(109));
//        System.out.println(Integer.toBinaryString(-109));
    }

    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }

    public static int bytesToInt(byte b[], int offset) {
        return    b[offset+3] & 0xff
                | (b[offset+2] & 0xff) << 8
                | (b[offset+1] & 0xff) << 16
                | (b[offset] & 0xff) << 24;
    }

    /**
     * 拼接字节数组
     * @param data1
     * @param data2
     * @return
     */
    public static byte[] getContactByte(byte[] data1, byte[] data2)
    {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1,0,data3,0,data1.length);
        System.arraycopy(data2,0,data3,data1.length,data2.length);
        return data3;

    }

    /**
     * 字节数组转16进制
     * @param bytes 需要转换的byte数组
     * @return 转换后的Hex字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if(hex.length() < 2){
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 字节转十六进制
     * @param b 需要进行转换的byte字节
     * @return 转换后的Hex字符串
     */
    public static String byteToHex(byte b){
        String hex = Integer.toHexString(b & 0xFF);
        if(hex.length() < 2){
            hex = "0" + hex;
        }
        return hex;
    }

}
