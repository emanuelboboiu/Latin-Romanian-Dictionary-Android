package ro.limbalatina.dictionarlatinroman;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;

/*
 * This is a class which contains only static methods to play sound in different ways.
 * This class is created by Manu, rewritten on 15 January 2015, early in the morning.
 */

public class SoundPlayer {

    // A method to play sound, a static one:
    @SuppressLint("DiscouragedApi")
    public static void playSimple(Context context, String fileName) {
        if (MainActivity.isSound) {
            int resID;
            resID = context.getResources().getIdentifier(fileName, "raw",
                    context.getPackageName());
            MediaPlayer mp = MediaPlayer.create(context, resID);
            if (mp != null) {
                mp.setOnCompletionListener(MediaPlayer::release);
                mp.start();
            }
        } // end if is sound activated.
    } // end static method playSimple.

} // end sound player class.
