package cs5011.logic.agent;

//import java.util.Collections;
//import java.util.List;

import cs5011.logic.game.Game;

/**
 * The Random Probing Strategy Agent 
 * @author 180008901
 *
 */
public class RPSAgent extends Agent {

//	private List<Coordinate> choices;
//	private int choiceIndex;
	public RPSAgent(int L) {
		super(L);
	}
	
	public void probeNext(Game g) {
		this.guess(g);
	}

	/**
	 * Instead of random x, y; the performance can be improved  by predefined order of tile that are going to be probed
	 * @param g
	 */
//	protected void guess(Game g) {
//		if (this.choices == null) {
//			this.choices = this.getPossibleTiles(g);
//			Collections.shuffle(choices);
//			this.choiceIndex = 0;
//		}
//		
//		Coordinate c = this.choices.get(this.choiceIndex);
//		int randX = c.x;
//		int randY = c.y;
//		
//		while(!g.isCovered(randX, randY) || g.isFlaged(randX, randY)) {
//			this.choiceIndex++;
//			if (this.choiceIndex >= this.choices.size()) {
//				break;
//			}
//			c = this.choices.get(this.choiceIndex);
//			randX = c.x;
//			randY = c.y;
//		}
//		
//		if (this.verbose) {
//			System.out.println("guess "+(new Coordinate(randX, randY)));
//		}
//		
//		g.probe(randX, randY, this);
//		this.guess++;
//		if (this.verbose) {
//			this.printStatus(g);
//		}
//	}

}