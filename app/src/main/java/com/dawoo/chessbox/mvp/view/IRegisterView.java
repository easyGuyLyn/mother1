package com.dawoo.chessbox.mvp.view;

import com.dawoo.chessbox.bean.RegisterBean;
import com.dawoo.chessbox.bean.RegisterInfoBean;

/**
 * Created by alex on 18-3-22.
 */

public interface IRegisterView extends IBaseView {
//    void accountRegister(String username,String password,String verificationCode);
      void getRegisterInfoSuccess(RegisterInfoBean registerInfoBean);
      void registerSuccess(RegisterBean registerBean);
      void registerError(String error);
      void dissmissDialog();
}
