package TeaVMPackage;

import org.apache.commons.lang3.ObjectUtils;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.templates.Templates;
import org.teavm.flavour.widgets.ApplicationTemplate;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.lang.*;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.*;

@BindTemplate("templates/sudoku.html")
public class Sudoku extends ApplicationTemplate
{
    private static String userName = "";
    private static boolean gameStarted=false;

    // Driver code
    public static void main(String[] args)
    {
        int N = 9, K = 2;
        Sudoku sudoku = new Sudoku(N, K);
        sudoku.fillValues();
        sudoku.printSudoku();
        //Templates.bind( sudoku, "application-content");
        sudoku.bind("application-content");
    }

    public String getUserName() {
        return userName;
    }
    public boolean getGameStarted() {
        return gameStarted;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }




    static int[][] mat;
    static int[][] matTwo;
    int N; // number of columns/rows.
    int SRN; // square root of N
    int K; // No. Of missing digits

    int [][] answers;





    // Constructor
    Sudoku(int N, int K)
    {
        this.N = N;
        this.K = K;

        // Compute square root of N
        Double SRNd = Math.sqrt(N);
        SRN = SRNd.intValue();

        mat = new int[N][N];

    }




    // Sudoku Generator
    public void fillValues()
    {
        // Fill the diagonal of SRN x SRN matrices
        fillDiagonal();

        // Fill remaining blocks
        fillRemaining(0, SRN);

        // Remove Randomly K digits to make game
        removeKDigits();
        matTwo = mat;
    }

    // Fill the diagonal SRN number of SRN x SRN matrices
    void fillDiagonal()
    {

        for (int i = 0; i<N; i=i+SRN)

            // for diagonal box, start coordinates->i==j
            fillBox(i, i);
    }

    // Returns false if given 3 x 3 block contains num.
    boolean unUsedInBox(int rowStart, int colStart, int num)
    {
        for (int i = 0; i<SRN; i++)
            for (int j = 0; j<SRN; j++)
                if (mat[rowStart+i][colStart+j]==num)
                    return false;

        return true;
    }

    // Fill a 3 x 3 matrix.
    void fillBox(int row,int col)
    {
        int num;
        for (int i=0; i<SRN; i++)
        {
            for (int j=0; j<SRN; j++)
            {
                do
                {
                    num = randomGenerator(N);
                }
                while (!unUsedInBox(row, col, num));

                mat[row+i][col+j] = num;
            }
        }
    }

    // Random generator
    int randomGenerator(int num)
    {
        return (int) Math.floor((Math.random()*num+1));
    }

    // Check if safe to put in cell
    boolean CheckIfSafe(int i,int j,int num)
    {
        return (unUsedInRow(i, num) &&
                unUsedInCol(j, num) &&
                unUsedInBox(i-i%SRN, j-j%SRN, num));
    }

    // check in the row for existence
    boolean unUsedInRow(int i,int num)
    {
        for (int j = 0; j<N; j++)
            if (mat[i][j] == num)
                return false;
        return true;
    }

    // check in the row for existence
    boolean unUsedInCol(int j,int num)
    {
        for (int i = 0; i<N; i++)
            if (mat[i][j] == num)
                return false;
        return true;
    }

    // A recursive function to fill remaining
    // matrix
    boolean fillRemaining(int i, int j)
    {
        //  System.out.println(i+" "+j);
        if (j>=N && i<N-1)
        {
            i = i + 1;
            j = 0;
        }
        if (i>=N && j>=N)
            return true;

        if (i < SRN)
        {
            if (j < SRN)
                j = SRN;
        }
        else if (i < N-SRN)
        {
            if (j==(int)(i/SRN)*SRN)
                j =  j + SRN;
        }
        else
        {
            if (j == N-SRN)
            {
                i = i + 1;
                j = 0;
                if (i>=N)
                    return true;
            }
        }

        for (int num = 1; num<=N; num++)
        {
            if (CheckIfSafe(i, j, num))
            {
                mat[i][j] = num;
                if (fillRemaining(i, j+1))
                    return true;

                mat[i][j] = 0;
            }
        }
        return false;
    }

    // Remove the K no. of digits to
    // complete game
    public void removeKDigits()
    {
        int count = K;
        while (count != 0)
        {
            int cellId = randomGenerator(N*N);

            // System.out.println(cellId);
            // extract coordinates i  and j
            int i = (cellId/N);
            int j = cellId%9;
            if (j != 0)
                j = j - 1;

            // System.out.println(i+" "+j);
            if (mat[i][j] != 0)
            {
                count--;
                mat[i][j] = 0;
            }
        }
    }

    public String getSudokuStatus(){
        String status;
        boolean bool = checkSudokuStatus();
        if(bool){
            return status = "Ergebnis stimmt";
        }
        return status = "Ergebnis stimmt nicht";
    }


    public boolean checkSudokuStatus() {
        for (int i = 0; i < 9; i++) {

            int[] row = new int[9];
            int[] square = new int[9];
            int[] column = mat[i].clone();

            for (int j = 0; j < 9; j ++) {
                row[j] = mat[j][i];
                square[j] = mat[(i / 3) * 3 + j / 3][i * 3 % 9 + j % 3];
            }
            if (!(validate(column) && validate(row) && validate(square)))
                return false;
        }
        return true;
    }

    private boolean validate(int[] check) {
        int i = 0;
        Arrays.sort(check);
        for (int number : check) {
            if (number != ++i)
                return false;
        }
        return true;
    }

    public int getValueAtPoint(int i, int j){
        return mat[i][j];
    }


    public void printSudoku()
    {
        for (int i = 0; i<N; i++)
        {
            for (int j = 0; j<N; j++)
                System.out.print(mat[i][j] + " ");
            System.out.println();
        }
        System.out.println();
    }



    public  Iterable<Integer> range(int from, int to) {
            return () -> new Iterator() {
                int current = from;
                @Override public boolean hasNext() {
                    return current < to;
                }
                @Override public Integer next() {
                    return current++;
                }
            };
    }



    public void newEntry(int i,int j){
        System.out.println("changed text" + i + j + userName);
        if(userName.length() > 1){
            throw new ArithmeticException();
        }
        else {
            matTwo[i][j] = Integer.parseInt(userName);
            System.out.println("changed text" + i + j + userName);
            userName = "";
        }
    }

    public void easyGame(){

        setGameStarted(false);
        Sudoku sudoku = new Sudoku(9,3);
        sudoku.fillValues();
        sudoku.printSudoku();
        setGameStarted(true);
    }

    public void middleGame(){
        setGameStarted(false);
        Sudoku sudoku = new Sudoku(9,12);
        sudoku.fillValues();
        sudoku.printSudoku();
        setGameStarted(true);
    }
    public void hardGame(){
        setGameStarted(false);
        Sudoku sudoku = new Sudoku(9,22);
        sudoku.fillValues();
        sudoku.printSudoku();
        setGameStarted(true);
    }

    public void safeAnswer(int i,int j){
        boolean allreadySafed;
        int positionOfIter = 0;
        int value = Integer.parseInt(userName);

        for (int iter = 0;iter <= answers.length; iter++){
            if(answers[iter][0]==i && answers[iter][1]==j){
                allreadySafed = true;
                positionOfIter = iter;
            }
            else{
                allreadySafed =false;
                positionOfIter = iter;
            }
            if(allreadySafed){
                answers[positionOfIter][2]=value;
            }
            else{
                answers[positionOfIter][2]=value;
            }

        }
        System.out.println("safed" + value + "in safe answers");
    }


}

