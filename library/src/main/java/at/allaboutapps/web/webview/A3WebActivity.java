package at.allaboutapps.web.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

/** Easy display of {@link WebViewSettings} in a standalone Activity. */
public class A3WebActivity extends AppCompatActivity {

  private static final String EXTRA_TITLE = "extra_title";
  private static final String EXTRA_SUBTITLE = "extra_subtitle";
  private static final String EXTRA_HOME_AS_UP = "extra_home_as_up";
  private static final String EXTRA_SETTINGS = "extra_settings";

  private A3WebFragment mFragment;

  /**
   * Create an intent for an Activity with an {@link A3WebView} to just display some content.
   *
   * @param context a context
   * @param settings the settings for the {@link A3WebView}
   * @return a builder to set up this intent
   */
  public static Builder with(Context context, WebViewSettings settings) {
    return new Builder(context, settings);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (savedInstanceState == null) {
      WebViewSettings settings = getIntent().getParcelableExtra(EXTRA_SETTINGS);
      mFragment = A3WebFragment.newInstance(settings);
      getSupportFragmentManager().beginTransaction().add(android.R.id.content, mFragment).commit();
    } else {
      mFragment =
          (A3WebFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
    }

    final ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      bindActionBarExtras(actionBar);
    }
  }

  private void bindActionBarExtras(ActionBar actionBar) {
    String title = resolveStringFromExtras(EXTRA_TITLE);
    if (title != null) {
      actionBar.setTitle(title);
    }
    String subtitle = resolveStringFromExtras(EXTRA_SUBTITLE);
    if (subtitle != null) {
      actionBar.setSubtitle(subtitle);
    }

    final boolean showHomeAsUp = getIntent().getBooleanExtra(EXTRA_HOME_AS_UP, false);
    actionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
  }

  /**
   * Could be int or String.
   *
   * @param identifier the name of the value in extras
   * @return the value as String
   */
  private String resolveStringFromExtras(String identifier) {
    final Object title = getIntent().getExtras().get(identifier);
    if (title instanceof String) {
      return (String) title;
    } else if (title instanceof Integer) {
      return getString((int) title);
    } else {
      return null;
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    if (!mFragment.onBackPressed()) {
      super.onBackPressed();
    }
  }

  /**
   * Creates the intent for {@link A3WebActivity}.
   *
   * @see A3WebActivity#with(Context, WebViewSettings)
   */
  public static class Builder {

    private Intent mIntent;

    private Builder(@NonNull Context context, WebViewSettings settings) {
      mIntent = new Intent(context, A3WebActivity.class);
      mIntent.putExtra(EXTRA_SETTINGS, settings);
    }

    /**
     * Set the title of the activity.
     *
     * @param title the activities title
     * @return this builder instance.
     * @see #setTitle(String)
     */
    public Builder setTitle(@StringRes int title) {
      mIntent.putExtra(EXTRA_TITLE, title);
      return this;
    }

    /**
     * Set the title of the activity.
     *
     * @param title the activities title
     * @return this builder instance.
     * @see #setTitle(int)
     */
    public Builder setTitle(@NonNull String title) {
      mIntent.putExtra(EXTRA_TITLE, title);
      return this;
    }
    /**
     * Set the subtitle of the activity.
     *
     * @param subtitle the activities subtitle
     * @return this builder instance.
     * @see #setSubtitle(String)
     */
    public Builder setSubtitle(@StringRes int subtitle) {
      mIntent.putExtra(EXTRA_SUBTITLE, subtitle);
      return this;
    }

    /**
     * Set the subtitle of the activity.
     *
     * @param subtitle the activities subtitle
     * @return this builder instance.
     * @see #setSubtitle(int)
     */
    public Builder setSubtitle(@NonNull String subtitle) {
      mIntent.putExtra(EXTRA_SUBTITLE, subtitle);
      return this;
    }

    /**
     * Enables home as up by calling finish when pressed.
     *
     * @return this builder instance.
     */
    public Builder enableHomeAsUp() {
      mIntent.putExtra(EXTRA_HOME_AS_UP, true);
      return this;
    }

    /**
     * Get the intent. Don't forget to call {@code startActivity(intent);}
     *
     * @return the built intent
     */
    public Intent build() {
      return mIntent;
    }
  }
}
