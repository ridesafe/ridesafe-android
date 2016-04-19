package io.android.backend.models

import io.android.models.BikeType
import io.android.models.RoadCondition
import io.android.models.RoadType

/**
 * Created by evoxmusic on 17/04/16.
 */
data class AccelerationForm(
         var activityType: ActivityType?,
        var startTimestamp: Long,
        var endTimestamp: Long,
        var bikeType: BikeType?,
        var roadType: RoadType?,
        var roadCondition: RoadCondition?)