package com.dawoo.chessbox.bean;

import android.text.TextUtils;

/**
 * API
 * Created by archar on
 */
public enum ApiEnum {
    GBBY("28", "GG捕鱼12", "logo12"),
    HBDZ("15", "HB电子6", "logo07"),
    MWDZ("35", "MW电子8", "logo11"),
    PNGDZ("26", "PNG电子11", "logo06"),
    BSGDZ("20", "BSG电子7", "logo03"),
    DTDZ("27", "DT电子14", "logo10"),
    PTDZ("6", "PT电子2", "logo04"),
    XBDZ("25", "新霸电子5", "logo05"),
    MGDZ("44", "MG电子4", "logo01"),
    NTDZ("3", "NT电子", "logo01"),
    XPPDZ("38", "新PP电子", "logo02"),
    PGLHJ("45", "PG老虎机", "logo14"),
    NYXDZ("14", "NYX电子15", "logo13"),
    PPDZ("32", "PP电子", "logo02"),
    AGDZ("9", "AG电子", "logo08"),
    BB("10", "BB电子", "logo09");

    private String code;
    private String memo;
    private String drawable;

    ApiEnum(String code, String memo, String drawable) {
        this.code = code;
        this.memo = memo;
        this.drawable = drawable;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDrawable() {
        return drawable;
    }

    public void setDrawable(String drawable) {
        this.drawable = drawable;
    }

    public static String getDrawableByCode(String code) {
        String drawable = "";
        if (!TextUtils.isEmpty(code)) {
            for (ApiEnum apiEnum : ApiEnum.values()) {
                if (code.equals(apiEnum.getCode())) {
                    drawable = apiEnum.getDrawable();
                    break;
                }
            }
        }
        return drawable;
    }
}
