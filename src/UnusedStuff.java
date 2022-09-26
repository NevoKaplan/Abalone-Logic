import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class UnusedStuff {

    final int maxTroops = 14, cols = 9, rows = 9;
    public Stone[][] hex;
    private static Scanner in = new Scanner(System.in);
    private int player;
    private ArrayList<String> dirList;
    private final int[][] dirArr = {{-1, 0}, {0, 1}, {1, 1}, {1, 0}, {0, -1}, {-1, -1}}; //

    private static UnusedStuff single_instance = null;

    private UnusedStuff() {
        boolean increase = false;
        player = 1;
        int x = 0, temp = (int) Math.floor(cols / 2.0);
        Stone[][] tempHex = new Stone[rows][cols];
        for (int i = 0; i < rows; i++) {
            x = temp;
            for (int j = 0; j < cols; j++) {
                if (!increase && cols - j <= temp) {
                    tempHex[i][j] = new Stone(4, i, j);
                }
                else if (increase && x > 0) {
                    tempHex[i][j] = new Stone(4, i, j);
                    x--;
                }
                else {
                    tempHex[i][j] = new Stone(0, i, j);
                }
            }
            if (!increase && temp <= 0)
                increase = true;
            temp = increase ? temp + 1 : temp - 1;
        }
        hex = tempHex;

        dirList = new ArrayList<>();
        dirList.add("UpRight");
        dirList.add("Right");
        dirList.add("DownRight");
        dirList.add("DownLeft");
        dirList.add("Left");
        dirList.add("UpLeft");
    }

    public static UnusedStuff getInstance()
    {
        if (single_instance == null)
            single_instance = new UnusedStuff();

        return single_instance;
    }

    public void organizeNormal() {
        int troops = maxTroops;
        for (int i = 0; i < hex.length; i++) {
            for (int j = 0; j < hex[i].length; j++) {
                if (((((i == 2) && (j > 1 && j < 5)) || i == 6 && (j > 2 && j <= 6)) || (i < 2 || i > 6)) && hex[i][j].getMainNum() != 4) {
                    if (troops != 0) {
                        hex[i][j].setMainNum(troops / Math.abs(troops)); }
                    troops--;
                }
            }
        }
    }

    public void print() {
        String space = "          ";
        for (int i = 0; i < hex.length; i++) {
            for (int r = 0; r < hex.length - i; r ++) {
                space += "";
            }
            for (int j = 0; j < hex[i].length; j++) {
                if (hex[i][j].getMainNum() != 4) {
                    if (hex[i][j].getSelected())
                        System.out.print(space + " " + 3 + "  ");
                    else if (hex[i][j].getMainNum() == -1)
                        System.out.print(space + " " + 2 + "  ");
                    else
                        System.out.print(space + " " +hex[i][j].getMainNum() + "  ");
                    space = "";
                }
                else if (hex[i][j].getMainNum() == 4)
                    space += "  ";

            }
            if (i >= hex.length/2)
                space += "  ";
            System.out.println();
        }
    }

    //will assume good ranges of input
    public void makeMove() {

        ArrayList<Stone> stones = new ArrayList<>();
        ArrayList<int[]> availableMove = new ArrayList<>();
        ArrayList<String> b = new ArrayList<>();
        ArrayList<Stone[]> s = new ArrayList<>();
        while (true) {
            System.out.print("Enter row: ");
            int row = in.nextInt();
            System.out.print("Enter col: ");
            int col = in.nextInt();


            System.out.println(s.size());
            int[] checked = checkPick(s, hex[row][col]);
            if ((((hex[row][col].getMainNum() == player) || (hex[row][col].getMainNum() == player)) && stones.isEmpty()) || (!stones.isEmpty() && checked[0] != 0)) {
                if (checked[0] == 1) {
                    stones.add(hex[row][col]);
                }
                else if (checked[0] == 2) {
                    Stone e = s.get(checked[1])[1];
                    stones.add(hex[e.row][e.col]);
                    stones.add(hex[row][col]);
                }
                stones.add(hex[row][col]);
                hex[row][col].changeSelected();
                s = availableStones(stones);
                 //---- this part shows stones you can select

                Iterator<Stone[]> s2 = s.iterator();
                while (s2.hasNext()) {
                    Stone[] temp = s2.next();
                    for (int i = 1; i < temp.length; i++) {
                        if (temp[i] != null)
                            hex[temp[i].row][temp[i].col].setMainNum(7);
                    }
                }


                for (int i = 0; i < dirArr.length; i++) {
                    if (check(dirArr[i][0], dirArr[i][1], hex[row][col], 0))
                        availableMove.add(dirArr[i]);
                }

                Iterator<int[]> a = availableMove.iterator();
                while (a.hasNext()) {
                    int[] t = a.next();
                    if (t[0] == -1 && t[1] == 0)
                        b.add("UpRight");
                    else if (t[0] == 0 && t[1] == 1)
                        b.add("Right");
                    else if (t[0] == 1 && t[1] == 1)
                        b.add("DownRight");
                    else if (t[0] == 1 && t[1] == 0)
                        b.add("DownLeft");
                    else if (t[0] == 0 && t[1] == -1)
                        b.add("Left");
                    else
                        b.add("UpLeft");
                }

                Iterator<String> b1 = b.iterator();
                while (b1.hasNext()) {
                    System.out.print(b1.next() + ", ");
                }
                System.out.println();
            }
            else {
                System.out.println("Something isn't right");
            }
            this.print();
        }

    }

    public int[] checkPick(ArrayList<Stone[]> stones2, Stone chosen) {

        Iterator<Stone[]> stones = stones2.iterator();
        int i = 0;
        while (stones.hasNext()) {
            Stone[] s = stones.next();
            for (int j = 1; j < 3; j++) {
                if (s[j] != null && chosen.equals(s[j])) {
                    return new int[] {j, i};
                }
            }
            i++;
        }
        return new int[] {-1, -1};
    }

    public ArrayList<Stone[]> availableStones(ArrayList<Stone> mainStone) { // not good enough
        ArrayList<Stone[]> stones = new ArrayList<>();
        int size = mainStone.size(), r = 0;
        if (size == 1) {
            Stone temp = mainStone.get(0);
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (!(i * -1 == j)) {
                        try {
                            if (hex[i + temp.row][j + temp.col].getMainNum() == player) {
                                stones.add(new Stone[3]);
                                stones.get(r)[0] = temp;
                                stones.get(r)[1] = hex[i + temp.row][j + temp.col];
                                try {
                                    if (hex[2 * i + temp.row][2 * j + temp.col].getMainNum() == player)
                                        stones.get(r)[2] = hex[i + temp.row][j + temp.col];
                                } catch (Exception e) {}
                                r++;
                            }
                        } catch (Exception e) {}
                    }
                }
            }
        }
        return stones;
    }

    public String choseDir(ArrayList<Stone> stones1) {

        ArrayList<String> temp = (ArrayList<String>) dirList.clone();
        Iterator<Stone> stones = stones1.iterator();
        while (stones.hasNext()) {
            Stone s = stones.next();

        }
        legalDir(stones1, 0, new ArrayList<>());
        return "";

    }

    public String legalDir(ArrayList<Stone> stones, int index, ArrayList<String> dir) {
        if (index == stones.size()-1) {
            return "";
        }
        return "";
    }

    public boolean check(int rowOffset, int colOffset, Stone stone, int count) {
        if (count >= 3)
            return false;
        if (hex[stone.row + rowOffset][stone.col + colOffset].getMainNum() == 0)
            return true;
        if (hex[stone.row + rowOffset][stone.col + colOffset].getMainNum() == player * -1)
            return check(rowOffset, colOffset, hex[stone.row + rowOffset][stone.col + colOffset], count+1);
        else
            return false;
    }

    private boolean choosePiece2(Stone stone) {
        Stone.sort(Stone.selected);
        Stone first = Stone.selected.get(0);
        Stone second = Stone.selected.get(1);
        int drow = first.row - second.row;
        int dcol = first.col - second.col;
        if (hex[first.row + drow][first.col + dcol].equals(stone) || hex[second.row - drow][second.col - dcol].equals(stone)) {
            stone.changeSelected();
            return true;
        }
        return false;
    }

    private ArrayList<Stone> makeMove2(ArrayList<ArrayList<Stone>> stones, Stone checkedStone) {
        if (stones == null)
            return null;
        Iterator<ArrayList<Stone>> it = stones.iterator();
        while (it.hasNext()) {
            ArrayList<Stone> temp = it.next();
            if (temp.contains(checkedStone))
                return temp;
        }
        return null; // if equals to null -> reset player choices
    }

    private ArrayList<ArrayList<Stone>> availableStones(Stone mainStone) { // not good enough
        Stone temp = mainStone;
        //stones[index] = new ArrayList<>();
        ArrayList<ArrayList<Stone>> stones = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i * -1 == j)) {
                    ArrayList<Stone> arrayList = new ArrayList<>();
                    try {
                        if (hex[i + temp.row][j + temp.col].getMainNum() == player) {
                            //stones[index].add(hex[i + temp.row][j + temp.col]);
                            arrayList.add(hex[i + temp.row][j + temp.col]);
                            try {
                                if (hex[2 * i + temp.row][2 * j + temp.col].getMainNum() == player) {
                                    //stones[index].add(hex[2 * i + temp.row][2 * j + temp.col]);
                                    arrayList.add(hex[2 * i + temp.row][2 * j + temp.col]);
                                }
                            } catch (Exception e) {}
                        }
                    } catch (Exception e) {}
                    if (!arrayList.isEmpty())
                        stones.add(arrayList);
                }
            }
        }
        return stones;
    }

    private int timesToMove(int drow, int dcol, Stone stone) {
        if (hex[stone.row][stone.col].getMainNum() == 0)
            return 1;
        else if (hex[stone.row][stone.col].getMainNum() == 4)
            return 0;
        if (hex[stone.row][stone.col].getMainNum() == player * -1) {
            //hex[stone.row][stone.col].changeSelected();
            Stone.toBe.add(hex[stone.row][stone.col]);
            System.out.println("Just changed: " + stone);
            try {
                return timesToMove(drow, dcol, hex[stone.row + drow][stone.col + dcol]) + 1;
            }
            catch (ArrayIndexOutOfBoundsException e) {
                return 0;
            }
        }
        return 0;
    }

    public static ArrayList<Stone> IntersectionSort(ArrayList<Stone>[] stones) {
        ArrayList<Stone> real = new ArrayList<>();
        if (stones[1] != null) {
            int size = Math.min(stones[0].size(), stones[1].size());

            for (int i = 0; i < size; i++) {
                try {
                    Stone s = stones[1].get(i);
                    if (stones[0].contains(s))
                        real.add(s);
                }
                catch (Exception e) {}
            }
            return real;
        }
        else {
            return stones[0];
        }
    }

    // stones layout
    private void organizeBelgianDaisy() {
        // this one is manual
        int[][] placeAcc =
                {
                        {1,1,0,-1,-1,4,4,4,4},
                        {1,1,1,-1,-1,-1,4,4,4},
                        {0,1,1,0,-1,-1,0,4,4},
                        {0,0,0,0,0,0,0,0,4},
                        {0,0,0,0,0,0,0,0,0},
                        {4,0,0,0,0,0,0,0,0},
                        {4,4,0,-1,-1,0,1,1,0},
                        {4,4,4,-1,-1,-1,1,1,1},
                        {4,4,4,4,-1,-1,0,1,1},
                };
        for (int i = 0; i < placeAcc.length; i++) {
            for (int j = 0; j < placeAcc[i].length; j++) {
                hex[i][j].setMainNum(placeAcc[i][j]);
            }
        }
    }

    // making a hex shape with arrays - no use.
    public static int[][] Hex(int rows, int cols) {
        String space = "";
        boolean increase = true;
        int x = cols / 2;
        System.out.println(x);
        int[][] hex = new int[rows][];
        for (int i = 0; i < rows; i++) {
            if (increase && x >= cols)
                increase = false;
            x = increase ? x + 1 : x - 1;
            hex[i] = new int[x];
            for (int r = 0; r < cols - x; r++) {
                space += " ";
            }
            for (int j = 0; j < x; j++) {
                System.out.print(space + hex[i][j]);
                space = "";
            }
            System.out.println();
        }
        return hex;
    }
}