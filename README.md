# golisAndroid

GOL inspection system for Android.

Functions
1.From webview use javascript call a Andorid function to start barcode scanor to read barcode.
2.After barcode reader return the barcode, then call javascript callback function to trigger event.
3.use example:
  WebView.load_url("file://android_asser/readme.html"); /*load readme.html page*/
  ...
  call callback_function() in readme.html, if callback_function does't exist, nothing will be do, 
  just output a error message to log. Please be sure your page has been load finished.
  WebView.load_url("javascript:callback_function()");   
