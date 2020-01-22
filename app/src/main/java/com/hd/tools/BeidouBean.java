package com.hd.tools;

public class BeidouBean {

    //发出指令后的反馈信息
    public static class Response{
        public String cmdName;
        //指令是否执行成功
        public boolean success;
        public boolean freqSetting;
        //0-发射抑制解除，大于0则不正常
        public int limitStatus;
        //当用户设备发送入站申请时，若距离上一次入站申请
        //的时间间隔小于服务频度时，给出等待时间提示，格式为hhss
        public int waitSecond;

        @Override
        public String toString() {
            return "Response{" +
                    "cmdName='" + cmdName + '\'' +
                    ", success=" + success +
                    ", freqSetting=" + freqSetting +
                    ", limitStatus=" + limitStatus +
                    ", waitSecond=" + waitSecond +
                    '}';
        }
    }

    //定位信息，文档字段多达15个，这里只取了主要的几个
    public static class Location{
        public String userId;
        //定位时刻(UTC)，084936.50
        public String time;
        public String lon;
        public String lonDirection;
        public String lat;
        public String latDirection;
        public String altitude;

        @Override
        public String toString() {
            return "Location{" +
                    "userId='" + userId + '\'' +
                    ", time='" + time + '\'' +
                    ", lon='" + lon + '\'' +
                    ", lonDirection='" + lonDirection + '\'' +
                    ", lat='" + lat + '\'' +
                    ", latDirection='" + latDirection + '\'' +
                    ", altitude='" + altitude + '\'' +
                    '}';
        }
    }
}
