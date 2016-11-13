package com.cs478.project.tictactoe;

import android.graphics.Color;
import android.media.Image;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    Handler handler;
    Button newGame;
    Button[][] boardView = new Button[3][3];
    char[][] board;
    ArrayList<Integer> availableSlot;
    TextView status;
    PlayerThread playerx, playero;
    char turn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newGame = (Button) findViewById(R.id.newGameButton);
        status = (TextView) findViewById(R.id.currentTurnTextView);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int turn = (int) msg.arg1;
                updateUI();
                if (checkBoard() == false) {
                    Message m1 = new Message();
                    m1.obj = board;
                    if (turn == 1){
                        playerx.myhandler.sendMessage(m1);
                        status.setText("Player X's turn");
                    }
                    else {
                        playero.myhandler.sendMessage(m1);
                        status.setText("Player O's turn");
                    }
                }
            }
        };

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Main", "Button Clicked");

                if (playero != null) playero.myhandler.getLooper().quit();
                if (playerx != null) playerx.myhandler.getLooper().quit();
                playerx = new PlayerThread('x');
                playero = new PlayerThread('o');
                setboard();
                playero.start();
                playerx.start();
                Message msg = new Message();
                msg.obj = board;
                Log.i("Main", "Sending Message to x");
                try {
                    Thread.sleep(200L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                playerx.myhandler.sendMessage(msg);
            }
        });


    }

    private boolean checkBoard() {
        boolean gameOver = false;
        if ((board[0][0] == 'o' && board[1][1] == 'o' && board[2][2] == 'o')
                || (board[0][2] == 'o' && board[1][1] == 'o' && board[2][0] == 'o')
                || (board[0][1] == 'o' && board[1][1] == 'o' && board[2][1] == 'o')
                || (board[0][2] == 'o' && board[1][2] == 'o' && board[2][2] == 'o')
                || (board[0][0] == 'o' && board[0][1] == 'o' && board[0][2] == 'o')
                || (board[1][0] == 'o' && board[1][1] == 'o' && board[1][2] == 'o')
                || (board[2][0] == 'o' && board[2][1] == 'o' && board[2][2] == 'o')
                || (board[0][0] == 'o' && board[1][0] == 'o' && board[2][0] == 'o')) {
            status.setText("O wins!");
            status.setTextColor(Color.GREEN);
            gameOver = true;
        } else if ((board[0][0] == 'x' && board[1][1] == 'x' && board[2][2] == 'x')
                || (board[0][2] == 'x' && board[1][1] == 'x' && board[2][0] == 'x')
                || (board[0][1] == 'x' && board[1][1] == 'x' && board[2][1] == 'x')
                || (board[0][2] == 'x' && board[1][2] == 'x' && board[2][2] == 'x')
                || (board[0][0] == 'x' && board[0][1] == 'x' && board[0][2] == 'x')
                || (board[1][0] == 'x' && board[1][1] == 'x' && board[1][2] == 'x')
                || (board[2][0] == 'x' && board[2][1] == 'x' && board[2][2] == 'x')
                || (board[0][0] == 'x' && board[1][0] == 'x' && board[2][0] == 'x')) {

            status.setText("X wins!");
            status.setTextColor(Color.BLUE);

            gameOver = true;
        } else {
            if (availableSlot.size() == 0) {
                status.setText("Game over. Its a Draw!");
                status.setTextColor(Color.RED);

                gameOver = true;
            }


        }
        return gameOver;

    }

    private void updateUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (board[i][j] == 'x')
                            boardView[i][j].setText("X");
                        else if (board[i][j] == 'o')
                            boardView[i][j].setText("O");
                        else
                            boardView[i][j].setText("");
                    }
                }
            }
        });
    }


    private void setboard() {
        board = new char[3][3];
        status.setText("");
        status.setTextColor(Color.BLACK);
        availableSlot = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++)
                board[i][j] = '-';
        }
        boardView[0][0] = (Button) findViewById(R.id.button11);
        boardView[0][1] = (Button) findViewById(R.id.button12);
        boardView[0][2] = (Button) findViewById(R.id.button13);
        boardView[1][0] = (Button) findViewById(R.id.button21);
        boardView[1][1] = (Button) findViewById(R.id.button22);
        boardView[1][2] = (Button) findViewById(R.id.button23);
        boardView[2][0] = (Button) findViewById(R.id.button31);
        boardView[2][1] = (Button) findViewById(R.id.button32);
        boardView[2][2] = (Button) findViewById(R.id.button33);
        updateUI();
    }


    class PlayerThread extends Thread implements Runnable {
        char id;
        Handler myhandler;

        PlayerThread(char id) {
            this.id = id;
        }

        @Override
        public void run() {
            Looper.prepare();
            Log.i("Player-" + id, "initiated");
            int i = 0;
            final Random random = new Random();
            myhandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    Log.i("Player-" + id, "Button Clicked, recieved message by " + id);
                    board = (char[][]) msg.obj;
                    Message msg1 = handler.obtainMessage();

                    if (id == 'x') {
                        msg1.obj = board;
                        msg1.arg1 = 0;
                      } else {
                        msg1.obj = board;
                        msg1.arg1 = 1;
                    }
                    int position = random.nextInt(availableSlot.size());
                    int choice = availableSlot.get(position);
                    Log.d("Player-" + id, "choice " + choice + " ->" + ((choice / 3)) + "," + (choice % 3));
                    availableSlot.remove(position);
                    board[(choice / 3)][choice % 3] = id;
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendMessage(msg1);
                }

            };
            Looper.loop();
        }
    }
}
