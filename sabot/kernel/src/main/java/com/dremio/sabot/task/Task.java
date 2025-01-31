/*
 * Copyright (C) 2017-2019 Dremio Corporation
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
package com.dremio.sabot.task;

/**
 * A runnable task that will be scheduled and run by the scheduler.<br>
 * The main assumption is that the task will yield after a short amount of time.
 */
public interface Task {

  /**
   * Task state
   */
  enum State {
    RUNNABLE,
    BLOCKED_ON_UPSTREAM,
    BLOCKED_ON_DOWNSTREAM,
    BLOCKED_ON_SHARED_RESOURCE,
    BLOCKED_ON_MEMORY, // blocked on obtaining a grant from the memory arbiter
    DONE
  }

  State getState();

  long getTaskWeight();
}
