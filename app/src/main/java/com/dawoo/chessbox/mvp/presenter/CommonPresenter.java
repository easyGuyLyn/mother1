package com.dawoo.chessbox.mvp.presenter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import com.dawoo.chessbox.mvp.model.mine.MineModel;
import com.dawoo.chessbox.mvp.view.IBaseView;
import com.dawoo.chessbox.mvp.view.ICommonProblemView;
import com.dawoo.chessbox.mvp.view.IGetUserPlayerRecommendView;
import com.dawoo.chessbox.mvp.view.IMineAboutView;
import com.dawoo.chessbox.mvp.view.IMineFragmentView;
import com.dawoo.chessbox.mvp.view.IMineHelpDerailsView;
import com.dawoo.chessbox.mvp.view.IMineTeamsView;
import com.dawoo.chessbox.mvp.view.ISeconundTypeView;
import com.dawoo.chessbox.mvp.view.IShareRuleRecordView;
import com.dawoo.chessbox.mvp.view.IShareRuleView;
import com.dawoo.chessbox.net.rx.ProgressSubscriber;

import java.util.Calendar;

import rx.Subscription;


/**
 * 我的页面presenter
 */

public class CommonPresenter<T extends IBaseView> extends BasePresenter {

    private final Context mContext;
    private T mView;
    private final MineModel mMineModel;
    private DatePickerDialog mDialog;

    public CommonPresenter(Context context, T mView) {
        super(context, mView);
        mContext = context;
        this.mView = mView;

        mMineModel = new MineModel();
    }


    /**
     * 我的页面获取相关数据
     */
    public void getLink() {
        Subscription subscription = mMineModel.getLink(new ProgressSubscriber<>(o -> ((IMineFragmentView) mView).onLinkResult(o), mContext));
        subList.add(subscription);
    }


    public void getUserPlayerRecommend() {
        Subscription subscription = mMineModel.getUserPlayerRecommend(new ProgressSubscriber<>(o -> ((IGetUserPlayerRecommendView) mView).onResult(o), mContext));
        subList.add(subscription);
    }

    /**
     * 分享规则
     */
    public void gradientTempArrayList(String startTime,
                                      String endTime,
                                      Integer pageNumber,
                                      Integer pageSize) {
        String startTim = startTime + " 00:00:01";
        String endTim = endTime + " 23:59:59";
        Subscription subscription = mMineModel.gradientTempArrayList(

                new ProgressSubscriber(o -> ((IShareRuleView) mView).onResult(o), mContext),
                startTim,
                endTim,
                pageNumber,
                pageSize);
        subList.add(subscription);
    }

    /**
     * 加载更多的分享规则
     */
    public void gradientTempLoadMore(String startTime,
                                     String endTime,
                                     Integer pageNumber,
                                     Integer pageSize) {
        String startTim = startTime + " 00:00:01";
        String endTim = endTime + " 23:59:59";
        Subscription subscription = mMineModel.gradientTempArrayList(
                new ProgressSubscriber(o -> ((IShareRuleView) mView).LoadMore(o), mContext),
                startTim,
                endTim,
                pageNumber,
                pageSize);
        subList.add(subscription);
    }


    public void getShareUserPlayerRecommend() {
        Subscription subscription = mMineModel.getUserPlayerRecommend(new ProgressSubscriber<>(o -> ((IShareRuleRecordView) mView).onResult(o), mContext));
        subList.add(subscription);
    }

    /**
     * 关于我们
     */
    public void getAbout() {
        Subscription subscription = mMineModel.about(new ProgressSubscriber<>(o -> ((IMineAboutView) mView).onAboutResult(o), mContext));
        subList.add(subscription);
    }


    /**
     * 详细条款
     */
    public void getTears() {
        Subscription subscription = mMineModel.terms(new ProgressSubscriber<>(o -> ((IMineTeamsView) mView).onResult(o), mContext));
        subList.add(subscription);
    }

    /**
     * 常见问题
     * onErrorSimpleReason
     */
    public void getCommentProblem() {
        Subscription subscription = mMineModel.helpFirstType(new ProgressSubscriber<>(o -> ((ICommonProblemView) mView).onResult(o), mContext));
        subList.add(subscription);
    }


    /**
     * 常见问题二级
     */
    public void getSeconundType(String searchId) {
        Subscription subscription = mMineModel.secondType(new ProgressSubscriber<>(o -> ((ISeconundTypeView) mView).onResult(o), mContext), searchId);
        subList.add(subscription);
    }

    /**
     * 常见问题三级
     */

    public void helpDetail(String searchId) {
        Subscription subscription = mMineModel.helpDetail(new ProgressSubscriber<>(o -> ((IMineHelpDerailsView) mView).onResult(o), mContext), searchId);
        subList.add(subscription);
    }

    /**
     * 時間選擇器
     *
     * @param type 類型：０　開始　　，１　結束
     */
    public void selectTime(int type) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        mDialog = new DatePickerDialog(mContext, (view, year1, month1, dayOfMonth) -> {
            String month_ = month1 + 1 + "";
            String day1 = "" + dayOfMonth;
            if ((month1 + 1) < 10)
                month_ = "0" + month_;
            if (dayOfMonth < 10)
                day1 = "0" + day1;
            if (type == 0)
                ((IShareRuleView) mView).chooseEndTime(year1 + "-" + month_ + "-" + day1);
            else
                ((IShareRuleView) mView).chooseStartTime(year1 + "-" + month_ + "-" + day1);
        }, year, month, day);
        mDialog.setCustomTitle(null);
        DatePicker dp = mDialog.getDatePicker();
        mDialog.show();

    }


    @Override
    public void onDestory() {
        super.onDestory();
    }
}
