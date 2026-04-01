package com.filip.safealert;

import android.content.Intent;
import android.speech.RecognizerIntent;

public class VoiceHelper {
    public static Intent obtineIntentAscultare() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "AJUTOR");
        return intent;
    }
}