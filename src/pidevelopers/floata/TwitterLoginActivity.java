package pidevelopers.floata;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class TwitterLoginActivity extends Activity {
	protected static final String CALLBACK_URL_KEY = "CALLBACK_URL_KEY";
	WebView webView;
	String mUrl;
	ProgressDialog progressDialog;
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    MainActivity.progressDialog.hide();
	    
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    //Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

	    

		setContentView(R.layout.activity_twitter_login);
		
		 progressDialog = new ProgressDialog(TwitterLoginActivity.this);
		progressDialog.setMessage("Loading ...");
		
		progressDialog.setCancelable(false);
		progressDialog.show();
		Intent intent = getIntent();
		 mUrl = intent.getStringExtra(MainActivity.AUTHENTICATION_URL_KEY);
		 webView = (WebView) findViewById(R.id.twitterloginwebview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new LoginToTwitterWebViewClient());
		webView.loadUrl(mUrl);	
	}

	
	private class LoginToTwitterWebViewClient extends WebViewClient
{
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
	
	

	 public void onPageFinished(WebView webView, String mUrl) {
		 
		    super.onPageFinished(webView, mUrl);
		    progressDialog.hide();
		    
		    }
}
}
