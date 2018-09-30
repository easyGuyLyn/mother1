package com.dawoo.chessbox.mvp.view;

import com.dawoo.chessbox.bean.payInfo.DepositBean;
import com.dawoo.chessbox.bean.payInfo.DepositResultBean;
import com.dawoo.chessbox.bean.payInfo.PayTypeBean;
import com.dawoo.chessbox.bean.payInfo.PopPayBean;

import java.util.List;

/**
 * Created by rain on 18-3-23.
 */

public interface IDisportView extends IBaseView {
    void getPayTypes(List<DepositBean> o);

    void getSecType(PayTypeBean o);

    void getPayInfo(PopPayBean o);

    void postPayInfo(DepositResultBean o);

    void onAccountResult(Object o);//获取账户数据
}
