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

package io.ridesafe.android.models

/**
 * Created by evoxmusic on 04/06/16.
 */
interface SensorData {

    val timestamp: Long
    val x: Float
    val y: Float
    val z: Float

}

data class AccelerometerSensorData(override val timestamp: Long, override val x: Float, override val y: Float, override val z: Float) : SensorData

data class GyroscopeSensorData(override val timestamp: Long, override val x: Float, override val y: Float, override val z: Float) : SensorData