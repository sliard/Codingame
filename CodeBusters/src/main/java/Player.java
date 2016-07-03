import java.util.*;

/**
 * Send your busters out into the fog to trap ghosts and bring them home!
 **/
class Player {

    /**
     * Log activation
     */
    public static boolean LOG_ON = true;

    /**
     * Bust range limite
     */
    public static int BUST_MAX = 1760;
    public static int BUST_MIN = 900;

    /**
     * Release range limite
     */
    public static int RELEASE_MAX = 1600-10;

    /**
     * Stun range limite
     */
    public static int STUN_MAX = 1760;

    /**
     * Board size
     */
    public static int WIDTH = 16001;
    public static int HEIGHT = 9001;

    /**
     * Buster state
     */
    public static int STATE_CAPTURED = 1;
    public static int STATE_FREE = 0;
    public static int STATE_STUN = 2;
    public static int STATE_TARGET = 3;

    public static int GOAL_GHOST = 0;
    public static int GOAL_STUN = 1;

    /**
     * Hunter buster
     */
    public static Buster hunter = null;

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
     * Points list to explore
     */
    public static class TargetList {

        public List<Target> allTarget;
        public int cursor = 0;

        public TargetList(int teamId) {
            allTarget = new ArrayList<>();
            if(teamId == 0) {
                allTarget.add(new Target(WIDTH-(WIDTH/10), HEIGHT-(HEIGHT/10)));
            } else {
                allTarget.add(new Target(RELEASE_MAX+(WIDTH/10), RELEASE_MAX+(HEIGHT/10)));
            }
            allTarget.add(new Target((WIDTH/10), HEIGHT-(HEIGHT/10)));
            allTarget.add(new Target(WIDTH-(WIDTH/10), (HEIGHT/10)));
        }

        public Target getNextTarget() {
            cursor = (cursor+1)%allTarget.size();
            return allTarget.get(cursor);
        }
    }

    /**
     * Element on board with x and x for position.
     *
     * (0,0) on top left
     */
    public static abstract class GridElement {
        public int x, y;

        public void setPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int distance(GridElement other) {
            return (int)Math.sqrt((x-other.x)*(x-other.x) + (y-other.y)*(y-other.y));
        }
    }

    public static class Target extends GridElement {
        public Target() {

        }

        public Target(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class Buster extends GridElement {
        public int id, state, value;
        public Ghost markGhost;
        public Target target;
        public int goal;

        public boolean readyToStun = true;
        public int stunTime;

        public Buster(int id, int x, int y, int state, int value) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.state = state;
            this.value = value;

            markGhost = null;
            goal = GOAL_GHOST;
        }

        public void update(int x, int y, int state, int value) {
            this.x = x;
            this.y = y;
            this.state = state;
            this.value = value;
            if(stunTime > 0) {
                stunTime--;
            }
        }

        public void stun() {
            readyToStun = false;
            stunTime = 20;
        }

        public void leaveGhost() {
            if(markGhost != null) {
                markGhost.leaveBy(this);
            }
            markGhost = null;
        }

        public void targetGhost(Ghost g) {
            if(g != null) {
                markGhost = g;
                g.targetBy(this);
            }
        }

        /**
         * return a integer
         * @return
         */
        public int captureRate() {
            return 1;
        }

        @Override
        public String toString() {
            return "Buster(" + id + ") p=" + x + "," + y + " state="+ state +" value=" + value + " markGhost="+markGhost+" markGhost="+markGhost;
        }
    }


    public static class Ghost extends GridElement {
        public int id, state, value;
        public Map<Integer, Buster> markBy;
        public Buster captureBy;

        public Ghost(int id, int x, int y, int state, int value) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.state = state;
            this.value = value;
            captureBy = null;
            markBy = new HashMap<>();
        }

        public void update(int x, int y, int state, int value) {
            this.x = x;
            this.y = y;
            this.state = state;
            this.value = value;
        }

        public void targetBy(Buster b) {
            markBy.put(b.id,b);
        }

        public void captureBy(Buster b) {
            captureBy = b;
            markBy.remove(b.id);
            Map<Integer, Buster> markByClone = new HashMap<>(markBy);
            for(Buster otherBuster : markByClone.values()) {
                otherBuster.leaveGhost();
            }
        }

        public void leaveBy(Buster b) {
            markBy.remove(b.id);
        }

        public boolean isTargetable() {
            return captureBy == null;
        }

