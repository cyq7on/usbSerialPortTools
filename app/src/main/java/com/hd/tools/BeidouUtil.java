package com.hd.tools;

import java.io.UnsupportedEncodingException;

/**
 * @Description: 基于北斗卫星导航系统用户终端通用数据接口（2.1 协议）
 * 没有暴露过多的设置，以最简单易用的形式提供给使用者，熟悉后可自行定制修改
 * @author: cyq7on
 * @date: 2020/1/22 16:25
 * @version: V1.0
 */
public class BeidouUtil {

    /**
     * 读取卡号
     *
     * @return 读取卡号命令
     */
    public static String getICCmd() {
        return "$CCICA,0,00*7B\r\n";
    }

    /**
     * 获取位置信息，北斗一代
     *
     * @return 获取位置信息命令
     */
    public static String getLocationCmdV1() {
        return "$CCDWA,0000000,V,1,L,,0,,,0*65\r\n";
    }

    /**
     * 获取位置信息，北斗二代，更加精确，频度60s
     *
     * @return 获取位置信息命令
     */
    public static String getLocationCmd() {
        return "$CCRMO,GGA,2,60*09\r\n";
    }

    public static String getLocationCmd(int freq) {
        String s = "CCRMO,GGA,2," + freq;
        String check = SerialPortUtil.getBCC(s.getBytes());
        return "$" + s + "*" + check + "\r\n";
    }

    /**
     * 停止输出所有指令
     * @return
     */
    public static String stopOutputCmd() {
        return "$CCRMO,,3,*4F"+ "\r\n";
    }

    /**
     * 发送短报文
     *
     * @param id      收信方用户id，必须为7位，eg：0967760
     * @param content 短报文内容
     * @return 发送短报文命令，eg：
     * $CCTXA,0967760,1,2,A43132335F414243BABAD7D6*77，其内容为”123_ABC汉字“
     */
    public static String getMsgCmd(String id, String content) {
        String contentFlag = "A4";
        String start = "CCTXA";
        //分别表示通信类别和传输方式，这里选择了普通通信、混合传输
        String middle = "1,2";
        String result = null;
        try {
            String charsetName = "gb2312";
            byte[] contentBytes = content.getBytes(charsetName);
            StringBuilder sb = new StringBuilder(start)
                    .append(",").append(id)
                    .append(",").append(middle)
                    .append(",").append(contentFlag);
            String hexString = SerialPortUtil.encodeHexString(contentBytes);
            sb.append(hexString);
            String s = sb.toString();
            String check = SerialPortUtil.getBCC(s.getBytes(charsetName));
            result = "$" + s + "*" + check + "\r\n";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析反馈信息
     *
     * @param response 反馈字符串，eg：$BDFKI,TXA,Y,Y,0,0000*13，
     *                 $BDFKI,DWA,N,Y,0,0058*16
     * @return
     */
    public static BeidouBean.Response parseResponse(String response) {
        String[] split = response.split(",");
        BeidouBean.Response res = new BeidouBean.Response();
        res.cmdName = split[1];
        res.success = "Y".equals(split[2]);
        res.freqSetting = "Y".equals(split[3]);
        res.limitStatus = Integer.parseInt(split[4]);
        String hourSecond = split[5];
        String hour = hourSecond.substring(0, 2);
        String second = hourSecond.substring(2, 4);
        res.waitSecond = Integer.parseInt(hour) * 60 +
                Integer.parseInt(second);
        return res;
    }

    /**
     * 解析位置信息，用于北斗一代
     *
     * @param response 回传字符串，eg：
     *                 $BDDWR,1,0242407,084936.50,2302.2434,N,11323.6667,E,14,M,-6,M,1,V,V,L*1F
     * @return
     */
    public static BeidouBean.Location parseLocationV1(String response) {
        String[] split = response.split(",");
        BeidouBean.Location location = new BeidouBean.Location();
        location.customStr = response;
        location.userId = split[2];
        location.time = split[3];
        location.lat = split[4];
        location.latDirection = split[5];
        location.lon = split[6];
        location.lonDirection = split[7];
        location.altitude = split[8];
        return location;
    }

    /**
     * 解析位置信息
     *
     * @param response 回传字符串，eg：
     *                 $GNGGA,063846.00,2914.96875,N,10444.57129,E,1,12,1.07,316.47,M,0,M,,,2.58*6A
     * @return
     */
    public static BeidouBean.Location parseLocation(String response) {
        String[] split = response.split(",");
        BeidouBean.Location location = new BeidouBean.Location();
        location.customStr = response;
        location.time = split[1];
        location.lat = split[2];
        location.latDirection = split[3];
        location.lon = split[4];
        location.lonDirection = split[5];
        location.altitude = split[9];
        return location;
    }
}
