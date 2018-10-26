
package com.yuyh.sprintnba.ui.Interactor;

import com.yuyh.sprintnba.http.api.RequestCallback;
import com.yuyh.sprintnba.http.bean.news.NewsDetail;

public interface NBANewsDetailInteractor {

    void getNewsDetail(String arcId, RequestCallback<NewsDetail> callback);
}
