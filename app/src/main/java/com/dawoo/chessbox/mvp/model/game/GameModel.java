package com.dawoo.chessbox.mvp.model.game;

import android.support.annotation.Nullable;

import com.dawoo.chessbox.bean.Banner;
import com.dawoo.chessbox.bean.GameLink;
import com.dawoo.chessbox.bean.VideoGame;
import com.dawoo.chessbox.bean.VideoGameType;
import com.dawoo.chessbox.mvp.model.BaseModel;
import com.dawoo.chessbox.mvp.service.IGameService;
import com.dawoo.chessbox.mvp.service.IHomeService;
import com.dawoo.chessbox.net.RetrofitHelper;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * 游戏
 * Created by benson on 18-1-8.
 */

public class GameModel extends BaseModel implements IGameModel {

    @Override
    public Subscription getCasinoGameList(Subscriber subscriber, int apiId, int apiTypeId, int PageNumber, int PageSize, String name) {
        Observable<VideoGame> observable = RetrofitHelper
                .getInstance().getService(IGameService.class)
                .getCasinoGame(apiId, apiTypeId, PageNumber, PageSize, name)
                .map(new HttpResultFunc<VideoGame>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getCasinoGameList(Subscriber subscriber, int apiId, int apiTypeId, int PageNumber, int PageSize, String name, String tagId) {
        Observable<VideoGame> observable = RetrofitHelper
                .getInstance().getService(IGameService.class)
                .getCasinoGame(apiId, apiTypeId, PageNumber, PageSize, name, tagId)
                .map(new HttpResultFunc<VideoGame>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getGameLink(Subscriber subscriber, int apiId, int apiTypeId, @Nullable int gameId, @Nullable String gameCode) {
        Observable<GameLink> observable = RetrofitHelper
                .getInstance().getService(IGameService.class)
                .getGameLink(apiId, apiTypeId, gameId, gameCode)
                .map(new HttpResultFunc<GameLink>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getGameLink(Subscriber subscriber, int apiId, int apiTypeId) {
        Observable<GameLink> observable = RetrofitHelper
                .getInstance().getService(IGameService.class)
                .getGameLink(apiId, apiTypeId)
                .map(new HttpResultFunc<GameLink>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getGameTag(Subscriber subscriber, int apiId, int apiTypeId) {
        Observable<List<VideoGameType>> observable = RetrofitHelper.getInstance().getService(IGameService.class).getGameTag(apiId, apiTypeId).map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }
}
