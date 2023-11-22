package com.example.lesson2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.Objects;
import java.util.Random;

//красный - была бомба
//синий-предполагаемая бомба
//зелёный - чистое поле
public class MainActivity extends AppCompatActivity {
    static String TAG = "MAINACTIVITY";

    Button[][] cells;
    final int WIDTH = 7;
    final int HEIGHT = 7;
    TextView mines;
    Random random = new Random();
    double rand = (random.nextInt(10) + 1) / 10;

    int MINESCONST = 5;
    int minecurrents = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mines = findViewById(R.id.Mines);
        mines.setText("" + minecurrents + "/" + MINESCONST);
        generate();
    }

    public void generate(){
        GridLayout layout = findViewById(R.id.GREED);
        layout.removeAllViews();
        layout.setColumnCount(WIDTH);
        cells = new Button[HEIGHT][WIDTH];
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        //devidebyzero();

        for (int i = 0; i < HEIGHT; i++) {//создание клеточек
            for (int j = 0; j < WIDTH; j++){
                //assert i == 2;//если i=2 - ошибка
                //if (i == 3){//вывод в  Logcat
                //    System.out.println("i= "+i);
                //}
                //Log.d(TAG, "i:= "+i);//упрощённое лучше
                cells[i][j] = (Button) inflater.inflate(R.layout.cell, layout, false);
            }
        }

        String[][] sp = new String[HEIGHT + 2][WIDTH + 2];
        for (int row = 0; row < HEIGHT + 2; row++) for (int col = 0; col < WIDTH + 2; col++) sp[row][col] = "33";
        Random random = new Random();
        for (int i = 0; i < MINESCONST; i++) {
            int rand = (random.nextInt(HEIGHT)) + 1;
            int randd = (random.nextInt(WIDTH)) + 1;
            while (Objects.equals(sp[rand][randd], "B")) {
                rand = (random.nextInt(HEIGHT)) + 1;
                randd = (random.nextInt(WIDTH)) + 1;
            }
            sp[rand][randd] = "B";
        }
        for (int row = 1; row < HEIGHT + 1; row++) {
            for (int col = 1; col < WIDTH + 1; col++) {
                int k = 0;
                if (!Objects.equals(sp[row][col], "B")) {
                    if (Objects.equals(sp[row - 1][col], "B")) k++;
                    if (Objects.equals(sp[row - 1][col - 1], "B")) k++;
                    if (Objects.equals(sp[row - 1][col + 1], "B")) k++;
                    if (Objects.equals(sp[row][col - 1], "B")) k++;
                    if (Objects.equals(sp[row][col + 1], "B")) k++;
                    if (Objects.equals(sp[row + 1][col], "B")) k++;
                    if (Objects.equals(sp[row + 1][col - 1], "B")) k++;
                    if (Objects.equals(sp[row + 1][col + 1], "B")) k++;
                    String t = k + "";
                    sp[row][col] = t;
                }
            }
        }

        for (int row = 0; row < HEIGHT + 2; row++) {
            for (int col = 0; col < WIDTH + 2; col++) {
                System.out.print(sp[row][col] + " ");
            }
            System.out.println();
        }
        String[][] spp = new String[HEIGHT][WIDTH];
        int[][] flow = new int[HEIGHT][WIDTH];
        int[][] good = new int[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                spp[i][j] = sp[i + 1][j + 1];
                flow[i][j] = 0;
                good[i][j] = 0;
            }
        }
        final boolean[] f = {true};

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++){
                cells[i][j].setText("");
                int finalJ = j;
                int finalI = i;
                cells[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (f[0]) {
                            if (!Objects.equals(spp[finalI][finalJ], "0")) {
                                if (Objects.equals(spp[finalI][finalJ], "B")) {
                                    mines.setText("LOSE");
                                    f[0] = false;
                                }
                                cells[finalI][finalJ].setText(spp[finalI][finalJ]);
                            }
                            else {
                                view.setBackgroundColor(Color.BLUE);
                                int t = finalI + 1, tt = finalJ + 1, p = finalI + 1, pp = finalJ + 1;
                                while (!Objects.equals(sp[t - 1][tt], "33")) {
                                    if (Objects.equals(sp[t - 1][tt], "0")) {
                                        t--;
                                        while (!Objects.equals(sp[p][pp+1], "33")) {
                                            if (Objects.equals(sp[p][pp+ 1], "0")) {
                                                pp++;
                                                while (!Objects.equals(sp[p + 1][pp], "33")) {
                                                    if (Objects.equals(sp[p + 1][pp], "0")) {
                                                        p++;
                                                    }
                                                    else{
                                                        break;
                                                    }
                                                }
                                            }
                                            else{
                                                break;
                                            }
                                        }
                                        while (!Objects.equals(sp[t][tt - 1], "33")) {
                                            if (Objects.equals(sp[t][tt - 1], "0")) {
                                                tt--;
                                            }
                                            else{
                                                break;
                                            }
                                        }
                                    }
                                    else{
                                        t--;
                                        break;
                                    }
                                }
                                while (!Objects.equals(sp[t][tt - 1], "33")) {
                                    if (Objects.equals(sp[t][tt - 1], "0")) {
                                        tt--;
                                        while (!Objects.equals(sp[p + 1][pp], "33")) {
                                            if (Objects.equals(sp[p + 1][pp], "0")) {
                                                p++;
                                                while (!Objects.equals(sp[p][pp+1], "33")) {
                                                    if (Objects.equals(sp[p][pp + 1], "0")) {
                                                        pp++;
                                                    } else {
                                                        break;
                                                    }
                                                }
                                            }
                                            else{
                                                break;
                                            }
                                        }
                                        while (!Objects.equals(sp[t - 1][tt], "33")) {
                                            if (Objects.equals(sp[t - 1][tt], "0")) {
                                                t--;
                                            }
                                            else{
                                                break;
                                            }
                                        }
                                    }
                                    else{
                                        tt--;
                                        break;
                                    }
                                }
                                while (!Objects.equals(sp[p + 1][pp], "33")) {
                                    if (Objects.equals(sp[p + 1][pp], "0")) {
                                        p++;
                                        while (!Objects.equals(sp[p][pp+1], "33")) {
                                            if (Objects.equals(sp[p][pp + 1], "0")) {
                                                pp++;
                                            } else {
                                                break;
                                            }
                                        }
                                        while (!Objects.equals(sp[p + 1][pp], "33")) {
                                            if (Objects.equals(sp[p + 1][pp], "0")) {
                                                p++;
                                                while (!Objects.equals(sp[p][pp+1], "33")) {
                                                    if (Objects.equals(sp[p][pp + 1], "0")) {
                                                        pp++;
                                                    } else {
                                                        break;
                                                    }
                                                }
                                            }
                                            else{
                                                break;
                                            }
                                        }
                                    }
                                    else{
                                        break;
                                    }
                                }
                                while (!Objects.equals(sp[p][pp+1], "33")) {
                                    if (Objects.equals(sp[p][pp+ 1], "0")) {
                                        pp++;while (!Objects.equals(sp[t - 1][tt], "33")) {
                                            if (Objects.equals(sp[t - 1][tt], "0")) {
                                                t--;
                                                while (!Objects.equals(sp[t][tt - 1], "33")) {
                                                    if (Objects.equals(sp[t][tt - 1], "0")) {
                                                        tt--;
                                                    }
                                                    else{
                                                        break;
                                                    }
                                                }
                                            }
                                            else{
                                                break;
                                            }
                                        }
                                        while (!Objects.equals(sp[p + 1][pp], "33")) {
                                            if (Objects.equals(sp[p + 1][pp], "0")) {
                                                p++;
                                            }
                                            else{
                                                break;
                                            }
                                        }
                                    }
                                    else{
                                        break;
                                    }
                                }
                                if (tt > 0){
                                    tt--;
                                }
                                if (t > 0){
                                    t--;
                                }
                                if (p > HEIGHT - 1){ p = HEIGHT - 1;}
                                if (pp > WIDTH - 1){ pp = WIDTH - 1;}
                                System.out.println(t+" "+tt);
                                System.out.println(p+" "+pp);
                                for (int r = t; r < p + 1;r++){
                                    for (int rr = tt; rr < pp + 1;rr++){
                                        boolean f = false;
                                        System.out.println(sp[r][rr + 1]+" "+sp[r+1][rr]);
                                        if (!Objects.equals(spp[r][rr], "0")){
                                            if (Objects.equals(sp[r][rr + 1], "0") || Objects.equals(sp[r + 1][rr], "0")) f = true;
                                            else if (Objects.equals(sp[r][rr], "0") || Objects.equals(sp[r+2][rr + 2], "0")) f = true;
                                            else if (Objects.equals(sp[r+2][rr + 1], "0") || Objects.equals(sp[r + 1][rr+2], "0")) f = true;
                                            else if (Objects.equals(sp[r][rr + 2], "0") || Objects.equals(sp[r+2][rr], "0")) f = true;
                                            if (f) {
                                                cells[r][rr].setText(spp[r][rr]);
                                                cells[r][rr].setBackgroundColor(Color.BLUE);
                                                good[r][rr] = 1;//чисто
                                            }
                                        }
                                        else {
                                            cells[r][rr].setText("");
                                            cells[r][rr].setBackgroundColor(Color.BLUE);
                                            good[r][rr] = 1;//чисто
                                        }
                                    }
                                }
                            }
                            if (flow[finalI][finalJ] == 1) {
                                flow[finalI][finalJ] = 0;
                                minecurrents++;
                                mines.setText("" + minecurrents + "/" + MINESCONST);
                            }
                            good[finalI][finalJ] = 1;
                            view.setBackgroundColor(Color.BLUE);//чисто
                        }
                    }

                });
                cells[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (f[0]) {
                            if (minecurrents != 0 && good[finalI][finalJ] != 1) {
                                view.setBackgroundColor(Color.RED);//бомба
                                cells[finalI][finalJ].setText("|►");
                                minecurrents--;
                                flow[finalI][finalJ] = 1;
                            }
                            int y = 0;
                            for (int col = 0; col < HEIGHT; col++) {
                                for (int row = 0; row < WIDTH; row++) {
                                    if (Objects.equals(spp[col][row], "B") && flow[col][row] == 1) {
                                        y++;
                                    }
                                }
                            }
                            if (f[0]) {
                                mines.setText("" + minecurrents + "/" + MINESCONST);
                            }
                            if (minecurrents == 0 && y == MINESCONST && f[0]) {
                                f[0] = false;
                                mines.setText("WIN");
                            }

                        }
                        return true;
                    }
                });
                cells[i][j].setTag("" + (j+HEIGHT*i));
                layout.addView(cells[i][j]);
            }
        }
    }

    private void devidebyzero() {
        int x = 10/0;
    }
    public static int onetwo(int x){//статик - можно вызвать чз название класса
        int a = 2, b = 10;
        return a * x + b;
    }

}