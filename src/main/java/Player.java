import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Base game classe
 */
class Player {

    /**
     * Log activation
     */
    public static boolean LOG_ON = true;

    public static int NB_ROW = 6;
    public static int NB_LINE = 12;

    public static String EMPTY_ROW = "......";
    public static String EMPTY_LINE = "............";

    public static Rotation nextRotation;
    public static int nextRow = -1;

    public static String POINT_START = "{";
    public static String POINT_LIMIT = ",";
    public static String POINT_END = "}";

    /**
     * All possible rotation
     */
    public enum Rotation {
        H_BA(2, -1, 0, NB_ROW - 1, NB_ROW - 1), V_AB(3, 0, 0, 0, NB_ROW - 1), H_AB(0, 0, 1, 0, NB_ROW - 2), V_BA(1, 0, 0, 0, NB_ROW - 1);

        private final int pValue;
        private final int min;
        private final int max;
        private final int start;
        private final int end;

        Rotation(int val, int min, int max, int start, int end) {
            this.pValue = val;
            this.min = min;
            this.max = max;
            this.start = start;
            this.end = end;
        }

        public int getValue() {
            return pValue;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public static Rotation get(int val) {
            for (Rotation p : Rotation.values()) {
                if (p.getValue() == val) {
                    return p;
                }
            }
            return null;
        }
    }

    /**
     * Logger
     */
    public static class Logger {
        public static void info(String txt) {
            if (LOG_ON) {
                System.err.println(txt);
            }
        }
    }

    /**
     * couple of 2 colors
     */
    public static class Couple {
        int colorA, colorB;

        public Couple(int a, int b) {
            this.colorA = a;
            this.colorB = b;
        }

        @Override
        public String toString() {
            return colorA + " : " + colorB + "\n";
        }
    }

    /**
     * All couple serie
     */
    public static class Hand {
        List<Couple> allCouple;

        public Hand() {
            allCouple = new ArrayList<>();
        }

        public void addCouple(int a, int b) {
            allCouple.add(new Couple(a, b));
        }

        public Couple get(int index) {
            return allCouple.get(index);
        }

        @Override
        public String toString() {
            String text = "** Hand **\n";
            for (Couple c : allCouple) {
                text += c;
            }
            return text;
        }
    }

    /**
     * Plaer grid
     */
    public static class Grid {
        List<String> lines;
        List<String> rows;

        public Grid() {
            lines = new ArrayList<>();
            rows = new ArrayList<>();
            for (int i = 0; i < NB_ROW; i++) {
                rows.add(EMPTY_LINE);
            }
        }

        /**
         * Create de Grid copy
         * @param g grid to copy
         */
        public Grid(Grid g) {
            lines = new ArrayList<>(g.lines);
            rows = new ArrayList<>(g.rows);
        }

        /**
         * Get first row where we can add couple vertically
         * @return row
         */
        public int getFirstVerticalSlot() {
            return lines.get(1).indexOf(".");
        }

        /**
         * Get first row where we can add couple horizontally
         * @return row
         */
        public int getFirstHorizontalSlot() {
            return lines.get(0).indexOf("..");
        }

        /**
         * Add a new line to construct grid
         * @param line line of points
         */
        public void addLine(String line) {
            lines.add(line);
            String row;
            int a = lines.size();
            for (int i = 0; i < NB_ROW; i++) {
                row = rows.get(i);
                rows.set(i, row.substring(0, NB_LINE - a) + line.charAt(i) + row.substring(NB_LINE - a + 1));
            }
        }

        /**
         * Can we add a couple with rotation r in row
         * @param row column number
         * @param r rotation
         * @return true if we have enough place for this move
         */
        public boolean canAdd(int row, Rotation r) {
            try {
                if (r == Rotation.H_BA) {
                    return lines.get(0).substring(row + Rotation.H_BA.getMin(), row + Rotation.H_BA.getMax() + 1).equals("..");
                }
                if (r == Rotation.H_AB) {
                    return lines.get(0).substring(row + Rotation.H_AB.getMin(), row + Rotation.H_AB.getMax() + 1).equals("..");
                }
                return lines.get(1).substring(row, row + 1).equals(".");

            } catch (Exception e) {
                Logger.info("row : " + row + " " + r);
                return false;
            }
        }

        /**
         * Count same color group size at this point
         * @param x column number (left : 0)
         * @param y line number (top : 0)
         * @return size group
         */
        public int countSerie(int x, int y) {
            char c = getAt(x, y);
            if (c == '.') {
                return 0;
            }
            return countSerieDeep(x, y, c, 5, "")+1;
        }

