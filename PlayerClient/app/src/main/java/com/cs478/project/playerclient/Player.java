package com.cs478.project.playerclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import com.cs478.project.service.*;

public class Player extends AppCompatActivity {
    private static final String LOG_TAG = Player.class.getSimpleName();
    public static PlayBack mPlaybackService;
    private boolean mIsBound = false;
    String clip;
    boolean nowPlaying = false;


    //Clip Number Edit Text
    TextView clipDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Button audioRecords = (Button) findViewById(R.id.audioRecords); //Button for DB records
        clipDialog = (TextView) findViewById(R.id.clipNumber); // User input
        final Button play = (Button) findViewById(R.id.play);
        final Button pause = (Button) findViewById(R.id.pause);
        final Button stop = (Button) findViewById(R.id.stop);
        final Button resume = (Button) findViewById(R.id.resume);
        stop.setEnabled(false);
        pause.setEnabled(false);
        resume.setEnabled(false);
        final NumberPicker clipNumber = (NumberPicker) findViewById(R.id.numberPicker);
        clipNumber.setMinValue(1);
        clipNumber.setMaxValue(5);
        clipNumber.setElevation(2);
        clipNumber.setBackgroundColor(Color.LTGRAY);

        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if (mIsBound) {
                        clip = "clip" + clipNumber.getValue();
                        mPlaybackService.playMusic(clip);
                      //  clipNumber.setEnabled(false);
                        resume.setEnabled(false);
                        stop.setEnabled(true);
                        pause.setEnabled(true);
                        nowPlaying = true;
                    } else {
                        Log.d(LOG_TAG, "Playback service not bound in client");
                    }
                } catch (RemoteException e) {
                    Log.e(LOG_TAG, e.toString());
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if (mIsBound) {
                        mPlaybackService.pauseMusic();
                        resume.setEnabled(true);
                        clipNumber.setEnabled(true);
                    } else {
                        Log.d(LOG_TAG, "Playback service not bound in client");
                    }
                } catch (RemoteException e) {
                    Log.e(LOG_TAG, e.toString());
                }
            }
        });

        //resume button click setup
        resume.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if (mIsBound) {
                        mPlaybackService.resumeMusic();
                        pause.setEnabled(true);
                        if (nowPlaying) {
                            clipNumber.setEnabled(false);
                        } else {
                            clipNumber.setEnabled(true);
                        }
                    } else {
                        Log.d(LOG_TAG, "Playback service not bound in client");
                    }
                } catch (RemoteException e) {
                    Log.e(LOG_TAG, e.toString());
                }
            }
        });

        //stop button onClick
        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if (mIsBound) {
                        mPlaybackService.stopMusic();
                        resume.setEnabled(false);
                        stop.setEnabled(false);
                        pause.setEnabled(false);
                        clipNumber.setEnabled(true);
                    } else {
                        Log.d(LOG_TAG, "Playback service not bound in client");
                    }
                } catch (RemoteException e) {
                    Log.e(LOG_TAG, e.toString());
                }
            }
        });


        //Calling activity for all records
        audioRecords.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

        });
    }

    // Bind to Playback Service
    @Override
    protected void onResume() {
        super.onResume();

        if (!mIsBound) {

            boolean bound = false;
            Intent i = new Intent(PlayBack.class.getName());

            ResolveInfo info = getPackageManager().resolveService(i, PackageManager.MATCH_ALL);
            i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));

            bound = bindService(i, this.mConnection, Context.BIND_AUTO_CREATE);
            if (bound) {
                Log.d(LOG_TAG, "bindService() succeeded!");
            } else {
                Log.d(LOG_TAG, "bindService() failed!");
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder iservice) {
            mPlaybackService = PlayBack.Stub.asInterface(iservice);
            mIsBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            mPlaybackService = null;
            mIsBound = false;
        }
    };

    // Stop Service on destroy of Client
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsBound) {
            //unbind service on app destroy
            unbindService(this.mConnection);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}

