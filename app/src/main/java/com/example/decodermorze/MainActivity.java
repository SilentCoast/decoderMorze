package com.example.decodermorze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextToSpeech textToSpeech;
    Locale locale = new Locale("RU");
    Map<Character,String> dictionaryText;
    Map<String,Character> dictionaryMorze;
    TextView textViewOutput;
    EditText textViewInputText;
    EditText textViewInputMorze;
    Button buttonTranslate;
    String response;
    char[] request;
    RadioButton radioButtonToText;
    RadioButton radioButtonFromText;
    RadioGroup radioGroup;
    MediaPlayer mpDot = new MediaPlayer();
    MediaPlayer mpTire = new MediaPlayer();
    int dotMillSecs = 100+50;
    int tireMillSecs = 300+50;
    AsyncTask morzeBackground;
    CheckBox checkBoxSound;
    String[] words;
    String[] letters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewOutput = findViewById(R.id.textViewOutput);
        textViewInputText = findViewById(R.id.textViewInputText);
        textViewInputMorze = findViewById(R.id.textViewInputMorze);
        buttonTranslate = findViewById(R.id.buttonTranslate);
        radioButtonFromText = findViewById(R.id.radioButtonFromText);
        radioButtonToText =  findViewById(R.id.radioButtonToText);
        radioGroup = findViewById(R.id.radioGroup);
        checkBoxSound = findViewById(R.id.checkBoxSound);
        MorzeBackground beforeThread;

        //default fromText
        radioButtonFromText.setChecked(true);
        textViewInputText.setVisibility(View.VISIBLE);
        textViewInputMorze.setVisibility(View.INVISIBLE);




        /* dictionaryText setting*/
        {
            dictionaryText = new HashMap<>();
            dictionaryText.put('а', ".-");
            dictionaryText.put('б', "-...");
            dictionaryText.put('в', ".--");
            dictionaryText.put('г', "--.");
            dictionaryText.put('д', "-..");
            dictionaryText.put('е', ".");
            dictionaryText.put('ж', "...-");
            dictionaryText.put('з', "--..");
            dictionaryText.put('и', "..");
            dictionaryText.put('й', ".---");
            dictionaryText.put('к', "-.-");
            dictionaryText.put('л', ".-..");
            dictionaryText.put('м', "--");
            dictionaryText.put('н', "-.");
            dictionaryText.put('о', "---");
            dictionaryText.put('п', ".--.");
            dictionaryText.put('р', ".-.");
            dictionaryText.put('с', "...");
            dictionaryText.put('т', "-");
            dictionaryText.put('у', "..-");
            dictionaryText.put('ф', "..-.");
            dictionaryText.put('х', "....");
            dictionaryText.put('ц', "-.-.");
            dictionaryText.put('ч', "---.");
            dictionaryText.put('ш', "----");
            dictionaryText.put('щ', "--.-");
            dictionaryText.put('ъ', ".--.-.");
            dictionaryText.put('ы', "-.--");
            dictionaryText.put('ь', "-..-");
            dictionaryText.put('э', "..-..");
            dictionaryText.put('ю', "..--");
            dictionaryText.put('я', ".-.-");
            dictionaryText.put(' ', "  ");
            dictionaryText.put('0', "-----");
            dictionaryText.put('1', ".----");
            dictionaryText.put('2', "..---");
            dictionaryText.put('3', "...--");
            dictionaryText.put('4', "....-");
            dictionaryText.put('5', ".....");
            dictionaryText.put('6', "-....");
            dictionaryText.put('7', "--...");
            dictionaryText.put('8', "---..");
            dictionaryText.put('9', "----.");

        }

        // dictionaryMorze settings
        {
            dictionaryMorze = new HashMap<>();
            dictionaryMorze.put(".-", 'а');
            dictionaryMorze.put("-...", 'б');
            dictionaryMorze.put(".--", 'в');
            dictionaryMorze.put("--.", 'г');
            dictionaryMorze.put("-..", 'д');
            dictionaryMorze.put(".", 'е');
            dictionaryMorze.put("...-", 'ж');
            dictionaryMorze.put("--..", 'з');
            dictionaryMorze.put("..", 'и');
            dictionaryMorze.put(".---", 'й');
            dictionaryMorze.put("-.-", 'к');
            dictionaryMorze.put(".-..", 'л');
            dictionaryMorze.put("--", 'м');
            dictionaryMorze.put("-.", 'н');
            dictionaryMorze.put("---", 'о');
            dictionaryMorze.put(".--.", 'п');
            dictionaryMorze.put(".-.", 'р');
            dictionaryMorze.put("...", 'с');
            dictionaryMorze.put("-", 'т');
            dictionaryMorze.put("..-", 'у');
            dictionaryMorze.put("..-.", 'ф');
            dictionaryMorze.put("....", 'х');
            dictionaryMorze.put("-.-.", 'ц');
            dictionaryMorze.put("---.", 'ч');
            dictionaryMorze.put("----", 'ш');
            dictionaryMorze.put("--.-", 'щ');
            dictionaryMorze.put(".--.-.", 'ъ');
            dictionaryMorze.put("-.--", 'ы');
            dictionaryMorze.put("-..-", 'ь');
            dictionaryMorze.put("..-..", 'э');
            dictionaryMorze.put("..--", 'ю');
            dictionaryMorze.put(".-.-", 'я');
            dictionaryMorze.put("  ", ' ');
            dictionaryMorze.put("-----", '0');
            dictionaryMorze.put(".----", '1');
            dictionaryMorze.put("..---", '2');
            dictionaryMorze.put("...--", '3');
            dictionaryMorze.put("....-", '4');
            dictionaryMorze.put(".....", '5');
            dictionaryMorze.put("-....", '6');
            dictionaryMorze.put("--...", '7');
            dictionaryMorze.put("---..", '8');
            dictionaryMorze.put("----.", '9');
        }

        //Media players

        mpDot.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
                mp=null;
            }
        });


        mpTire.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
                mp=null;
            }
        });





        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButtonFromText:
                        textViewInputText.setVisibility(View.VISIBLE);
                        textViewInputMorze.setVisibility(View.INVISIBLE);


                        break;
                    case R.id.radioButtonToText:
                        textViewInputText.setVisibility(View.INVISIBLE);
                        textViewInputMorze.setVisibility(View.VISIBLE);
                        textViewOutput.setText("Правила ввода:\nПосле каждого символа(буквы или цифры) ставьте пробел \nПосле каждого слова ставьте ДВА пробела");
                }
            }
        });


        textToSpeech= new TextToSpeech(getApplicationContext(),i -> textToSpeech.setLanguage(locale));

        checkBoxSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(!b){
                        if (morzeBackground != null) {
                            morzeBackground.cancel(false);
                        }
                        textToSpeech.stop();
                    }
            }
        });

        buttonTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(morzeBackground!=null){
                    morzeBackground.cancel(false);
                }
                response ="";



                if(radioButtonFromText.isChecked()) {
                    request = textViewInputText.getText().toString().toLowerCase(Locale.ROOT).toCharArray();
                    for (int i = 0; i < textViewInputText.length(); i++) {

                        response += dictionaryText.get(request[i]);
                    }

                    textViewOutput.setText(response);


                    if (checkBoxSound.isChecked()) {

                        morzeBackground = new MorzeBackground().execute();
                    }


                }

                if(radioButtonToText.isChecked()){

                        words = textViewInputMorze.getText().toString().split("  ");

                        for(int j = 0; j< words.length; j++){
                            letters = words[j].split(" ");
                            for(int i =0;i<letters.length;i++){
                                response+=dictionaryMorze.get(letters[i]);
                            }
                            response+=" ";
                        }


                    textViewOutput.setText(response);
                        if(checkBoxSound.isChecked()){
                            textToSpeech.speak(response,TextToSpeech.QUEUE_FLUSH,null);
                        }

                }

            }
        });


    }

        public class MorzeBackground extends AsyncTask<Void,Void,Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                //makes sound and take on/off flashlight
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                    String cameraId = null;


                    for (int i = 0; i < response.length(); i++) {
                        if(isCancelled()){
                            return null;
                        }
                        try{
                            if (response.toCharArray()[i] == '.') {

                                cameraId = camManager.getCameraIdList()[0];
                                camManager.setTorchMode(cameraId, true);
                                mpDot = MediaPlayer.create(getApplicationContext(), R.raw.dotbeep);
                                mpDot.start();
                                Thread.sleep(dotMillSecs);
                                camManager.setTorchMode(cameraId, false);
                                Thread.sleep(dotMillSecs);

                            }
                            if (response.toCharArray()[i] == '-') {

                                cameraId = camManager.getCameraIdList()[0];
                                camManager.setTorchMode(cameraId, true);
                                mpTire = MediaPlayer.create(getApplicationContext(),R.raw.tirebeep);
                                mpTire.start();
                                Thread.sleep(tireMillSecs);
                                camManager.setTorchMode(cameraId, false);
                                Thread.sleep(dotMillSecs);

                            }
                            if(response.toCharArray()[i] == ' '){
                                Thread.sleep(3*dotMillSecs);
                            }
                            else {
                                Thread.sleep(dotMillSecs);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                return null;
            }
        }


}