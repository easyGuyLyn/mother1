package com.dawoo.chessbox.mvp.view;

import com.dawoo.chessbox.bean.ApiBean;
import com.dawoo.chessbox.bean.BankCards;
import com.dawoo.chessbox.bean.ConversionInfoBean;
import com.dawoo.chessbox.bean.QuotaConversionBean;
import com.dawoo.chessbox.bean.RefreshhApis;
import com.dawoo.chessbox.bean.UserAccount;
import com.dawoo.chessbox.bean.UserAssert;
import com.dawoo.chessbox.net.HttpResult;

/**
 * Created by b on 18-3-26.
 */

public interface IQuotaConversionView extends IBaseView{

    void onNoAutoTransferInfo(ConversionInfoBean o);

    void onRefreshApis(UserAssert bankCards);

    void onTransfersMoney(QuotaConversionBean o);

    void onReconnectTransfer(QuotaConversionBean o);

    void onRefreshApi(ApiBean o);

    void selectedGame(String bankName,int index);

    void onRecovery(HttpResult refreshhApis);

}
