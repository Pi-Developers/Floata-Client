package pidevelopers.floata;

import java.io.File;
import java.io.FileOutputStream;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import wei.mark.standout.StandOutWindow;
import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FloatingActivity extends StandOutWindow {
	int location[] = new int[2];
	SharedPreferences spf;
	Editor edit;
	static EditText text;

	Twitter twitter;
	Button tweet,button1;
	static TextView number,username;
	static String access_token, access_token_secret, Status, img, xt, yt;
	twitter4j.Status response;

	File f;
	Bitmap bm;
	FileOutputStream out;
	PendingIntent pIntent;
	String url;
	NotificationManager notificationManager;

	static Context c;

	@Override
	public String getAppName() {
		return "Floata is";
	}

	@Override
	public int getAppIcon() {
		return R.drawable.ic_launcher;
	}

	@Override
	public void createAndAttachView(int id, FrameLayout frame) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.activity_floating, frame, true);		
		spf = PreferenceManager.getDefaultSharedPreferences(this);
		Intent intent = new Intent(FloatingActivity.this, Close.class);
		pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		access_token = ChatHead.access_token;
		access_token_secret = ChatHead.access_token_secret;
		
		text = (EditText) view.findViewById(R.id.text);
		tweet = (Button) view.findViewById(R.id.tweet);
		button1 = (Button) view.findViewById(R.id.button1);
		number = (TextView) view.findViewById(R.id.number);
	
		username = (TextView) view.findViewById(R.id.textView1);
//		loadimg();

		loadtext();

		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				save("chatheadon", "false");
				save("text", FloatingActivity.text.getText().toString());

				StandOutWindow.closeAll(FloatingActivity.this,
						FloatingActivity.class);

				ChatHead.windowManager.addView(ChatHead.twitter,
						ChatHead.twitterp);

				Notification n = new NotificationCompat.Builder(FloatingActivity.this)
						.setContentTitle("Floata")
						.setContentText("Touch to stop service")
						.setSmallIcon(R.drawable.ic_launcher)
						.setContentIntent(pIntent).setAutoCancel(false).build();
				n.flags = Notification.FLAG_ONGOING_EVENT;

				notificationManager.notify(0, n);

				loadlocation();

			}
		});

		tweet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Status = text.getText().toString();
				access_token = ChatHead.access_token;
				access_token_secret = ChatHead.access_token_secret;

				if ((access_token.equals("")) || (access_token.equals(null))) {
					Toast.makeText(FloatingActivity.this, "You have to login",
							Toast.LENGTH_SHORT).show();
				} else {

					if (Status.trim().length() == 0) {

						Toast.makeText(FloatingActivity.this,
								"You have to type any thing",
								Toast.LENGTH_SHORT).show();

					} else {

						new updateTwitterStatus().execute(Status);

					}

				}
			}
		});

		text.setFilters(new InputFilter[] { new InputFilter.LengthFilter(140) });

//		pic.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				loadimg();
//				f = new File(img);
//
//				if (f.exists()) {
//					save("img", null);
//					loadimg();
//
//					pic.setImageResource(R.drawable.camera);
//
//				} else {
//					StandOutWindow.closeAll(FloatingActivity.this,
//							FloatingActivity.class);
//
//					save("text", FloatingActivity.text.getText().toString());
//
//					Intent i = new Intent(FloatingActivity.this,
//							ImageChooser.class);
//					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					startActivity(i);
//
//				}
//
//			}
//		});

		text.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				number.setText(String.valueOf(s.length()));

			}
		});

	}

	public void loadtext() {
		if (!text.equals(null)) {

			text.setText(spf.getString("text", ""));
			number.setText(spf.getString("text", "").length() + "");
		}
	}

	@Override
	public StandOutLayoutParams getParams(int id, Window window) {
		return new StandOutLayoutParams(id, StandOutLayoutParams.MATCH_PARENT,
				StandOutLayoutParams.WRAP_CONTENT, StandOutLayoutParams.CENTER,
				StandOutLayoutParams.TOP);
	}

	@Override
	public int getFlags(int id) {
		return super.getFlags(id)
				| StandOutFlags.FLAG_WINDOW_FOCUS_INDICATOR_DISABLE
				| StandOutFlags.FLAG_BODY_MOVE_ENABLE
				| StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE;
	}

	@Override
	public int getThemeStyle() {
		return android.R.style.Theme_Light;
	}

	public void save(String key, String value) {
		edit = spf.edit();
		edit.putString(key, value);
		edit.commit();

		Log.v("save", "yes");
	}

//	public void loadimg() {
//
//		img = spf.getString("img", "");
//		f = new File(img);
//
//		if (f.exists()) {
//
//			pic.setImageResource(R.drawable.close);
//
//		} else {
//			pic.setImageResource(R.drawable.camera);
//
//		}
//
//		Log.v("img", img);
//	}

	public void uploadPic(File file, String message, Twitter twitter)
			throws Exception {
		try {
			StatusUpdate status = new StatusUpdate(message);
			status.setMedia(file);
			twitter.updateStatus(status);
		} catch (TwitterException e) {
			throw e;
		}
	}

	class updateTwitterStatus extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			Toast toast = Toast.makeText(FloatingActivity.this,
					"Sending Tweet Please Wait..", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}

		protected String doInBackground(String... args) {

			String status = args[0];

			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(getString(R.string.TWITTER_CONSUMER_KEY));
			builder.setOAuthConsumerSecret(getString(R.string.TWITTER_CONSUMER_SECRET));

			AccessToken accessToken = new AccessToken(access_token,
					access_token_secret);
			twitter = new TwitterFactory(builder.build())
					.getInstance(accessToken);

//			loadimg();

			if (f.exists()) {
				try {

					bm = ShrinkBitmap(img, 800, 800);

					String extStorageDirectory = Environment
							.getExternalStorageDirectory().toString();
					File fx = new File(extStorageDirectory, "Floata.png");

					try {
						out = new FileOutputStream(fx);
						bm.compress(Bitmap.CompressFormat.PNG, 100, out);

						uploadPic(fx, status);

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							out.close();
						} catch (Throwable ignore) {
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				try {
					UploadStatus(status);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			super.onPostExecute(file_url);

			text.setText("");

			save("img", null);
//			loadimg();

		}

	}

	public void uploadPic(File file, String message) throws Exception {
		// try{
		StatusUpdate status = new StatusUpdate(message);

		status.setMedia(file);

		twitter.updateStatus(status);

	}

	public void UploadStatus(String message) throws Exception {

		StatusUpdate status = new StatusUpdate(message);

		twitter.updateStatus(status);

	}

	Bitmap ShrinkBitmap(String file, int width, int height) {

		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
				/ (float) height);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
				/ (float) width);

		if (heightRatio > 1 || widthRatio > 1) {
			if (heightRatio > widthRatio) {
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}

		bmpFactoryOptions.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
		return bitmap;
	}

	public void loadlocation() {
		xt = spf.getString("x", "");
		yt = spf.getString("y", "");
		ChatHead.twitterp.x = Integer.parseInt(xt);
		ChatHead.twitterp.y = Integer.parseInt(yt);
		ChatHead.windowManager.updateViewLayout(ChatHead.twitter,
				ChatHead.twitterp);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		save("text", FloatingActivity.text.getText().toString());
		save("chatheadon", "false");

	}

	@Override
	public String getPersistentNotificationMessage(int id) {
		return "Tweet from any where";
	}

	@Override
	public Intent getPersistentNotificationIntent(int id) {

		return null;
	}

}
