package com.dawoo.chessbox.bean;

/**
 * 数据中心
 * Created by benson on 18-1-26.
 */

public class DataCenter {

    private String domain;
    private String ip;
    private String cookie;

    private static DataCenter instance;


    public static DataCenter getInstance() {
        if (instance == null) {
            synchronized (DataCenter.class) {
                if (instance == null) {
                    instance = new DataCenter();
                }
            }
        }
        return instance;
    }


    public String getDomain() {
        return domain;
    }

    public void setDomain(String d) {
        domain = d;
    }

    public void setIp(String ip_) {
        ip = ip_;
    }

    public String getIp() {
        return ip;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String c) {
        cookie = c;
    }



}
