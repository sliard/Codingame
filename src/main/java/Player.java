import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


class Player {

    public static boolean LOG_ON = true;

    public static int NB_ROW = 6;
    public static int NB_LINE = 12;

    public static String EMPTY_ROW = "......";
    public static String EMPTY_LINE = "............";

    public static Position nextPosition;
    public static int nextRow = -1;

    public enum Position {
        H_BA(2,-1,0,NB_ROW-1,NB_ROW-1), V_AB(3,0,0,0,NB_ROW-1), H_AB(0,0,1,0,NB_ROW-2), V_BA(1,0,0,0,NB_ROW-1);

        private final int pValue;
        private final int min;
        private final int max;
        private final int start;
        private final int end;

        Position(int val, int min, int max, int start, int end) {
            this.pValue = val;
            this.min = min;
            this.max = max;
            this.start = start;
            this.end = end;
        }
        public int getValue() { return pValue; }
        public int getMin() { return min; }
        public int getMax() { return max; }
        public int getStart() { return start; }
        public int getEnd() { return end; }

        public static Position get(int val) {
            for(Position p : Position.values()) {
                if(p.getValue() == val) {
                    return p;
                }
            }
            return null;
        }
    }

    public static class Logger {

        public static void info(String txt) {
            if (LOG_ON) {
                System.err.println(txt);
            }
        }
    }

    public static class Couple {
        int colorA, colorB;
        public Couple(int a, int b) {
            this.colorA = a;
            this.colorB = b;
        }
        @Override
        public String toString() {
            return colorA+" : "+colorB+"\n";
        }
    }

    public static class Hand {
        List<Couple> allCouple;
        public Hand() {
            allCouple = new ArrayList<>();
        }
        public void addCouple(int a, int b) {
            allCouple.add(new Couple(a,b));
        }

        public Couple get(int index) {
            return allCouple.get(index);
        }

        @Override
        public String toString() {
            String text = "** Hand **\n";
            for(Couple c : allCouple) {
                text += c;
            }
            return text;
        }
    }

    public static class Grid {
        List<String> lines;
        List<String> rows;
        public String topColor = EMPTY_ROW;

        public int topLevel = 0;

        public Grid() {
            lines = new ArrayList<>();
            rows = new ArrayList<>();
            for(int i=0; i<NB_ROW; i++) {
                rows.add(EMPTY_LINE);
            }
        }

        public Grid(Grid g) {
            lines = new ArrayList<>(g.lines);
            rows = new ArrayList<>(g.rows);
        }

        public int getFirstVerticalSlot() {
            return lines.get(1).indexOf(".");
        }

        public int getFirstHorizontalSlot() {
            return lines.get(0).indexOf("..");
        }

        public void addLine(String line) {
            lines.add(line);
            String row;
            int a = lines.size();
            for(int i=0; i<NB_ROW; i++) {
                row = rows.get(i);
                rows.set(i,row.substring(0,NB_LINE-a)+line.charAt(i)+row.substring(NB_LINE-a+1));
            }
        }

        public boolean canAdd(int row, Position p) {
            try {
                if(p == Position.H_BA) {
                    return lines.get(0).substring(row+ Position.H_BA.getMin(),row+ Position.H_BA.getMax()+1).equals("..");
                }
                if(p == Position.H_AB) {
                    return lines.get(0).substring(row+ Position.H_AB.getMin(),row+ Position.H_AB.getMax()+1).equals("..");
                }
                return lines.get(1).substring(row,row+1).equals(".");

            } catch (Exception e) {
                Logger.info("row : "+row+" "+p);
                return false;
            }
        }

        public int valueAt(int x, int y) {
            char c = getAt(x,y);
            if(c == '.') {
                return 0;
            }
            return valueAtDeap(x,y,c,2,"",2);
        }

        public int valueAtDeap(int x, int y, char c, int deep, String path, int multi) {
            char cibleChar;
            int value = 0;
            int localMulti = multi;
            String currentPath = path + "("+x+","+y+")";
            for(int i=(x-1); (i<=(x+1)) && (i<NB_ROW); i++) {
                for (int j=(y-1); (i>=0) && (j<=(y+1)) && (j<NB_LINE); j++) {
                    if((j>=0) && ((x == i) ^ (y == j))) {
                        if(!currentPath.contains("("+i+","+j+")")) {
                            cibleChar = getAt(i,j);
                            if(cibleChar == c) {
                                value += 2*localMulti+2*(3-deep);
                                localMulti++;
                                if(deep > 0) {
                                    value += valueAtDeap(i,j,c,deep-1, currentPath, localMulti);
                                }
                            } else {
                                localMulti = 1;
                            }
                        }
                    }
                }
            }
            return value;
        }

