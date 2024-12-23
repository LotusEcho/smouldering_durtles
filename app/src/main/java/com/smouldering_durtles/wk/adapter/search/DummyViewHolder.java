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

package com.smouldering_durtles.wk.adapter.search;

import android.view.View;

/**
 * Dummy view holder that should never be instantiated.
 */
public final class DummyViewHolder extends ResultItemViewHolder {
    /**
     * The view for this holder, inflated but not yet bound.
     *
     * @param adapter the adapter this holder was created for
     * @param view the view
     */
    public DummyViewHolder(final SearchResultAdapter adapter, final View view) {
        super(adapter, view);
    }

    @Override
    public void bind(final ResultItem newItem) {
        //
    }
}
