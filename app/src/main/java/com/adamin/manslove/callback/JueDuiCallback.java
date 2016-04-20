package com.adamin.manslove.callback;

import com.adamin.manslove.domain.juedui.JueDuiWrapper;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by adamlee on 2016/3/30.
 */
public abstract class JueDuiCallback extends Callback<JueDuiWrapper> {

    @Override
    public JueDuiWrapper parseNetworkResponse(Response response) throws Exception {
        String json=response.body().string();

        return new Gson().fromJson(json,JueDuiWrapper.class);
    }
}
