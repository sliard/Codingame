
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class TestPlayer {

    private void initGrid(Player.Grid grid, List<String> gridTxt) {

        for(String line : gridTxt) {
            grid.addLine(line);
        }

    }
//         DEAD(0,"DEAD"), BLUE(1,"BLUE"), GREEN(2,"GREEN"), PINK(4,"PINK"), YELLOW(5,"YELLOW"), RED(6,"RED");

    /**
     * Test ajout couple 1
     */
    @Test
    public void testDown() {
        System.out.println("Test Down");

        List<String> grid1 = new ArrayList<>();
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("...222");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("11....");

        Player.Grid myGrid = new Player.Grid();
        initGrid(myGrid, grid1);
        myGrid.downAll();

        List<String> response = new ArrayList<>();
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("11.222");

        List<String> responseH = new ArrayList<>();
        responseH.add("1...........");
        responseH.add("1...........");
        responseH.add("............");
        responseH.add("2...........");
        responseH.add("2...........");
        responseH.add("2...........");

        for(int i=0; i<response.size(); i++) {
            assertTrue(response.get(i).equals(myGrid.lines.get(i)));
        }

        for(int i=0; i<responseH.size(); i++) {
            assertTrue(responseH.get(i).equals(myGrid.rows.get(i)));
        }
        System.out.println("Test Down OK !");
    }

    /**
     * Test ajout couple 2
     */
    @Test
    public void testAdd() {
        System.out.println("Test Add");

        List<String> grid1 = new ArrayList<>();
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("...222");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("11....");

        Player.Grid myGrid = new Player.Grid();
        initGrid(myGrid, grid1);
        System.out.println(myGrid);

        myGrid.add(new Player.Couple(3,4),1, Player.Rotation.V_BA);

        List<String> response = new ArrayList<>();
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("...222");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add(".4....");
        grid1.add(".3....");
        grid1.add("11....");

        List<String> responseH = new ArrayList<>();
        responseH.add("1...........");
        responseH.add("134.........");
        responseH.add("............");
        responseH.add("......2.....");
        responseH.add("......2.....");
        responseH.add("......2.....");

        System.out.println(myGrid);

        for(int i=0; i<response.size(); i++) {
            assertTrue(response.get(i).equals(myGrid.lines.get(i)));
        }

        for(int i=0; i<responseH.size(); i++) {
            assertTrue(responseH.get(i).equals(myGrid.rows.get(i)));
        }

    }

    /**
     * Test ajout chute 2
     */
    @Test
    public void testDown2() {

        List<String> grid1 = new ArrayList<>();
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("...222");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("11....");

        Player.Grid myGrid2 = new Player.Grid();
        initGrid(myGrid2, grid1);
        myGrid2.add(new Player.Couple(3,4),1, Player.Rotation.H_AB);

        List<String> response = new ArrayList<>();
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("...222");
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("......");
        response.add(".3....");
        response.add("114...");

        List<String> responseH = new ArrayList<>();
        responseH.add("1...........");
        responseH.add("13..........");
        responseH.add("4...........");
        responseH.add("......2.....");
        responseH.add("......2.....");
        responseH.add("......2.....");

        System.out.println(myGrid2);

        for(int i=0; i<response.size(); i++) {
            assertTrue(response.get(i).equals(myGrid2.lines.get(i)));
        }

        for(int i=0; i<responseH.size(); i++) {
            assertTrue(responseH.get(i).equals(myGrid2.rows.get(i)));
        }
    }

    /**
     * Test ajout chute 3
     */
    @Test
    public void testDown3() {
        List<String> grid1 = new ArrayList<>();
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("...222");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("11....");

        Player.Grid myGrid2 = new Player.Grid();
        initGrid(myGrid2, grid1);
        myGrid2.add(new Player.Couple(3,4),1, Player.Rotation.H_BA);

        List<String> response = new ArrayList<>();
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("...222");
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("43....");
        response.add("11....");

        List<String> responseH = new ArrayList<>();
        responseH.add("14..........");
        responseH.add("13..........");
        responseH.add("............");
        responseH.add("......2.....");
        responseH.add("......2.....");
        responseH.add("......2.....");

        System.out.println(myGrid2);

        for(int i=0; i<response.size(); i++) {
            assertTrue(response.get(i).equals(myGrid2.lines.get(i)));
        }

        for(int i=0; i<responseH.size(); i++) {
            assertTrue(responseH.get(i).equals(myGrid2.rows.get(i)));
        }

    }

    @Test
    public void testFusion() {
        List<String> grid1 = new ArrayList<>();
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add("......");
        grid1.add(".2....");
        grid1.add(".1....");
        grid1.add(".1....");
        grid1.add("11233.");

        Player.Grid myGrid2 = new Player.Grid();
        initGrid(myGrid2, grid1);
        System.out.println(myGrid2);

        List<String> colorGroups = myGrid2.getColorGroups();
        myGrid2.fusion(colorGroups);

        List<String> response = new ArrayList<>();
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("......");
        response.add("......");
        response.add(".2....");
        response.add("......");
        response.add("......");
        response.add("..233.");

        List<String> responseH = new ArrayList<>();
        responseH.add("............");
        responseH.add("...2........");
        responseH.add("2...........");
        responseH.add("3...........");
        responseH.add("3...........");
        responseH.add("............");

        System.out.println(myGrid2);

        for(int i=0; i<response.size(); i++) {
            assertTrue(response.get(i).equals(myGrid2.lines.get(i)));
        }

        for(int i=0; i<responseH.size(); i++) {
            assertTrue(responseH.get(i).equals(myGrid2.rows.get(i)));
        }
    }

    @Test
    public void testFusion2() {
        List<String> grid1 = new ArrayList<>();
        grid1.add("...4..");
        grid1.add("...4..");
        grid1.add("...3..");
        grid1.add("...3..");
        grid1.add("...2..");
        grid1.add("...2..");
        grid1.add("...1..");
        grid1.add("...1..");
        grid1.add(".2.1..");
        grid1.add(".1.44.");
        grid1.add(".1.43.");
        grid1.add("112333");

        Player.Grid myGrid2 = new Player.Grid();
        initGrid(myGrid2, grid1);
        System.out.println(myGrid2);

        List<String> colorGroups = myGrid2.getColorGroups();
        myGrid2.fusion(colorGroups);

        List<String> response = new ArrayList<>();
        response.add("...4..");
        response.add("...4..");
        response.add("...3..");
        response.add("...3..");
        response.add("...2..");
        response.add("...2..");
        response.add("...1..");
        response.add("...1..");
        response.add(".2.1..");
        response.add("...44.");
        response.add("...4..");
        response.add("..2...");

        List<String> responseH = new ArrayList<>();
        responseH.add("............");
        responseH.add("...2........");
        responseH.add("2...........");
        responseH.add(".44111223344");
        responseH.add("..4.........");
        responseH.add("............");

        System.out.println(myGrid2);

        for(int i=0; i<response.size(); i++) {
            assertTrue(response.get(i).equals(myGrid2.lines.get(i)));
        }

        for(int i=0; i<responseH.size(); i++) {
            assertTrue(responseH.get(i).equals(myGrid2.rows.get(i)));
        }

    }

    @Test
    public void testFusion3() {
        List<String> grid1 = new ArrayList<>();
        grid1.add("...4..");
        grid1.add("...4..");
        grid1.add("...3..");
        grid1.add("...3..");
        grid1.add("...2..");
        grid1.add("...2..");
        grid1.add("...1..");
        grid1.add("...10.");
        grid1.add(".2.10.");
        grid1.add("01.44.");
        grid1.add("01.430");
        grid1.add("112333");

        Player.Grid myGrid2 = new Player.Grid();
        initGrid(myGrid2, grid1);
        System.out.println(myGrid2);

        List<String> colorGroups = myGrid2.getColorGroups();
        myGrid2.fusion(colorGroups);

        List<String> response = new ArrayList<>();
        response.add("...4..");
        response.add("...4..");
        response.add("...3..");
        response.add("...3..");
        response.add("...2..");
        response.add("...2..");
        response.add("...1..");
        response.add("...10.");
        response.add(".2.10.");
        response.add("...44.");
        response.add("...4..");
        response.add("..2...");

        List<String> responseH = new ArrayList<>();
        responseH.add("............");
        responseH.add("...2........");
        responseH.add("2...........");
        responseH.add(".44111223344");
        responseH.add("..400.......");
        responseH.add("............");

        System.out.println(myGrid2);

        for(int i=0; i<response.size(); i++) {
            assertTrue(response.get(i).equals(myGrid2.lines.get(i)));
        }

        for(int i=0; i<responseH.size(); i++) {
            assertTrue(responseH.get(i).equals(myGrid2.rows.get(i)));
        }

    }


    @Test
    public void testAlgo() {
        List<String> grid1 = new ArrayList<>();
        grid1.add("...4..");
        grid1.add("...4..");
        grid1.add("...3..");
        grid1.add("...3..");
        grid1.add("...2..");
        grid1.add("...2..");
        grid1.add("...1..");
        grid1.add("...1..");
        grid1.add(".2.1..");
        grid1.add(".1.44.");
        grid1.add(".1.43.");
        grid1.add("112333");

        Player.Grid myGrid2 = new Player.Grid();
        initGrid(myGrid2, grid1);

        Player.Hand hand = new Player.Hand();
        hand.addCouple(1,1);
        hand.addCouple(2,2);

        Player.ComputePosition comput = new Player.ComputePosition(hand, myGrid2);

        System.out.println( comput.getOneCombo());

    }

}

