package com.dawoo.chessbox.util;

import android.support.v4.app.FragmentManager;

import com.dawoo.chessbox.view.feagment_game.BaseTipsDialogFragment;

public class TipsDialogFaragmentUtils {


    public static void showTipsDialogFragmentdialog(FragmentManager supportFragmentManager, String msg) {
        BaseTipsDialogFragment baseTipsDialogFragment = BaseTipsDialogFragment.newInstance(msg, false);
        DialogFramentManager.getInstance().showDialog(supportFragmentManager, baseTipsDialogFragment);
    }
    public static void showTipsDialogFragmentdialog(FragmentManager supportFragmentManager, String msg,boolean isShowCancle) {
        BaseTipsDialogFragment baseTipsDialogFragment = BaseTipsDialogFragment.newInstance(msg, isShowCancle);
        DialogFramentManager.getInstance().showDialog(supportFragmentManager, baseTipsDialogFragment);
    }

    public static void showTipsDialogFragmentdialog(FragmentManager supportFragmentManager, String msg, BaseTipsDialogFragment.OnSweetClickListener listener) {
        BaseTipsDialogFragment baseTipsDialogFragment = BaseTipsDialogFragment.newInstance(msg, false);
        baseTipsDialogFragment.setConfirmClickListener(listener);
        DialogFramentManager.getInstance().showDialog(supportFragmentManager, baseTipsDialogFragment);
    }
    public static void showTipsDialogFragmentdialog(FragmentManager supportFragmentManager, String msg,boolean isShowCancle,boolean isH5) {
        BaseTipsDialogFragment baseTipsDialogFragment = BaseTipsDialogFragment.newInstance(msg, isShowCancle,isH5);
        DialogFramentManager.getInstance().showDialog(supportFragmentManager, baseTipsDialogFragment);
    }

}
