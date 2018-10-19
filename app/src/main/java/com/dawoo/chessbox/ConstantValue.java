package com.dawoo.chessbox;

/**
 * 常量
 */

public interface ConstantValue {


    /**
     * 21 测试环境 code码
     */
    String TEST_APP_CODE = "7wt3";

    /**
     * 获取公网ip 地址
     */
    String IP_LOCAL_URL = "http://ip.chinaz.com/getip.aspx";


    /**
     * 阿里云总线路服务器
     */
    String BASE_URL_1_IP = "http://203.107.1.33/194768/d?host=apiplay.info";
    String BASE_URL_2_IP = "http://203.107.1.33/194768/d?host=hpdbtopgolddesign.com";
    String BASE_URL_3_IP = "http://203.107.1.33/194768/d?host=agpicdance.info";

    /**
     * 线路域名URL
     */
    String BOSS_ = ":1344/boss";

    String BOSS_API = "/boss-api";

    String BOSS_API_1 = ":1344/boss-api";

    String BASE_URL_HEAD = "https://";

    String LINE_URL = "/app/line.html";

    String BASE_URL = BASE_URL_HEAD + "apiplay.info" + BOSS_;
    String BASE_URL_1 = BASE_URL_HEAD + "apiplay.info" + BOSS_API_1;
    String BASE_URL_2 = BASE_URL_HEAD + "agpicdance.info" + BOSS_API_1;
    String BASE_URL_3 = BASE_URL_HEAD + "hpdbtopgolddesign.com" + BOSS_API_1;
    String[] fecthUrl = {BASE_URL_1 + LINE_URL, BASE_URL_2 + LINE_URL, BASE_URL_3 + LINE_URL};

    String BASE_URL_4 = "http://boss-api-test.gbboss.com" + BOSS_API; // 测试 外网
    String BASE_URL_5 = "http://192.168.0.92:8787" + BOSS_API; // 测试


    /**
     * webView 类型
     */
    String WEBVIEW_URL = "WEBVIEW_URL";
    String WEBVIEW_TYPE = "WEBVIEW_TYPE";
    String WEBVIEW_TYPE_THIRD_ORDINARY = "WEBVIEW_TYPE_THIRD_ORDINARY";
    String EVENT_REFRSH_API = "EVENT_REFRSH_API";

    /**
     * 登出帐户
     */
    String EVENT_TYPE_LOGOUT = "EVENT_TYPE_LOGOUT";
    /**
     * 登出帐户
     */
    String EVENT_CLOSER_App = "EVENT_CLOSER_App";


    /**
     * 接口失败，抛出异常
     * 页面有刷新或者加载更多，或者页面要在抛出异常处理一些事物
     */
    String EVENT_TYPE_NETWORK_EXCEPTION = "EVENT_TYPE_NETWORK_EXCEPTION";
    String VOICE_ON_CLICK = "VOICE_ON_CLICK";
    /**
     * 清除缓存
     */
    String EVENT_CLEAR_CACHE = "EVENT_CACHE";

    String packageQQ = "com.tencent.mobileqq";
    String packageali = "com.eg.android.AlipayGphone";
    String packagewechat = "com.tencent.mm";
    String packagejd = "com.wangyin.payment";
    String packageBd = "com.baidu.wallet";

    String PERMISSIONS_STORAGE_WRITE = "android.permission.WRITE_EXTERNAL_STORAGE";
    String PERMISSIONS_STORAGE_READ = "android.permission.READ_EXTERNAL_STORAGE";


    /**
     * 横屏 调整
     */
    String SCREEN_ORIENTATION_REVERSE_LANDSCAPE = "SCREEN_ORIENTATION_REVERSE_LANDSCAPE";

    /**
     * 横屏
     */
    String SCREEN_ORIENTATION_LANDSCAPE = "SCREEN_ORIENTATION_LANDSCAPE";

    /**
     * 竖屏 调整
     */
    String SCREEN_ORIENTATION_REVERSE_PORTRAIT = "SCREEN_ORIENTATION_REVERSE_PORTRAIT";

    /**
     * 竖屏
     */
    String SCREEN_ORIENTATION_PORTRAIT = "SCREEN_ORIENTATION_PORTRAIT";


}
