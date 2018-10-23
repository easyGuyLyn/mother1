package com.dawoo.chessbox.beans;

import com.dawoo.chessbox.base.BaseBean;

import java.util.List;

/**
 * Created by ywl on 2017-7-26.
 */

public class LivePageBean extends BaseBean {

    private String id;
    private List<LiveChannelTypeBean> liveChannelType;
    private List<LiveChannelBean> liveChannel;

    public List<LiveChannelBean> getLiveChannel() {
        return liveChannel;
    }

    public void setLiveChannel(List<LiveChannelBean> liveChannel) {
        this.liveChannel = liveChannel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<LiveChannelTypeBean> getLiveChannelType() {
        return liveChannelType;
    }

    public void setLiveChannelType(List<LiveChannelTypeBean> liveChannelType) {
        this.liveChannelType = liveChannelType;
    }
}
