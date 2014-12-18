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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	protected static final String AUTHENTICATION_URL_KEY = "AUTHENTICATION_URL_KEY";
	protected static final int LOGIN_TO_TWITTER_REQUEST = 0;
	static final String PREFERENCE_NAME = "twitter_oauth";
	static final String ACCESS_TOKEN = "oauth_token";
	static final String ACCESS_SECRET = "oauth_token_secret";
	static Twitter twitter;
	static ProgressDialog progressDialog;
	static String access_token; 
	static String access_token_secret;
	static String shape, size, floataon, seek;
	Button login, start;
	SeekBar transperancy;
	RadioButton pen, cloud, twittericon, xlarge, large, small;
	SharedPreferences spf;
	Editor edit;
	String zz, on;
	AccessToken accessToken;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		android.support.v7.app.ActionBar bar = getSupportActionBar();// or MainActivity.getInstance().getActionBar()
		spf = PreferenceManager.getDefaultSharedPreferences(this);
		login = (Button) findViewById(R.id.button2);
		start = (Button) findViewById(R.id.button1);
		transperancy = (SeekBar) findViewById(R.id.seek);
		pen = (RadioButton) findViewById(R.id.radio1);
		twittericon = (RadioButton) findViewById(R.id.radio2);
		cloud = (RadioButton) findViewById(R.id.radio0);
		xlarge = (RadioButton) findViewById(R.id.radio00);
		large = (RadioButton) findViewById(R.id.radio11);
		small = (RadioButton) findViewById(R.id.radio22);
		cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        
        bar.setBackgroundDrawable(new ColorDrawable(0xff2196F3));
		bar.setDisplayShowTitleEnabled(false);  
		bar.setDisplayShowTitleEnabled(true);
		bar.setTitle(Html.fromHtml("<b>Floata</b>"));
		
        if (!isInternetPresent) {

			new AlertDialog.Builder(MainActivity.this)
			.setTitle("Sorry !")
			.setMessage("you don't have internet connection, Floata won't be fully fuctional, please turn on Wi-Fi or Data to use Floata properly")
			.setPositiveButton("Turn on wifi",new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) {startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));} })
			.setNegativeButton("I understand", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) { } }).show();

        }

		loadon();
		loadseek();
		if (!(seek.equals(null))) {
			
			if (!(seek.equals(""))) {
				
			transperancy.setProgress(Integer.parseInt(seek)*100/255);
			
			}
			
			else {
				
				transperancy.setProgress(Integer.parseInt("255"));
			}
		}
		
		loadshapeandsize();
		loadtoken();
		
		transperancy.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {

				int transperancyseek = (arg1 * 225 / 100);

				save("seek", String.valueOf(transperancyseek));
				
				
		if (floataon.equals("true")) { loadChatHead(); }

			}
		});

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				loadtoken();
				if ((access_token.equals("")) || (access_token.equals(null))) {
					login.setText("Login");
					loginToTwitter();

					progressDialog = new ProgressDialog(MainActivity.this);
					progressDialog.setMessage("Loading ...");

					progressDialog.setCancelable(false);
					progressDialog.show();

				} else {

					logoutToTwitter();

				}

			}
		});

		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (floataon.equals("false")) {
					loadshapeandsize();
					loadseek();
					save("on", "true");

					startService(new Intent(MainActivity.this, ChatHead.class));
					loadon();

				} else {
					save("on", "false");

					stopService(new Intent(MainActivity.this, ChatHead.class));
					loadon();
					


				}

			}
		});

		pen.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub

				if (arg1 == true) {

					save("shape", "pen");
					if (floataon.equals("true")) {
						loadChatHead();
					}

				} else {

				}

			}
		});

		cloud.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub

				if (arg1 == true) {
					save("shape", "cloud");
					if (floataon.equals("true")) {
						loadChatHead();
					}

				} else {

				}

			}
		});

		twittericon.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub

				if (arg1 == true) {
					save("shape", "twittericon");
					if (floataon.equals("true")) {
						loadChatHead();
					}

				} else {

				}

			}
		});

		xlarge.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub

				if (arg1 == true) {
					save("size", "xlarge");
					if (floataon.equals("true")) {
						loadChatHead();
					}

				} else {

				}

			}
		});

		large.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub

				if (arg1 == true) {
					save("size", "large");
					if (floataon.equals("true")) {
						loadChatHead();
					}

				} else {

				}

			}
		});

		small.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub

				if (arg1 == true) {
					save("size", "small");
					if (floataon.equals("true")) {
						loadChatHead();
					}

				} else {

				}

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.help) {

			new AlertDialog.Builder(MainActivity.this)
					.setTitle("Help")
					.setMessage(getString(R.string.help))
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).setIcon(R.drawable.help).show();

		}

		if (item.getItemId() == R.id.about) {
			new AlertDialog.Builder(MainActivity.this)
					.setTitle("About")
					.setMessage(getString(R.string.about))
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).setIcon(R.drawable.about).show();

		}

		return super.onOptionsItemSelected(item);

	}

	public void save(String key, String value) {
		edit = spf.edit();
		edit.putString(key, value);
		edit.commit();

	}

	public void loadon() {

	

		if(ChatHead.twitter == null){
			floataon = spf.getString("on", "false");
			
		}else{
			
			floataon = spf.getString("on", "");
		}

	}

	public void loadshapeandsize() {

		shape = spf.getString("shape", "");
		size = spf.getString("size", "");

		if (size.equals("xlarge")) {

			xlarge.setChecked(true);

		} else if (size.equals("large")) {
			large.setChecked(true);
		} else if (size.equals("small")) {
			small.setChecked(true);
		} else if (size.equals("")) {
			large.setChecked(true);
			size = "large";

		} else if (size.equals(null)) {
			large.setChecked(true);
			size = "large";
		}

		if (shape.equals("pen")) {
			pen.setChecked(true);

		} else if (shape.equals("cloud")) {
			cloud.setChecked(true);

		} else if (shape.equals("twittericon")) {
			twittericon.setChecked(true);

		} else if (shape.equals("")) {
			pen.setChecked(true);

		} else if (shape.equals(null)) {
			pen.setChecked(true);

		}

	}

	public void loadseek() {

		seek = spf.getString("seek", "");
		
		

	}

	@SuppressWarnings("deprecation")
	public void loadChatHead() {

		loadshapeandsize();
		loadseek();

		if (floataon.equals("true")) {
			try {

				if (!(seek.equals(null)))
				{
					if (!(seek.equals("")))
					{
					
					ChatHead.twitter.setAlpha(Integer.parseInt(seek));
					}
				}
			} catch (NumberFormatException e) {
				// TODO: handle exception
			}

		}

		if (shape.equals("pen")) {
			if (size.equals("xlarge")) {
				ChatHead.twitter.setImageResource(R.drawable.penxlarge);
			} else if (size.equals("large")) {
				ChatHead.twitter.setImageResource(R.drawable.penlarge);
			} else if (size.equals("small")) {
				ChatHead.twitter.setImageResource(R.drawable.pensmall);
			} else if (size.equals("")) {
				ChatHead.twitter.setImageResource(R.drawable.penlarge);
			} else if (size.equals(null)) {
				ChatHead.twitter.setImageResource(R.drawable.penlarge);
			}
		}

		else if (shape.equals("cloud")) {
			if (size.equals("xlarge")) {
				ChatHead.twitter.setImageResource(R.drawable.cloudxlarge);
			} else if (size.equals("large")) {
				ChatHead.twitter.setImageResource(R.drawable.cloudlarge);
			} else if (size.equals("small")) {
				ChatHead.twitter.setImageResource(R.drawable.cloudsmall);
			} else if (size.equals("")) {
				ChatHead.twitter.setImageResource(R.drawable.cloudlarge);
			} else if (size.equals(null)) {
				ChatHead.twitter.setImageResource(R.drawable.cloudlarge);
			}
		}

		else if (shape.equals("twittericon")) {
			if (size.equals("xlarge")) {
				ChatHead.twitter.setImageResource(R.drawable.twitterxlarge);
			} else if (size.equals("large")) {
				ChatHead.twitter.setImageResource(R.drawable.twitterlarge);
			} else if (size.equals("small")) {
				ChatHead.twitter.setImageResource(R.drawable.twittersmal);
			} else if (size.equals("")) {
				ChatHead.twitter.setImageResource(R.drawable.twitterlarge);
			} else if (size.equals(null)) {
				ChatHead.twitter.setImageResource(R.drawable.twitterlarge);
			}
		}

		else if (shape.equals("")) {
			if (size.equals("xlarge")) {
				ChatHead.twitter.setImageResource(R.drawable.penxlarge);
			} else if (size.equals("large")) {
				ChatHead.twitter.setImageResource(R.drawable.penlarge);
			} else if (size.equals("small")) {
				ChatHead.twitter.setImageResource(R.drawable.pensmall);
			} else if (size.equals("")) {
				ChatHead.twitter.setImageResource(R.drawable.penlarge);
			} else if (size.equals(null)) {
				ChatHead.twitter.setImageResource(R.drawable.penlarge);
			}

		} else if (shape.equals(null)) {
			if (size.equals("xlarge")) {
				ChatHead.twitter.setImageResource(R.drawable.penxlarge);
			} else if (size.equals("large")) {
				ChatHead.twitter.setImageResource(R.drawable.penlarge);
			} else if (size.equals("small")) {
				ChatHead.twitter.setImageResource(R.drawable.pensmall);
			} else if (size.equals("")) {
				ChatHead.twitter.setImageResource(R.drawable.penlarge);
			} else if (size.equals(null)) {
				ChatHead.twitter.setImageResource(R.drawable.penlarge);
			}
		}
	}

	private void logoutToTwitter() {
		
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Logout")
		.setMessage("Are you sure you want to logout ?")
		.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						save("token", "");
						save("secret", "");

						loadtoken();

						Toast.makeText(MainActivity.this, "Disconnected from Twitter",
								Toast.LENGTH_SHORT).show();

						login.setText("Login to twitter");

					}
				})
		
		.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
					

						
					}
				});
		AlertDialog alertDialog = alert.create();
		alertDialog.show();

		
	}

	private void loginToTwitter() {
		// starting class to get access token
		GetRequestTokenTask getRequestTokenTask = new GetRequestTokenTask();
		getRequestTokenTask.execute();
		loadtoken();
		return;
	}

	private void launchLoginWebView(RequestToken requestToken) {
		Intent intent = new Intent(this, TwitterLoginActivity.class);
		intent.putExtra(MainActivity.AUTHENTICATION_URL_KEY,
				requestToken.getAuthenticationURL());
		startActivityForResult(intent, LOGIN_TO_TWITTER_REQUEST);

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == LOGIN_TO_TWITTER_REQUEST) {
			if (resultCode == Activity.RESULT_OK) {
				getAccessToken(data
						.getStringExtra(TwitterLoginActivity.CALLBACK_URL_KEY));
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

			save("token", accessToken.getToken());
			save("secret", accessToken.getTokenSecret());
			loadtoken();

			Toast.makeText(getApplicationContext(), "Connected to Twitter",
					Toast.LENGTH_SHORT).show();
			login.setText("Logout from twitter");
			super.onPostExecute(result);
		}
	}

	public void loadtoken() {

		access_token = spf.getString("token", "");
		access_token_secret = spf.getString("secret", "");

		loadon();
		if (floataon.equals("true")) {
			ChatHead.access_token = access_token;
			ChatHead.access_token_secret = access_token_secret;

		}

		if (access_token.equals("")) {
			login.setText("Login to twitter");
		} else {
			
			login.setText("Logout from twitter");


		}

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

}
