package com.cs478.project.tictactoe;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Handler uiHandler;
    Button newGameButton;
    TicTacToeGame game;                             //Game Thread - UIThread
    Button[][] boardView = new Button[3][3];
    char[][] board;
    ArrayList<Integer> availableMoves;
    TextView status;
    PlayerThread playerx, playero;                  // Two Player Threads

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newGameButton = (Button) findViewById(R.id.newGameButton);
        status = (TextView) findViewById(R.id.currentTurnTextView);

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Main", "New Game Button Clicked");
                if(game!=null) game.interrupt();            // If instance of game is running, kill it
                game=new TicTacToeGame();                   // and start a new game
                game.run();
            }
        });


    }

    class TicTacToeGame extends Thread implements Runnable{

        @Override
        public void run() {
            /**
             * Associate uiHandler with the main looper
             * and overriding handleMessage to implement
             * game logic to handle communication between two
             * player threads.
             */
            uiHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    /**
                     * turn = 0 --> Player O's turn is next
                     * turn = 1 --> Player X's turn is next
                     */
                    int turn = (int) msg.arg1;
                    updateUI();
                    if (checkBoard() == false) {        //Game is still not over
                        Message m1 = new Message();
                        m1.obj = board;                 //Inform players of current state of game
                        if (turn == 1){
                            playerx.threadHandler.sendMessage(m1);      //Let Player X know that its his turn
                            status.setText("Player X's turn");
                        }
                        else {
                            playero.threadHandler.sendMessage(m1);      //Let Player O know that its his turn
                            status.setText("Player O's turn");
                        }
                    }
                    else {
                        /**
                         * If game is over , inform both players so that they will stop process
                         */
                        playero.threadHandler.getLooper().quit();
                        playerx.threadHandler.getLooper().quit();
                    }
                }
            };


            playerx = new PlayerThread('x');
            playero = new PlayerThread('o');
            setboard();                 // Set initial state of board
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
            playerx.threadHandler.sendMessage(msg);     // Give player X first chance
        }

        /**
         * Function check if the game is over
         * @return true if either of players is a winner or if no more moves left
         *         false otherwise
         */
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
                if (availableMoves.size() == 0) {
                    status.setText("Game over. Its a Draw!");
                    status.setTextColor(Color.RED);

                    gameOver = true;
                }
            }
            return gameOver;
        }

        /**
         * UI update function. Runs a runnable function on UI thread to update the
         * board layout.
         *
         */
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

        /**
         * Reset the board to initial setup
         */
        private void setboard() {
            board = new char[3][3];
            status.setText("");
            status.setTextColor(Color.BLACK);
            availableMoves = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
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

    }

    /**
     * Player thread class
     * Will receive messages from UIThread and take turns according to message
     * The class will randomly choose between a smart move and a random move and inform UI thread
     * once it has decided the choice.
     */


    class PlayerThread extends Thread implements Runnable {
        char id;
        char opponent;
        Handler threadHandler;
        final Random random = new Random();

        PlayerThread(char id) {
            this.id = id;
            if(id=='x')
                opponent='o';
            else
                opponent='x';
        }

        @Override
        public void run() {
            Looper.prepare();
            Log.i("Player-" + id, "initiated");
            threadHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    board = (char[][]) msg.obj;
                    Message msg1 = uiHandler.obtainMessage();
                    if (id == 'x') {
                        msg1.obj = board;
                        msg1.arg1 = 0;
                      } else {
                        msg1.obj = board;
                        msg1.arg1 = 1;
                    }
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int position = random.nextInt(availableMoves.size());
                    int choices[]=new int[2];
                    choices[0] = availableMoves.get(position);          // A random move
                    choices[1] = smartMove(board);                      // A smart move
                    int choice = choices[random.nextInt(2)];            //Randomly choose between two choices
                                                                        //So that all the games are not a draw
                    Log.d("Player-"+id,"random:"+choices[0]+" smart:"+choices[1]+" chosen:"+choice);
                    /**
                     * Chosen move should be removed from list of available moves
                     */
                    for(int i=0; i <availableMoves.size(); i++)
                        if(choice==availableMoves.get(i)) {
                            availableMoves.remove(i);
                            break;
                        }
                    board[(choice / 3)][choice % 3] = id;
                    try {
                        Thread.sleep(400L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    uiHandler.sendMessage(msg1);
                }

            };
            Looper.loop();
            Log.i("Player-" + id, "Quiting Thread:" + id);
        }

        private int smartMove(char[][] board) {
            if(availableMoves.contains(0) &&
                    ((board[0][1]==opponent && board[0][2]==opponent) ||
                            (board[1][1]==opponent && board[2][2]==opponent) ||
                            (board[1][0]==opponent && board[2][0]==opponent))) {
                return 0;
            } else if (availableMoves.contains(1) &&
                    ((board[1][1]==opponent && board[2][1]==opponent) ||
                            (board[0][0]==opponent && board[0][2]==opponent))) {
                return 1;
            } else if(availableMoves.contains(2) &&
                    ((board[0][0]==opponent && board[0][1]==opponent) ||
                            (board[2][0]==opponent && board[1][1]==opponent) ||
                            (board[1][2]==opponent && board[2][2]==opponent))) {
                return 2;
            } else if(availableMoves.contains(3) &&
                    ((board[1][1]==opponent && board[1][2]==opponent) ||
                            (board[0][0]==opponent && board[2][0]==opponent))){
                return 3;
            } else if(availableMoves.contains(4) &&
                    ((board[0][0]==opponent && board[2][2]==opponent) ||
                            (board[0][1]==opponent && board[2][1]==opponent) ||
                            (board[2][0]==opponent && board[0][2]==opponent) ||
                            (board[1][0]==opponent && board[1][2]==opponent))) {
                return 4;
            } else if(availableMoves.contains(5) &&
                    ((board[1][0]==opponent && board[1][1]==opponent) ||
                            (board[0][2]==opponent && board[2][2]==opponent))) {
                return 5;
            } else if(availableMoves.contains(6) &&
                    ((board[0][0]==opponent && board[1][0]==opponent) ||
                            (board[2][1]==opponent && board[2][2]==opponent) ||
                            (board[1][1]==opponent && board[0][2]==opponent))){
                return 6;
            } else if(availableMoves.contains(7) &&
                    ((board[0][1]==opponent && board[1][1]==opponent) ||
                            (board[2][0]==opponent && board[2][2]==opponent))) {
                return 7;
            }else if( availableMoves.contains(8) &&
                    ((board[0][0]==opponent && board[1][1]==opponent) ||
                            (board[0][2]==opponent && board[1][2]==opponent) ||
                            (board[2][0]==opponent && board[2][1]==opponent))) {
                return 8;
            }
            return availableMoves.get(random.nextInt(availableMoves.size()));
        }
    }
}
