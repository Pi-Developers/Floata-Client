package pidevelopers.floata;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	static Twitter twitter;

	Button start, about, setting, help;
    ConnectionDetector cd;
	ImageView twitterbutton;
	String access_token, floata, access_token_secret;

	protected static final String AUTHENTICATION_URL_KEY = "AUTHENTICATION_URL_KEY";

	protected static final int LOGIN_TO_TWITTER_REQUEST = 0;

	static SharedPreferences spf;

	static final String PREFERENCE_NAME = "twitter_oauth";

	static final String ACCESS_TOKEN = "oauth_token";

	static final String ACCESS_SECRET = "oauth_token_secret";

	AccessToken accessToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		startService(new Intent (this, ServiceL.class));
		
		  cd = new ConnectionDetector(getApplicationContext());
		  

	        if (!cd.isConnectingToInternet()) {


	        	
	        	  cd = new ConnectionDetector(getApplicationContext());
	        	  
	              if (!cd.isConnectingToInternet()) {
	            	  
	            	  new AlertDialog.Builder(this)
	            	    .setTitle("Opps!")
	            	    .setMessage("You are not connected to internet, check your connection and retry")
	            	    .setPositiveButton("Exit Floata", new DialogInterface.OnClickListener() {
	            	        public void onClick(DialogInterface dialog, int which) { 
                         finish();
                             }
	            	     })

	            	    .setIcon(android.R.drawable.ic_dialog_alert)
	            	     .show();

	            	  return;
	              }
	        	
	        	
	        	
	            return;
	        }
		
		start = (Button) findViewById(R.id.start);
		twitterbutton = (ImageView) findViewById(R.id.twitter);
		about = (Button) findViewById(R.id.about);
		setting = (Button) findViewById(R.id.setting);
		help = (Button) findViewById(R.id.help);

		setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				startActivity(new Intent(MainActivity.this, Setting.class));

			}
		});

		help.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {

				AlertDialog alertDialog = new AlertDialog.Builder(
						MainActivity.this).create();

				alertDialog.setTitle("Floata Help");

				alertDialog.setMessage(getString(R.string.help));

				alertDialog.setIcon(R.drawable.ic_launcher);

				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});

				alertDialog.show();

			}
		});

		about.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {

				AlertDialog alertDialog = new AlertDialog.Builder(
						MainActivity.this).create();

				alertDialog.setTitle("About floata");

				alertDialog.setMessage(getString(R.string.floata));

				alertDialog.setIcon(R.drawable.ic_launcher);

				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});

				alertDialog.show();

			}
		});


		twitterbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				loadtoken();

				if (access_token.equals("")) {
					int x = 10000;
					twitterbutton.setImageResource(R.drawable.login);
					loginToTwitter();
					Toast toast = Toast.makeText(MainActivity.this,
							"Loading Twitter", x);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();

				} else {

					logoutToTwitter();

					twitterbutton.setImageResource(R.drawable.logout);

				}

			}
		});

		start.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				loadtoken();
				loadfloata();

				if (floata.equals("")) {

					if(access_token.equals("")){
					
					

						
						new AlertDialog.Builder(MainActivity.this)
	            	    .setTitle("Opps!")
	            	    .setMessage("You are not connected to Twitter, login and retry")
	            	    .setPositiveButton("Exit Floata", new DialogInterface.OnClickListener() {
	            	        public void onClick(DialogInterface dialog, int which) { 
                         finish();
                             }
	            	     })

	            	    .setIcon(android.R.drawable.ic_dialog_alert)
	            	     .show();

	            	  return;
	            	 
						
					}else
					save("floata", "on");
					startService(new Intent(MainActivity.this, Chathead.class));
				
				} else {

					save("floata", "");
					stopService(new Intent(MainActivity.this, Chathead.class));

				}

			}
		});

		loadtoken();

	}

	private class GetRequestTokenTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... voids) {

			ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
			configurationBuilder
					.setOAuthConsumerKey(getString(R.string.TWITTER_CONSUMER_KEY));
			configurationBuilder
					.setOAuthConsumerSecret(getString(R.string.TWITTER_CONSUMER_SECRET));
			Configuration configuration = configurationBuilder.build();
			twitter = new TwitterFactory(configuration).getInstance();

			try {

				RequestToken requestToken = twitter
						.getOAuthRequestToken(getString(R.string.TWITTER_CALLBACK_URL));
				launchLoginWebView(requestToken);
			} catch (TwitterException e) {
				e.printStackTrace();

			}
			return null;
		}
	}

	private void loginToTwitter() {
		// starting class to get access token
		GetRequestTokenTask getRequestTokenTask = new GetRequestTokenTask();
		getRequestTokenTask.execute();
		loadtoken();
		return;
	}

	private void logoutToTwitter() {

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage("Delete current Twitter connection?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								save("ACCESS_TOKEN", null);
								save("ACCESS_SECRET", null);

								Toast.makeText(MainActivity.this,
										"Disconnected from Twitter",
										Toast.LENGTH_SHORT).show();

								loadtoken();

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

					}

				});

		final AlertDialog alert = builder.create();

		alert.show();

	}

	private void launchLoginWebView(RequestToken requestToken) {
		// intent to launch activity + sending request
		Intent intent = new Intent(this, Logintwitter.class);
		intent.putExtra(MainActivity.AUTHENTICATION_URL_KEY,
				requestToken.getAuthenticationURL());
		startActivityForResult(intent, LOGIN_TO_TWITTER_REQUEST);

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == LOGIN_TO_TWITTER_REQUEST) {
			if (resultCode == Activity.RESULT_OK) {
				getAccessToken(data
						.getStringExtra(Logintwitter.CALLBACK_URL_KEY));
			}
		}
	}

	private void getAccessToken(String callbackUrl) {

		Uri uri = Uri.parse(callbackUrl);
		String verifier = uri.getQueryParameter("oauth_verifier");

		GetAccessTokenTask getAccessTokenTask = new GetAccessTokenTask();
		getAccessTokenTask.execute(verifier);
	}

	private class GetAccessTokenTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... strings) {
			String verifier = strings[0];
			try {
				accessToken = twitter.getOAuthAccessToken(verifier);
				Log.d(MainActivity.class.getSimpleName(),
						accessToken.getToken());

			} catch (Exception e) {

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			save("ACCESS_TOKEN", accessToken.getToken());
			save("ACCESS_SECRET", accessToken.getTokenSecret());

			Toast.makeText(getApplicationContext(), "Connected to Twitter",
					Toast.LENGTH_SHORT).show();
			twitterbutton.setImageResource(R.drawable.logout);

			super.onPostExecute(result);
		}

	}

	public void loadtoken() {

		MainActivity.spf = PreferenceManager.getDefaultSharedPreferences(this);
		access_token = MainActivity.spf.getString("ACCESS_TOKEN", "");
		access_token_secret = MainActivity.spf.getString("ACCESS_SECRET", "");

		loadfloata();
		if (floata.equals("true")) {
			Chathead.access_token = access_token;
			Chathead.access_token_secret = access_token_secret;

		}

		if (access_token.equals("")) {

			twitterbutton.setImageResource(R.drawable.login);

		} else {

			twitterbutton.setImageResource(R.drawable.logout);

		}

	}

	public void save(String key, String value) {
		spf = PreferenceManager.getDefaultSharedPreferences(this);
		Editor edit = spf.edit();
		edit.putString(key, value);
		edit.commit();

	}

	public void loadfloata() {

		MainActivity.spf = PreferenceManager.getDefaultSharedPreferences(this);

		floata = MainActivity.spf.getString("floata", "");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		loadfloata();

		if (floata.equals("on")) {

		} else {
			save("floata", "");
			loadfloata();
		}
		finish();

	}

	@Override
	public void onPause() {
		super.onPause();
		loadfloata();

		if (floata.equals("on")) {

		} else {
			save("floata", "");
			loadfloata();
		}

	}

}