package cs5011.logic.agent;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import cs5011.logic.game.Game;

class SPSAgentTest {

	private SPSAgent agent;
	@BeforeEach
	void setUp() throws Exception {
		agent = new SPSAgent(10);
	}

	/**
	 * Create a board which there is a cell that satisfies with AFN; in case that the cell surrounded by only uncovered tiles
	 * @result allFreeNeighbours() should return true
	 */
	@Test
	void testAllFreeNeighboursWithUncoveredTiles() {
		String[][] map = new String [][] {
			 {"2","d"},
			 {"d","2"}};
			 
		Game world = new Game(map);
		world.probe(0, 0, agent);
		world.probe(0, 1, agent);
		world.probe(1, 0, agent);
		
		assertTrue(agent.allFreeNeighbours(world, 1, 1));
	}
	
	/**
	 * Create a board which there is a cell that satisfies with AFN; in case that the cell surrounded by uncovered/flagged tiles
	 * @result allFreeNeighbours() should return true
	 */
	@Test
	void testAllFreeNeighboursWithFlaggedTiles() {
		String[][] map = new String [][] {
			 {"2","d"},
			 {"d","2"}};
			 
		Game world = new Game(map);
		world.probe(0, 0, agent);
		world.flag(0, 1);
		world.flag(1, 0);
		
		assertTrue(agent.allFreeNeighbours(world, 1, 1));
	}
	
	/**
	 * Create a board which there is no cells that satisfies with AFN
	 * @result allFreeNeighbours() should return false
	 */
	@Test
	void testAllFreeNeighboursWithInvalidCase() {

		String[][] map = new String [][] {
			 {"2","d"},
			 {"d","2"}};
			 
		Game world = new Game(map);
		world.probe(0, 0, agent);
		world.probe(0, 1, agent);
		
		assertFalse(agent.allFreeNeighbours(world, 1, 1));
	}
	
	/**
	 * Create a board which there is a cell that satisfies with AMN
	 * @result allMarkedNeighbours() should return true
	 */
	@Test
	void testAllMarkedNeighbours() {
		String[][] map = new String [][] {
			 {"2","d"},
			 {"d","2"}};
			 
		Game world = new Game(map);
		world.probe(0, 0, agent);
		world.probe(1, 1, agent);
		
		assertTrue(agent.allMarkedNeighbours(world, 0, 1));
		assertTrue(agent.allMarkedNeighbours(world, 1, 0));
	}
	
	/**
	 * Create a board which there is no cells that satisfies with AMN
	 * @result allMarkedNeighbours() should return false
	 */
	@Test
	void testAllMarkedNeighboursWithInvalidCase() {
		String[][] map = new String [][] {
			 {"2","d"},
			 {"d","2"}};
		
		Game world = new Game(map);
		world.probe(0, 0, agent);
		
		assertFalse(agent.allMarkedNeighbours(world, 0, 1));
		assertFalse(agent.allMarkedNeighbours(world, 1, 0));
	}
	
	/**
	 * Create a board which there is a cell that satisfies with AFN
	 * @result that cell should be probed by agent
	 */
	@Test
	void testApplyRulesAFN() {

		String[][] map = new String [][] {
			 {"2","d"},
			 {"d","2"}};
			 
		Game world = new Game(map);
		world.probe(0, 0, agent);
		world.probe(0, 1, agent);
		world.probe(1, 0, agent);
		Game spyWorld = Mockito.spy(world);
		
		assertTrue(agent.allFreeNeighbours(spyWorld, 1, 1));
		agent.applyRules(spyWorld);
		
		Mockito.verify(spyWorld, Mockito.times(1)).probe(1, 1, agent);
	}
	
