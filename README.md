# A3WebView

Utilities to wrap around the Android WebView.

[ ![Build Status](https://api.travis-ci.org/allaboutapps/A3WebView.svg?branch=master) ](https://travis-ci.org/allaboutapps/A3WebView)
[ ![Download](https://api.bintray.com/packages/allaboutapps/A3-Android/at.allaboutapps.web.a3webview/images/download.svg) ](https://bintray.com/allaboutapps/A3-Android/at.allaboutapps.web.a3webview/_latestVersion)

## This library contains the following

* **A3WebActivity**  
  Standalone Activity with Builder for easy display of HTML content.
* **A3WebFragment**  
  Fragment displaying HTML content.
* **A3WebView**  
  Wrapper around WebView that also displays loading and error.
  
## Usage

Create WebViewSettings with some information about what you would like to do:

```java
WebViewSettings settings =
	WebViewSettings.loadUrl("https://allaboutapps.at").enableJavaScript();

  // or

WebViewSettings settings =
	WebViewSettings.loadData(
		"<center>Displaying some <b>inline content</b> in a WebView</center>");
		
  // or
  
WebViewSettings settings =
	WebViewSettings.loadAssetFile("imprint.html")
		.disableLoadingIndicator()
		.openLinksExternally();
```
			
Use those settings along with one of  `A3WebActivity`, `A3WebFragment`, or `A3WebView` to load and display your content. The Activity also uses a builder for some additional options.

```java
Intent intent =
	A3WebActivity.with(getContext(), settings)
		.enableHomeAsUp()
		.setTitle("aaa - all about apps")
		.build();
		
  // or
		
Fragment fragment = A3WebFragment.newInstance(settings);

  // or

webView.loadWithSettings(settings);
```

## Including it in your project

    compile "at.allaboutapps.web:a3webview:${latestVersion}"

## License

	MIT License

	Copyright (c) 2017 aaa - all about apps GmbH