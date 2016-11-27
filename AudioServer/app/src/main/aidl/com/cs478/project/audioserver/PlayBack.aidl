// PlayBack.aidl
package com.cs478.project.audioserver;

// Declare any non-default types here with import statements

interface PlayBack {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     void playMusic(String str);
       void pauseMusic();
       void resumeMusic();
       void stopMusic();
       String[] getPlayData();
}
