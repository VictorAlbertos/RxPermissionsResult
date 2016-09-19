/*
 * Copyright 2016 Víctor Albertos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.victoralbertos.rxpermissionsresult;

import android.Manifest;
import android.os.Bundle;
import android.widget.TextView;
import com.trello.rxlifecycle.components.RxActivity;
import io.victoralbertos.rx1_permissions_result.RxPermissionsResult;

public final class OnCreateActivity extends RxActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.common);

    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CAMERA};

    RxPermissionsResult.on(this).requestPermissions(permissions)
        .compose(bindToLifecycle())
        .subscribe(result ->
            result.targetUI()
                .showPermissionStatus(result.permissions(), result.grantResults())
        );
  }

  private void showPermissionStatus(String[] permissions, int[] grantResults) {
    Helper.showPermissionStatus((TextView) findViewById(R.id.tv_output), permissions, grantResults);
  }
}
