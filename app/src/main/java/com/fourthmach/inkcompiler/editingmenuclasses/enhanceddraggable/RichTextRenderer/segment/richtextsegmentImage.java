package com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.RichTextRenderer.segment;

import static com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.helperFunctions.helperFunctions.getStaticLayoutWithTextView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourthmach.inkcompiler.R;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.RichTextRenderer.RichTextRenderer;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.helperFunctions.helperFunctions;

public class richtextsegmentImage extends richtextsegment {



    private Context context;

    @Override
    public void continueFrom(RichTextRenderer.CurrentRenderState renderState) {
        richtextsegment lastSegment = renderState.lastSegmentInfo;
        Context context = renderState.context;
        FrameLayout container = renderState.container;

        int containerWidth = container.getWidth(); // total width
        if (lastSegment instanceof richtextsegmentText) {
            richtextsegmentText textSegment = (richtextsegmentText) lastSegment;
            TextView lastView = (TextView) renderState.lastView;


            SpannableStringBuilder spannable = textSegment.built;


            StaticLayout staticLayout = getStaticLayoutWithTextView(spannable, lastView, containerWidth);
            int lineCount = staticLayout.getLineCount();
            int lastLineIndex = lineCount - 1;

            float lastLineWidth = staticLayout.getLineWidth(lastLineIndex);
            // get the total row width without the TextView, then add the last line of that text view,

            ImageView image = new ImageView(context);
            int imageWidth = createdBitmap.getWidth();
            int imageHeight = createdBitmap.getHeight();
            image.setImageBitmap(createdBitmap);
            {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(imageWidth, imageHeight);
                image.setLayoutParams(params);
            }





            {
                float toUseAsFilling = (lineCount < 2) ? renderState.totalRowWidth : lastLineWidth;
                float remainingSpace = containerWidth - toUseAsFilling;

                renderState.lastView = image;
                renderState.lastRowViewWidth = imageWidth;
                if (imageWidth > remainingSpace) { // image cant fit into the row; // put as priority as it is most common
                    renderState.CloseCurrentLastRow();

                    renderState.totalHeight = renderState.totalHeight + imageHeight;
                    renderState.lastRowHeight = imageHeight;
                    renderState.totalRowWidth = imageWidth;
                    renderState.addViewToLastRow(image);

                    container.addView(image);
                    return;
                }
            }
            // image can be put along with the same last row in the textView


            float lastRowHeight = renderState.lastRowHeight;
            float totalHeightWithoutLastRow = renderState.totalHeight - lastRowHeight;

            float additionalForTotalRowWidth = imageWidth;
            /*
            if the line count is just singular:
                then that means the image can be put alongside the
                textview and that no clearing will be done;
                renderState.totalRowWidth can be sustained with imageWidth
            otherwise
                the row is cleared, totalRowWidth is set to 0
                then all you have to do is add both the lastLineWidth and imageWidth
             */


            // if there is more than one row then that means the text scales across the container
            if (lineCount > 1) { // split the textview at the part before the last row
                renderState.CloseCurrentLastRow();
                additionalForTotalRowWidth = additionalForTotalRowWidth + lastLineWidth;

                int lastLineHeight = staticLayout.getLineBottom(lastLineIndex) - staticLayout.getLineTop(lastLineIndex);
                int charPosOfLastLineStart = staticLayout.getLineStart(lastLineIndex);
                { // first half ; will be left as the second to last row in the current render state
                    SpannableStringBuilder withoutTheLastLine = new SpannableStringBuilder(
                            spannable.subSequence(0, charPosOfLastLineStart)
                    );
                    lastView.setText(withoutTheLastLine);


                    // since this is a new row: totalHeightWithoutLastRow has to have this textview's height
                    totalHeightWithoutLastRow = totalHeightWithoutLastRow + (staticLayout.getHeight() - lastLineHeight);
                }

                { // second half
                    TextView tv = new TextView(context);


                    SpannableStringBuilder lastLine = new SpannableStringBuilder(
                            spannable.subSequence(
                                    charPosOfLastLineStart, staticLayout.getLineEnd(lastLineIndex)
                            ));

                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT, // width in pixels (manual)
                            lastLineHeight
                    );
                    tv.setLayoutParams(params);

                    tv.setText(lastLine);
                    container.addView(tv);
                    renderState.addViewToLastRow(tv);
                }
            }

            renderState.lastRowHeight = (imageHeight > lastRowHeight) ? imageHeight : lastRowHeight;
            renderState.totalHeight = totalHeightWithoutLastRow + renderState.lastRowHeight;


            image.setX(renderState.totalRowWidth + (additionalForTotalRowWidth - imageWidth));
            renderState.totalRowWidth = renderState.totalRowWidth + additionalForTotalRowWidth;

            renderState.addViewToLastRow(image);
            //image.setY(currentHeightOffset); // this is done much better when either: (a new row is created) or (at the end of the render)


            container.addView(image);
        }
        else if (lastSegment instanceof richtextsegmentImage) {
            richtextsegmentImage imageSegment = (richtextsegmentImage) lastSegment;

            float remainingSpace = containerWidth - renderState.totalRowWidth;
            // get the total row width without the TextView, then add the last line of that text view,

            ImageView image = new ImageView(context);
            int imageWidth = createdBitmap.getWidth();
            int imageHeight = createdBitmap.getHeight();
            image.setImageBitmap(createdBitmap);
            {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(imageWidth, imageHeight);
                image.setLayoutParams(params);
            }

            renderState.lastView = image;
            renderState.lastRowViewWidth = imageWidth;
            if (imageWidth > remainingSpace) { // cant fit
                renderState.CloseCurrentLastRow();

                renderState.lastRowHeight = imageHeight;
                renderState.totalHeight = renderState.totalHeight + imageHeight;
                renderState.totalRowWidth = imageWidth;
                renderState.addViewToLastRow(image);

                container.addView(image);
                return;
            }


            image.setX(renderState.totalRowWidth);
            float totalHeightWithoutLastRow = renderState.totalHeight - renderState.lastRowHeight;
            renderState.lastRowHeight = (imageHeight > renderState.lastRowHeight) ? imageHeight : renderState.lastRowHeight;
            renderState.totalHeight = totalHeightWithoutLastRow + renderState.lastRowHeight;
            renderState.totalRowWidth = renderState.totalRowWidth + imageWidth;

            renderState.addViewToLastRow(image);

            container.addView(image);
        }


    }


    private Bitmap createdBitmap;



    public richtextsegmentImage(
            Context context, String ImageID,
            float sizeMultX, float sizeMultY,
            float cropPercentX, float cropPercentY,
            float cropPercentPosX, float cropPercentPosY) {
        this.context = context;



        Log.d("RichTextRenderer", String.format(
                "sizeMultX: %.2f\nsizeMultY: %.2f\ncropPercentX: %.2f\ncropPercentY: %.2f\ncropPercentPosX: %.2f\ncropPercentPosY: %.2f\n",
                sizeMultX, sizeMultY, cropPercentX, cropPercentY, cropPercentPosX, cropPercentPosY
        ));


        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.jojo);

        int bmpWidth = originalBitmap.getWidth();
        int bmpHeight = originalBitmap.getHeight();


        int cropPosX = (int) helperFunctions.clamp(bmpWidth * cropPercentPosX, 1, bmpWidth - 1);
        int cropPosY = (int) helperFunctions.clamp(bmpHeight * cropPercentPosY, 1, bmpHeight - 1);

        int cropX;
        {
            // cropPosX = 800
            // bmpWidth = 1000
            float minus = bmpWidth - cropPosX; // 1000 - 800 = 200
            cropX = (int) helperFunctions.clamp(bmpWidth * cropPercentX, 1, minus);
        }
        int cropY;
        {
            float minus = bmpHeight - cropPosY;
            cropY = (int) helperFunctions.clamp(bmpHeight * cropPercentY, 1, minus);
        }

        Bitmap croppedBitmap = Bitmap.createBitmap(originalBitmap, cropPosX, cropPosY, cropX, cropY);


        int prepostX = (int) (croppedBitmap.getWidth() * sizeMultX);
        int prepostY = (int) (croppedBitmap.getHeight() * sizeMultY);
        createdBitmap = Bitmap.createScaledBitmap(
                croppedBitmap,
                prepostX < 1 ? 1 : prepostX,
                prepostY < 1 ? 1 : prepostY,
                true
        );
    }
}
