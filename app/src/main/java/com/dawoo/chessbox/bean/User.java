package com.dawoo.chessbox.bean;

/**
 * Created by benson on 17-12-21.
 */

public class User {
    /**
     * 域名线路
     */
    public String domain = "";
    /**
     * 域名线路ip
     */
    public String ip;
    /**
     * 协议头cookie
     */
    public String cookie = "";
    /**
     * 是否登录
     */
    public boolean isLogin;
    /**
     * 用户的登录帐户
     */
    public String username;
    /**
     * 用户的登录密码
     */
    public String password;
    /**
     * 用户没加密真实姓名
     */
    public String realName;

    /**
     * 用户名字
     */
    public String nickName;

    /**
     * 用户余额
     */
    public String balanceAccount;
    /**
     * 随身福利
     */
    public String walletBalance;

    /**
     * 上次登录时间
     */

    public String loginTimeLast;


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getBalanceAccount() {
        return balanceAccount;
    }

    public void setBalanceAccount(String balanceAccount) {
        this.balanceAccount = balanceAccount;
    }

    public String getLoginTimeLast() {
        return loginTimeLast;
    }

    public void setLoginTimeLast(String loginTimeLast) {
        this.loginTimeLast = loginTimeLast;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(String walletBalance) {
        this.walletBalance = walletBalance;
    }
}
