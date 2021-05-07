package at.allaboutapps.web.webview;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import android.webkit.WebView;

/** Loads a string into the {@code WebView}. e.g. if the server sent HTML to display within json. */
class DataWebViewLoadingMethod implements WebViewSettings.WebViewLoadingMethod, Parcelable {
  public static final Creator<DataWebViewLoadingMethod> CREATOR =
      new Creator<DataWebViewLoadingMethod>() {
        @Override
        public DataWebViewLoadingMethod createFromParcel(Parcel source) {
          return new DataWebViewLoadingMethod(source);
        }

        @Override
        public DataWebViewLoadingMethod[] newArray(int size) {
          return new DataWebViewLoadingMethod[size];
        }
      };

  private final String mData;
  private final String mMimeType;
  private final String mEncoding;
  @Nullable
  private final String mBaseUrl;

  public DataWebViewLoadingMethod(String data, String mimeType, String encoding) {
    this(data, mimeType, encoding, null);
  }

  public DataWebViewLoadingMethod(
      String data, String mimeType, String encoding, @Nullable String baseUrl) {
    mBaseUrl = baseUrl;
    mData = data;
    mMimeType = mimeType;
    mEncoding = encoding;
  }

  protected DataWebViewLoadingMethod(Parcel in) {
    this.mBaseUrl = in.readString();
    this.mData = in.readString();
    this.mMimeType = in.readString();
    this.mEncoding = in.readString();
  }

  @Override
  public void startLoading(WebView webView) {
    if (mBaseUrl == null) {
      webView.loadData(mData, mMimeType, mEncoding);
    } else {
      webView.loadDataWithBaseURL(mBaseUrl, mData, mMimeType, mEncoding, null);
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.mBaseUrl);
    dest.writeString(this.mData);
    dest.writeString(this.mMimeType);
    dest.writeString(this.mEncoding);
  }
}
