package com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.helperFunctions;

import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class helperFunctions {

    public static String getDateRightNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatted = sdf.format(new Date());

        return formatted;
    }
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

    public static float safeStringToFloat(String target, float defaultValue) {
        if (target.isEmpty()) return defaultValue;

        float textSize;

        try { textSize = Float.parseFloat(target); }
        catch (NumberFormatException e) { textSize = defaultValue; }

        return textSize;
    }

}