        public int add(Couple c, int row, Position pos) {

            if(!canAdd(row,pos)) {
                return 0;
            }

            int r1 = row + pos.getMin();
            int r2 = row + pos.getMax();

            String sh1 = rows.get(r1);
            int h1;
            for(h1 = NB_LINE-1; (h1 >= 0) && (sh1.charAt(h1) == '.'); h1--) {
            }
            h1++;

            rows.set(r1,sh1.substring(0,h1)+(pos == Position.H_AB || pos == Position.V_BA ? c.colorA : c.colorB)+sh1.substring(h1+1));
            String sv1 = lines.get(NB_LINE-h1-1);
            lines.set(NB_LINE-h1-1,sv1.substring(0,r1)+(pos == Position.H_AB || pos == Position.V_BA ? c.colorA : c.colorB)+sv1.substring(r1+1));

            String sh2 = rows.get(r2);
            int h2;
            for(h2 = NB_LINE-1; (h2 >= 0) && (sh2.charAt(h2) == '.'); h2--) {
            }
            h2++;
            if(r1 == r2) {
                h2 = h1+1;
            }

            rows.set(r2,sh2.substring(0,h2)+(pos == Position.H_AB || pos == Position.V_BA ? c.colorB : c.colorA)+sh2.substring(h2+1));
            String sv2 = lines.get(NB_LINE-h2-1);
            lines.set(NB_LINE-h2-1,sv2.substring(0,r2)+(pos == Position.H_AB || pos == Position.V_BA ? c.colorB : c.colorA)+sv2.substring(r2+1));

            int value = 0;
            int v1 = valueAt(r1,NB_LINE-h1-1);
            value += v1;
            if(c.colorA != c.colorB) {
                int v2 = valueAt(r2,(NB_LINE-h2-1));
                value += v2;
            }
            return value;
        }

        public char getAt(int x, int y) {
            return lines.get(y).charAt(x);
        }

        public void setAt(char c, int x, int y) {
            String line = lines.get(y);
            lines.set(y,line.substring(0,x)+c+line.substring(x+1));
            String row = rows.get(x);
            rows.set(x,row.substring(0,NB_LINE-y-1)+c+row.substring(NB_LINE-y));
        }

        public boolean isEmpty() {
            return lines.get(lines.size()-1).equals(EMPTY_ROW);
        }

        public void downAll() {
            String line, row;
            for(int j=0; j<rows.size(); j++) {
                row = rows.get(j).replace(".", "");
                rows.set(j,row+EMPTY_LINE.substring(0,NB_LINE-row.length()));
            }
            for(int i=0; i<lines.size(); i++) {
                line = "";
                for(int j=0; j<rows.size(); j++) {
                    line += ""+rows.get(j).charAt(i);
                }
                lines.set(NB_LINE-i-1,line);
            }
        }

        private List<String> colorGroup;
        String path;

        public int pointExist(int x, int y, List<String> groups) {
            int result = -1;
            for(int i=0; i<groups.size(); i++) {
                if(groups.get(i).contains("{"+x+","+y+"}")) {
                    return i;
                }
            }
            return result;
        }


        public List<String> createGroup() {

            List<String> colorGroup = new ArrayList<>();

            String line, lineAfter, g;
            for(int i=NB_LINE-1; i>0; i--) {
                lineAfter = lines.get(i-1);
                line = lines.get(i);
                for(int j=0; j<NB_ROW-1; j++) {
                    char e = line.charAt(j);
                    char eAfter = line.charAt(j+1);
                    char eTop = lineAfter.charAt(j);

                    int pos = pointExist(j,i,colorGroup);

                    if(pos >= 0) {
                        g = colorGroup.get(pos);
                    } else {
                        g = "{"+j+","+i+"}";
                    }
                    if(e == eAfter) {
                        g += "{"+(j+1)+","+i+"}";
                    }
                    if(e == eTop) {
                        g += "{"+j+","+(i-1)+"}";
                    }
                    if(pos >= 0) {
                        colorGroup.set(pos,g);
                    } else if (e != '.') {
                        colorGroup.add(g);
                    }
                }
            }

            return colorGroup;
        }

