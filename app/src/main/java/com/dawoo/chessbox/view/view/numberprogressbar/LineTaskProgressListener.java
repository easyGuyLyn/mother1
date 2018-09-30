package com.dawoo.chessbox.view.view.numberprogressbar;

import com.dawoo.chessbox.bean.line.LineErrorDialogBean;
import com.dawoo.chessbox.util.line.LineTaskBaseListener;

/**
 * Created by archar on 15-4-23.
 * SpashAcitivity的线路任务UI回调器
 */
public interface LineTaskProgressListener extends LineTaskBaseListener {

    void onProgressBarChange(int current, int max);

    void onErrorSimpleReason(String result);

    void onErrorComplexReason(LineErrorDialogBean lineErrorDialogBean);

    void onSpalshGetLineSuccess(String domain, String ip, LineTaskProgressListener lineTaskProgressListener);
}
