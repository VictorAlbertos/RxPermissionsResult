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

import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.TextView;

public final class Helper {

  static void showPermissionStatus(TextView tvOutput, String[] permissions, int[] grantResults) {
    StringBuilder stringBuilder = new StringBuilder();

    for (int i = 0; i < permissions.length; i++) {
      String permission = permissions[i];
      boolean granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
      if (granted) {
        stringBuilder.append(permission).append(" was granted").append("\n");
      } else {
        stringBuilder.append(permission).append(" wasn't granted").append("\n");
      }
    }

    Log.d("rx_permissions_result", stringBuilder.toString());
    tvOutput.setText(stringBuilder.toString());
  }

}
