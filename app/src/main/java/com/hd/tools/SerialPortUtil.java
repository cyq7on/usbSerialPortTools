package com.hd.tools;

public class SerialPortUtil {
    /**
     * 获取校验码（计算方式如下：cs= 256-（data1+data2+data3+data4+datan））
     */
    public static String getCheckSum(String data) {
        Integer in = Integer.valueOf(makeChecksum(data), 16);
        String st = Integer.toHexString(256 - in).toUpperCase();
        st = String.format("%2s", st);
        return st.replaceAll(" ", "0");
    }

    /**
     * 生成校验码，十六进制相加
     *
     * @param data
     * @return
     */
    public static String makeChecksum(String data) {
        if (data == null || data.equals("")) {
            return "00";
        }
        int iTotal = 0;
        int iLen = data.length();
        int iNum = 0;

        while (iNum < iLen) {
            String s = data.substring(iNum, iNum + 2);
            System.out.println(s);
            iTotal += Integer.parseInt(s, 16);
            iNum = iNum + 2;
        }

        /**
         * 用256求余最大是255，即16进制的FF
         */
        int iMod = iTotal % 256;
        String sHex = Integer.toHexString(iMod);
        iLen = sHex.length();
        //如果不够校验位的长度，补0,这里用的是两位校验
        if (iLen < 2) {
            sHex = "0" + sHex;
        }
        return sHex;
    }

    /**
     * 获取CRC检验
     *
     * @param command 命令集
     * @param len     命令长度
     * @return
     */
    public static int CalCrc(byte[] command, int len) {
        long MSBInfo;
        int i, j;
        int nCRCData;
        nCRCData = 0xffff;
        for (i = 0; i < len; i++) {
            int temp = (int) (command[i] & 0xff);
            nCRCData = nCRCData ^ temp;
            for (j = 0; j < 8; j++) {
                MSBInfo = nCRCData & 0x0001;
                nCRCData = nCRCData >> 1;
                if (MSBInfo != 0)
                    nCRCData = nCRCData ^ 0xa001;
            }
        }
        return nCRCData;
    }
    /**
     *  数据转换工具类
     *  @author frey
     */

    /**
     * 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
     *
     * @param num
     * @return
     */
    public static int isOdd(int num) {
        return num & 0x1;
    }

    /**
     * 将int转成byte
     *
     * @param number
     * @return
     */
    public static byte intToByte(int number) {
        return hexToByte(intToHex(number));
    }

    /**
     * 将int转成hex字符串
     *
     * @param number
     * @return
     */
    public static String intToHex(int number) {
        String st = Integer.toHexString(number).toUpperCase();
        return String.format("%2s", st).replaceAll(" ", "0");
    }

    /**
     * 字节转十进制
     *
     * @param b
     * @return
     */
    public static int byteToDec(byte b) {
        String s = byteToHex(b);
        return (int) hexToDec(s);
    }

    /**
     * 字节数组转十进制
     *
     * @param bytes
     * @return
     */
    public static int bytesToDec(byte[] bytes) {
        String s = encodeHexString(bytes);
        return (int) hexToDec(s);
    }

    /**
     * Hex字符串转int
     *
     * @param inHex
     * @return
     */
    public static int hexToInt(String inHex) {
        return Integer.parseInt(inHex, 16);
    }

    /**
     * 字节转十六进制字符串
     *
     * @param num
     * @return
     */
    public static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits).toUpperCase();
    }

    /**
     * 十六进制转byte字节
     *
     * @param hexString
     * @return
     */
    public static byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    private static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if (digit == -1) {
            throw new IllegalArgumentException(
                    "Invalid Hexadecimal Character: " + hexChar);
        }
        return digit;
    }

    /**
     * 字节数组转十六进制
     *
     * @param byteArray
     * @return
     */
    public static String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuffer.append(byteToHex(byteArray[i]));
        }
        return hexStringBuffer.toString().toUpperCase();
    }

    /**
     * 十六进制转字节数组
     *
     * @param hexString
     * @return
     */
    public static byte[] decodeHexString(String hexString) {
        if (hexString.length() % 2 == 1) {
            throw new IllegalArgumentException(
                    "Invalid hexadecimal String supplied.");
        }
        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
        }
        return bytes;
    }

    /**
     * 十进制转十六进制
     *
     * @param dec
     * @return
     */
    public static String decToHex(int dec) {
        String hex = Integer.toHexString(dec);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return hex.toLowerCase();
    }

    /**
     * 十六进制转十进制
     *
     * @param hex
     * @return
     */
    public static long hexToDec(String hex) {
        return Long.parseLong(hex, 16);
    }

    /**
     * 十六进制转十进制，并对卡号补位
     */
    public static String setCardNum(String cardNun) {
        String cardNo = null;
        if (cardNun != null) {
            Long cardNo2 = Long.parseLong(cardNun, 16);
            //cardNo=String.format("%015d", cardNo2);
            cardNo = String.valueOf(cardNo2);
        }
        return cardNo;
    }

    public static String xor(String s){
        int key = 0x10;
        char[] charArray = s.toCharArray();
        for(int i =0;i<charArray.length;i++){
            charArray[i]=(char)(charArray[i]^key);
        }
        return String.valueOf(charArray);

//		byte key = 0x10;
//		byte[] bts =  para.getBytes();
//		for(int i = 0; i< bts.length;i++){
//			bts[i]^=key;
//		}
//		String s = new String(bts);

    }

    public static String getBCC(byte[] data) {
        return getBCC(data, 0, data.length);
    }

    public static String getBCC(byte[] data, int start, int end) {
        byte[] BCC = new byte[1];
        for (int i = start; i < data.length; i++) {
            if (i == end) {
                break;
            }
            BCC[0] = (byte) (BCC[0] ^ data[i]);
        }
        String hex = Integer.toHexString(BCC[0] & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return hex.toUpperCase();
    }

}
