package com.example.game_service.entity;


import com.example.game_service.exception.ColumnExceededException;
import com.example.game_service.exception.PlayerNotFoundException;
import com.example.game_service.exception.PositionTakenException;
import com.example.game_service.exception.RowExceededException;
import com.example.game_service.helpers.GameInterface;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

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
    private GameInterface gameInterface;
    private String gamesessionId;

    private Player winner;
    private Player loser;
    private boolean draw;


    public Game(List<Player> playerList, GameInterface gameInterface) {
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


        this.gameInterface = gameInterface;



    }


    public void changePlayer() {


        if (currentPlayer.isTurn() == true) {
            currentPlayer.setTurn(false);
            otherPlayer.setTurn(true);

            Player tempPlayer = currentPlayer;
            currentPlayer = otherPlayer;
            otherPlayer = tempPlayer;

        } else {

            currentPlayer.setTurn(true);
            otherPlayer.setTurn(false);
            Player tempPlayer = currentPlayer;
            currentPlayer = otherPlayer;
            otherPlayer = tempPlayer;
        }

    }


    public void playRound(Long userID, String gameMove) {

        // [ROW, COL]

        Player player = null;


        int[] coordinates = convertToCoordinates(gameMove);

        int row = coordinates[0];
        int col = coordinates[1];


        // Get the id, and cross check it with the playerList.getplayername

        if (findPlayerById(userID) != null) {

            player = findPlayerById(userID);


        }

        if (row > 3) {
            throw new ColumnExceededException("Selected column outside of boundary");
        }

        if (col > 3) {
            throw new RowExceededException("Select row outside of boundary");
        }

        if (gameArray[row][col] != 3) {
            throw new PositionTakenException("Position already taken");
        } else {
            gameArray[row][col] = currentPlayer.getPlayerNumber();
        }

        checkRound();


        changePlayer();


    }


    public void checkRound() {
        // Check rows for noughts (0) and crosses (1)
        for (int i = 0; i < 3; i++) {
            if (gameArray[i][0] == 0 && gameArray[i][1] == 0 && gameArray[i][2] == 0) {
                winner = playerList.get(0); // Noughts player
                loser = playerList.get(1);
                System.out.println("Noughts won");
                hasGameEnded = true;
                endGame();


                return;
            } else if (gameArray[i][0] == 1 && gameArray[i][1] == 1 && gameArray[i][2] == 1) {
                winner = playerList.get(1); // Crosses player
                loser = playerList.get(0);
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
                loser = playerList.get(1);
                System.out.println("Noughts won");

                hasGameEnded = true;
                endGame();

                return;
            } else if (gameArray[0][j] == 1 && gameArray[1][j] == 1 && gameArray[2][j] == 1) {
                winner = playerList.get(1); // Crosses
                loser = playerList.get(0);
                System.out.println("Crosses won");

                hasGameEnded = true;
                endGame();

                return;
            }
        }

        // Check diagonals for noughts (0) and crosses (1)
        if (gameArray[0][0] == 0 && gameArray[1][1] == 0 && gameArray[2][2] == 0) {
            winner = playerList.get(0); // Noughts player
            loser = playerList.get(1);
            System.out.println("Noughts won");

            hasGameEnded = true;
            endGame();


            return;
        } else if (gameArray[0][0] == 1 && gameArray[1][1] == 1 && gameArray[2][2] == 1) {
            winner = playerList.get(1); // Crosses player
            loser = playerList.get(0);
            System.out.println("Crosses won");

            hasGameEnded = true;
            endGame();


            return;
        }

        if (gameArray[0][2] == 0 && gameArray[1][1] == 0 && gameArray[2][0] == 0) {
            winner = playerList.get(0); // Noughts player
            loser = playerList.get(1);
            System.out.println("Noughts won");
            endGame();

            hasGameEnded = true;
            return;
        } else if (gameArray[0][2] == 1 && gameArray[1][1] == 1 && gameArray[2][0] == 1) {
            winner = playerList.get(1); // Crosses player
            loser = playerList.get(0);

            System.out.println("Crosses won");
            endGame();

            return;
        }


        boolean isDraw = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (gameArray[i][j] == 3) {
                    isDraw = false;
                    break;
                }
            }
            if (!isDraw) {
                break;
            }
        }


        if (isDraw) {
            System.out.println("The game is a draw!");
            hasGameEnded = true;
            winner = null;
            loser = null;
            endGame();

            return;
        }




        winner = null;


    }


    public void endGame() {
        System.out.println("End game function in game instance called");
        gameInterface.endGameWithWinner(winner, loser, draw, gamesessionId);
        setHasGameEnded(true);

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




        return sb.toString();
    }



    public int [][] getGameBoardArrayNormal(){

        return gameArray;

    }

    public Player findPlayerById(Long userId) {

        return playerList.stream().filter(p -> p.getPlayerName().equals(userId))
                .findFirst().orElseThrow(() -> new PlayerNotFoundException("Player not found in game"));

    }


}
