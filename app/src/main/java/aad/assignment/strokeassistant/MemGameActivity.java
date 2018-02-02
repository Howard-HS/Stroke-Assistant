package aad.assignment.strokeassistant;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

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
    private static final int ANIMATION_DURATION=200;
    MediaPlayer wrongSound;
    TextView txtHighestScore;
    Animation anim;
    Animation anim2;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem_game);
        wrongSound=MediaPlayer.create(getApplicationContext(),R.raw.wrong);
        anim2=AnimationUtils.loadAnimation(
                this, R.anim.slide_in_top
        );
        anim2.setDuration(100);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        txtScore.setText(String.format(getString(+R.string.game_score),currentScore,""));
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
        dialog.setTitle(R.string.activity_mem_game);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish();
                    dialog.dismiss();
                }
                return true;
            }
        });

        txtHighestScore=(TextView) dialog.findViewById(R.id.highest_score);
        txtHighestScore.setText(generateHighestScore(highestScore));

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
        txtTimer.setText(String.format(getString(R.string.time),0));
        //txtTimer.setText(R.string.over);
        if(currentScore > highestScore) {
            highestScore = currentScore;
            txtHighestScore.setText(generateHighestScore(highestScore));
            updateHighestScore();
        }
        btnYellow.setClickable(false);
        btnBlue.setClickable(false);
        currentScore=0;
        txtScore.setText(String.format(getString(+R.string.game_score),currentScore,""));
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

        if(yellowClicked )
        {
            anim= AnimationUtils.loadAnimation(
                    this, R.anim.slide_out_left
            );
        }
        else{
            anim= AnimationUtils.loadAnimation(
                    this, R.anim.slide_out_right
            );

        }
        anim.setDuration(ANIMATION_DURATION);

        if(adapter.isEmpty())
            showTheEnd();
        else if(correctPrediction)
            updateScore();
        else if(yellowClicked || blueClicked )
           alertWrong();

    }

    private void alertWrong() {
       if(!wrongSound.isPlaying())
           wrongSound.start();
    }

    private void showTheEnd() {
        txtMessage.setText(getText(R.string.game_ending));
        txtMessage.setVisibility(View.VISIBLE);
    }

    //update user current score
    private void updateScore() {

        final int lastChildPos=listView.getChildCount()-1;
        listView.getChildAt(lastChildPos).startAnimation(anim );
        for (int i=0;i<lastChildPos;i++)
            listView.getChildAt(i).startAnimation(anim2);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                btnYellow.setClickable(false);
                btnBlue.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                adapter.removeLastItem();
                btnYellow.setClickable(true);
                btnBlue.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

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
            stopGame();
            dialog.show();
    }
}