        /**
         * Recursive count group size
         * @param x column number (left : 0)
         * @param y line number (top : 0)
         * @param c color at x,y
         * @param deep max deep
         * @param path current path to avoid duplication count
         * @return group size
         */
        public int countSerieDeep(int x, int y, char c, int deep, String path) {
            char cibleChar;
            int value = 0;
            String currentPath = path + "(" + x + "," + y + ")";
            for (int i = (x - 1); (i <= (x + 1)) && (i < NB_ROW); i++) {
                for (int j = (y - 1); (i >= 0) && (j <= (y + 1)) && (j < NB_LINE); j++) {
                    if ((j >= 0) && ((x == i) ^ (y == j))) {
                        if (!currentPath.contains("(" + i + "," + j + ")")) {
                            cibleChar = getAt(i, j);
                            if (cibleChar == c) {
                                value++;
                                if (deep > 0) {
                                    value += countSerieDeep(i, j, c, deep - 1, currentPath);
                                }
                            }
                        }
                    }
                }
            }
            return value;
        }

        /**
         * Add a couple in the grid
         * @param c couple of colors
         * @param row column position
         * @param pos rotation
         * @return true if we can add the couple
         */
        public boolean add(Couple c, int row, Rotation pos) {

            if (!canAdd(row, pos)) {
                return false;
            }

            int r1 = row + pos.getMin();
            int r2 = row + pos.getMax();

            String sh1 = rows.get(r1);
            int h1;
            for (h1 = NB_LINE - 1; (h1 >= 0) && (sh1.charAt(h1) == '.'); h1--) {
            }
            h1++;

            rows.set(r1, sh1.substring(0, h1) + (pos == Rotation.H_AB || pos == Rotation.V_BA ? c.colorA : c.colorB) + sh1.substring(h1 + 1));
            String sv1 = lines.get(NB_LINE - h1 - 1);
            lines.set(NB_LINE - h1 - 1, sv1.substring(0, r1) + (pos == Rotation.H_AB || pos == Rotation.V_BA ? c.colorA : c.colorB) + sv1.substring(r1 + 1));

            String sh2 = rows.get(r2);
            int h2;
            for (h2 = NB_LINE - 1; (h2 >= 0) && (sh2.charAt(h2) == '.'); h2--) {
            }
            h2++;
            if (r1 == r2) {
                h2 = h1 + 1;
            }

            rows.set(r2, sh2.substring(0, h2) + (pos == Rotation.H_AB || pos == Rotation.V_BA ? c.colorB : c.colorA) + sh2.substring(h2 + 1));
            String sv2 = lines.get(NB_LINE - h2 - 1);
            lines.set(NB_LINE - h2 - 1, sv2.substring(0, r2) + (pos == Rotation.H_AB || pos == Rotation.V_BA ? c.colorB : c.colorA) + sv2.substring(r2 + 1));

            return true;
        }

        /**
         * get the color at this position
         * @param x column number (left : 0)
         * @param y line number (top : 0)
         * @return color
         */
        public char getAt(int x, int y) {
            return lines.get(y).charAt(x);
        }

        /**
         * set the color at a position
         * @param c color
         * @param x column number (left : 0)
         * @param y line number (top : 0)
         */
        public void setAt(char c, int x, int y) {
            String line = lines.get(y);
            lines.set(y, line.substring(0, x) + c + line.substring(x + 1));
            String row = rows.get(x);
            rows.set(x, row.substring(0, NB_LINE - y - 1) + c + row.substring(NB_LINE - y));
        }

        public boolean isEmpty() {
            return lines.get(lines.size() - 1).equals(EMPTY_ROW);
        }

        /**
         * Drop down all color
         */
        public void downAll() {
            String line, row;
            for (int j = 0; j < rows.size(); j++) {
                row = rows.get(j).replace(".", "");
                rows.set(j, row + EMPTY_LINE.substring(0, NB_LINE - row.length()));
            }
            for (int i = 0; i < lines.size(); i++) {
                line = "";
                for (int j = 0; j < rows.size(); j++) {
                    line += "" + rows.get(j).charAt(i);
                }
                lines.set(NB_LINE - i - 1, line);
            }
        }

        /**
         * Research a point into groups
         * @param x column number (left : 0)
         * @param y line number (top : 0)
         * @param groups group of point of same coloe
         * @return index of point group or -1 if point are not in a group
         */
        public int pointExist(int x, int y, List<String> groups) {
            int result = -1;
            for (int i = 0; i < groups.size(); i++) {
                if (groups.get(i).contains(makeStringPoint(x,y))) {
                    return i;
                }
            }
            return result;
        }

