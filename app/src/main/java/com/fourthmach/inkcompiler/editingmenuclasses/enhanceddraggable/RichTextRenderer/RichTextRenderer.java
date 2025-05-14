package com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.RichTextRenderer;

import static com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.helperFunctions.helperFunctions.getStaticLayoutWithTextView;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fourthmach.inkcompiler.SaveFileSystem.EditingSaveFile;
import com.fourthmach.inkcompiler.editingmenuclasses.EFIActivityInfo;
import com.fourthmach.inkcompiler.editingmenuclasses.HelperForDraggableContainer;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.EnhancedDraggableLayout.EnhancedDraggableLayout;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.RichTextRenderer.segment.richtextsegment;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.RichTextRenderer.segment.richtextsegmentImage;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.RichTextRenderer.segment.richtextsegmentText;

import java.util.ArrayList;
import java.util.Objects;

public class RichTextRenderer extends FrameLayout {

    public RichTextRenderer(Context context) {
        super(context);
    }

    public RichTextRenderer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RichTextRenderer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    public defaultThing tobemadeinanotherscript_basicallySettngs;
    private class defaultThing {
        boolean bold = false;
        boolean italic = false;
        boolean underline = false;
        float textSize = 16f;
    }

    private EFIActivityInfo efiActivityInfo;
    private EnhancedDraggableLayout enhancedDraggableBox;
    public void actualStart(EFIActivityInfo efiActivityInfo, EnhancedDraggableLayout enhancedDraggableBox) {
        this.efiActivityInfo = efiActivityInfo;

        tobemadeinanotherscript_basicallySettngs = new defaultThing();
        this.enhancedDraggableBox = enhancedDraggableBox;
    }


    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()
        );
    }




    public class textStyleSave {
        public boolean bold = false;
        public boolean italic = false;
        public boolean underline = false;
        public float textSize = 16f;

        public String text;
        public textStyleSave(TextStyleTally fromTally, String text) {
            this.text = text;
            this.bold = fromTally.bold != 0;
            this.italic = fromTally.italic != 0;
            this.underline = fromTally.underline != 0;
            this.textSize = fromTally.textSize.get(fromTally.textSize.size() - 1);
        }
    }
    private class TextStyleTally {
        //
        int bold;
        int italic;
        int underline;
        ArrayList<Float> textSize = new ArrayList<>();
        public TextStyleTally() {
            EditingSaveFile.NoteSettings noteSettings = efiActivityInfo.editingSaveFile.noteSettings;
            bold = noteSettings.bold ? 1 : 0;
            italic = noteSettings.italic ? 1 : 0;
            underline = noteSettings.underline ? 1 : 0;

            textSize.add(noteSettings.textSize); // default size

        }
    }



    private String lastText = "";
    public void reload() {
        renderText(lastText);
    }
    public void renderText(String input) {
        lastText = input;

        TextStyleTally currentStyle = new TextStyleTally();       // starts with default size = 16
        StringBuilder stringBuilder = new StringBuilder();

        ArrayList<textStyleSave> textsegments = new ArrayList<textStyleSave>();
        ArrayList<richtextsegment> buildingRenderSegments = new ArrayList<richtextsegment>();


        Runnable FlushBuffer = () -> { // each format | i.e. THIS, bold, __undrline__,
            if (stringBuilder.length() > 0) {
                textsegments.add(new textStyleSave(currentStyle, stringBuilder.toString()));
                stringBuilder.setLength(0);
            }
        };
        Runnable BuildFullText = () -> { // to build the whole sentence
            if (textsegments.isEmpty()) return;

            FlushBuffer.run();
            buildingRenderSegments.add(new richtextsegmentText(efiActivityInfo.activity, textsegments));
            textsegments.clear();
        };

        for (int i = 0; i < input.length(); ) {
            char c = input.charAt(i);

            // 1) ESCAPE logic: “\X” => show X literally
            if (c == '\\' && i + 1 < input.length()) {
                stringBuilder.append(input.charAt(i + 1));
                i += 2;
                continue;
            }

            // 2) plain character
            if (c != '<') {
                stringBuilder.append(c);
                i++;
                continue;
            }


            // flush buffer before handling tag
            FlushBuffer.run();

            // 2) TAG logic: look for <…>
            int close = input.indexOf('>', i);
            if (close == -1) { // malformed
                stringBuilder.append("< | MALFORMED TAG DETECTED! | PlEASE PUT VALID CLOSING ANGLE BRACKETS i.e. \">\"");
                break;
            };

            // get content inside < … >

            boolean isClosingTag;
            String[] tagArguments;
            {
                String insideTag = input.substring(i + 1, close).trim();
                isClosingTag = insideTag.startsWith("/");
                String tagContent = isClosingTag ? insideTag.substring(1) : insideTag;
                tagArguments = tagContent.split(";");
            }

            int tagArgumentsLength = tagArguments.length;

            i = close + 1; // Move past the tag
            if (tagArgumentsLength == 0) continue;

            String tagName = tagArguments[0];

            // 3a) Handle custom styles (size, bold, etc.)
            if (isClosingTag) {
                switch (tagName) {
                    case "size":
                        ArrayList<Float> textSize = currentStyle.textSize;
                        int length = textSize.size();
                        /*
                        Log.d("RichTextRenderer", String.format(
                                "In-process | closing tag for size, handle = %d",
                                length
                        ));
                        */
                        if (length == 1) break;
                        textSize.remove(length - 1);
                        break;
                    case "b":
                        currentStyle.bold--;
                        break;
                    case "u":
                        currentStyle.underline--;
                        break;
                    case "i":
                        currentStyle.italic--;
                        break;
                }
            } else {

                // 3b) Handle opening tags
                switch (tagName) {
                    case "b":
                        currentStyle.bold++;
                        break;
                    case "i":
                        currentStyle.italic++;
                        break;
                    case "u":
                        currentStyle.underline++;
                        break;
                    case "size":
                        if (tagArgumentsLength < 2) break;
                        currentStyle.textSize.add(parseFloatOrDefault(tagArguments[1], 15f));
                        break;
                    // Handle other custom tags
                    case "image":
                        BuildFullText.run();

                        float one = 1f;
                        float zero = 0f;


                        String ImageID = tagArgumentsLength > 1 ? tagArguments[1] : "PlaceHolderImage";

                        float f1 = tagArgumentsLength > 2 ? parseFloatOrDefault(tagArguments[2], one) : one;
                        float f2 = tagArgumentsLength > 3 ? parseFloatOrDefault(tagArguments[3], one) : one;
                        float f3 = tagArgumentsLength > 4 ? parseFloatOrDefault(tagArguments[4], one) : one;
                        float f4 = tagArgumentsLength > 5 ? parseFloatOrDefault(tagArguments[5], one) : one;
                        float f5 = tagArgumentsLength > 6 ? parseFloatOrDefault(tagArguments[6], zero) : zero;
                        float f6 = tagArgumentsLength > 7 ? parseFloatOrDefault(tagArguments[7], zero) : zero;

                        buildingRenderSegments.add(new richtextsegmentImage(efiActivityInfo.activity, ImageID, f1, f2, f3, f4, f5, f6));
                        break;
                }
            }
        }

        // flush any remaining text
        FlushBuffer.run(); /*
            if BuildFullText was ran and the (to render text) didn't have a starting tag "<"
            FlushBuffer would never be called, and therefore textsegments would be empty

            and then BuildFullText would not work
        */

        BuildFullText.run();

        renderSegments = buildingRenderSegments;
        UpdateRender();
    }

    private ArrayList<richtextsegment> renderSegments;

    public class CurrentRenderState {
        public Context context;
        public FrameLayout container;


        public View lastView;
        public richtextsegment lastSegmentInfo;


        public float lastRowHeight; // refers to the largest View among the Views in the last row
        public float totalHeight;
        public float lastRowViewWidth = 0;
        public float totalRowWidth = 0;
        private ArrayList<View> viewsInLastRow = new ArrayList<View>();
        public void CloseCurrentLastRow() {
            lastRowViewWidth = 0;
            totalRowWidth = 0;

            float saveTotalHeight = totalHeight;
            ArrayList<View> copy = new ArrayList<>(viewsInLastRow);
            container.post(() -> {
                int i = 0;
                for (View v : copy) {
                    i++;


                    float yPos = saveTotalHeight - v.getHeight();
                    v.setY(yPos);
                    Log.d("RichTextRenderer", String.format(
                            "item: %d | get height from this view = %d | y Position = %.2f",
                            i, v.getHeight(), yPos
                    ));
                }
            });

            Log.d("RichTextRenderer",
                    String.format("Cleared Row! | changed = %d | lastRowHeight = %.2f \n saveTotalHeight = %.2f",
                            viewsInLastRow.size(), lastRowHeight,
                            saveTotalHeight
                    ));
            viewsInLastRow.clear();
        }
        public void addViewToLastRow(View v) {
            viewsInLastRow.add(v);
        }
    }
    public void UpdateRender() {

        removeAllViews();

        int currentSize = renderSegments.size();
        if (currentSize == 0) return;


        FrameLayout container = this;

        CurrentRenderState renderState = new CurrentRenderState();
        renderState.container = this;
        renderState.context = efiActivityInfo.activity;

        { // initial for renderState
            richtextsegment segment = renderSegments.get(0);
            if (segment instanceof richtextsegmentText) {
                richtextsegmentText textSegment = (richtextsegmentText) segment;

                TextView tv = new TextView(efiActivityInfo.activity);


                SpannableStringBuilder spannable = textSegment.built;
                tv.setText(spannable);

                int containerWidth = getWidth();
                StaticLayout staticLayout = getStaticLayoutWithTextView(spannable, tv, containerWidth);
                float totalHeight = staticLayout.getHeight();
                {
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            containerWidth, // width in pixels (manual)
                            (int) totalHeight  // height stays dynamic
                    );
                    tv.setLayoutParams(params);
                }
                container.addView(tv);
                Log.d("RichTextRenderer", "before-post initial textview size y " + tv.getHeight() );

                tv.post(() -> Log.d("RichTextRenderer", "initial textview size y " + tv.getHeight() ));

                renderState.lastSegmentInfo = segment;
                renderState.lastView = tv;
                renderState.lastRowHeight = totalHeight;
                renderState.totalHeight = totalHeight;
                float totalWidth = (staticLayout.getLineCount() < 2) ? staticLayout.getLineWidth(0) : staticLayout.getWidth();
                renderState.lastRowViewWidth = totalWidth;
                renderState.totalRowWidth = totalWidth;
                Log.d("RichTextRenderer", String.format(
                        "totalHeight = %.2f | width of container = %d",
                        totalHeight, this.getWidth()
                        ));

                renderState.addViewToLastRow(tv);

            } else if (segment instanceof richtextsegmentImage) {
                richtextsegmentImage imageSegment = (richtextsegmentImage) segment;
                Log.d("RichTextRenderer", "bluh you haven't done a thang for if the user does an image first");
            }
        }

        Log.d("RichTextRenderer", "----------- initializing segments.... ( first segment not included )");

        // main loop after the first index/initial
        for (int i = 1; i < currentSize; i++) {
            richtextsegment segment = renderSegments.get(i);
            segment.continueFrom(renderState);
            renderState.lastSegmentInfo = segment;
            Log.d("RichTextRenderer", "----------- ^^^ segment number : " + i + "\n");
        }

        Log.d("RichTextRenderer", String.format(
                "Finished doing the segments! | count = %d",
                currentSize
        ));
        // finish up entirely
        renderState.CloseCurrentLastRow();


        {
            int newHeight = (int) renderState.totalHeight;
            newHeight = lastText.equals("") ? 50 : newHeight;

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, // match parent width
                    newHeight                            // manually set height in px
            );

            this.setLayoutParams(params);
            enhancedDraggableBox.updateHeight(newHeight);
        }
    }

    public float parseFloatOrDefault(String input, float defaultValue) {
        try {
            return Float.parseFloat(input);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
