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

package com.the_tinkering.wk.db;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.the_tinkering.wk.db.model.Subject;
import com.the_tinkering.wk.util.Logger;
import com.the_tinkering.wk.util.SearchUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.the_tinkering.wk.util.ObjectSupport.orElse;

/**
 * Content provider for search results, used for incremental search.
 */
public final class SubjectContentProvider extends ContentProvider {
    private static final Logger LOGGER = Logger.get(SubjectContentProvider.class);

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(final @Nonnull Uri uri,
                        final @Nullable String[] projection,
                        final @Nullable String selection,
                        final @Nullable String[] selectionArgs,
                        final @Nullable String sortOrder) {
        try {
            if (selectionArgs == null || selectionArgs.length == 0 || selectionArgs[0] == null || getContext() == null) {
                return new SubjectCursor(Collections.emptyList());
            }
            final String query = selectionArgs[0];
            final List<Subject> subjects = SearchUtil.searchSubjectSuggestions(query);
            return new SubjectCursor(subjects);
        } catch (final Exception e) {
            LOGGER.uerr(e);
            return new SubjectCursor(Collections.emptyList());
        }
    }

    @Override
    public String getType(final @Nonnull Uri uri) {
        return "vnd.android.cursor.dir/vnd.android.search.suggest";
    }

    @Override
    public @Nullable Uri insert(final @Nonnull Uri uri, final @Nullable ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(final @Nonnull Uri uri, final @Nullable String selection, final @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(final @Nonnull Uri uri, final @Nullable ContentValues values,
                      final @Nullable String selection, final @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    /**
     * Cursor implementation for search results.
     */
    static final class SubjectCursor extends AbstractCursor {
        private final List<Subject> subjects;

        /**
         * The constructor.
         *
         * @param subjects the subjects for this search result.
         */
        SubjectCursor(final List<Subject> subjects) {
            this.subjects = new ArrayList<>(subjects);
        }

        @Override
        public int getCount() {
            return subjects.size();
        }

        @Override
        public String[] getColumnNames() {
            return new String[] {
                    BaseColumns._ID,
                    SearchManager.SUGGEST_COLUMN_TEXT_1,
                    SearchManager.SUGGEST_COLUMN_TEXT_2,
                    SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID};
        }

        @Override
        public @Nullable String getString(final int column) {
            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }
            final Subject subject = subjects.get(getPosition());
            switch (column) {
                case 0:
                case 3:
                    return Long.toString(subject.getId());
                case 1:
                    return String.format("%s %s - %s",
                            subject.getSearchSuggestionType(),
                            orElse(subject.getCharacters(), orElse(subject.getSlug(), "")),
                            subject.getOneMeaning());
                case 2:
                    return subject.getMeaningRichText("").toString();
                default:
                    return null;
            }
        }

        @Override
        public @Nullable byte[] getBlob(final int column) {
            final @Nullable String value = getString(column);
            if (value == null) {
                return null;
            }
            try {
                return value.getBytes("UTF-8");
            } catch (final UnsupportedEncodingException e) {
                // Can't happen
                return null;
            }
        }

        @Override
        public int getInt(final int column) {
            return (int) getLong(column);
        }

        @Override
        public short getShort(final int column) {
            return (short) getLong(column);
        }

        @Override
        public long getLong(final int column) {
            if (isBeforeFirst() || isAfterLast()) {
                return 0;
            }
            final Subject subject = subjects.get(getPosition());
            if (column == 0 || column == 3) {
                return subject.getId();
            }
            return 0;
        }

        @Override
        public float getFloat(final int column) {
            return getLong(column);
        }

        @Override
        public double getDouble(final int column) {
            return getLong(column);
        }

        @Override
        public int getType(final int column) {
            switch (column) {
                case 0:
                case 3:
                    return FIELD_TYPE_INTEGER;
                case 1:
                case 2:
                    return FIELD_TYPE_STRING;
                default:
                    return FIELD_TYPE_NULL;
            }
        }

        @Override
        public boolean isNull(final int column) {
            if (isBeforeFirst() || isAfterLast()) {
                return true;
            }
            return getType(column) == FIELD_TYPE_NULL;
        }
    }
}