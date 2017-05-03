package at.allaboutapps.web.webview;

import android.os.Parcel;
import android.os.Parcelable;
import android.webkit.WebView;

/** Loads an url into the WebView. */
public class UrlWebViewLoadingMethod implements WebViewSettings.WebViewLoadingMethod, Parcelable {
  public static final Parcelable.Creator<UrlWebViewLoadingMethod> CREATOR =
      new Parcelable.Creator<UrlWebViewLoadingMethod>() {
        @Override
        public UrlWebViewLoadingMethod createFromParcel(Parcel source) {
          return new UrlWebViewLoadingMethod(source);
        }

        @Override
        public UrlWebViewLoadingMethod[] newArray(int size) {
          return new UrlWebViewLoadingMethod[size];
        }
      };

  private String mUrl;

  public UrlWebViewLoadingMethod(String url) {
    mUrl = url;
  }

  protected UrlWebViewLoadingMethod(Parcel in) {
    this.mUrl = in.readString();
  }

  @Override
  public void startLoading(WebView webView) {
    webView.loadUrl(mUrl);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.mUrl);
  }
}
