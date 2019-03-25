package FileManagement;

import java.io.*;
import java.util.Scanner;

public class FileManager {
    File file;
    public FileManager() {
        file = new File("stats.txt");
    }

    public void incrementSinglePlayer() {
        incrementValues(1, 0, 0, 0);
    }

    public void incrementMultiplayer() {
        incrementValues(0, 1, 0, 0);
    }

    public void incrementGamesWon() {
        incrementValues(0, 0, 1, 0);
    }

    public void incrementLinesCleared() {
        incrementValues(0, 0, 0, 1);
    }

    private void incrementValues(int singlePlayerPlayed, int multiplayerPlayed, int multiplayerWon, int linesCleared) {
        if (null != file && file.exists()) {
            try {
                Scanner input = new Scanner(file);
                if (input.hasNext()) {
                    singlePlayerPlayed += Integer.valueOf(input.next());
                }
                if (input.hasNext()) {
                    multiplayerPlayed += Integer.valueOf(input.next());
                }
                if (input.hasNext()) {
                    multiplayerWon += Integer.valueOf(input.next());
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

    public int getSinglePlayerPlayed() {
        int singlePlayerPlayed = 0;
        if (null != file && file.exists()) {
            try {
                Scanner input = new Scanner(file);
                if (input.hasNext()) {
                    singlePlayerPlayed += Integer.valueOf(input.next());
                }
                input.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return singlePlayerPlayed;
    }

    public int getMultiplayerPlayed() {
        int multiplayerPlayed = 0;
        if (null != file && file.exists()) {
            try {
                Scanner input = new Scanner(file);
                if (input.hasNext()) {
                    input.next();
                }
                if (input.hasNext()) {
                    multiplayerPlayed = Integer.valueOf(input.next());
                }
                input.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return multiplayerPlayed;
    }

    public int getMultiplayerWon() {
        int multiplayerWon = 0;
        if (null != file && file.exists()) {
            try {
                Scanner input = new Scanner(file);
                if (input.hasNext()) {
                    input.next();
                }
                if (input.hasNext()) {
                    input.next();
                }
                if (input.hasNext()) {
                    multiplayerWon = Integer.valueOf(input.next());
                }
                input.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return multiplayerWon;
    }

    public int getLinesCleared() {
        int linesCleared = 0;
        if (null != file && file.exists()) {
            try {
                Scanner input = new Scanner(file);
                if (input.hasNext()) {
                    input.next();
                }
                if (input.hasNext()) {
                    input.next();
                }
                if (input.hasNext()) {
                    input.next();
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
        return linesCleared;
    }
}
