package com.cs478.project.audioserver;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class PlayBackImpl extends Service {

    protected static final String LOG_TAG = PlayBackImpl.class.getSimpleName();
    private MediaPlayer mediaPlayer;
    private AudioDatabase audioDatabase;
    String currentSong = "";

    public PlayBackImpl() {
    }

    public void onCreate() {
        audioDatabase = new AudioDatabase(this); //Initialize database
    }

    // Implement the Stub for this Object
    private final PlayBack.Stub mBinder = new PlayBack.Stub() {
        public void playMusic(String song) {
            String songSrc = "android.resource://" + getApplicationContext().getPackageName() + "/raw/" + song;
            currentSong = song;
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
            }

            //Initialize media player
            mediaPlayer = new MediaPlayer();

            try {
                //set player data source
                mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(songSrc));
            } catch (IOException e) {
                Log.i(LOG_TAG, e.toString());
            }

            try {
                mediaPlayer.prepare();       //prevent two songs playing simultaneously
            } catch (IOException e) {
                Log.i(LOG_TAG, e.toString());
            }

            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(0);                // Start from beginning
                } else {
                    mediaPlayer.seekTo(0);
                    mediaPlayer.start();
                }

                //Using simple data format to store time
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();
                String time = simpleDateFormat.format(currentDate);
                //Record keeping
                audioDatabase.insertSong(song + " Play", time);
                Log.i(LOG_TAG, "Playback Service bound in the server");
            } else {
                Log.i(LOG_TAG, "Playback Service not bound in the server");
            }
        }

        // Implement the pause method
        public void pauseMusic() {
            if (null != mediaPlayer) {
                if (mediaPlayer.isPlaying()) {
                    // Pause playing song
                    mediaPlayer.pause();
                    if (!currentSong.equals("")) {
                        //Same time fetching operation
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Calendar calendar = Calendar.getInstance();
                        Date currentDate = calendar.getTime();
                        String time = simpleDateFormat.format(currentDate);
                        //Inserting data
                        audioDatabase.insertSong(currentSong + " Paused", time);
                    }
                }
            } else {
                Log.i(LOG_TAG, "Playback Service not bound in the server");
                }
        }

        public void resumeMusic() {
         if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    //do nothing
                } else {
                    // Start song
                    mediaPlayer.start();
                    if (!currentSong.equals("")) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Calendar calendar = Calendar.getInstance();
                        Date currentDate = calendar.getTime(); // set the current datetime in a Date-object
                        String time = simpleDateFormat.format(currentDate);
                        audioDatabase.insertSong(currentSong + " Resumed", time);
                    }
                }
                Log.i(LOG_TAG, "Playback Service bound in the server");
            } else {
                Log.i(LOG_TAG, "Playback Service not bound in the server");
            }
        }

        public void stopMusic() {
            if (null != mediaPlayer) {
                if (mediaPlayer.isPlaying()) {
                    // Pause song
                    mediaPlayer.pause();
                    //Seek to start
                    mediaPlayer.seekTo(0);

                    if (!currentSong.equals("")) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Calendar calendar = Calendar.getInstance();

                        Date currentDate = calendar.getTime(); // set the current datetime in a Date-object

                        String time = simpleDateFormat.format(currentDate);
                        audioDatabase.insertSong(currentSong + " Stopped", time);
                    }
                } else {
                    if (mediaPlayer.getCurrentPosition() != 0) {
                        mediaPlayer.seekTo(0);
                        if (!currentSong.equals("")) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Calendar calendar = Calendar.getInstance();
                            Date now = calendar.getTime();
                            String time = simpleDateFormat.format(now);
                            audioDatabase.insertSong(currentSong + " Stopped", time);
                        }
                    }
                }
            } else {
                Log.i(LOG_TAG, "Playback Service not stopped in the server");
            }
        }

         public String[] getPlayData() {
            String songs[] = audioDatabase.getAll();
            return songs;
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
