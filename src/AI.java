import java.util.ArrayList;
import java.util.Random;

public class AI {
    private int AiPlayer;
    private static AI instance = null;
    private static Random rnd = new Random();

    public void setAiPlayer(int aiPlayer) {
        this.AiPlayer = aiPlayer;
    }

    private AI (int player) {
        AiPlayer = player;
    }

    public static AI getInstance(int player) {
        if (instance == null) {
            instance = new AI(player);
        }
        return instance;
    }

    public AIBoard bestMove(AIBoard board) {
        Board bestSelected = null;
        ArrayList<AIBoard> children = board.getNextBoards();
        return children.get(rnd.nextInt(children.size()));
        /*for (AIBoard child : children) {
            child.setVal(checkBoard(child, child.getDepth(), Integer.MIN_VALUE, Integer.MAX_VALUE));
        }
        if (board.player == AiPlayer) {
            int bestVal = Integer.MIN_VALUE;
            for (AIBoard child : children) {
                int val = child.getVal();
                if (val >= bestVal) {
                    bestVal = val;
                    bestSelected = child.getBestSelected();
                }
            }
        } else {
            int bestVal = Integer.MAX_VALUE;
            for (AIBoard child : children) {
                int val = child.getVal();
                if (val <= bestVal) {
                    bestVal = val;
                    bestSelected = child.getBestSelected();
                }
            }
        }
        board.setBestSelected(bestSelected);
        return bestSelected;*/
    }

    private int checkBoard(AIBoard board, int depth, int alpha, int beta) {
        int val;
        int winner = board.getWinner();
        if (winner != 0) {
            return Integer.MAX_VALUE * winner;
        }
        if (depth == 0) {
            val = board.evaluate();
            board.setVal(val);
            return val;
        }

        ArrayList<AIBoard> nextBoards = board.getNextBoards();

        // checking whose turn it is
        if (board.player == AiPlayer) {
            for (AIBoard child : nextBoards) {
                alpha = Math.max(alpha, checkBoard(child, depth - 1, alpha, beta));
                if (beta < alpha)
                    break;
            }
            return alpha;
        } else
        {
            for (AIBoard child : nextBoards) {
                beta = Math.min(beta, checkBoard(child, depth - 1, alpha, beta));
                if (beta < alpha)
                    break;
            }
            return beta;
        }
    }

    public void makeMove(Board board) {
        AIBoard bestBoard = bestMove(new AIBoard(board));
        bestBoard.doMove();
    }

}