        /**
         * Create a string with point position
         * @param x column number (left : 0)
         * @param y line number (top : 0)
         * @return String "{x,y}"
         */
        public String makeStringPoint(int x, int y) {
            return POINT_START+x+POINT_LIMIT+y+POINT_END;
        }

        /**
         * Create all group of color array
         * @return collection of color group
         */
        public List<String> getColorGroups() {
            List<String> colorGroup = new ArrayList<>();
            String line, lineAfter, g;
            for (int i = NB_LINE - 1; i > 0; i--) {
                lineAfter = lines.get(i - 1);
                line = lines.get(i);
                for (int j = 0; j < NB_ROW - 1; j++) {
                    char e = line.charAt(j);
                    char eAfter = line.charAt(j + 1);
                    char eTop = lineAfter.charAt(j);
                    int pos = pointExist(j, i, colorGroup);
                    if (pos >= 0) {
                        g = colorGroup.get(pos);
                    } else {
                        g = makeStringPoint(j,i);
                    }
                    if (e == eAfter) {
                        g += makeStringPoint(j+1,i);
                    }
                    if (e == eTop) {
                        g += makeStringPoint(j,i-1);
                    }
                    if (pos >= 0) {
                        colorGroup.set(pos, g);
                    } else if (e != '.') {
                        colorGroup.add(g);
                    }
                }
            }
            return colorGroup;
        }

        /**
         * Remove group of 4 or more consecutive color
         * @param colorGroup goups of same color
         * @return number of dead head remove by fusion
         */
        public int fusion(List<String> colorGroup) {

            int nbDead = 0;
            int a, b;

            for (String g : colorGroup) {
                int count = g.length() - g.replace(POINT_START, "").length();
                if (count >= 4) {
                    g = g.replaceAll("(\\"+POINT_END+"\\"+POINT_START+")", ",");
                    String[] strs = g.replaceAll("(\\{|\\})", "").split(",");
                    for (int j = 0; j < strs.length; j += 2) {
                        a = Integer.parseInt(strs[j]);
                        b = Integer.parseInt(strs[j + 1]);
                        this.setAt('.', a, b);
                        if ((a + 1) < NB_ROW && (getAt(a + 1, b) == '0')) {
                            nbDead++;
                            this.setAt('.', a + 1, b);
                        }
                        if ((a - 1) >= 0 && (getAt(a - 1, b) == '0')) {
                            nbDead++;
                            this.setAt('.', a - 1, b);
                        }
                        if ((b - 1) >= 0 && (getAt(a, b - 1) == '0')) {
                            nbDead++;
                            this.setAt('.', a, b - 1);
                        }
                        if ((b + 1) < NB_LINE && (getAt(a, b + 1) == '0')) {
                            nbDead++;
                            this.setAt('.', a, b + 1);
                        }
                    }
                }
            }
            return nbDead;
        }

        @Override
        public String toString() {
            String text = "** Grid **\n";
            for (String l : lines) {
                text += l + "\n";
            }
            text += "** ROW **\n";
            for (String l : rows) {
                text += l + "\n";
            }
            return text;
        }
    }

    /**
     * Find best solution
     */
    public static class ComputePosition {

        Hand playerHand;
        Grid myGrid;

        /**
         * init class
         * @param playerHand
         * @param myGrid
         */
        public ComputePosition(Hand playerHand, Grid myGrid) {
            this.playerHand = playerHand;
            this.myGrid = myGrid;
        }

        /**
         * Find best play
         * @return string format "x y" with x for couple position and y for rotation
         */
        public String getSolution() {
            // first on center
            if (myGrid.isEmpty()) {
                return "2 " + Rotation.H_AB.getValue();
            }
            return getSimpleCombo();
        }

