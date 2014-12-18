package pidevelopers.floata;

import wei.mark.standout.StandOutWindow;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

public class ChatHead extends Service {

	static WindowManager windowManager;
	static WindowManager.LayoutParams twitterp, XClosew;
	static ImageView twitter, XClose;
	int open;
	NotificationManager notificationManager;
	SharedPreferences spf;
	Editor edit;
	static String shape, size, seek, chatheadon, xt, yt, access_token,
			access_token_secret;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@SuppressWarnings("deprecation")
	public void onCreate() {

		super.onCreate();

		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		Intent intent = new Intent(this, Close.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		Notification n = new NotificationCompat.Builder(this)
				.setContentTitle("Floata")
				.setContentText("Touch to stop service")
				.setSmallIcon(R.drawable.ic_launcher).setContentIntent(pIntent)
				.setAutoCancel(false).build();
		n.flags = Notification.FLAG_ONGOING_EVENT;

		notificationManager.notify(0, n);

		spf = PreferenceManager.getDefaultSharedPreferences(this);

		shape = MainActivity.shape;
		size = MainActivity.size;
		seek = MainActivity.seek;
		access_token = MainActivity.access_token;
		access_token_secret = MainActivity.access_token_secret;

		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		twitter = new ImageView(this);
		twitter.setImageResource(R.drawable.penlarge);

		try {

			if (!seek.equals(null)) {
				try {
					twitter.setAlpha(Integer.parseInt(seek));
				} catch (NumberFormatException e) {
				}

				if (shape.equals("pen")) {
					if (size.equals("xlarge")) {
						twitter.setImageResource(R.drawable.penxlarge);
					} else if (size.equals("large")) {
						twitter.setImageResource(R.drawable.penlarge);
					} else if (size.equals("small")) {
						twitter.setImageResource(R.drawable.pensmall);
					} else if (size.equals("")) {
						twitter.setImageResource(R.drawable.penlarge);
					} else if (size.equals(null)) {
						twitter.setImageResource(R.drawable.penlarge);
					}
				}

				else if (shape.equals("cloud")) {
					if (size.equals("xlarge")) {
						twitter.setImageResource(R.drawable.cloudxlarge);
					} else if (size.equals("large")) {
						twitter.setImageResource(R.drawable.cloudlarge);
					} else if (size.equals("small")) {
						twitter.setImageResource(R.drawable.cloudsmall);
					} else if (size.equals("")) {
						twitter.setImageResource(R.drawable.cloudlarge);
					} else if (size.equals(null)) {
						twitter.setImageResource(R.drawable.cloudlarge);
					}
				}

				else if (shape.equals("twittericon")) {
					if (size.equals("xlarge")) {
						twitter.setImageResource(R.drawable.twitterxlarge);
					} else if (size.equals("large")) {
						twitter.setImageResource(R.drawable.twitterlarge);
					} else if (size.equals("small")) {
						twitter.setImageResource(R.drawable.twittersmal);
					} else if (size.equals("")) {
						twitter.setImageResource(R.drawable.twitterlarge);
					} else if (size.equals(null)) {
						twitter.setImageResource(R.drawable.twitterlarge);
					}
				}

				else if (shape.equals("")) {
					if (size.equals("xlarge")) {
						twitter.setImageResource(R.drawable.penxlarge);
					} else if (size.equals("large")) {
						twitter.setImageResource(R.drawable.penlarge);
					} else if (size.equals("small")) {
						twitter.setImageResource(R.drawable.pensmall);
					} else if (size.equals("")) {
						twitter.setImageResource(R.drawable.penlarge);
					} else if (size.equals(null)) {
						twitter.setImageResource(R.drawable.penlarge);
					}

				} else if (shape.equals(null)) {
					if (size.equals("xlarge")) {
						twitter.setImageResource(R.drawable.penxlarge);
					} else if (size.equals("large")) {
						twitter.setImageResource(R.drawable.penlarge);
					} else if (size.equals("small")) {
						twitter.setImageResource(R.drawable.pensmall);
					} else if (size.equals("")) {
						twitter.setImageResource(R.drawable.penlarge);
					} else if (size.equals(null)) {
						twitter.setImageResource(R.drawable.penlarge);
					}
				}

			}
		} catch (NullPointerException e) {
		}

		twitterp = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		twitterp.gravity = Gravity.TOP | Gravity.LEFT;
		twitterp.x = 0;
		twitterp.y = 0;

		windowManager.addView(twitter, twitterp);

		twitter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				loadon();

				if (open < 40) {

					if (chatheadon.equals("true")) {

					}

					else {

						save("chatheadon", "true");
						loadon();

						xt = twitterp.x + "";
						yt = twitterp.y + "";

						save("x", xt);
						save("y", yt);

						windowManager.removeView(twitter);

						notificationManager.cancel(0);

						StandOutWindow.show(ChatHead.this,
								FloatingActivity.class,
								StandOutWindow.DEFAULT_ID);
					}

				}

			}
		});

		twitter.setOnTouchListener(new View.OnTouchListener() {

			private int initialX, initialY;
			private float initialTouchX, initialTouchY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					open = 0;

					initialX = twitterp.x;
					initialY = twitterp.y;
					initialTouchX = event.getRawX();
					initialTouchY = event.getRawY();

					return false;

				case MotionEvent.ACTION_UP:

					return false;
				case MotionEvent.ACTION_MOVE:

					open++;

					twitterp.x = initialX
							+ (int) (event.getRawX() - initialTouchX);
					twitterp.y = initialY
							+ (int) (event.getRawY() - initialTouchY);

					windowManager.updateViewLayout(twitter, twitterp);

					return false;

				}

				return false;
			}

		});

	}

	public void loadon() {
		chatheadon = spf.getString("chatheadon", "");
	}

	public void loadlocation() {
		xt = spf.getString("x", "");
		yt = spf.getString("y", "");
		ChatHead.twitterp.x = Integer.parseInt(xt);
		ChatHead.twitterp.y = Integer.parseInt(yt);
		ChatHead.windowManager.updateViewLayout(ChatHead.twitter,
				ChatHead.twitterp);

	}

	public void save(String key, String value) {
		edit = spf.edit();
		edit.putString(key, value);
		edit.commit();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (twitter != null) {
			windowManager.removeView(twitter);
			twitter = null;
		}
		notificationManager.cancel(0);

		save("on", "false");
		save("chatheadon", "false");
		loadon();

		StandOutWindow.closeAll(ChatHead.this, FloatingActivity.class);

	}

}
