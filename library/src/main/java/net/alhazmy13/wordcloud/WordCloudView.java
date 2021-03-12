package net.alhazmy13.wordcloud;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The type Word cloud view.
 */
public class WordCloudView extends WebView {

    private JavascriptInterface jsInterface;
    private List<WordCloud> dataSet;

    private int[] colors;
    private Random random;
    private int parentHeight;
    private int parentWidth;
    private int currentLowestWeight;
    private int currentLargestWeight;
    private int maxWeight;
    private int minWeight;

    /**
     * Instantiates a new Word cloud view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public WordCloudView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.dataSet = new ArrayList<>();
        this.maxWeight = 100;
        this.minWeight = 20;
        this.colors = new int[0];
        this.random = new Random();
        this.jsInterface = new JavascriptInterface(getContext());

        init();
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        super.onSizeChanged(xNew, yNew, xOld, yOld);

        parentWidth = xNew;
        parentHeight = yNew;

        // max should never be largest than 5% of the biggest dimension.
        float biggestDimen = Math.max(parentHeight, parentWidth);
        maxWeight = (int) biggestDimen / 20;
        minWeight = (int) maxWeight / 5;
    }

    /**
     * Init.
     */
    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    void init() {
        WebSettings webSettings = getSettings();
        webSettings.setBuiltInZoomControls(false);
        webSettings.setJavaScriptEnabled(true);

        // Use HTML5 local storage to maintain app state
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setAppCacheEnabled(false);
        webSettings.setAllowFileAccess(false);

        webSettings.setLoadWithOverviewMode(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setUserAgentString("Android");

        addJavascriptInterface(jsInterface, "jsinterface");

        // add centered progress bar to view and tie appearance to load phases
        final LinearLayout container = new LinearLayout(getContext());
        ProgressBar progressBar = new ProgressBar(getContext());
        container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        container.setGravity(Gravity.CENTER);
        container.addView(progressBar);
        addView(container);
        setWebViewClient(new WebViewClient() {

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                clearCache(true);
                clearView();
                container.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                container.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getContext(), "Error:" + description, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Sets data set.
     *
     * @param dataSet the data set
     */
    public void setDataSet(List<WordCloud> dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * Notify data set changed.
     */
    public void notifyDataSetChanged() {
        post(new Runnable() {
            @Override
            public void run() {
                updateMaxMinValues();
                jsInterface.setCloudParams("", getData(), "FreeSans", parentWidth, parentHeight);
                loadUrl("file:///android_asset/wordcloud.html");
            }
        });
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public String getData() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < dataSet.size(); i++) {
            sb.append("{\"word\":\"").append(dataSet.get(i).getText());
            sb.append("\",\"size\":\"").append(scale(dataSet.get(i).getWeight()));
            sb.append("\",\"color\":\"");
            sb.append(getColor()).append("\"}");
            if (i < dataSet.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private float scale(int inputY) {
        float x = inputY - currentLowestWeight;
        float y = currentLargestWeight - currentLowestWeight;
        float percent = x / y;
        return percent * (maxWeight - minWeight) + minWeight;
    }

    /**
     * fetch and store min and max weights from data set
     */
    private void updateMaxMinValues() {
        currentLowestWeight = Integer.MAX_VALUE;
        currentLargestWeight = Integer.MIN_VALUE;
        for (WordCloud wordCloud : dataSet) {
            if (wordCloud.getWeight() < currentLowestWeight) {
                currentLowestWeight = wordCloud.getWeight();
            }
            if (wordCloud.getWeight() > currentLargestWeight) {
                currentLargestWeight = wordCloud.getWeight();
            }
        }
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    private String getColor() {
        if (colors.length == 0)
            return "0";
        return "#" + Integer.toHexString(colors[random.nextInt(colors.length - 1)]).substring(2);
    }

    /**
     * View size.
     *
     * @param width  the width
     * @param height the height
     */
    public void setSize(int width, int height) {
        this.parentWidth = width;
        this.parentHeight = height;
    }
}
