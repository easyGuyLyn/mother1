
package com.yuyh.sprintnba.ui.Interactor;

import com.yuyh.sprintnba.http.api.RequestCallback;
import com.yuyh.sprintnba.http.bean.player.Players;

public interface PlayersListInteractor {

    void getAllPlayers(RequestCallback<Players> callback);
}
