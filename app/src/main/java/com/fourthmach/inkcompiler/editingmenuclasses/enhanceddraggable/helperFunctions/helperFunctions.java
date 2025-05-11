package com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.helperFunctions;

import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.widget.TextView;

public class helperFunctions {

    public static StaticLayout getStaticLayoutWithTextView(SpannableStringBuilder spannable, TextView text, int containerWidth) {

        StaticLayout staticLayout = StaticLayout.Builder.obtain(
                        spannable,
                        0,
                        spannable.length(),
                        text.getPaint(),
                        containerWidth
                )
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(
                        text.getLineSpacingExtra(),
                        text.getLineSpacingMultiplier()
                )
                .setIncludePad(text.getIncludeFontPadding())
                .build();

        return staticLayout;
    }
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

}
