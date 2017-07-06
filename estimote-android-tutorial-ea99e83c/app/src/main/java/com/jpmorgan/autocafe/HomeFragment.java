package com.jpmorgan.autocafe;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by tapas on 2/14/2017.
 */
public class HomeFragment extends Fragment implements TextToSpeech.OnInitListener {
    private FloatingActionButton fab;
    private TextView voiceInput;
    private EditText mobileView;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private TextToSpeech tts;
    private String whatToDo = "";
    private String toSpeak = "";
    private Button mobileViewButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tts = new TextToSpeech(getActivity(), this);

        voiceInput = (TextView) view.findViewById(R.id.voiceInput);
        mobileViewButton = (Button) view.findViewById(R.id.mobileViewButton);
        mobileView = (EditText) view.findViewById(R.id.mobileView);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askSpeechInput();
            }
        });

        mobileViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobileViewString = mobileView.getText().toString();
                if (TextUtils.isEmpty(mobileViewString)) {
                    Toast.makeText(getActivity(), "Please Enter URL", Toast.LENGTH_SHORT).show();
                    speakOut("Please Enter URL", 1);
                    return;
                } else {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mobileViewString));
                    intent.setPackage("com.good.gdgma");
                    startActivity(intent);
                }

            }
        });

        return view;
    }

// Showing google speech input dialog

    private void askSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hungry , Try me !!!");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    // Receiving speech input

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == Activity.RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    voiceInput.setText(result.get(0));

                    Toast.makeText(getActivity(), "Speech Detected: " + result.get(0), Toast.LENGTH_SHORT).show();

                    if (!TextUtils.isEmpty(result.get(0))) {
                        String textFromVoice = result.get(0);
                        whatToDo = "0";
                        toSpeak = "Going to ";
                        // Decision
                        if (textFromVoice.contains("go to") || textFromVoice.contains("move to")) {
                            whatToDo = "0";
                            toSpeak = "Going to ";
                        }

                        // Where to Navigate
                        if (textFromVoice.contains("home")) {
                            whatToDo = whatToDo + "0";
                            toSpeak = toSpeak + "Home Page";
                        } else if (textFromVoice.contains("smart cafe")) {
                            whatToDo = whatToDo + "1";
                            toSpeak = toSpeak + "Smart Cafe";
                        } else if (textFromVoice.contains("menu")) {
                            whatToDo = whatToDo + "2";
                            toSpeak = toSpeak + "Menu Page";
                        } else if (textFromVoice.contains("sign out")) {
                            whatToDo = whatToDo + "3";
                            toSpeak = toSpeak + "Sign Out";
                        } else if (textFromVoice.contains("about")) {
                            whatToDo = whatToDo + "4";
                            toSpeak = toSpeak + "About Us Page";
                        }

                        // Special Condition
                        if (textFromVoice.contains("hungry")) {
                            whatToDo = "99999999";
                            toSpeak = "Looks like you are very hungry. Let's use Smart Cafe";
                        }

                        if (textFromVoice.contains("love")) {
                            whatToDo = "999999999";
                            toSpeak = "I love you too. Let's have food first.";
                        }

                        if (textFromVoice.contains("juice") || textFromVoice.contains("biryani") || textFromVoice.contains("snacks") || textFromVoice.contains("tea")) {
                            whatToDo = "9999999999";
                            toSpeak = "Let's check today's menu.";
                        }

                        Toast.makeText(getActivity(), "" + toSpeak, Toast.LENGTH_SHORT).show();
                        if (whatToDo.length() >= 2) {
                            speakOut(toSpeak, 0);
                        } else {
                            speakOut("We couldn't match your requirement. Please try again", 1);
                        }

                    }

                }
                break;
            }

        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.i("TTS", "This Language is not supported");
                Toast.makeText(getActivity(), "This Language is not supported", Toast.LENGTH_SHORT).show();
            } else {
                String greeting = "";
                Calendar c = Calendar.getInstance();
                int hours = c.get(Calendar.HOUR_OF_DAY);
                if (hours >= 1 && hours < 12) {
                    greeting = "Good Morning! ";
                } else if (hours >= 12 && hours < 16) {
                    greeting = "Good Afternoon! ";
                } else if (hours >= 16 && hours < 21) {
                    greeting = "Good Evening! ";
                } else if (hours >= 21 && hours < 24) {
                    greeting = "Good Night! ";
                }

                speakOut(greeting + "Welcome to Auto Cafe. How may I help you today ?", 0);
            }

        } else {
            Log.i("TTS", "Initilization Failed!");
        }
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private void speakOut(final String task, final int common) {
        tts.speak(task, TextToSpeech.QUEUE_ADD, null);
        if (common == 0) {
            final Handler h = new Handler();
            Runnable r = new Runnable() {
                public void run() {
                    if (!tts.isSpeaking()) {
                        onTTSSpeechFinished();
                    }
                    h.postDelayed(this, 1000);
                }
            };
            h.postDelayed(r, 1000);
        }
    }

    private void onTTSSpeechFinished() {
        if (getActivity() != null) {
            switch (whatToDo) {
                case "00":
                    ((HomeActivity) getActivity()).goToHomeFragment();
                    break;

                case "01":
                    ((HomeActivity) getActivity()).goToSmartFragment();
                    break;

                case "02":
                    ((HomeActivity) getActivity()).goToMenuFragment();
                    break;

                case "03":
                    ((HomeActivity) getActivity()).goToSignOutFragment();
                    break;

                case "04":
                    ((HomeActivity) getActivity()).goToAboutusFragment();
                    break;

                case "99999999":
                    ((HomeActivity) getActivity()).goToSmartFragment();
                    break;

                case "999999999":
                    ((HomeActivity) getActivity()).goToMenuFragment();
                    break;

                case "9999999999":
                    ((HomeActivity) getActivity()).goToMenuFragment();
                    break;

                default:
                    // Do Nothing
                    break;
            }
        }
        whatToDo = "";
        toSpeak = "";
    }
}
