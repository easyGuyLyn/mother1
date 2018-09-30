package com.dawoo.chessbox.bean;

import com.dawoo.chessbox.ipc.IPCSocketManager;

/**
 * 数据中心
 * Created by benson on 18-1-26.
 */

public class DataCenter {
    private static DataCenter instance;
    private static User mUser = new User();
    private static SysInfo mSysInfo = new SysInfo();

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


    public void setUser(User user) {
        mUser.setDomain(user.getDomain());
        mUser.setIp(user.getIp());
        mUser.setCookie(user.getCookie());
        mUser.setLogin(user.isLogin);
        mUser.setUsername(user.getUsername());
        mUser.setPassword(user.getPassword());
        mUser.setRealName(user.getRealName());
        mUser.setBalanceAccount(user.getBalanceAccount());
        mUser.setNickName(user.getNickName());
        mUser.setLoginTimeLast(user.getLoginTimeLast());
    }

    public User getUser() {
        return mUser;
    }

    public String getDomain() {
        String domain = mUser.getDomain();
        return domain;
    }

    public void setDomain(String domain) {
        mUser.setDomain(domain);
    }

    public void setIp(String ip) {
        mUser.setIp(ip);
    }

    public String getIp() {
        return mUser.getIp();
    }

    public String getCookie() {
        return mUser.getCookie();
    }

    public void setCookie(String cookie) {
        mUser.setCookie(cookie);
    }

    public boolean isLogin() {
        return mUser.isLogin();
    }

    public void setLogin(boolean isLogin) {
        mUser.setLogin(isLogin);
    }


    public String getUserName() {
        return mUser.getUsername();
    }

    public void setUserName(String userName) {
        mUser.setUsername(userName);
    }

    public String getPassword() {
        return mUser.getPassword();
    }

    public void setPassword(String password) {
        mUser.setPassword(password);
    }

    public String getRealName() {
        return mUser.getRealName();
    }

    public void setRealName(String realName) {
        mUser.setRealName(realName);
    }


    public SysInfo getSysInfo() {
        return mSysInfo;
    }

    public void setNickName(String nickName) {
        mUser.setNickName(nickName);
    }

    public void setLoginTimeLast(String loginTimeLast) {
        mUser.setLoginTimeLast(loginTimeLast);
    }

    public void setBalanceAccount(String balanceAccount) {
        mUser.setBalanceAccount(balanceAccount);
    }

    public String getNickName() {
        return mUser.getNickName();
    }

    public String getLoginTimeLast() {
        return mUser.getLoginTimeLast();
    }

    public String getBalanceAccount() {
        return mUser.getBalanceAccount();
    }

    public String getWalletBalance() {
        return mUser.getWalletBalance();
    }

    public void setWalletBalance(String walletbalance) {
        mUser.setWalletBalance(walletbalance);
    }


}
