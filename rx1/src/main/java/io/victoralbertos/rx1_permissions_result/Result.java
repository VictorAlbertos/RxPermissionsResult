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

package io.victoralbertos.rx1_permissions_result;

public class Result<T> {
  private final T targetUI;
  private final String permissions[];
  private final int[] grantResults;

  public Result(T targetUI, String[] permissions, int[] grantResults) {
    this.targetUI = targetUI;
    this.permissions = permissions;
    this.grantResults = grantResults;
  }

  public T targetUI() {
    return targetUI;
  }

  public String[] permissions() {
    return permissions;
  }

  public int[] grantResults() {
    return grantResults;
  }
}
