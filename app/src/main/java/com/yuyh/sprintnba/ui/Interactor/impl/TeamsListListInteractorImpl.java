package com.yuyh.sprintnba.ui.Interactor.impl;

import com.yuyh.sprintnba.http.api.RequestCallback;
import com.yuyh.sprintnba.http.api.tencent.TencentService;
import com.yuyh.sprintnba.http.bean.player.Teams;
import com.yuyh.sprintnba.ui.Interactor.TeamsListInteractor;

/**
 * @author yuyh.
 * @date 16/6/24.
 */
public class TeamsListListInteractorImpl implements TeamsListInteractor {

    @Override
    public void getAllTeams(RequestCallback<Teams> callback) {
        TencentService.getTeamList(false, callback);
    }
}
