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

package io.ridesafe.android.extensions

import io.ridesafe.android.models.AccelerometerSensorData
import io.ridesafe.android.models.Data
import io.ridesafe.android.models.GyroscopeSensorData
import io.ridesafe.android.models.SensorData

/**
 * Created by evoxmusic on 04/06/16.
 */
fun List<SensorData>.mergeToSingleData(): Data {
    val data = Data(this[0].timestamp)

    this.map { v ->
        when (v) {
            is AccelerometerSensorData -> {
                data.accX = v.x
                data.accY = v.y
                data.accZ = v.z
                data
            }
            is GyroscopeSensorData -> {
                data.gyrX = v.x
                data.gyrY = v.y
                data.gyrZ = v.z
                data
            }
            else -> data
        }
    }

    return data
}