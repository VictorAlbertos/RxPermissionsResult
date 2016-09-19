/*
 * Copyright 2016 Copyright 2016 Víctor Albertos
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

package io.victoralbertos.rx2_permissions_result.internal;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

public class ShadowActivity extends Activity {
  private static Request request;
  private OnResult onResult;
  private String permissions[];
  private int[] grantResults;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (request == null) {
      finish();
      return;
    }

    onResult = request.onResult();

    if (savedInstanceState != null) return;

    ActivityCompat.requestPermissions(this, request.permissions(), 0);
  }

  @Override public void onRequestPermissionsResult(int requestCode,
      String permissions[], int[] grantResults) {
    this.permissions = permissions;
    this.grantResults = grantResults;
    finish();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (onResult != null) {
      onResult.response(permissions, grantResults);
    }
  }

  public static void setRequest(Request aRequest) {
    request = aRequest;
  }
}
