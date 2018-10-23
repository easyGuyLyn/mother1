package com.dawoo.chessbox;


/**
 * Created by ywl on 2018-1-13.
 */

public class MyApplication {

    private static MyApplication instance = new MyApplication();
    private Long token;

    public static MyApplication getInstance() {
        return instance;
    }


    public String getToken() {
        return String.valueOf(token);
    }

    public void setToken(Long token) {
        this.token = token;
    }
}
