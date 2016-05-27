/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.ridesafe.android.services

import io.ridesafe.android.models.Acceleration
import java.io.Serializable
import java.util.*

/**
 * Created by evoxmusic on 11/04/16.
 */
interface ActivityObservable: Serializable {

    fun getObservers(): HashSet<ActivityObserver>

    fun addObserver(activityObserver: ActivityObserver) = getObservers().add(activityObserver)

    fun removeObserver(activityObserver: ActivityObserver) = getObservers().remove(activityObserver)

    fun notifyObservers(acceleration: Acceleration?) = getObservers().forEach { e -> acceleration?.let { e.onAcceleration(it) } }

}