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
import android.app.Application;
import android.os.Bundle;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import java.util.concurrent.TimeUnit;

public class ActivitiesLifecycleCallbacks {
  final Application application;
  volatile Activity liveActivityOrNull;
  Application.ActivityLifecycleCallbacks activityLifecycleCallbacks;

  public ActivitiesLifecycleCallbacks(Application application) {
    this.application = application;
    registerActivityLifeCycle();
  }

  private void registerActivityLifeCycle() {
    if (activityLifecycleCallbacks != null) application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);

    activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
      @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        liveActivityOrNull = activity;
      }

      @Override public void onActivityStarted(Activity activity) {}

      @Override public void onActivityResumed(Activity activity) {
        liveActivityOrNull = activity;
      }

      @Override public void onActivityPaused(Activity activity) {
        liveActivityOrNull = null;
      }

      @Override public void onActivityStopped(Activity activity) {}

      @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

      @Override public void onActivityDestroyed(Activity activity) {}
    };

    application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
  }

  public Activity getLiveActivity() {
    return liveActivityOrNull;
  }

  /**
   * Emits just one time a valid reference to the current activity
   * @return the current activity
   */
  volatile boolean emitted = false;
  public Observable<Activity> getOLiveActivity() {
    emitted = false;
    return Observable.interval(50, 50, TimeUnit.MILLISECONDS)
        .map(new Function<Long, Object>() {
          @Override public Object apply(Long aLong) throws Exception {
            if (liveActivityOrNull == null) return 0;
            return liveActivityOrNull;
          }
        })
        .takeWhile(new Predicate<Object>() {
          @Override public boolean test(Object candidate) throws Exception {
            boolean continueEmitting = true;
            if (emitted) continueEmitting = false;
            if (candidate instanceof Activity) emitted = true;
            return continueEmitting;
          }
        })
        .filter(new Predicate<Object>() {
          @Override public boolean test(Object candidate) throws Exception {
            return candidate instanceof Activity;
          }
        })
        .map(new Function<Object, Activity>() {
          @Override public Activity apply(Object activity) throws Exception {
            return (Activity) activity;
          }
        });
  }

}