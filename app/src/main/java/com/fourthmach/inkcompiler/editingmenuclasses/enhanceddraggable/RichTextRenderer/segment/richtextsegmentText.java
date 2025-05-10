package com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.RichTextRenderer.segment;

import static com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.helperFunctions.helperFunctions.getStaticLayoutWithTextView;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.RichTextRenderer.RichTextRenderer;

import java.util.ArrayList;

public class richtextsegmentText extends richtextsegment{


    @Override
    public void continueFrom(RichTextRenderer.CurrentRenderState renderState) {
        richtextsegment lastSegment = renderState.lastSegmentInfo;
        Context context = renderState.context;
        FrameLayout container = renderState.container;

        // there is no way that it the last segment is of a Text type bcuz o

        int containerWidth = container.getWidth(); // total width
        if (lastSegment instanceof richtextsegmentImage) {
            richtextsegmentImage imageSegment = (richtextsegmentImage) lastSegment;

            float remainingSpace = containerWidth - renderState.totalRowWidth;

            TextView toUse_TextView = new TextView(context);


            StaticLayout narrowedLayout = getStaticLayoutWithTextView(built, toUse_TextView, (int) remainingSpace);
            int narrowed_firstLine_start = narrowedLayout.getLineStart(0);
            int narrowed_firstline_end = narrowedLayout.getLineEnd(0);
            boolean fillInGap = false;
            {
                CharSequence text = narrowedLayout.getText();

                for (int i = narrowed_firstLine_start; i < narrowed_firstline_end; i++) {
                    char c = text.charAt(i);
                    if (!Character.isWhitespace(c)) {
                        fillInGap = true;
                        break;
                    }
                }
            }


            SpannableStringBuilder toUse_spannable = built;
            StaticLayout toUse_Layout = getStaticLayoutWithTextView(built, toUse_TextView, (int) containerWidth);

            if (fillInGap) { // create the narrowed textview; most of the cases
                SpannableStringBuilder narrowedLine = new SpannableStringBuilder(
                        built.subSequence(
                                narrowed_firstLine_start, narrowed_firstline_end
                        ));
                // if true, cut off at the end of the narrow thing, then create a new text view, then continue on from there

                float narrowedViewWidth = narrowedLayout.getLineWidth(0);
                int narrowedViewHeight = narrowedLayout.getLineTop(0) - narrowedLayout.getLineBottom(0);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        (int) narrowedViewWidth, // width in pixels (manual)
                        narrowedViewHeight
                );
                toUse_TextView.setLayoutParams(params);
                toUse_TextView.setText(narrowedLine);
                toUse_TextView.setX(renderState.totalRowWidth);

                renderState.lastView = toUse_TextView;
                renderState.addViewToLastRow(toUse_TextView);
                {
                    float totalHeightWithoutLastRow = renderState.totalHeight - renderState.lastRowHeight;
                    renderState.lastRowHeight = (narrowedViewHeight > renderState.lastRowHeight) ? narrowedViewHeight : renderState.lastRowHeight;
                    renderState.totalHeight = totalHeightWithoutLastRow + renderState.lastRowHeight;
                }

                // look at the query in the description at the ( (narrowedLayout.getLineCount() < 2) )
                renderState.lastRowViewWidth = narrowedViewWidth;
                renderState.totalRowWidth = renderState.totalRowWidth + narrowedViewWidth;

                renderState.addViewToLastRow(toUse_TextView);
                container.addView(toUse_TextView);


                Log.d("RichTextRenderer", String.format(
                        "Container Width = %d | Line Count = %d | first line raw = %s | narrowed ( first ) line width = %.2f",
                        containerWidth, narrowedLayout.getLineCount(), narrowedLine.toString(), narrowedViewWidth
                ));

                if (narrowedLayout.getLineCount() < 2) /*
                    this text segment only has a singular line of text
                    and it is after a (non-text segment)
                    the thingy after is also a (non-text segment)
                    inbetween the border of the container and the last view

                    the next segment will also likely check to see if it can fit into the current row

                    so this accounts for both: if the textview reaches the border, or doesn't ( ~as the next segment will handle it)

                    so no need to use renderState.CloseCurrentLastRow() here
                     */ return;


                // ~and so if it reaches here, then that means that there's still some stuff
                toUse_TextView = new TextView(context);

                { // split after the narrow part
                    SpannableStringBuilder lastLine = new SpannableStringBuilder(
                            built.subSequence(
                                    narrowed_firstline_end, built.length()
                            ));
                    toUse_spannable = lastLine;
                    toUse_Layout = getStaticLayoutWithTextView(lastLine, toUse_TextView, containerWidth);
                }
            }

            renderState.CloseCurrentLastRow(); // a new row is created

            renderState.lastView = toUse_TextView;

            renderState.lastRowViewWidth = // (row has one line) use its calculated width (otherwise) use the container width
                    (toUse_Layout.getLineCount() < 2) ? toUse_Layout.getWidth() : containerWidth;
            renderState.totalRowWidth = renderState.lastRowViewWidth;

            float height = toUse_Layout.getHeight();
            renderState.lastRowHeight = height;
            renderState.totalHeight = renderState.totalHeight + renderState.lastRowHeight;

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    (int) renderState.lastRowViewWidth, // width in pixels (manual)
                    (int) height
            );
            toUse_TextView.setLayoutParams(params);

            toUse_TextView.setText(toUse_spannable);

            renderState.addViewToLastRow(toUse_TextView);
            container.addView(toUse_TextView);
        }
    }


    public SpannableStringBuilder built;
    public richtextsegmentText(Context context, ArrayList<RichTextRenderer.textStyleSave> segments) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        for (RichTextRenderer.textStyleSave segment : segments) {
            int start = builder.length();
            builder.append(segment.text);
            int end = builder.length();

            if (segment.bold && segment.italic) {
                builder.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (segment.bold) {
                builder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (segment.italic) {
                builder.setSpan(new StyleSpan(Typeface.ITALIC), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (segment.underline) {
                builder.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            // Set text size (converted from sp to px)
            int trueStart = start;
            int trueEnd = end;


            while (trueStart < end && Character.isWhitespace(builder.charAt(trueStart))) {
                trueStart++;
            }
            while (trueEnd > start && Character.isWhitespace(builder.charAt(trueEnd - 1))) {
                trueEnd--;
            }
            if (trueStart > trueEnd) continue;

            float scaledSizeInPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP,
                    segment.textSize,
                    context.getResources().getDisplayMetrics()
            );
            builder.setSpan(new AbsoluteSizeSpan((int) scaledSizeInPx), trueStart, trueEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        Log.d("RichTextRenderer", "Built segment | to string'd = " + builder.toString());
        built = builder;
    }}
