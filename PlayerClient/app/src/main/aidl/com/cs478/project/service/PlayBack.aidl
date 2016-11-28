// PlayBack.aidl
package com.cs478.project.service;

// Declare any non-default types here with import statements

interface PlayBack {
     void playMusic(String str);
       void pauseMusic();
       void resumeMusic();
       void stopMusic();
       String[] getPlayData();
}
