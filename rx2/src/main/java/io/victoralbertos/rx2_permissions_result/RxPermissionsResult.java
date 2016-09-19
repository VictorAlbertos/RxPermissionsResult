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

package io.victoralbertos.rx2_permissions_result;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.victoralbertos.rx2_permissions_result.internal.ActivitiesLifecycleCallbacks;
import io.victoralbertos.rx2_permissions_result.internal.Locale;
import io.victoralbertos.rx2_permissions_result.internal.OnResult;
import io.victoralbertos.rx2_permissions_result.internal.Request;
import io.victoralbertos.rx2_permissions_result.internal.ShadowActivity;
import java.util.List;

public class RxPermissionsResult {
  private static ActivitiesLifecycleCallbacks activitiesLifecycle;

  public static void register(final Application application) {
    activitiesLifecycle = new ActivitiesLifecycleCallbacks(application);
  }

  public static <T extends Activity> Builder<T> on(T activity) {
    return new Builder<T>(activity);
  }

  public static <T extends Fragment> Builder<T> on(T fragment) {
    return new Builder<T>(fragment);
  }

  public static class Builder<T> {
    final Class clazz;
    final PublishSubject<Result<T>> subject = PublishSubject.create();
    private final boolean uiTargetActivity;

    public Builder(T t) {
      if (activitiesLifecycle == null) {
        throw new IllegalStateException(Locale.RX_PERMISSIONS_RESULT_NOT_REGISTER);
      }

      this.clazz = t.getClass();
      this.uiTargetActivity = t instanceof Activity;
    }

    public Observable<Result<T>> requestPermissions(String... permissions) {
      return startShadowActivity(new Request(permissions));
    }

    private Observable<Result<T>> startShadowActivity(Request request) {

      OnResult onResult = uiTargetActivity ? onResultActivity() : onResultFragment();
      request.setOnResult(onResult);

      ShadowActivity.setRequest(request);

      activitiesLifecycle.getOLiveActivity().subscribe(new Consumer<Activity>() {
        @Override public void accept(Activity activity) throws Exception {
          activity.startActivity(new Intent(activity, ShadowActivity.class));
        }
      });

      return subject;
    }

    private OnResult onResultActivity() {
      return new OnResult() {
        @Override public void response(String permissions[], int[] grantResults) {
          if (activitiesLifecycle.getLiveActivity() == null) return;

          //If true it means some other activity has been stacked as a secondary process.
          //Wait until the current activity be the target activity
          if (activitiesLifecycle.getLiveActivity().getClass() != clazz) {
            return;
          }

          T activity = (T) activitiesLifecycle.getLiveActivity();
          subject.onNext(new Result<T>((T) activity, permissions, grantResults));
          subject.onComplete();
        }
      };
    }

    private OnResult onResultFragment() {
      return new OnResult() {
        @Override public void response(String permissions[], int[] grantResults) {
          if (activitiesLifecycle.getLiveActivity() == null) return;

          Activity activity = activitiesLifecycle.getLiveActivity();

          FragmentActivity fragmentActivity = (FragmentActivity) activity;
          FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();

          Fragment targetFragment = getTargetFragment(fragmentManager.getFragments());

          if(targetFragment != null) {
            subject.onNext(new Result<T>((T) targetFragment, permissions, grantResults));
            subject.onComplete();
          }

          //If code reaches this point it means some other activity has been stacked as a secondary process.
          //Do nothing until the current activity be the target activity to get the associated fragment
        }
      };
    }

    @Nullable private Fragment getTargetFragment(List<Fragment> fragments) {
      if (fragments == null) return null;

      for (Fragment fragment : fragments) {
        if(fragment != null && fragment.isVisible() && fragment.getClass() == clazz) {
          return fragment;
        } else if (fragment != null && fragment.getChildFragmentManager() != null) {
          List<Fragment> childFragments = fragment.getChildFragmentManager().getFragments();
          Fragment candidate = getTargetFragment(childFragments);
          if (candidate != null) return candidate;
        }
      }

      return null;
    }
  }
}
