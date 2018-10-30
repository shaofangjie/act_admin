package com.act.admin;

import act.Act;
import act.controller.Controller;
import act.job.OnAppStart;

import javax.inject.Inject;

public class AppEntry extends Controller.Util {

    @Inject
    private AppInitData appInitData;

    public static void main(String[] args) throws Exception {
        Act.start("ACT_ADMIN");
    }

    @OnAppStart
    public void onStart() {

        appInitData.initMenu();

    }



}