        @Override
        public String toString() {
            return "Ghost(" + id + ") "+(captureBy != null ? "[capture by "+captureBy.id+"] " : "")+"p=" + x + "," + y + " state="+ state +" value=" + value;
        }
    }


    public static class BusterTeam {
        Map<Integer, Buster> allBuster;
        int teamId;
        Target home;
        Target opponentHome;
        TargetList targets;

        public BusterTeam(int teamId) {
            allBuster = new HashMap<>();
            this.teamId = teamId;
            this.home = new Target();
            this.opponentHome = new Target();
            this.targets = new TargetList(teamId);

            if(teamId == 0) {
                this.home.setPosition(0,0);
                this.opponentHome.setPosition(WIDTH-1,HEIGHT-1);
            } else {
                this.home.setPosition(WIDTH-1,HEIGHT-1);
                this.opponentHome.setPosition(0,0);
            }
        }

        public void update(int entityId, int x, int y, int state, int value) {
            Buster b = allBuster.get(entityId);
            if(b != null) {
                b.update(x, y, state, value);
            } else {
                b = new Buster(entityId, x, y, state, value);
                b.target = targets.getNextTarget();
                allBuster.put(entityId, b);
            }
        }

        public void printSolution(GhostTeam ghosts, BusterTeam otherTeam, List<Integer> availableGhost) {

            for(Buster b : allBuster.values()) {
                Logger.info("***********************");
                Logger.info(""+b);

                Buster concurrentWithGhost = getConcurentToStun(b,otherTeam, true);


                if(b.state == STATE_STUN) {
                    // If buster is stun, don't move
                    b.leaveGhost();
                    Logger.print("MOVE "+b.x+" "+b.y);
                } else if(concurrentWithGhost != null && b.readyToStun) {
                    b.stun();
                    Logger.print("STUN "+concurrentWithGhost.id);
                    if(b.goal == GOAL_STUN) {
                        b.goal = GOAL_GHOST;
                        hunter = null;
                    }
                } else if(b.markGhost != null) {
                    // buster with a target
                    Ghost g = b.markGhost;

                    if(b.state == STATE_CAPTURED) {
                        // If buster capture a ghost
                        if(b.distance(home) < RELEASE_MAX) {
                            // release ghost
                            Logger.print("RELEASE");
                            b.state = STATE_FREE;
                            b.leaveGhost();
                            ghosts.remove(g.id);

                            if (hunter == null && b.readyToStun) {
                                hunter = b;
                                b.goal = GOAL_STUN;
                                b.target = opponentHome;
                            }

                        } else {
                            // go back home
                            Logger.print("MOVE "+home.x+" "+home.y);
                        }
                    } else if(b.distance(g) < BUST_MAX) {
                        // If near the ghost
                        if(!availableGhost.contains(g.id)) {
                            // but no more ghost here :(
                            Logger.info("Remove ghost "+g.id);
                            b.leaveGhost();
                            ghosts.remove(g.id);
                            b.state = STATE_FREE;
                            Logger.print("MOVE "+b.target.x+" "+b.target.y);
                        } else if(b.distance(g) < BUST_MIN) {
                            // If to close, don't move because ghost move
                            Logger.print("MOVE " + b.x + " " + b.y);
                        } else {
                            Logger.print("BUST "+g.id);
                            b.state = STATE_CAPTURED;
                        }
                    } else {
                        // else go to ghost
                        Logger.print("MOVE "+g.x+" "+g.y);
                    }
                } else if(b.goal == GOAL_STUN) {
                    b.target = opponentHome;
                    Logger.info("go stun !");
                    Buster concurrent = getConcurentToStun(b,otherTeam, true);

                    if((concurrent != null) && b.readyToStun) {
                        b.stun();
                        b.target = targets.getNextTarget();
                        b.goal = GOAL_GHOST;
                        Logger.print("STUN "+concurrent.id);
                        hunter = null;
                    } else if((b.x == b.target.x) && (b.y == b.target.y)) {
                        b.target = targets.getNextTarget();
                        b.goal = GOAL_GHOST;
                        Logger.print("MOVE "+b.target.x+" "+b.target.y);
                        hunter = null;
                    } else {
                        Logger.print("MOVE "+b.target.x+" "+b.target.y+" BANZAI!");
                    }

                } else {
                    Logger.info("No ghost");
                    Ghost g = ghosts.getClosest(b);
                    if(g != null) {
                        int newDistance = b.distance(g);
                        Logger.info("Find best ghost ! ("+newDistance+") "+g);
                        b.targetGhost(g);

                        if(newDistance < RELEASE_MAX) {
                            Logger.print("BUST "+g.id);
                            b.state = STATE_CAPTURED;
                        } else {
                            Logger.print("MOVE "+g.x+" "+g.y);
                        }
                    } else {
                        Logger.info("No ghost :(");
                        if((b.x == b.target.x) && (b.y == b.target.y)) {
                            b.target = targets.getNextTarget();
                        }
                        Logger.print("MOVE "+b.target.x+" "+b.target.y);
                    }
                }
            }
        }

