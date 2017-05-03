package at.allaboutapps.web;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.allaboutapps.web.webview.A3WebActivity;
import at.allaboutapps.web.webview.A3WebView;
import at.allaboutapps.web.webview.WebViewSettings;

public class HomeFragment extends Fragment {

  public static Fragment newInstance() {
    return new HomeFragment();
  }

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_home, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    view.findViewById(R.id.launch_website)
        .setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                final WebViewSettings settings =
                    WebViewSettings.loadUrl("https://allaboutapps.at").enableJavaScript();

                final Intent intent =
                    A3WebActivity.with(getContext(), settings)
                        .enableHomeAsUp()
                        .setTitle("aaa - all about apps")
                        .build();

                startActivity(intent);
              }
            });

    final A3WebView webView = (A3WebView) view.findViewById(R.id.web_view);
    WebViewSettings settings =
        WebViewSettings.loadData(
            "<center>Displaying some <b>inline content</b> in a WebView</center>");
    webView.loadWithSettings(settings);
  }
}
