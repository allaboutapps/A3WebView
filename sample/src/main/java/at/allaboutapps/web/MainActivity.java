package at.allaboutapps.web;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import at.allaboutapps.web.webview.A3WebFragment;
import at.allaboutapps.web.webview.WebViewSettings;

public class MainActivity extends AppCompatActivity
    implements BottomNavigationView.OnNavigationItemSelectedListener {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);

    if (savedInstanceState == null) {
      replaceContentWithFragment(createHome());
    }

    final BottomNavigationView bottomNavigation = (BottomNavigationView) findViewById(R.id.menu);
    bottomNavigation.setOnNavigationItemSelectedListener(this);
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    Fragment fragment;
    if (item.getItemId() == R.id.home) {
      fragment = createHome();
    } else {
      fragment = createImprint();
    }
    replaceContentWithFragment(fragment);
    return true;
  }

  private void replaceContentWithFragment(Fragment fragment) {
    getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
  }

  @NonNull
  private Fragment createHome() {
    return HomeFragment.newInstance();
  }

  private A3WebFragment createImprint() {
    WebViewSettings settings =
        WebViewSettings.loadAssetFile("imprint.html")
            .disableLoadingIndicator()
            .openLinksExternally();

    return A3WebFragment.newInstance(settings);
  }
}
