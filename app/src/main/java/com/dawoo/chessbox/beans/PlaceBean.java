package com.dawoo.chessbox.beans;

import com.dawoo.chessbox.base.BaseBean;

/**
 * Created by ywl on 2017/7/27.
 */

public class PlaceBean extends BaseBean {

    private String id;
    private String name;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
