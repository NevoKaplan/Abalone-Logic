import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Stone {

    private int mainNum;
    public int row, col;
    private boolean isSelected;
    public static ArrayList<Stone> selected = new ArrayList<>();
    public static ArrayList<Stone> toBe = new ArrayList<>();
    public static int size = 0;

    public Stone(int mainNum, int row, int col) {
        this.mainNum = mainNum;
        this.row = row;
        this.col = col;
        isSelected = false;
    }

    public int getMainNum() {return this.mainNum;}

    public void setMainNum(int num) {this.mainNum = num;}

    public String toString() {
        return this.mainNum + " [" + this.row + "]" + "[" + this.col + "]";
    }

    public boolean getSelected() {return this.isSelected;}

    public void setSelected(boolean bool) {this.isSelected = bool;}

    public boolean changeSelected() {
        this.isSelected = !this.isSelected;
        if (isSelected) {
            selected.add(this);
            size++;
        }
        else {
            selected.remove(this);
            size--;
        }
        sort(selected);
        return this.isSelected;
    }

    public boolean isLarger(Stone stone) { // returns true if parameter is larger than instance
        if (this.row < stone.row)
            return false;
        if (this.row == stone.row) {
            if (this.col < stone.col)
                return false;
            return true;
        }
        return true;
    }

    public boolean equals(Stone stone) {
        return stone.col == this.col && stone.row == this.row;
    }

    public static void sort(ArrayList<Stone> stones) { // NOT WORKING!!
        int size = stones.size();
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                Stone current = stones.get(j);
                Stone next = stones.get(j+1);
                if (current.equals(next)) {
                    stones.remove(j+1);
                    size--;
                }
                else if (current.isLarger(next)) {
                    Stone stone = stones.get(j);
                    stones.set(j, stones.get(j + 1));
                    stones.set(j+1, stone);
                }
            }
        }
    }

    public static void reverseList(ArrayList<Stone> stones) {
        Collections.reverse(stones);
    }

    public static void cleanSelected() {
        Iterator<Stone> it = selected.iterator();
        System.out.println("--\nWere Selected:");
        while (it.hasNext()) {
            Stone stone = it.next();
            System.out.println(stone);
            stone.setSelected(false);
        }
        selected = new ArrayList<>();
        size = 0;
        toBe.clear();
        System.out.println("--\nSELECTED DELETED\n--");
    }

    public static void merge(ArrayList<Stone> selected, ArrayList<Stone> toBe) {
        selected.addAll(toBe);
        size += toBe.size();
        toBe.clear();
    }
}