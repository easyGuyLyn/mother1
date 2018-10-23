package com.dawoo.chessbox.beans;

import com.dawoo.chessbox.base.BaseBean;

import java.util.List;

/**
 * Created by ywl on 2017/7/21.
 */

public class HomePageBean extends BaseBean {

    private List<ScrollImgBean> scrollImg;

    public List<ScrollImgBean> getScrollImg() {
        return scrollImg;
    }

    public void setScrollImg(List<ScrollImgBean> scrollImg) {
        this.scrollImg = scrollImg;
    }
}
