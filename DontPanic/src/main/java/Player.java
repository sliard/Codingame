import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    /**
     * Log activation
     */
    public static boolean LOG_ON = true;

    /**
     * Logger
     */
    public static class Logger {
        /**
         * Debug log
         * @param txt
         */
        public static void info(String txt) {
            if (LOG_ON) {
                System.err.println(txt);
            }
        }

        /**
         * Print output
         * @param txt
         */
        public static void print(String txt) {
            System.out.println(txt);
        }
    }

    /**
     * Clone direction
     */
    public enum Direction {
        LEFT("LEFT"), RIGHT("RIGHT"), NONE("NONE");

        private final String pValue;

        Direction(String text) {
            this.pValue = text;
        }

        public String getValue() {
            return pValue;
        }

        public static Direction get(String val) {
            for (Direction d : Direction.values()) {
                if (d.getValue().equals(val)) {
                    return d;
                }
            }
            return null;
        }
    }

    /**
     * action result
     */
    public enum Action {
        WAIT("WAIT"), BLOCK("BLOCK");

        private final String pValue;

        Action(String text) {
            this.pValue = text;
        }

        public String getValue() {
            return pValue;
        }

        @Override
        public String toString() {
            return this.pValue;
        }
    }

    /**
     * Elevator information
     */
    public static class Elevator {

        int elevatorFloor;
        int elevatorPos;

        public Elevator(int elevatorFloor, int elevatorPos) {
            this.elevatorFloor = elevatorFloor;
            this.elevatorPos = elevatorPos;
        }

        @Override
        public String toString() {
            return "Elevator("+elevatorFloor+","+elevatorPos+")";
        }
    }

    public static class CloneGame {

        // number of floors
        int nbFloors;
        // width of the area
        int width;
        // maximum number of rounds
        int nbRounds;
        // floor on which the exit is found
        int exitFloor;
        // position of the exit on its floor
        int exitPos;
        // number of generated clones
        int nbTotalClones;
        // ignore (always zero)
        int nbAdditionalElevators;

        int currentRound;
        int currentNbClone;

        Map<Integer, Elevator> elevators;

        public CloneGame(int nbFloors, int width, int nbRounds, int exitFloor, int exitPos, int nbTotalClones, int nbAdditionalElevators) {
            this.nbFloors = nbFloors;
            this.width = width;
            this.nbRounds = nbRounds;
            this.exitFloor = exitFloor;
            this.exitPos = exitPos;
            this.nbTotalClones = nbTotalClones;
            this.nbAdditionalElevators = nbAdditionalElevators;

            currentRound = 0;
            currentNbClone = nbTotalClones;

            elevators = new HashMap<>();
        }

        public void setElevatorList(List<Elevator> elevators) {
            Logger.info(elevators.toString());
            for(Elevator e : elevators) {
                this.elevators.put(e.elevatorFloor,e);
            }
        }

        public Action loop(int cloneFloor, int clonePos, String directionString) {
            Direction direction = Direction.get(directionString);
            Action result = Action.WAIT;
            int targetPosition;
            currentRound++;

            if(direction == Direction.NONE) {
                return result;
            }

            if(cloneFloor == exitFloor) {
                targetPosition = exitPos;
            } else {
                targetPosition = elevators.get(cloneFloor).elevatorPos;
            }

            if ((direction == Direction.RIGHT) && (clonePos - targetPosition > 0)) {
                result = Action.BLOCK;
            } else if ((direction == Direction.LEFT) && (clonePos - targetPosition < 0)) {
                result = Action.BLOCK;
            }

            if(result == Action.BLOCK) {
                currentNbClone--;
            }

            return result;
        }

    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int nbFloors = in.nextInt(); // number of floors
        int width = in.nextInt(); // width of the area
        int nbRounds = in.nextInt(); // maximum number of rounds
        int exitFloor = in.nextInt(); // floor on which the exit is found
        int exitPos = in.nextInt(); // position of the exit on its floor
        int nbTotalClones = in.nextInt(); // number of generated clones
        int nbAdditionalElevators = in.nextInt(); // ignore (always zero)

        CloneGame cloneGame = new CloneGame(nbFloors, width, nbRounds, exitFloor, exitPos, nbTotalClones, nbAdditionalElevators);

        List<Elevator> elevators = new ArrayList<>(nbFloors);
        int nbElevators = in.nextInt(); // number of elevators
        for (int i = 0; i < nbElevators; i++) {
            int elevatorFloor = in.nextInt(); // floor on which this elevator is found
            int elevatorPos = in.nextInt(); // position of the elevator on its floor
            elevators.add(new Elevator(elevatorFloor, elevatorPos));
        }
        cloneGame.setElevatorList(elevators);

        // game loop
        while (true) {
            int cloneFloor = in.nextInt(); // floor of the leading clone
            int clonePos = in.nextInt(); // position of the leading clone on its floor
            String direction = in.next(); // direction of the leading clone: LEFT or RIGHT

            Logger.info(""+cloneFloor);

            Logger.print(cloneGame.loop(cloneFloor, clonePos, direction).toString());
        }
    }
}