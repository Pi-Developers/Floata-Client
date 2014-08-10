package pidevelopers.floata;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Floating extends Activity {
	EditText text;
	static Button post;
	static RelativeLayout postlayout;
	String loadtokensecret, loadtoken, on;
	String Status;
    TextView textletters,Xtext;
	twitter4j.Status response;
	String access_token;
    Twitter twitter;
	String access_token_secret;
    int X, Y ;
    long userID;
    String username;
    User user;
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_floating);

		RelativeLayout mainlayout = (RelativeLayout) findViewById(R.id.mainlayout);
		postlayout = (RelativeLayout) findViewById(R.id.postlayout);
		textletters = (TextView)findViewById(R.id.textletters);
		text = (EditText) findViewById(R.id.text);
		post = (Button) findViewById(R.id.Tweet);
		Xtext = (TextView)findViewById(R.id.Xtext);
		loadtext();
		loadXtext();

		
		text.setFilters(new InputFilter[] {
				
				new InputFilter.LengthFilter(140)
						
		});
		
		
		
		
		
		postlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});

		mainlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				save("on", "false");
				Floating.postlayout.setVisibility(RelativeLayout.GONE);

				loadon();
			}
		});

		loadtoken();
		

		post.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Status = text.getText().toString();
				
				
			
					
					
				
				if (Status.trim().length() > 0) {
					
					
					
						if (Status.trim().length() <= 140) {

							try {

								

									new updateTwitterStatus().execute(Status);


							

							} catch (Exception e) {

							}
						} else {
							Toast.makeText(
									Floating.this,
									"Your tweet is"
											+ Status.trim().length()
											+ " letter it must be less than 140 letter",
									Toast.LENGTH_SHORT).show();
						}

					

				

					
					
				}
				else
				{
					Toast.makeText(
							Floating.this,
							"You have to type any thing",
							Toast.LENGTH_SHORT).show();
				}
					
				}
			

		});

		loadon();
		
		text.addTextChangedListener(new TextWatcher(){
			
	        public void afterTextChanged(Editable s) {

	     

	        }
	        
	        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	        
	        public void onTextChanged(CharSequence s, int start, int before, int count){
	        	
	        	textletters.setText(String.valueOf(s.length()));
	        	if (s.length() > 140){
	        		
	        		post.setEnabled(false);
	        		
	        	}
	        }
	        	
	   
	    }); 

	}

	

	class updateTwitterStatus extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			Toast toast = Toast.makeText(Floating.this, "Sending Tweet Please Wait..",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {

			String status = args[0];
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(getString(R.string.TWITTER_CONSUMER_KEY));
				builder.setOAuthConsumerSecret(getString(R.string.TWITTER_CONSUMER_SECRET));

				AccessToken accessToken = new AccessToken(access_token,
						access_token_secret);
				 twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

				response = twitter.updateStatus(status);
				

			} catch (TwitterException e) {

			}
			return null;
		}

		protected void onPostExecute(String file_url) {

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(Floating.this, "Tweet Posted Successfully !",
							Toast.LENGTH_SHORT).show();
					text.setText("");
					X++;
					Xtext.setText(String.valueOf(X + Y) + "  Tweets by floata");
					save("Xtext", String.valueOf(X + Y));
 				
				}
			});
		}

	}

	public void save(String key, String value) {
		Chathead.spf = PreferenceManager.getDefaultSharedPreferences(this);
		Editor edit = Chathead.spf.edit();
		edit.putString(key, value);
		edit.commit();

	}
	
	


	public void loadon() {

		Chathead.spf = PreferenceManager.getDefaultSharedPreferences(this);
		on = Chathead.spf.getString("on", "");
		if (on.equals("false")) {

			
			Chathead.xt = Chathead.spf.getString("x", "");
			Chathead.yt = Chathead.spf.getString("y", "");
			
			Chathead.twitterp.x = Integer.parseInt(Chathead.xt);
			Chathead.twitterp.y = Integer.parseInt(Chathead.yt);

			
			Chathead.windowManager.updateViewLayout(Chathead.twitter,
					Chathead.twitterp);


			finish();
			


		} else {

		}

	}

	public void loadtext() {
		Chathead.spf = PreferenceManager.getDefaultSharedPreferences(this);
		text.setText(Chathead.spf.getString("text", ""));
		textletters.setText(Chathead.spf.getString("text", "").length()+"");
	}
	
	public void loadXtext() {
		Chathead.spf = PreferenceManager.getDefaultSharedPreferences(this);
		String  Xtxt = Chathead.spf.getString("Xtext", "");
	
		
		if (Xtxt.equals("") || Xtxt.equals(null)){
			
		}else{
		Xtext.setText(Chathead.spf.getString("Xtext", "") + " Tweets by Floata");
		Y = Integer.parseInt(Chathead.spf.getString("Xtext", ""));
		}
		}
	

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			save("on", "false");
			Floating.postlayout.setVisibility(RelativeLayout.GONE);

			loadon();

			return true;
		}
		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		save("on", "false");
		Floating.postlayout.setVisibility(RelativeLayout.GONE);
		save("text", text.getText().toString());

		loadon();

	}

	@Override
	public void onPause() {
		super.onPause();
		save("on", "false");
		save("text", text.getText().toString());
		Floating.postlayout.setVisibility(RelativeLayout.GONE);

		loadon();

	}
	
	public void loadtoken() {

		MainActivity.spf = PreferenceManager.getDefaultSharedPreferences(this);
		access_token = MainActivity.spf.getString("ACCESS_TOKEN", "");
		access_token_secret = MainActivity.spf.getString("ACCESS_SECRET", "");

	
	}
	
	   
}
