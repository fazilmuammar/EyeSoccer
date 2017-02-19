package com.ferhatproduction.eyesoccer.Class;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import org.json.JSONObject;

/**
 * Created by leo on 1/28/17.
 */

public class Globals extends Application {
    public JSONObject clubDetail;

    @Override
    public void onCreate() {
        super.onCreate();

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(getApplicationContext())
                .setDownsampleEnabled(true)
                .build();

        Fresco.initialize(this, config);
    }

    public JSONObject getClubDetail() {
        return clubDetail;
    }

    public void setClubDetail(JSONObject clubDetail) {
        this.clubDetail = clubDetail;
    }
}
