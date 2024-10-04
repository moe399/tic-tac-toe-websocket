package com.example.game_service.entity;


import com.example.game_service.exception.PlayerNotFoundException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Game {

    int[][] gameArray;

    private Player currentPlayer;
    private Player otherPlayer;
    private List<Player> playerList;
    private boolean hasGameEnded = false;

    private Player winner;

    public Game(List<Player> playerList) {
        this.currentPlayer = playerList.get(0);
        this.otherPlayer = playerList.get(1);
        this.playerList = playerList;
        // Set the first player to have first turn
        playerList.get(0).setTurn(true);
        gameArray = new int[][]
                {{3, 3, 3},
                        {3, 3, 3},
                        {3, 3, 3}
                };
    }



    public void changePlayer(){



        if(currentPlayer.isTurn() == true){
            currentPlayer.setTurn(false);
            otherPlayer.setTurn(true);

            Player tempPlayer = currentPlayer;
            currentPlayer = otherPlayer;
            otherPlayer = tempPlayer;

        }

        else{

            currentPlayer.setTurn(true);
            otherPlayer.setTurn(false);
            Player tempPlayer = currentPlayer;
            currentPlayer = otherPlayer;
            otherPlayer = tempPlayer;
        }

    }


    public String playRound(Long userID, String gameMove ){

        // [ROW, COL]

        Player player = null;


        int[] coordinates = convertToCoordinates(gameMove);

        int row = coordinates[0];
        int col = coordinates[1];





        // Get the id, and cross check it with the playerList.getplayername

        if(findPlayerById(userID) != null){

            player = findPlayerById(userID);


        }

        if(row > 3){
            return "Row exceed";
        }

        if(col > 3){
            return "Col Exceed";
        }

        if(gameArray[row][col] != 3){
            return "Space Taken";
        }

        else{
            gameArray[row][col] = currentPlayer.getPlayerNumber();
        }

        checkRound();



        changePlayer();


        return getGameBoardString();






    }


    public void checkRound() {
        // Check rows for noughts (0) and crosses (1)
        for (int i = 0; i < 3; i++) {
            if (gameArray[i][0] == 0 && gameArray[i][1] == 0 && gameArray[i][2] == 0) {
                winner = playerList.get(0); // Noughts player
                System.out.println("Noughts won");
                hasGameEnded = true;
                endGame();


                return;
            } else if (gameArray[i][0] == 1 && gameArray[i][1] == 1 && gameArray[i][2] == 1) {
                winner = playerList.get(1); // Crosses player
                System.out.println("Crosses won");
                hasGameEnded = true;
                endGame();

                return;
            }
        }

        // Check columns for noughts (0) and crosses (1)
        for (int j = 0; j < 3; j++) {
            if (gameArray[0][j] == 0 && gameArray[1][j] == 0 && gameArray[2][j] == 0) {
                winner = playerList.get(0); // Noughts player
                System.out.println("Noughts won");

                hasGameEnded = true;
                endGame();

                return;
            } else if (gameArray[0][j] == 1 && gameArray[1][j] == 1 && gameArray[2][j] == 1) {
                winner = playerList.get(1); // Crosses
                System.out.println("Crosses won");

                hasGameEnded = true;
                endGame();

                return;
            }
        }

        // Check diagonals for noughts (0) and crosses (1)
        if (gameArray[0][0] == 0 && gameArray[1][1] == 0 && gameArray[2][2] == 0) {
            winner = playerList.get(0); // Noughts player
            System.out.println("Noughts won");

            hasGameEnded = true;
            endGame();


            return;
        } else if (gameArray[0][0] == 1 && gameArray[1][1] == 1 && gameArray[2][2] == 1) {
            winner = playerList.get(1); // Crosses player
            System.out.println("Crosses won");

            hasGameEnded = true;
            endGame();


            return;
        }

        if (gameArray[0][2] == 0 && gameArray[1][1] == 0 && gameArray[2][0] == 0) {
            winner = playerList.get(0); // Noughts player
            System.out.println("Noughts won");
            endGame();

            hasGameEnded = true;
            return;
        } else if (gameArray[0][2] == 1 && gameArray[1][1] == 1 && gameArray[2][0] == 1) {
            winner = playerList.get(1); // Crosses player
            System.out.println("Crosses won");
            endGame();

            return;
        }


        winner = null; // No winner yet



    }


    public void endGame(){


    }



    // HELPER FUNCTIONS BELOW

    public static int[] convertToCoordinates(String gameMove) {
        String trimmed = gameMove.replaceAll("[\\[\\]]", "");

        String[] parts = trimmed.split(",");

        int[] coordinates = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            coordinates[i] = Integer.parseInt(parts[i].trim());
        }

        return coordinates;
    }



    public String getGameBoardString() {
        StringBuilder sb = new StringBuilder();


        for (int[] row : gameArray) {
            sb.append("[");
            for (int col = 0; col < row.length; col++) {
                sb.append(row[col]);
                if (col < row.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            if (row != gameArray[gameArray.length - 1]) {
                sb.append(", ");
            }
        }


        sb.append(" - Success");

        return sb.toString();
    }


    public Player findPlayerById(Long userId){

        return playerList.stream().filter(p -> p.getPlayerName().equals(userId))
                .findFirst().orElseThrow(() -> new PlayerNotFoundException("Player not found in game"));

    }






}
