import java.io.*;
import java.net.*;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

class TerminalWordle{

    public static final String RESET = "\033[0m";
    public static final String RED_BACKGROUND = "\033[41m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String GREEN_BACKGROUND = "\033[42m";
    public static final String BLUE_BACKGROUND = "\033[44m";
    public static final String YELLOW_BACKGROUND = "\033[43m";
    public static final String YELLOW = "\u001b[33m";
    public static final String WHITE = "\u001b[37m";
    public static final String BOLD = "\033[1m";
    public static final String FLUSH_CONSOLE = "\033[H\033[2J";

    private String wordle;

    public static void main(String args[]){
        TerminalWordle tw = new TerminalWordle();
        try {
            System.out.println(BOLD + "<--Welcome to the terminal Wordle!-->" + RESET);
            System.out.println(BOLD + "<--Let's start!-->" + RESET);
            
            tw.fetchWordle();            
            Thread.sleep(2000);            

            while(tw.startGame()) {}

            System.out.println(BOLD + "Goodbye!" + RESET);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    public boolean startGame(){
        Scanner sc = new Scanner(System.in);
        clearConsole();
        try {

            boolean guessed = false;

            String ans;

            System.out.println(BOLD + "Make your guess" + RESET);
            for (int i = 0; i < 5 && !guessed; i++) {
                ans = sc.nextLine().toUpperCase();
                if (ans.length() != 5) {
                    System.out.println(RED + "Wrong format, remember that your guess consists of 5 characters" + RESET);
                    i--;
                } else {
                    guessed = this.guess(ans);
                }
            }

            if (guessed) {
                System.out.println(BOLD + GREEN + "Congratulations, you've nailed it!!" + RESET);
            } else {
                System.out.println(BOLD + RED + "Ohh, I'm sorry, you didn't guess it, the wordle was: " + getWordle() + RESET);
            }

            this.fetchWordle();
            Thread.sleep(2000);

            clearConsole();

        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        System.out.println(BOLD + "Do you want to try again with another wordle? (y/n)" + RESET);

        return sc.nextLine().equalsIgnoreCase("y");
    }

    public boolean guess(String ans) {
        char aux;
        int guessedChars = 0;
        
        System.out.print("\033[1A" + "\033[k" +  "|");
        for(int i = 0; i < 5; i++){
            aux = ans.charAt(i);
            if (aux == this.getWordle().charAt(i)) {
                System.out.print(GREEN_BACKGROUND + WHITE + BOLD + aux + RESET + "|");
                guessedChars++;
            } else if (this.getWordle().contains("" + aux)){
                System.out.print(YELLOW + BOLD + aux + RESET + "|");
            } else {
                System.out.print(aux + "|");
            }
        }

        System.out.println();

        return (guessedChars == 5);
    }

    public void fetchWordle() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://random-word-api.herokuapp.com/word?length=5"))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            this.wordle = response.body().toString().substring(2, 7).toUpperCase();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    public static void clearConsole() {
        System.out.println(FLUSH_CONSOLE);
        System.out.flush();
    }

    public TerminalWordle(){ }

    public String getWordle() {
        return this.wordle;
    }

    public void setWordle(String wordle) {
        this.wordle = wordle;
    }

}
