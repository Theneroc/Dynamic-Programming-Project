package com.example.dp_project_khalil_khawaja_1202472;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert;
import java.util.LinkedList;

public class HelloController {



    @FXML
    private TextField lengthTF;

    static int max(int a,int b){ // basic maximum comparison b/w 2 elements
        return a>b?a:b;
    }
    static int min(int a,int b){ // basic minimum comparison b/w 2 elements
        return a<b?a:b;
    }
    public void enter(ActionEvent e){

        p1Sum = 0;
        p2Sum = 0;

        tableGrid.getChildren().clear();//new game clears the dynamic table
        simGrid.getChildren().clear();//new game clears the row of coins
        p1TF.setText("");// new player
        p2TF.setText("");// new player
        n = Integer.parseInt(lengthTF.getText());//takes amount of coins that will be in game

        if(n%2!=0 || n<=0 ||n>12) {// has to be an even number to work

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);

            if(n%2!=0 )
                alert.setContentText("Not Even!");
            else if(n<=0)
                alert.setContentText("Cannot be 0 or less");
            else
                alert.setContentText("Too many coins!");

            alert.showAndWait();

        }
        initializeSpaces(e);//creates row spaces

    }
    public void autoInput(ActionEvent e){

        for (int i = 0; i < n; i++) {//a loop to initialize the coins as text fields and their values and set them on the grid

            int num = (int) (Math.random() * 15);
            TextField field= new TextField(String.valueOf(num));
            coin[i] = field;
            simGrid.add(coin[i], i, 1, 1, 1);//adds coin to the row

        }

        makeTableAndCoinSet(e);

    }
    public void manualInput(ActionEvent e){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        boolean flag = false;
        for(int i = 0; i < n; i++)
            if (coin[i].getText().equals("")) {
                alert.setContentText("All spaces must be full!");
                flag = true;
            }
        if(flag) {
            alert.showAndWait();
        }
        if(!flag)
            nextP.setDisable(false);
        makeTableAndCoinSet(e);

    }

    @FXML
    private TextField p1TF;

    @FXML
    private TextField p2TF;

    @FXML
    public Button randomizeB;
    static int n;//number of coins in the game
    static int[][] pTable;// the path table (which coin is taken at a specific play in the game)

    static TextField[] coin;// the row of coins (mainly for display)
    static int p1Sum = 0,p2Sum = 0;//sums of each player
    static int moveB = 0;// the move number of the specific play

    static int[] currCoin;//coins ordered per player (0 is p1, 1 is p2, 2 is p1...etc)
    static String[] dir;//direction of the play

    public void initializeSpaces(ActionEvent e){
        nextP.setDisable(true);
        coin = new TextField[n];
        for (int i = 0; i < n; i++) {//a loop to initialize the coins as text fields and their values and set them on the grid
            coin[i] = new TextField();
            coin[i].setAlignment(Pos.CENTER);

            simGrid.add(coin[i], i, 1, 1, 1);//adds coin to the row
        }

    }
     public void makeTableAndCoinSet(ActionEvent e){

            nextP.setDisable(false);
             pTable = new int[n][n];
             dir = new String[n];


             //coin row doubles...FIX
             simGrid.setPadding(new Insets(5, 5, 5, 5));
             simGrid.setAlignment(Pos.CENTER);
             tableGrid.setAlignment(Pos.CENTER);
             TextField[][] Table = new TextField[n][n];// table created to memorize every path that will be taken from start to finish

            for(int i = 0;i<n;i++)//initializes table to 0s
                for(int j = 0;j<n;j++){
                    Table[i][j] = new TextField("0");
                    Table[i][j].setAlignment(Pos.CENTER);
                    tableGrid.add(Table[i][j],j,i,1, 1);
                }
             // each diff for loop goes across a single diagonal of values
             for(int diff = 0;diff<n;diff++){//this nested loop fills the values into the table diagonally without crossing anything where i<j
                 for(int i=0,j=diff;j<n;i++,j++){
                     int pathX=0,pathY=0,pathZ=0;// these are the paths possible to the player

                     if(diff>1){//diff is j-i
                         pathX = Integer.parseInt(Table[i+2][j].getText());//if player 1 picks i and player 2 picks i+1
                         pathY = Integer.parseInt(Table[i+1][j-1].getText());//if player 1 picks (i or j) and player 2 picks (i or j)
                         pathZ = Integer.parseInt(Table[i][j-2].getText());// if player 1 picks j and player 2 picks j+1
                     }
                     int num = max(Integer.parseInt(coin[i].getText()) + min(pathX,pathY),Integer.parseInt(coin[j].getText()) + min(pathY,pathZ));// main formula
                     Table[i][j].setText(String.valueOf(num));

                    //here on out --> fills up the path table
                     if(Integer.parseInt(coin[i].getText()) + min(pathX,pathY)==Integer.parseInt(coin[j].getText()) + min(pathY,pathZ)) {
                         int ans = max(Integer.parseInt(coin[i].getText()), Integer.parseInt(coin[j].getText()));//if outcome is the same, the greater number at the beginning is chosen (stylistic choice)
                         if(ans == Integer.parseInt(coin[i].getText()))
                             pTable[i][j] = i;
                         else
                             pTable[i][j] = j;
                     }
                     else{
                         if(Integer.parseInt(coin[i].getText()) + min(pathX,pathY)>Integer.parseInt(coin[j].getText()) + min(pathY,pathZ))// the index of the coin picked is put into the path table
                             pTable[i][j] = i;
                         else
                             pTable[i][j] = j;

                     }
                 }
             }
             //loop to get the order of plays in the game-->
             LinkedList<Integer> coinList = new LinkedList<>();// linked list for ease of use and mutability... don't need to keep recreating arrays
             for(int i=0;i<n;i++){
                 coinList.add(Integer.parseInt(coin[i].getText()));
             }

             currCoin = new int[n];// array to fit order of coins picked for player text fields

             int first = 0, last = n - 1;
             for(int i = 0;i<n;i++) {
                 currCoin[i] = coinList.get(pTable[first][last]);//8
                 if(first == pTable[first][last]){
                     dir[i] = "left";
                     first++;
                     }
                 else {
                     dir[i] = "right";
                     last--;
                 }
              }


        //logic portion over
     }
     public void nextPlay(ActionEvent e){// purely for interface


         //time to enter what the players played:
         if(moveB<n){
            // System.out.println(currCoin[moveB]);
             if(moveB%2==0) {
                 p1TF.appendText((currCoin[moveB])+" "+dir[moveB]);
                 p1Sum+=currCoin[moveB];
                 if(moveB+1 != n-1)
                     p1TF.appendText(" + ");
             }
             else{
                 p2TF.appendText((currCoin[moveB]+" "+dir[moveB]));
                 p2Sum+=currCoin[moveB];
                 if(moveB+1 != n)
                     p2TF.appendText(" + ");
             }
             ++moveB;
         }
         if(moveB==n) {
             p1TF.appendText(" = " + p1Sum);
             p2TF.appendText(" = " + p2Sum);
            nextP.setDisable(true);
            moveB=0;
         }



     }

    @FXML
    private Button nextP;

    @FXML
    private GridPane simGrid;


    @FXML
    private GridPane tableGrid;

}
