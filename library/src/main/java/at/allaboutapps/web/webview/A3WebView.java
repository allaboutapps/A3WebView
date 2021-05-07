package at.allaboutapps.web.webview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.MailTo;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

/**
 * Wrapper around WebView that accepts settings to enable simple loading of content.
 *
 * @see WebViewSettings
 */
public class A3WebView extends FrameLayout {
  private ProgressBar mProgress;
  private WebView mWebView;
  private View mError;

  private WebViewSettings mSettings;

  public A3WebView(@NonNull Context context) {
    super(context);
    initialize(context);
  }

  public A3WebView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    initialize(context);
  }

  public A3WebView(
      @NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize(context);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public A3WebView(
      @NonNull Context context,
      @Nullable AttributeSet attrs,
      @AttrRes int defStyleAttr,
      @StyleRes int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initialize(context);
  }

  private void initialize(Context context) {
    mProgress = new ProgressBar(context);
    mProgress.setIndeterminate(true);
    final LayoutParams progressLayoutParams =
        new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    progressLayoutParams.gravity = Gravity.CENTER;
    mProgress.setLayoutParams(progressLayoutParams);
    addView(mProgress);

    mWebView = new WebView(context);
    mWebView.setLayoutParams(
        new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    addView(mWebView);
  }

  public void loadWithSettings(WebViewSettings settings) {
    mSettings = settings;

    if (mError != null) {
      removeView(mError);
      mError = null;
    }

    mWebView.getSettings();

    if (settings.showLoading) {
      showProgress();
    } else {
      showWebView();
    }

    final WebSettings webSettings = mWebView.getSettings();
    webSettings.setJavaScriptEnabled(settings.javaScriptEnabled);

    webSettings.setBuiltInZoomControls(false);
    webSettings.setSupportZoom(false);
    webSettings.setNeedInitialFocus(false);     // prevent scrolling to first focus by not focusing
    webSettings.setDisplayZoomControls(false);

    mWebView.setWebViewClient(new WebViewClient(settings));
    startLoadingContent(settings);
    settings.loadingMethod.startLoading(mWebView);
  }

  private void showProgress() {
    mWebView.setVisibility(GONE);
    mProgress.setVisibility(VISIBLE);
    if (mError != null) {
      mError.setVisibility(GONE);
    }
  }

  private void startLoadingContent(WebViewSettings settings) {
    mWebView.requestFocusFromTouch();
    settings.loadingMethod.startLoading(mWebView);
  }

  public boolean canGoBack() {
    return mWebView.canGoBack();
  }

  public void goBack() {
    mWebView.goBack();
  }

  private void showWebView() {
    mWebView.setVisibility(VISIBLE);
    mWebView.requestFocusFromTouch();
    mProgress.setVisibility(GONE);
    if (mError != null) {
      mError.setVisibility(GONE);
    }
  }

  private void showError(final String failedUrl) {
    mWebView.setVisibility(GONE);
    mProgress.setVisibility(GONE);
    if (mError != null) {
      mError.setVisibility(VISIBLE);
    } else {
      mError = LayoutInflater.from(getContext()).inflate(mSettings.errorLayout, this, false);
      mError
          .findViewById(R.id.action_reload)
          .setOnClickListener(
              new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  mWebView.loadUrl(failedUrl);
                  if (mSettings.showLoading) {
                    showProgress();
                  } else {
                    showWebView();
                  }
                }
              });
      addView(mError);
    }
  }

  private class WebViewClient extends android.webkit.WebViewClient {

    private static final String PREFIX_HTTP = "http:";
    private static final String PREFIX_HTTPS = "https:";
    private static final String PREFIX_TEL = "tel:";
    private static final String PREFIX_MAIL_TO = "mailto:";

    private final WebViewSettings mSettings;
    private String mFailedUrl;

    public WebViewClient(WebViewSettings settings) {
      mSettings = settings;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      if (isHttpUrl(url)) {
        if (mSettings.openLinksExternally) {
          Intent urlIntent = newUrlIntent(url);
          view.getContext().startActivity(Intent.createChooser(urlIntent, ""));
          return true;
        } else {
          return false;
        }
      } else if (isPhoneNumberUrl(url)) {
        Intent telIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
        view.getContext().startActivity(Intent.createChooser(telIntent, ""));
        return true;
      } else if (isMailToUrl(url)) {
        MailTo mt = MailTo.parse(url);
        Intent mailToIntent = newEmailIntent(mt.getTo(), mt.getSubject(), mt.getBody(), mt.getCc());
        view.getContext().startActivity(Intent.createChooser(mailToIntent, ""));
        return true;
      }

      return false;
    }

    public Intent newEmailIntent(String address, String subject, String body, String cc) {
      Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", address, null));
      intent.putExtra(Intent.EXTRA_EMAIL, address);
      intent.putExtra(Intent.EXTRA_TEXT, body);
      intent.putExtra(Intent.EXTRA_SUBJECT, subject);
      intent.putExtra(Intent.EXTRA_CC, cc);
      return intent;
    }

    private Intent newUrlIntent(String url) {
      Intent i = new Intent(Intent.ACTION_VIEW);
      i.setData(Uri.parse(url));
      return i;
    }

    private boolean isMailToUrl(String url) {
      return url.startsWith(PREFIX_MAIL_TO);
    }

    private boolean isPhoneNumberUrl(String url) {
      return url.startsWith(PREFIX_TEL);
    }

    private boolean isHttpUrl(String url) {
      return url.startsWith(PREFIX_HTTP) || url.startsWith(PREFIX_HTTPS);
    }

    @Override
    public void onReceivedError(
        WebView view, int errorCode, String description, String failingUrl) {
      super.onReceivedError(view, errorCode, description, failingUrl);
      mFailedUrl = failingUrl;
      showError(mFailedUrl);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
      super.onPageFinished(view, url);
      if (mFailedUrl != null && mFailedUrl.equals(url)) {
        mFailedUrl = null;
        return;
      }
      showWebView();
    }
  }
}
