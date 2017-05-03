package at.allaboutapps.web.webview;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.webkit.WebView;

/**
 * Settings for {@link A3WebView} that define what and how gets loaded, as well as error and loading
 * states.
 *
 * @see A3WebView
 */
@SuppressWarnings("WeakerAccess")
public class WebViewSettings implements Parcelable {

  public static final Creator<WebViewSettings> CREATOR =
      new Creator<WebViewSettings>() {
        @Override
        public WebViewSettings createFromParcel(Parcel source) {
          return new WebViewSettings(source);
        }

        @Override
        public WebViewSettings[] newArray(int size) {
          return new WebViewSettings[size];
        }
      };
  public static final String PATH_TO_ASSETS = "file:///android_asset/";
  private static final String MIME_TEXT_HTML = "text/html";
  private static final String CHARSET_UTF_8 = "UTF-8";
  /*package*/ final WebViewLoadingMethod loadingMethod;

  /*package*/ boolean showLoading = true;
  /*package*/ boolean javaScriptEnabled = false;
  /*package*/ boolean openLinksExternally = false;
  /*package*/ int errorLayout = R.layout.a3_webview_error_layout;

  private WebViewSettings(@NonNull WebViewLoadingMethod urlLoadingMethod) {
    loadingMethod = urlLoadingMethod;
  }

  protected WebViewSettings(Parcel in) {
    this.showLoading = in.readByte() != 0;
    this.javaScriptEnabled = in.readByte() != 0;
    this.openLinksExternally = in.readByte() != 0;
    this.errorLayout = in.readInt();
    this.loadingMethod = in.readParcelable(WebViewLoadingMethod.class.getClassLoader());
  }

  /**
   * Load a url into the {@code WebView}.
   *
   * @param url the url including its http or https prefix.
   * @return the settings
   */
  public static WebViewSettings loadUrl(String url) {
    return new WebViewSettings(new UrlWebViewLoadingMethod(url));
  }

  /**
   * Load a file from assets into the {@code WebView}. If the file is at {@code assets/index.html}
   * you would call this method with {@code "index.html"}
   *
   * @param assetFilePath the file path <i>within</i> the assets folder. A file in {@code
   *     assets/index.html} would be {@code "index.html"}
   * @return the settings
   */
  public static WebViewSettings loadAssetFile(String assetFilePath) {
    return loadUrl(PATH_TO_ASSETS + assetFilePath);
  }

  /**
   * Display HTML data within the {@code WebView}.
   *
   * @param data Encoded in UTF-8 of type text/html. If you need different settings please use
   *     {@link #using(WebViewLoadingMethod)} with a custom {@link DataWebViewLoadingMethod}.
   * @return the settings
   * @see DataWebViewLoadingMethod
   */
  public static WebViewSettings loadData(@NonNull String data) {
    return new WebViewSettings(new DataWebViewLoadingMethod(data, MIME_TEXT_HTML, CHARSET_UTF_8));
  }

  /**
   * Display HTML data within the {@code WebView}.
   *
   * @param data Encoded in UTF-8 of type text/html. If you need different settings please use
   *     {@link #using(WebViewLoadingMethod)} with a custom {@link DataWebViewLoadingMethod}.
   * @param baseUrl the baseUrl to use along with {@link WebView#loadDataWithBaseURL(String, String,
   *     String, String, String)}
   * @return the settings
   * @see DataWebViewLoadingMethod
   */
  public static WebViewSettings loadData(@NonNull String data, String baseUrl) {
    return new WebViewSettings(
        new DataWebViewLoadingMethod(data, MIME_TEXT_HTML, CHARSET_UTF_8, baseUrl));
  }

  /**
   * Provide a custom implementation of {@link WebViewLoadingMethod}
   *
   * @param loadingMethod the loading method you want to use
   * @return the settings
   * @see WebViewLoadingMethod
   */
  public static WebViewSettings using(@NonNull WebViewLoadingMethod loadingMethod) {
    return new WebViewSettings(loadingMethod);
  }

  /**
   * Disable the default indeterminate progress view.
   *
   * @return the settings
   */
  public WebViewSettings disableLoadingIndicator() {
    showLoading = false;
    return this;
  }

  /**
   * Enables javascript.
   *
   * @return the settings
   */
  public WebViewSettings enableJavaScript() {
    javaScriptEnabled = true;
    return this;
  }

  /**
   * When set, all links clicked will open in the browser. This is useful if you don't want to
   * handle outbound links.
   *
   * @return the settings
   */
  public WebViewSettings openLinksExternally() {
    openLinksExternally = true;
    return this;
  }

  /**
   * Set a custom error layout. The layout must contain a clickable view with {@link
   * at.allaboutapps.utilities.R.id#action_reload}
   *
   * @param layoutResId the layout including a view with id {@link
   *     at.allaboutapps.utilities.R.id#action_reload}
   * @return the settings
   */
  public WebViewSettings setErrorLayoutId(@LayoutRes int layoutResId) {
    errorLayout = layoutResId;
    return this;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte(this.showLoading ? (byte) 1 : (byte) 0);
    dest.writeByte(this.javaScriptEnabled ? (byte) 1 : (byte) 0);
    dest.writeByte(this.openLinksExternally ? (byte) 1 : (byte) 0);
    dest.writeInt(this.errorLayout);
    dest.writeParcelable(this.loadingMethod, flags);
  }

  /**
   * Handler interface for the content loading.
   *
   * @see DataWebViewLoadingMethod
   */
  public interface WebViewLoadingMethod extends Parcelable {

    /**
     * Load the content into the {@code webView}
     *
     * @param webView the loading target
     */
    void startLoading(WebView webView);
  }
}
