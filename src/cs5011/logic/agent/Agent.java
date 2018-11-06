package cs5011.logic.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cs5011.logic.game.Game;

public class Agent {
	public int life;
	public int guess = 0;
	public boolean verbose = true;
	
	public Agent(int L) {
		this.life = L;
	}
	
	public boolean run(Game g) {
		g.probe(0, 0, this);
		System.out.println("=== START ===");
		this.guess = 0;
		this.printStatus(g);
		
		
		while (!g.isWin() && !this.isLose()) {
			this.probeNext(g);
		}
		
		System.out.println("#Guess: "+this.guess);
		System.out.println("=== END ===");
		
		if (g.isWin()) {
			return true;
		}
		
		return false;
	}
	
	public void probeNext(Game g) {
		this.guess(g);
	}

	protected void guess(Game g) {
		Random ran = new Random();
		int pos = ran.nextInt(g.getHeight() * g.getWidth());
		int randX = (int)(pos/g.getWidth());
		int randY = (int)(pos%g.getWidth());
		
		while(!g.isCovered(randX, randY) || g.isFlaged(randX, randY)) {
			pos = ran.nextInt(g.getHeight() * g.getWidth());
			randX = (int)(pos/g.getWidth());
			randY = (int)(pos%g.getWidth());
		}
		
		System.out.println("guess "+(new Coordinate(randX, randY)));
		g.probe(randX, randY, this);
		this.guess++;
		this.printStatus(g);
	}

	public void printStatus(Game g) {
		if (!verbose) {
			return;
		}
		
		System.out.println("");
		System.out.println("Life: "+this.life);
		System.out.println(g);
		
	
		if (g.isWin()) {
			System.out.println("game won");
		} else if (this.isLose()) {
			System.out.println("game lost");
		}
		
		System.out.println("============");
	}
	
	protected List<Coordinate> getPossibleTiles(Game g) {
		List<Coordinate> possiblePoints = new ArrayList<Coordinate>();
		
		for(int i=0; i< g.getHeight(); i++) {
			for(int j=0; j< g.getWidth(); j++) {
				if (g.isCovered(i, j) && !g.isFlaged(i, j)) {
					possiblePoints.add(new Coordinate(i, j));
				}
			} 
		}
		
		return possiblePoints;
	}
	
	public boolean isLose() {
		if (this.life <= 0) {
			return true;
		}
		
		return false;
	}
	
	public void takePenalty() {
		this.life--;
	}
	
	public void getReward() {
		this.life++;
	}
	
	@Override
	public String toString() {
		return "com";
	}
}
