package io.victoralbertos.rxpermissionsresult;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public final class SplashActivity extends AppCompatActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.splash_activity);

    findViewById(R.id.bt_on_click).setOnClickListener(view ->
        startActivity(new Intent(this, OnClickActivity.class))
    );

    findViewById(R.id.bt_on_create).setOnClickListener(view ->
        startActivity(new Intent(this, OnCreateActivity.class))
    );

    findViewById(R.id.bt_on_create_interval).setOnClickListener(view ->
        startActivity(new Intent(this, OnCreateIntervalActivity.class))
    );
  }
}
