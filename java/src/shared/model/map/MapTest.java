package shared.model.map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import shared.exceptions.*;
import shared.locations.*;
import shared.definitions.*;

import static org.junit.Assert.*;

/**
 * Created by joel on 1/31/16.
 */
public class MapTest {

    private Map map;

    @Before
    public void setUp() throws Exception {
        map = new Map();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGiveResources() throws Exception {

    }

    @Test
    public void testInitiateSettlement() throws Exception {
        HexLocation hexLocOne = new HexLocation(0, 0);
        VertexLocation vertexLocOne = new VertexLocation(hexLocOne, VertexDirection.West);
        HexLocation hexLocTwo = new HexLocation(-1,0);
        VertexLocation vertexLocTwo = new VertexLocation(hexLocTwo, VertexDirection.NorthWest);
        try{
            map.initiateSettlement(1, vertexLocOne);
            map.initiateSettlement(2, vertexLocTwo);
        } catch (StructureException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testInitiateRoad() throws Exception {
        HexLocation hexLocOne = new HexLocation(0, 0);
        VertexLocation vertexLocOne = new VertexLocation(hexLocOne, VertexDirection.West);
        HexLocation hexLocTwo = new HexLocation(-1,0);
        EdgeLocation edgeLocTwo = new EdgeLocation(hexLocTwo, EdgeDirection.North);
        try{
            map.initiateSettlement(1, vertexLocOne);
            map.initiateRoad(2, edgeLocTwo, vertexLocOne);
        } catch (StructureException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testCanBuildRoad() throws Exception {

    }

    @Test
    public void testBuildRoad() throws Exception {

    }

    @Test
    public void testCanBuildSettlement() throws Exception {

    }

    @Test
    public void testBuildSettlement() throws Exception {

    }

    @Test
    public void testCanBuildCity() throws Exception {

    }

    @Test
    public void testBuildCity() throws Exception {

    }

    @Test
    public void testGetLongestRoadSize() throws Exception {

    }

    @Test
    public void testGetPortTypes() throws Exception {

    }

    @Test
    public void testMoveRobber() throws Exception {

    }
}