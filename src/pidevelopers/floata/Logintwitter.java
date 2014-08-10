package pidevelopers.floata;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class Logintwitter extends Activity {
	protected static final String CALLBACK_URL_KEY = "CALLBACK_URL_KEY";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_logintwitter);
		
		
		Toast toast = Toast.makeText(Logintwitter.this,"Loading ..... ", 8000);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		
		Intent intent = getIntent();
		String mUrl = intent.getStringExtra(MainActivity.AUTHENTICATION_URL_KEY);

		WebView webView = (WebView) findViewById(R.id.webViewLoginToTwitter);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new LoginToTwitterWebViewClient());

		webView.loadUrl(mUrl);
	}
	private class LoginToTwitterWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        if (url.startsWith(getString(R.string.TWITTER_CALLBACK_URL))) {
	            Intent intent = new Intent();
	            intent.putExtra(CALLBACK_URL_KEY, url);
	            setResult(Activity.RESULT_OK, intent);
	            
	            finish();
	            
	        }
	        return false;
	    }
	}
}
