import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {

        HashMap<Integer, List<Integer>> links = new HashMap<>();
        List<Integer> gateways = new ArrayList<>();

        Scanner in = new Scanner(System.in);
        int N = in.nextInt(); // the total number of nodes in the level, including the gateways
        int L = in.nextInt(); // the number of links
        int E = in.nextInt(); // the number of exit gateways
        for (int i = 0; i < L; i++) {
            int N1 = in.nextInt(); // N1 and N2 defines a link between these nodes
            int N2 = in.nextInt();

            List<Integer> nodeList1 = links.get(N1);
            if(nodeList1 == null) {
                nodeList1 = new ArrayList<>();
            }
            nodeList1.add(N2);
            links.put(N1,nodeList1);

            List<Integer> nodeList2 = links.get(N2);
            if(nodeList2 == null) {
                nodeList2 = new ArrayList<>();
            }
            nodeList2.add(N1);
            links.put(N2,nodeList2);
        }
        for (int i = 0; i < E; i++) {
            int EI = in.nextInt(); // the index of a gateway node
            gateways.add(EI);
        }

        List<Integer> listNextAgent;
        boolean find;
        Integer lastNode = -1;
        // game loop
        while (true) {
            int SI = in.nextInt(); // The index of the node on which the Skynet agent is positioned this turn

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            find = false;
            listNextAgent = links.get(SI);
            for (Integer nextAgent : listNextAgent) {
                if(gateways.contains(nextAgent)) {
                    System.out.println(SI+" "+nextAgent);
                    find=true;
                }
                lastNode = nextAgent;
            }
            if(!find) {
                System.out.println(SI+" "+lastNode);
            }
        }
    }
}