        public String getOneCombo() {
            Couple c1 = playerHand.get(0);
            Couple c2 = playerHand.get(1);

            if (nextRotation != null) {
                System.err.println("Find OLD : " + nextRow + " " + nextRotation);
                String res = nextRow + " " + nextRotation.getValue();
                nextRotation = null;
                return res;
            }

            Rotation bestRotation = Rotation.H_BA;
            int bestRow = 2;
            int bestScore = 0;
            int score = 0;

            int nbFusion;
            int nbFusion2;

            List<String> colorGroup;

            for (Rotation p1 : Rotation.values()) {
                for (int i = p1.getStart(); i <= p1.getEnd(); i++) {
                    Logger.info("***** " + i + " p1:" + p1);

                    for (Rotation p2 : Rotation.values()) {

                        int s = ((i - 1) >= p2.getMin() ? (i - 1) : p2.getMin());
                        int e = ((i + 1) <= p2.getMax() ? (i + 1) : p2.getMax());

                        for (int j = (s >= 0 ? s : 0); j <= (e > NB_ROW ? NB_ROW : e); j++) {
                            Grid g2 = new Grid(myGrid);
                            g2.add(c1, i, p1);
                            g2.add(c2, j, p2);
                            colorGroup = g2.getColorGroups();
                            nbFusion = g2.fusion(colorGroup);
                            g2.downAll();
                            colorGroup = g2.getColorGroups();
                            nbFusion2 = g2.fusion(colorGroup);
                            score = nbFusion + nbFusion2 * 5;

                            Logger.info(i + " p1:" + p1 + " - " + j + " p2:" + p2);
                            Logger.info("addVal:" + " nbFusion:" + nbFusion + " nbFusion2:" + nbFusion2);

                            if (bestScore < score) {
                                bestRotation = p1;
                                bestRow = i;
                                bestScore = score;

                                if (nbFusion2 > 0) {
                                    //    nextRotation = p2;
                                    //    nextRow = j;
                                }
                            }
                        }
                    }
                }
            }

            if (bestScore > 0) {
                Logger.info("Find best, score : " + bestScore);
                return bestRow + " " + bestRotation.getValue();
            }

            Logger.info("Random");
            return getLeftSolution();
        }


        public String getSimpleCombo() {
            Couple c1 = playerHand.get(0);

            Rotation bestRotation = Rotation.H_BA;
            int bestRow = 2;
            int bestScore = 0;
            int score = 0;

            int nbFusion;
            int nbFusion2;

            List<String> colorGroup;

            for (Rotation p1 : Rotation.values()) {
                for (int i = p1.getStart(); i <= p1.getEnd(); i++) {
                    Grid g2 = new Grid(myGrid);
                    if (g2.add(c1, i, p1)) {
                        colorGroup = g2.getColorGroups();
                        nbFusion = g2.fusion(colorGroup);
                        g2.downAll();
                        colorGroup = g2.getColorGroups();
                        nbFusion2 = g2.fusion(colorGroup);
                        score = nbFusion + nbFusion2 * 5;
                        Logger.info("nbFusion:" + nbFusion + " nbFusion2:" + nbFusion2);
                        if (bestScore < score) {
                            bestRotation = p1;
                            bestRow = i;
                            bestScore = score;
                        }
                    }
                }
            }

            if (bestScore > 0) {
                Logger.info("Find best, score : " + bestScore);
                return bestRow + " " + bestRotation.getValue();
            }

            Logger.info("Default");
            return getLeftSolution();
        }


        /**
         * Return always a random solution
         * @return string format "x y" with x for couple position and y for rotation
         */
        public String getRandomSolution() {
            Random randomGenerator = new Random();

            int randRow = randomGenerator.nextInt(NB_ROW);
            int randPos = randomGenerator.nextInt(3);
            if (randRow == 0 && randPos == 2) {
                randPos = 0;
            } else if (randRow == 5 && randPos == 0) {
                randPos = 2;
            }

            if (myGrid.canAdd(randRow, Rotation.get(randPos))) {
                return "" + randRow + " " + randPos;
            }
            return getLeftSolution();
        }

        /**
         * Always on left
         * @return string format "x y" with x for couple position and y for rotation
         */
        public String getLeftSolution() {
            int v = myGrid.getFirstVerticalSlot();
            if (v >= 0) {
                return "" + v + " 1";
            }
            int h = myGrid.getFirstHorizontalSlot();
            if (h >= 0) {
                return "" + h + " 1";
            }
            return "0 0";
        }
    }

    public static void main(String args[]) {

        Scanner in = new Scanner(System.in);

        // game loop
        while (true) {

            Hand hand = new Hand();
            Grid myGrid = new Grid();

            for (int i = 0; i < 8; i++) {
                int colorA = in.nextInt(); // color of the first block
                int colorB = in.nextInt(); // color of the attached block
                hand.addCouple(colorA, colorB);
                // A en haut B en bas
            }
            for (int i = 0; i < 12; i++) {
                String row = in.next();
                myGrid.addLine(row);
            }
            for (int i = 0; i < 12; i++) {
                String row = in.next(); // One line of the map ('.' = empty, '0' = skull block, '1' to '5' = colored block)
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            ComputePosition solution = new ComputePosition(hand, myGrid);
            System.out.println(solution.getLeftSolution()); // "x": the column in which to drop your blocks
        }
    }
}
