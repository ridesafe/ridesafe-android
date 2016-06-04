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

package io.android.backend.models

import io.ridesafe.android.models.BikeType
import io.ridesafe.android.models.RoadCondition
import io.ridesafe.android.models.RoadType
import java.io.Serializable

/**
 * Created by evoxmusic on 17/04/16.
 */
data class DataForm(
        var activityType: ActivityType?,
        var startTimestamp: Long,
        var endTimestamp: Long,
        var bikeType: BikeType?,
        var roadType: RoadType?,
        var roadCondition: RoadCondition?) : Serializable