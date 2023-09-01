/*
 * Copyright 2021 Arthur Ivanets, arthur.ivanets.work@gmail.com
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

package com.arthurivanets.googleplayscraper.util

import okhttp3.Response
import okhttp3.ResponseBody

internal fun Response.peekBody(): ResponseBody {
    return this.peekBody(Long.MAX_VALUE)
}

internal fun Response.requireBody(): ResponseBody {
    return checkNotNull(this.body) {
        "The Response Body is NULL"
    }
}