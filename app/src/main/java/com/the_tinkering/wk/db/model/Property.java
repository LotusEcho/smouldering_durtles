/*
 * Copyright 2019-2020 Ernst Jan Plugge <rmc@dds.nl>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.the_tinkering.wk.db.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import javax.annotation.Nonnull;

/**
 * Room entity for the properties table, which is a simple key/value store with Strings for both keys and values.
 * This entity class is not actually used directly, it's only used by Room to generate the schema.
 */
@Entity(tableName = "properties")
public final class Property {
    /**
     * The name and primary key.
     */
    @NonNull
    @PrimaryKey
    public @Nonnull String name = "";

    /**
     * The value.
     */
    @NonNull
    public @Nonnull String value = "";
}