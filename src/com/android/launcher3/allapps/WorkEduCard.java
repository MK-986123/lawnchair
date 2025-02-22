/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.launcher3.allapps;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.android.launcher3.Launcher;
import com.android.launcher3.R;

import app.lawnchair.font.FontManager;
import app.lawnchair.theme.color.ColorTokens;
import app.lawnchair.theme.drawable.DrawableTokens;

/**
 * Work profile toggle switch shown at the bottom of AllApps work tab
 */
public class WorkEduCard extends FrameLayout implements View.OnClickListener,
        Animation.AnimationListener {

    private final Launcher mLauncher;
    Animation mDismissAnim;
    private int mPosition = -1;

    public WorkEduCard(Context context) {
        this(context, null, 0);
    }

    public WorkEduCard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WorkEduCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLauncher = Launcher.getLauncher(getContext());
        mDismissAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        mDismissAnim.setDuration(500);
        mDismissAnim.setAnimationListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mDismissAnim.reset();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mDismissAnim.cancel();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Button button = ViewCompat.requireViewById(this, R.id.action_btn);
        button.setOnClickListener(this);
        button.setAllCaps(false);
        FontManager.INSTANCE.get(getContext()).setCustomFont(button, R.id.font_button);

        MarginLayoutParams lp = ((MarginLayoutParams) findViewById(R.id.wrapper).getLayoutParams());
        lp.width = mLauncher.getAppsView().getActiveRecyclerView().getTabWidth();

        TextView title = ViewCompat.requireViewById(this, R.id.work_apps_paused_title);
        title.setTextColor(ColorTokens.TextColorPrimary.resolveColor(getContext()));
    }

    @Override
    public void onClick(View view) {
        startAnimation(mDismissAnim);
        mLauncher.getSharedPrefs().edit().putInt(WorkAdapterProvider.KEY_WORK_EDU_STEP, 1).apply();
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        removeCard();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    private void removeCard() {
        if (mPosition == -1) {
            if (getParent() != null) ((ViewGroup) getParent()).removeView(WorkEduCard.this);
        } else {
            AllAppsRecyclerView rv = mLauncher.getAppsView()
                    .mAH[AllAppsContainerView.AdapterHolder.WORK].recyclerView;
            rv.getApps().getAdapterItems().remove(mPosition);
            rv.getAdapter().notifyItemRemoved(mPosition);
        }
    }

    public void setPosition(int position) {
        mPosition = position;
    }

}
