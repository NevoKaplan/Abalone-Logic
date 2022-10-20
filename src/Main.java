import java.util.Scanner;

public class Main {

    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("---------------------------");
        Board board = new Board(true);
        //AIBoard a = new AIBoard(board);
       // a.getNextBoards();
        //System.out.println("THE COUNTER: " + AIBoard.run);
        boolean play = true;
        while (play) {
            board.print();
            board.makeMove();
            System.out.println("\nDead blue: " + board.deadBlue + "\nDead red: " + board.deadRed);
            if (board.deadBlue >= 6) {
                System.out.println("Red Won!");
                play = again();
                if (play)
                    board = new Board(true);
            }
            else if (board.deadRed >= 6) {
                System.out.println("Blue Won!");
                play = again();
                if (play)
                    board = new Board(true);
            }
        }
    }

    // User wants to play again?
    private static boolean again() {
        String answer;
        while (true) {
            System.out.println("Would you like to play again(Y/N)?");
            answer = in.nextLine();
            answer = answer.toLowerCase();
            if (answer.equals("yes") || answer.equals("y"))
                return true;
            else if (answer.equals("no") || answer.equals("n"))
                    return false;
            else
                System.out.println("Invalid answer, try again.\n");
        }
    }

}