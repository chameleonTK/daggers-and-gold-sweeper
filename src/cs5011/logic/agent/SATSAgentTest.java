package cs5011.logic.agent;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sat4j.specs.ContradictionException;

import cs5011.logic.game.Game;

class SATSAgentTest {

	private SATSAgent agent;
	@BeforeEach
	void setUp() throws Exception {
		agent = new SATSAgent(10);
	}

	@Test
	String c(int i, int j) {
		return agent.convertCoordinateToNumber(i, j);
	}
	/**
	 * @result correct String that correspond to Coordination(i, j); D10X0Y
	 */
	@Test
	void testConvertCoordinateToNumber() {
		assertEquals(agent.convertCoordinateToNumber(0, 0), "D10000");
		assertEquals(agent.convertCoordinateToNumber(5, 1), "D10501");
		assertEquals(agent.convertCoordinateToNumber(0, 10), "D100010");
		assertEquals(agent.convertCoordinateToNumber(10, 0), "D101000");
	}

//	/**
//	 * Create a board 
//	 * @result 
//	 */
//	@Test
//	void testProbeNext() {
//		fail("Not yet implemented");
//	}
//
	@Test
	void testFormulateKnowledgeBase() {
		String[][] map = new String [][] {
			 {"2","d"},
			 {"d","2"}};
			 
		Game world = new Game(map);
		world.probe(0, 0, agent);
		try {
			String D00 = c(0, 0);
			String D01 = c(0, 1);
			String D10 = c(1, 0);
			String D11 = c(1, 1);
			
			String s = agent.formulateKnowledgeBase(world);
			// (~D10000) & ((D10100 & D10001 & ~D10101) | (D10100 & ~D10001 & D10101) | (~D10100 & D10001 & D10101))
			
			String D00xD01xND10 = String.format("(%s & %s & ~%s)", D10, D01, D11);
			String D00xND01xD10 = String.format("(%s & ~%s & %s)", D10, D01, D11);
			String ND00xD01xD10 = String.format("(~%s & %s & %s)", D10, D01, D11);
			assertTrue(s.startsWith(String.format("(~%s)", D00)));
			assertTrue(s.contains(D00xD01xND10));
			assertTrue(s.contains(D00xND01xD10));
			assertTrue(s.contains(ND00xD01xD10));
			
		} catch (ContradictionException e) {
			fail("Formulate KB failed");
		}
	}

//	@Test
//	void testApplyLogic() {
//		fail("Not yet implemented");
//	}

}
