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

import java.io.Serializable

/**
 * Created by evoxmusic on 10/04/16.
 */
data class Data(var timestamp: Long? = 0L,
                var accX: Float? = 0f,
                var accY: Float? = 0f,
                var accZ: Float? = 0f,
                var gyrX: Float? = 0f,
                var gyrY: Float? = 0f,
                var gyrZ: Float? = 0f,
                var key: String? = null) : Serializable