import java.util.ArrayList;

public class AIBoard extends Board {

    final static int MAX_DEPTH = 5;
    int depth;
    private int val;
    private Board bestSelected;
    public static int run = 0;

    private boolean sideMoveable;

    public AIBoard(Board board) {
        super(false);
        updateBoard(board.hex);
        this.player *= -1;
        this.depth = MAX_DEPTH;
        this.bestSelected = null;
    }

    public AIBoard(AIBoard board) {
        super(false);
        run++;
        updateBoard(board.hex);
        this.player *= -1;
        this.depth = board.depth-1;
        this.val = 0;
        this.bestSelected = null;
    }

    private void updateBoard(Stone[][] stones) {
        for (int i = 0; i < this.hex.length; i++) {
            System.arraycopy(stones[i], 0, this.hex[i], 0, this.hex[i].length);
        }
    }

    public int getDepth() {
        return this.depth;
    }
    public int getVal() {
        return this.val;
    }
    public void setVal(int val) {
        this.val = val;
    }
    public Board getBestSelected() {
        return this.bestSelected;
    }
    public void setBestSelected(Board board) { this.bestSelected = board; }

    public ArrayList<AIBoard> getNextBoards() {
        ArrayList<AIBoard> nextBoards = this.IterateNextBoards();
        //nextBoards.get(0).doMove();
        return nextBoards;
    }


    // need to check for every move
    private ArrayList<AIBoard> IterateNextBoards() {
        ArrayList<AIBoard> nextBoards = new ArrayList<>();
        int count = 0;                               // count for how many troops already checked - to check less
        if (this.player == 1)
            count += this.deadBlue;
        else
            count += this.deadRed;
        for (int i = 0; i < this.hex.length && count <= 14; i++){
            for (int j = 0; j < this.hex[i].length; j++) {
                if (this.hex[i][j].getMainNum() == this.player) {
                    if (!this.selected.isEmpty()) {
                        this.doMove();
                    }
                    count++;
                    changeSelected(this.hex[i][j]);

                    ArrayList<Stone> targetStones = availableTargets();
                    nextBoards.addAll(iterate(targetStones));

                    this.cleanSelected();
                    changeSelected(this.hex[i][j]);
                    ArrayList<Stone> availableStones = availableStones2();
                    this.cleanSelected();

                    if (availableStones != null) {
                        for (Stone stone : availableStones) {
                            changeSelected(this.hex[i][j]);
                            choosePiece(stone, availableStones);
                            targetStones = availableTargets();
                            nextBoards.addAll(iterate(targetStones));
                            this.cleanSelected();
                        }
                    }
                }
            }
        }

        return nextBoards;
    }

    private ArrayList<AIBoard> iterate(ArrayList<Stone> targetStones) {
        ArrayList<AIBoard> tempList = new ArrayList<>();
        if (targetStones != null) {
            AIBoard aiBoard;
            for (Stone target : targetStones) {
                aiBoard = new AIBoard(this);
                aiBoard.selected = this.selected;
                aiBoard.selectedSize = this.selectedSize;

                aiBoard.sideMoveable = aiBoard.getFullMove(target);

                //aiBoard.selectedSize -= aiBoard.toBe.size();
                //aiBoard.selected.removeAll(aiBoard.toBe);
                tempList.add(aiBoard);

            }
        }
        return tempList;
    }

    private boolean getFullMove(Stone moveTo) {
        boolean reverse = shouldReverse(moveTo);
        if (beforeSideMove(moveTo, false))
            return true;

        shouldReverse2(reverse);
        return false;
    }

    public void doMove() {
        Stone last = selected.get(selectedSize - 1);
        Stone moveTo = this.toBe.get(0);
        System.out.println("To BE:");
        for (Stone stone : this.toBe) {
            System.out.println(stone);
        }
        System.out.println("------------------");
        System.out.println("selected:");
        for (Stone stone : this.selected) {
            System.out.println(stone);
        }
        System.out.println("------------------");
        moveTo.setMainNum(0);

        int drow = moveTo.row - last.row;
        int dcol = moveTo.col - last.col;

        if (this.sideMoveable) {
            this.sideMove(drow, dcol);
        }
        else
            beforeLineMove(drow, dcol);
        moveTo.setOgNum(moveTo.getMainNum());
        this.cleanSelected();
    }

    public int getWinner() {
        if (this.deadBlue >= 6)
            return 1;
        else if (this.deadRed >= 6)
            return -1;
        else
            return 0;
    }

    public int evaluate() {
        return 0;
    }

    @Override
    protected void merge() {
        selected.addAll(toBe);
        selectedSize += toBe.size();
    }

}
