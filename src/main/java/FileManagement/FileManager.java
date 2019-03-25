package FileManagement;

import java.io.*;
import java.util.Scanner;

public class FileManager {
    File file;
    public FileManager() {
        file = new File("stats.txt");
    }

    public void incrementSinglePlayer() {
        // Read file if it exists
        int singlePlayerPlayed = 1;
        int multiplayerPlayed = 0;
        int multiplayerWon = 0;
        int linesCleared = 0;
        if (null != file && file.exists()) {
            try {
                Scanner input = new Scanner(file);
                if (input.hasNext()) {
                    singlePlayerPlayed += Integer.valueOf(input.next());
                }
                if (input.hasNext()) {
                    multiplayerPlayed = Integer.valueOf(input.next());
                }
                if (input.hasNext()) {
                    multiplayerWon = Integer.valueOf(input.next());
                }
                if (input.hasNext()) {
                    linesCleared = Integer.valueOf(input.next());
                }
                input.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        writeToFile(singlePlayerPlayed, multiplayerPlayed, multiplayerWon, linesCleared);
    }

    public void incrementMultiplayer() {
        // Write number of lines cleared to file
        File file = new File("stats.txt");
        int singlePlayerPlayed = 0;
        int multiplayerPlayed = 1;
        int multiplayerWon = 0;
        int linesCleared = 0;
        if (null != file && file.exists()) {
            try {
                Scanner input = new Scanner(file);
                if (input.hasNext()) {
                    singlePlayerPlayed = Integer.valueOf(input.next());
                }
                if (input.hasNext()) {
                    multiplayerPlayed += Integer.valueOf(input.next());
                }
                if (input.hasNext()) {
                    multiplayerWon = Integer.valueOf(input.next());
                }
                if (input.hasNext()) {
                    linesCleared = Integer.valueOf(input.next());
                }
                input.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        writeToFile(singlePlayerPlayed, multiplayerPlayed, multiplayerWon, linesCleared);
    }

    public void incrementGamesWon() {
        // Write number of lines cleared to file
        File file = new File("stats.txt");
        int singlePlayerPlayed = 0;
        int multiplayerPlayed = 0;
        int multiplayerWon = 1;
        int linesCleared = 0;
        if (null != file && file.exists()) {
            try {
                Scanner input = new Scanner(file);
                if (input.hasNext()) {
                    singlePlayerPlayed = Integer.valueOf(input.next());
                }
                if (input.hasNext()) {
                    multiplayerPlayed = Integer.valueOf(input.next());
                }
                if (input.hasNext()) {
                    multiplayerWon += Integer.valueOf(input.next());
                }
                if (input.hasNext()) {
                    linesCleared = Integer.valueOf(input.next());
                }
                input.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        writeToFile(singlePlayerPlayed, multiplayerPlayed, multiplayerWon, linesCleared);
    }

    public void incrementLinesCleared() {
        // Write number of lines cleared to file
        File file = new File("stats.txt");
        int singlePlayerPlayed = 0;
        int multiplayerPlayed = 0;
        int multiplayerWon = 0;
        int linesCleared = 1;
        if (null != file && file.exists()) {
            try {
                Scanner input = new Scanner(file);
                if (input.hasNext()) {
                    singlePlayerPlayed = Integer.valueOf(input.next());
                }
                if (input.hasNext()) {
                    multiplayerPlayed = Integer.valueOf(input.next());
                }
                if (input.hasNext()) {
                    multiplayerWon = Integer.valueOf(input.next());
                }
                if (input.hasNext()) {
                    linesCleared += Integer.valueOf(input.next());
                }
                input.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        writeToFile(singlePlayerPlayed, multiplayerPlayed, multiplayerWon, linesCleared);
    }

    private void writeToFile(int singlePlayerPlayed, int multiplayerPlayed, int multiplayerWon, int linesCleared) {
        // Write to file
        try {
            PrintWriter output = new PrintWriter(file);
            output.println(singlePlayerPlayed);
            output.println(multiplayerPlayed);
            output.println(multiplayerWon);
            output.println(linesCleared);
            output.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
