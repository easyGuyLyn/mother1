package com.dawoo.chessbox.base;




import com.dawoo.chessbox.util.BeanUtil;

import java.io.Serializable;

/**
 * Created by ywl on 2017/9/4.
 */

public class BaseBean implements Serializable {

    public static final long serialVersionUID = -316172390920775219L;

    @Override
    public String toString() {
        return BeanUtil.bean2string(this);
    }

}
