
package com.yuyh.sprintnba.ui.Interactor;

import com.yuyh.sprintnba.http.api.RequestCallback;
import com.yuyh.sprintnba.http.bean.player.Teams;

public interface TeamsListInteractor {

    void getAllTeams(RequestCallback<Teams> callback);
}
