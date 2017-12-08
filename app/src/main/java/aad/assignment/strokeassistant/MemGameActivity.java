package aad.assignment.strokeassistant;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.concurrent.ThreadLocalRandom;

import java.util.ArrayList;

public class MemGameActivity extends AppCompatActivity{
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    GameAdapter adapter;
    ArrayList<Integer> arrayOfColors=new ArrayList<>();
    Button btnYellow;
    Button btnBlue;
    ListView listView;
    TextView txtScore;
    TextView txtMessage;
    TextView txtTimer;
    CountDownTimer timer;
    static Dialog dialog ;

    private final static String PREFERENCES_KEY ="mem_game";
    int highestScore;
    int currentScore =0;
    private static final int MAX_NUM_OF_BALLS =999999;
    private static final int GAME_DURATION =30000;
    private static final int TIME_INTERVAL =1000;
    private static final int YELLOW_BALL=0;
    private static final int BLUE_BALL=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem_game);
        sharedPreferencesInit();
        generateBalls();
        initView();


        listView.setAdapter(adapter);
        listView.setSelection(listView.getAdapter().getCount()-1);
        listView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }
        });

        btnYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluateGuessResult(v.getId());
            }
        });
        btnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluateGuessResult(v.getId());
            }
        });

        txtScore.setText(generateHighestScore(highestScore));
        txtTimer.setText(String.format(getString(R.string.time),GAME_DURATION / 1000));


        timer= new CountDownTimer(GAME_DURATION, TIME_INTERVAL) {

            public void onTick(long millisUntilFinished) {

                txtTimer.setText(String.format(getString(R.string.time),(millisUntilFinished / 1000)));
            }

            public void onFinish() {
                stopGame();
            }
        };
        dialog= new Dialog(MemGameActivity.this);
        dialog.setContentView((R.layout.game_custom_dialog));
        dialog.setTitle(generateHighestScore(highestScore));
        dialog.setCancelable(false);
        final Button start = (Button) dialog.findViewById(R.id.btn_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
        dialog.show();
    }

    //stop the game
    private void stopGame() {
        txtTimer.setText(R.string.over);
        if(currentScore > highestScore) {
            highestScore = currentScore;
            dialog.setTitle( generateHighestScore(highestScore));
            updateHighestScore();
        }
        btnYellow.setClickable(false);
        btnBlue.setClickable(false);
        txtMessage.setText(R.string.time_is_up);
        showMessage();
        dialog.show();
    }

    private String generateHighestScore(int highestScore) {
        return String.format(getString(+R.string.game_score),highestScore,getString(R.string.highest));
    }


    //Start the game
    private void startGame() {
        timer.start();
        btnYellow.setClickable(true);
        btnBlue.setClickable(true);

        txtMessage.setText(R.string.wrong_please_try_again);
        hideMessage();
        currentScore =0;
        dialog.dismiss();
    }

    //update the highest score in the database if the user get the highest score
    private void updateHighestScore() {
        editor  = sharedPreferences.edit();
        editor.remove("highestScore");
        editor.putInt("highestScore", currentScore);
        editor.apply();
    }

    //find highest score previously, if not, return 0
    private void sharedPreferencesInit() {
        sharedPreferences=getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        highestScore = sharedPreferences.getInt("highestScore", 0);
    }

    //initialize balls in the array
    private void generateBalls() {
        //0 is yellow, 1 is blue
        for(int i = 0; i< MAX_NUM_OF_BALLS; i++){
            int randomNum = ThreadLocalRandom.current().nextInt(YELLOW_BALL, BLUE_BALL + 1);
            arrayOfColors.add(randomNum);
        }
    }

    //initialize view
    private void initView() {
        adapter = new GameAdapter(this, arrayOfColors);
        listView = (ListView) findViewById(R.id.list_view_ball);
        btnYellow =(Button) findViewById(R.id.btn_yellow);
        btnBlue =(Button) findViewById(R.id.btn_blue);
        txtScore =(TextView) findViewById(R.id.txt_score) ;
        txtMessage =(TextView) findViewById(R.id.txt_message) ;
        txtTimer =(TextView) findViewById(R.id.txt_timer);
    }

    //evaluate whether user guess right.If user guessed it right, output result. Otherwise, show user that he answered incorrectly
    private void evaluateGuessResult(int buttonId) {

        boolean isYellowLast = false;
        boolean isBlueLast = false;

        if(!adapter.isEmpty()){
            isYellowLast=adapter.getItem(adapter.size() - 1) == YELLOW_BALL;
            isBlueLast=  adapter.getItem(adapter.size() - 1) == BLUE_BALL;
        }


        boolean yellowClicked = buttonId == R.id.btn_yellow;
        boolean blueClicked = buttonId == R.id.btn_blue;

        boolean correctPrediction = (yellowClicked && isYellowLast) || (blueClicked && isBlueLast);

        if(adapter.isEmpty())
            showTheEnd();
        else if(correctPrediction)
            updateScore();
        else
            showMessage();

    }

    private void showMessage() {
        txtMessage.setVisibility(View.VISIBLE);
    }
    private void hideMessage() {
        txtMessage.setVisibility(View.INVISIBLE);
    }

    private void showTheEnd() {
        txtMessage.setText(getText(R.string.game_ending));
        showMessage();
    }

    //update user current score
    private void updateScore() {
        adapter.removeLastItem();
        hideMessage();
        currentScore++;
        txtScore.setText(String.format(getString(+R.string.game_score),currentScore,""));
    }


    //stop timer on pause to avoid badTokenException when user wanted to quit mid-game
    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    //to ask user start the game.
    @Override
    protected void onResume() {
        super.onResume();
        if(!dialog.isShowing())
            dialog.show();
    }
}
