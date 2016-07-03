
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class TestPlayer {

    /**
     * Test ajout couple 1
     */
    @Test
    public void testDistance() {
        System.out.println("Test Distance");

        int nbGhost = 0;
        Player.Ghost g1 = new Player.Ghost(nbGhost++, 0, 0, 0, 0);
        Player.Ghost g2 = new Player.Ghost(nbGhost++, 0, 10, 0, 0);
        assertTrue(g1.distance(g2) == 10);
        g2.setPosition(10,0);
        assertTrue(g1.distance(g2) == 10);
        g2.setPosition(33,0);
        assertTrue(g1.distance(g2) == 33);
        g1.setPosition(0,0);
        g2.setPosition(3,4);
        assertTrue(g1.distance(g2) == 5);
        g1.setPosition(10,10);
        g2.setPosition(13,14);
        assertTrue(g1.distance(g2) == 5);
        g1.setPosition(10,10);
        g2.setPosition(7,14);
        assertTrue(g1.distance(g2) == 5);
        g1.setPosition(10,10);
        g2.setPosition(7,6);
        assertTrue(g1.distance(g2) == 5);

        System.out.println("Test Distance OK !");
    }

}

