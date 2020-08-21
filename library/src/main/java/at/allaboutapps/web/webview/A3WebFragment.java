package at.allaboutapps.web.webview;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/** Easy display of {@link WebViewSettings} in a standalone Fragment. */
public class A3WebFragment extends Fragment {

  private static final String ARG_SETTINGS = "arg_settings";
  private A3WebView mWebView;

  /**
   * Create a fragment with an {@link A3WebView} to just display some content.
   *
   * @param settings the settings for the {@link A3WebView}
   * @return the fragment
   * @see A3WebView
   */
  public static A3WebFragment newInstance(WebViewSettings settings) {
    A3WebFragment fragment = new A3WebFragment();
    Bundle args = new Bundle();
    args.putParcelable(ARG_SETTINGS, settings);
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.a3_webview_fragment, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mWebView = (A3WebView) view.findViewById(R.id.web_view);

    final WebViewSettings webViewSettings = loadSettings();
    mWebView.loadWithSettings(webViewSettings);
  }

  @NonNull
  private WebViewSettings loadSettings() {
    final WebViewSettings settings = getArguments().getParcelable(ARG_SETTINGS);
    if (settings == null) {
      throw new NullPointerException("settings not defined. Use the newInstance method!");
    }
    return settings;
  }

  @SuppressWarnings("unused")
  public boolean onBackPressed() {
    if (mWebView.canGoBack()) {
      mWebView.goBack();
      return true;
    }
    return false;
  }
}