	/**
	 * Create a board which there is a cell that satisfies with AMN
	 * @result that cell should be flagged by agent
	 */
	@Test
	void testApplyRulesAMN() {

		String[][] map = new String [][] {
			 {"2","d"},
			 {"d","2"}};
			 
		Game world = new Game(map);
		Game spyWorld = Mockito.spy(world);
		spyWorld.probe(0, 0, agent);
		spyWorld.probe(1, 1, agent);
		
		assertTrue(agent.allMarkedNeighbours(world, 0, 1));
		agent.applyRules(spyWorld);
		System.out.println(spyWorld);
		
		Mockito.verify(spyWorld, Mockito.times(1)).flag(0, 1);
	}
	
	/**
	 * @result Agent should try to apply AFN/AMN on all covered cells
	 */
	@Test
	void testApplyRulesOnlyForCoveredTiles() {
		String[][] map = new String [][] {
			 {"2","d"},
			 {"d","2"}};
			 
		Game world = new Game(map);
		SPSAgent spyAgent = Mockito.spy(agent);
		
		spyAgent.applyRules(world);
		for(int i=0; i<world.getHeight(); i++) {
			for(int j=0; j<world.getWidth(); j++) {
				Mockito.verify(spyAgent, Mockito.times(1)).allFreeNeighbours(world, i, j);
				Mockito.verify(spyAgent, Mockito.times(1)).allMarkedNeighbours(world, i, j);
			}
		}
	}
	
	/**
	 * @result Agent should not apply AFN/AMN on uncovered cells
	 */
	@Test
	void testApplyRulesSkipForUncoveredTiles() {
		String[][] map = new String [][] {
			 {"2","d"},
			 {"d","2"}};
			 
		Game world = new Game(map);
		SPSAgent spyAgent = Mockito.spy(agent);
		world.probe(0, 0, agent);
		world.probe(0, 1, agent);
		world.probe(1, 0, agent);
		world.probe(1, 1, agent);
		spyAgent.applyRules(world);
		for(int i=0; i<world.getHeight(); i++) {
			for(int j=0; j<world.getWidth(); j++) {
				Mockito.verify(spyAgent, Mockito.never()).allFreeNeighbours(world, i, j);
				Mockito.verify(spyAgent, Mockito.never()).allMarkedNeighbours(world, i, j);
			}
		}
	}
	
	/**
	 * @result Agent should not apply AFN/AMN on flagged cells
	 */
	@Test
	void testApplyRulesSkipForFlaggedTiles() {
		String[][] map = new String [][] {
			 {"2","d"},
			 {"d","2"}};
			 
		Game world = new Game(map);
		SPSAgent spyAgent = Mockito.spy(agent);
		world.flag(0, 0);
		world.flag(0, 1);
		world.flag(1, 0);
		world.flag(1, 1);
		spyAgent.applyRules(world);
		for(int i=0; i<world.getHeight(); i++) {
			for(int j=0; j<world.getWidth(); j++) {
				Mockito.verify(spyAgent, Mockito.never()).allFreeNeighbours(world, i, j);
				Mockito.verify(spyAgent, Mockito.never()).allMarkedNeighbours(world, i, j);
			}
		}
	}
	
	/**
	 * Create a board that there is no clues
	 * @result Agent should call guess()
	 */
	@Test
	void testProbeNextAllCovered() {
		String[][] map = new String [][] {
			 {"2","d"},
			 {"d","2"}};
			 
		Game world = new Game(map);
		SPSAgent spyAgent = Mockito.spy(agent);
		spyAgent.probeNext(world);
		
		Mockito.verify(spyAgent, Mockito.times(1)).guess(world);
	}
	
	/**
	 * Create a board that there are clues but clues is not enough to infer any further changes.
	 * @result Agent should call guess()
	 */
	@Test
	void testProbeNextStuckSituation() {
		String[][] map = new String [][] {
			 {"1","1"},
			 {"1","d"},
			 {"1","1"}};
			 
		Game world = new Game(map);
		SPSAgent spyAgent = Mockito.spy(agent);
		world.probe(0, 0, agent);
		world.probe(1, 0, agent);
		world.probe(2, 0, agent);
		
		spyAgent.probeNext(world);
		
		Mockito.verify(spyAgent, Mockito.times(1)).guess(world);
	}
	
}
