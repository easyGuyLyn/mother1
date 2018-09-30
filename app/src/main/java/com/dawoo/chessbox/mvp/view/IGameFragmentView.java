package com.dawoo.chessbox.mvp.view;

/**
 * Created by benson on 17-12-27.
 */

public interface IGameFragmentView extends IBaseView {

    void onSiteApiRelationResult(Object o);

    void onGameLinkResult(Object o, int apiId);
}
