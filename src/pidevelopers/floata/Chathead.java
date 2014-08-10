package pidevelopers.floata;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

public class Chathead extends Service {

	static WindowManager windowManager;
	static WindowManager.LayoutParams twitterp;
	static ImageView   twitter;

	static String access_token, access_token_secret;
	String on,r114 ,r111;
	static SharedPreferences spf;
	static String xt ;
	static String yt ;
	int n,iii;

	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		showNotification();
		loadM();
		
		
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		twitter = new ImageView(this);
		twitter.setImageResource(R.drawable.ic_launcher);

		loadr();
		if (r111.equals(null)){
			twitter.setImageResource(R.drawable.ic_launcher);
		}
			
		else if(r111.equals("1")){
        	twitter.setImageResource(R.drawable.ic_launcher);

        }
        else if(r111.equals("2")){
        	twitter.setImageResource(R.drawable.twitter);
        }
        else if(r111.equals("3")){
        	twitter.setImageResource(R.drawable.ideabubble);
        }      else if(r111.equals("")){
        	twitter.setImageResource(R.drawable.ideabubble);
        }
        
		
		if(r114.equals(null) || r114.equals("")){
			twitter.setAlpha(255);
		}else{
		
			twitter.setAlpha(Integer.parseInt(r114));


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
                // TODO Auto-generated method stub
                iii++ ;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {

                        iii = 0;
                    }
                };
                if(iii==1){
                	
                	
                    handler.postDelayed(r, 250);
                }else if(iii == 2){
                	
                	 iii = 0;
                	 
                	 
     				loadon();

    				if (on.equals("true")) {
    					
    					


    						savechat("on", "false");
    						loadon();
    						
    					

    						Intent i = new Intent(Chathead.this, Broadcast.class);

    						getApplicationContext().sendBroadcast(i);

    					
    				
    				} else {

    					savechat("on", "true");
    					loadon();
    					
    					String xt = twitterp.x+"";
    					String yt = twitterp.y+"";
    					
    					
    					savechat("x", xt);
    					savechat("y", yt);

    					
    					twitterp.x = 0;
    					twitterp.y = 0;

    					windowManager.updateViewLayout(twitter, twitterp);

    					Intent i = new Intent(Chathead.this, Broadcast.class);

    					getApplicationContext().sendBroadcast(i);

    					access_token = MainActivity.spf.getString("ACCESS_TOKEN",
    							"");
    					access_token_secret = MainActivity.spf.getString(
    							"ACCESS_SECRET", "");

    				

    				}
                	
                	
                	
                	
                
             }


            }
        });
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

		

		twitter.setOnTouchListener(new View.OnTouchListener() {

			private int initialX;
			private int initialY;
			private float initialTouchX;
			private float initialTouchY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				loadon();

				
				

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					initialX = twitterp.x;
					initialY = twitterp.y;
					initialTouchX = event.getRawX();
					initialTouchY = event.getRawY();
					
					
					
					/////////
					
					

					
					

					return false;

				case MotionEvent.ACTION_UP:

					return false;
				case MotionEvent.ACTION_MOVE:
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

	
	public void loadr() {

		Chathead.spf = PreferenceManager.getDefaultSharedPreferences(this);
		r111 = Chathead.spf.getString("image", "");
		
	}

	public void loadM() {

		Chathead.spf = PreferenceManager.getDefaultSharedPreferences(this);
		r114 = Chathead.spf.getString("tran", "");
		
	}
	public void savemain(String key, String value) {
		MainActivity.spf = PreferenceManager.getDefaultSharedPreferences(this);
		Editor edit = MainActivity.spf.edit();
		edit.putString(key, value);
		edit.commit();

	}

	public void savechat(String key, String value) {
		Chathead.spf = PreferenceManager.getDefaultSharedPreferences(this);
		Editor edit = Chathead.spf.edit();
		edit.putString(key, value);
		edit.commit();

	}

	public void loadon() {

		Chathead.spf = PreferenceManager.getDefaultSharedPreferences(this);
		on = Chathead.spf.getString("on", "");

	}

	

	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (twitter != null)
			windowManager.removeView(twitter);
		savemain("floata", "");
	}

	
	@SuppressWarnings("deprecation")
	private void showNotification() {

		Notification notification = new Notification(R.drawable.ic_launcher, "Floata is Running", System.currentTimeMillis());

		Intent main = new Intent(this, Close.class);
		main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, main, PendingIntent.FLAG_UPDATE_CURRENT);

		notification.setLatestEventInfo(this, "Floata",
				"Click to dismiss", pendingIntent);
		notification.flags |= Notification.FLAG_ONGOING_EVENT
				| Notification.FLAG_FOREGROUND_SERVICE
				| Notification.FLAG_NO_CLEAR;

		startForeground(2, notification);

	}
	
}