        public Buster getConcurentToStun(Buster b, BusterTeam otherTeam, boolean all) {
            int currentDist;
            for(Buster other : otherTeam.allBuster.values()) {
                currentDist = other.distance(b);
                if((currentDist < STUN_MAX) && (all || other.state == STATE_CAPTURED || other.state == STATE_TARGET)) {
                    return other;
                }
            }
            return null;
        }

        public void updateState(GhostTeam ghostTeam, List<Integer> availableGhost) {
            for(Buster b : allBuster.values()) {
                if(b.state == STATE_CAPTURED) {
                    Logger.info("Buster("+b.id+") capture ghost("+b.value+")");
                    Ghost capture = ghostTeam.get(b.value);
                    if(capture != null) {
                        capture.captureBy(b);
                    }
                }
            }
        }

        @Override
        public String toString() {
            String result = "Buster Team : \n";
            for (Buster buster : allBuster.values()) {
                result += buster.toString()+"\n";
            }
            return result;
        }
    }

    public static class GhostTeam {
        Map<Integer, Ghost> allGhost;

        public GhostTeam() {
            allGhost = new HashMap<>();
        }

        public void update(int entityId, int x, int y, int state, int value) {
            Ghost g = allGhost.get(entityId);
            if(g != null) {
                g.update(x, y, state, value);
            } else {
                g = new Ghost(entityId, x, y, state, value);
                allGhost.put(entityId, g);
            }
        }

        public Ghost get(Integer id) {
            return allGhost.get(id);
        }

        public void remove(Integer id) {
            allGhost.remove(id);
        }

        public Ghost getClosest(Buster b) {
            Ghost bestGhost = null;
            int minDist = 99999;
            int currentDist;

            for(Ghost g : allGhost.values()) {
                currentDist = g.distance(b);
                if(g.isTargetable() && (currentDist < minDist) && (currentDist > BUST_MIN)) {
                    minDist = currentDist;
                    bestGhost = g;
                }
            }
            return bestGhost;
        }

        @Override
        public String toString() {
            String result = "Ghost Team : \n";
            for (Ghost ghost : allGhost.values()) {
                result += ghost.toString()+"\n";
            }
            return result;
        }
    }


    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int bustersPerPlayer = in.nextInt(); // the amount of busters you control
        int ghostCount = in.nextInt(); // the amount of ghosts on the map
        int myTeamId = in.nextInt(); // if this is 0, your base is on the top left of the map, if it is one, on the bottom right

        BusterTeam myTeam = new BusterTeam(myTeamId);

        GhostTeam ghostTeam = new GhostTeam();

        List<Integer> availableGhost = new ArrayList<>();

        // game loop
        while (true) {
            int entities = in.nextInt(); // the number of busters and ghosts visible to you
            availableGhost.clear();

            BusterTeam otherTeam = new BusterTeam(myTeamId == 0 ? 1 : 0);

            for (int i = 0; i < entities; i++) {
                int entityId = in.nextInt(); // buster id or ghost id
                int x = in.nextInt();
                int y = in.nextInt(); // position of this buster / ghost
                int entityType = in.nextInt(); // the team id if it is a buster, -1 if it is a ghost.
                int state = in.nextInt(); // For busters: 0=idle, 1=carrying a ghost.
                int value = in.nextInt(); // For busters: Ghost id being carried. For ghosts: number of busters attempting to trap this ghost.

                if(entityType == -1) {
                    ghostTeam.update(entityId, x, y, state, value);
                    availableGhost.add(entityId);
                } else if(myTeamId == entityType) {
                    myTeam.update(entityId, x, y, state, value);
                } else {
                    otherTeam.update(entityId, x, y, state, value);
                }
            }
            Logger.info(""+ghostTeam);

            Logger.info("myTeam: "+myTeam);

            myTeam.updateState(ghostTeam, availableGhost);
            otherTeam.updateState(ghostTeam, availableGhost);


            myTeam.printSolution(ghostTeam, otherTeam, availableGhost);
        }
    }
}