        public int fusion() {
            List<String> colorGroup;
            path = "";

            String base = lines.get(lines.size()-1);
            int i;
            char currentChar;
            for(i=0; i<NB_ROW; i++) {
                currentChar = base.charAt(i);
                if(currentChar != '.') {
                    break;
                }
            }

            int nbFusion = 0;
            int a,b;

            colorGroup = createGroup();

            for(String g : colorGroup) {
                int count = g.length() - g.replace("{", "").length();
                if(count >= 4) {
                    nbFusion += count*2;
                    g = g.replaceAll("(\\}\\{)", ",");
                    String[] strs = g.replaceAll("(\\{|\\})", "").split(",");
                    for(int j=0; j<strs.length; j+=2) {
                        a = Integer.parseInt(strs[j]);
                        b = Integer.parseInt(strs[j+1]);

                        this.setAt('.',a,b);

                        if((a+1)<NB_ROW && (getAt(a+1,b) == '0')) {
                            nbFusion+=20;
                            this.setAt('.',a+1,b);
                        }
                        if((a-1)>=0 && (getAt(a-1,b) == '0')) {
                            nbFusion+=20;
                            this.setAt('.',a-1,b);
                        }
                        if((b-1)>=0 && (getAt(a,b-1) == '0')) {
                            nbFusion+=20;
                            this.setAt('.',a,b-1);
                        }
                        if((b+1)<NB_LINE && (getAt(a,b+1) == '0')) {
                            nbFusion+=20;
                            this.setAt('.',a,b+1);
                        }
                    }
                }
            }
            return nbFusion;
        }

        @Override
        public String toString() {
            String text = "** Grid **\n";
            for(String l : lines) {
                text += l+"\n";
            }
            text += "** ROW **\n";
            for(String l : rows) {
                text += l+"\n";
            }
            return text;
        }
    }

    public static class ComputePosition {

        Hand playerHand;
        Grid myGrid;

        public ComputePosition(Hand playerHand, Grid myGrid) {
            this.playerHand = playerHand;
            this.myGrid = myGrid;
        }

        public String getNewSolution() {
            // centre au debut
            if(myGrid.isEmpty()) {
                return "2 "+Position.H_AB.getValue();
            }

            String solution = getOneCombo();
            if(solution != null) {
                return solution;
            }

            System.err.println("No best : Random");
            return getRandomSolution();

        }

        public String getOneCombo() {
            Couple c1 = playerHand.get(0);
            Couple c2 = playerHand.get(1);

            if(nextPosition != null) {
                System.err.println("Find OLD : "+nextRow+" "+nextPosition);
                String res = nextRow+" "+nextPosition.getValue();
                nextPosition = null;
                return res;
            }

            Position bestPosition = Position.H_BA;
            int bestRow = 2;
            int bestScore = 0;
            int score = 0;

            int nbFusion;
            int nbFusion2;

            List<Position> allPos = new ArrayList<>();
            allPos.add(Position.H_AB);
            allPos.add(Position.V_AB);

            for (Position p1 : Position.values()) {
                for (int i = p1.getStart(); i <= p1.getEnd(); i++) {

                    for (Position p2 : allPos) {

                        int s = ((i-1) >= p2.getMin() ? (i-1) : p2.getMin());
                        int e = ((i+1) <= p2.getMax() ? (i+1) : p2.getMax());

                        for (int j = (s >= 0 ? s : 0); j <= (e > NB_ROW ? NB_ROW : e); j++) {
                            Grid g2 = new Grid(myGrid);
                            int addVal = g2.add(c1,i,p1);

                            if(addVal > 0) {
                                nbFusion = g2.fusion();
                                g2.downAll();

                                addVal += g2.add(c2,j,p2);

                                nbFusion2 = g2.fusion();

                                score = addVal + nbFusion*3 + nbFusion2;

                                Logger.info(i+" p1:"+p1+" - "+j+" p2:"+p2);
                                Logger.info("addVal:"+addVal+" nbFusion:"+nbFusion+" nbFusion2:"+nbFusion2);

                                if(bestScore < score) {
                                    bestPosition = p1;
                                    bestRow = i;
                                    bestScore = score;

                                    if(nbFusion2 > 0) {
                                    //    nextPosition = p2;
                                    //    nextRow = j;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if(bestScore > 0) {
                Logger.info("Find best, score : "+bestScore);
                return bestRow+" "+bestPosition.getValue();
            }

            Logger.info("Random");
            return getRandomSolution();
        }

        public String getRandomSolution() {
            Random randomGenerator = new Random();

            int randRow = randomGenerator.nextInt(NB_ROW);
            int randPos = randomGenerator.nextInt(3);
            if(randRow == 0 && randPos == 2) {
                randPos = 0;
            } else if(randRow == 5 && randPos == 0) {
                randPos = 2;
            }

            if(myGrid.canAdd(randRow, Position.get(randPos))) {
                return ""+randRow+" "+randPos;
            }
            return getFirstSolution();
        }

        public String getFirstSolution() {

            int h = myGrid.getFirstHorizontalSlot();
            if(h >= 0) {
                return ""+h+" 1";
            }

            int v = myGrid.getFirstVerticalSlot();
            if(v >= 0) {
                return ""+v+" 1";
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
            System.out.println(solution.getNewSolution()); // "x": the column in which to drop your blocks
        }
    }
}
