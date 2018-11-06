package cs5011.logic.game;
import cs5011.logic.agent.Agent;

public class Game {
	private int width, height, boardSize, noOfDaggers, noOfGoldMines;
	private String[][] map;
	private boolean[][] covered;
	private boolean[][] flaged;
	private int noOfUncovered;
	
	public boolean verbose = false;
	public Game(String[][] map) {
		this.map = map;
		this.height = this.map.length;
		this.width = this.map[0].length;
		this.boardSize = this.width * this.height;
		
		this.noOfDaggers = 0;
		this.noOfGoldMines = 0;
		this.flaged = new boolean[this.height][this.width];
		this.covered = new boolean[this.height][this.width];
		this.noOfUncovered = 0;
		
		//Initial value;
		for(int i=0; i< this.height; i++) {
			for(int j=0; j< this.width; j++) {
				this.covered[i][j] = true;
				this.flaged[i][j] = false;
				
				if (this.map[i][j].endsWith("g")) {
					this.noOfGoldMines++;
				} else if (this.map[i][j].equals("d")) {
					this.noOfDaggers++;
				}
			} 
		}
		
		if (this.verbose) {
			this.printMapInfo();
		}
	}
	
	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}
	
	public int getNoOfDaggers() {
		return this.noOfDaggers;
	}
	
	public int getNoOfGoldMines() {
		return this.noOfGoldMines;
	}
	
	public boolean inBorder(int i, int j) {
		if (i<0 || i >= this.height) {
			return false;
		}
		
		if (j<0 || j >= this.width) {
			return false;
		}
		
		
		return true;
	}
	
	public void probe(int i, int j, Agent a) {
		//Ignore if it is out of border or it has been probed
		if (!this.inBorder(i, j) || !this.covered[i][j]) {
			return;
		}
		
		this.covered[i][j] = false;
		if (this.verbose) {
			System.out.println("reveal "+i+", "+j);
		}
		
		if (!this.isDagger(i, j)) {
			this.noOfUncovered ++;
		}
		
		//Decrease/Increase agent's life
		if (this.isDagger(i, j)) {
			if (this.verbose) {
				System.out.println("dagger-in "+i+", "+j);
			}
			a.takePenalty();
		}
		
		if (this.isGoldMine(i, j)) {
			if (this.verbose) {
				System.out.println("goldmine-in "+i+", "+j);
			}
			a.getReward();
		}
		
		
		//If it is empty, expand further until it reveals other hints.
		if (this.isEmpty(i, j)) {
			this.probe(i+1, j, a);
			this.probe(i-1, j, a);
			this.probe(i, j+1, a);
			this.probe(i, j-1, a);
			
			this.probe(i+1, j+1, a);
			this.probe(i-1, j+1, a);
			this.probe(i+1, j-1, a);
			this.probe(i-1, j-1, a);
		}
	}
	
	public void flag(int i, int j) {
		if (!this.inBorder(i, j)) {
			return;
		}
		
		this.flaged[i][j] = true;
		if (this.verbose) {
			System.out.println("stone-in "+i+", "+j);
		}
	}
	
	public boolean isWin() {
		if (this.noOfUncovered >= this.boardSize-this.noOfDaggers) {
			return true;
		}
		return false; 
	}
	
	//Agent can check dagger when it is uncovered
	public boolean isDagger(int i, int j) {
		if (!this.inBorder(i, j)) {
			return false;
		}
		
		if (this.isCovered(i, j)) {
			return false;
		}
		
		return this.map[i][j].equals("d"); 
	}
		
	//Agent can check goldmine when it is uncovered
	public boolean isGoldMine(int i, int j) {
		if (!this.inBorder(i, j)) {
			return false;
		}
		
		if (this.isCovered(i, j)) {
			return false;
		}
		
		return this.map[i][j].endsWith("g"); 
	}
	
	public boolean isCovered(int i, int j) {
		if (!this.inBorder(i, j)) {
			return false;
		}
		
		return this.covered[i][j]; 
	}
	
	public boolean isFlaged(int i, int j) {
		if (!this.inBorder(i, j)) {
			return false;
		}
		
		return this.flaged[i][j]; 
	}
	
	public boolean isEmpty(int i, int j) {
		if (!this.inBorder(i, j)) {
			return false;
		}
		return (this.map[i][j].equals("0") || this.map[i][j].equals("g")); 
	}
	
	public String getInfo(int i, int j) {
		if (!this.inBorder(i, j)) {
			return null;
		}
		
		//The outsider should not be able to get until it is uncovered;
		if (this.covered[i][j]) {
			return null;
		}
		
		return this.map[i][j]; 
	}
	
	public void printMapInfo() {
		System.out.println("=== INFO ===");
		System.out.println("NxN: "+this.width+"x"+this.height);
		System.out.println("#Daggers: "+this.noOfDaggers);
		System.out.println("#GoldMines: "+this.noOfGoldMines);
	}
	
	public String printMap() {
		String s = "";
		for(int i=0; i< this.height; i++) {
			for(int j=0; j< this.width; j++) {
				if (!this.covered[i][j]) {
					s += this.map[i][j]+" ";
				} else if (this.flaged[i][j]) {
					s += "X ";
				} else {
					s += "? ";
				}
				
			} 
			s += "\n";
		}
		
		return s;
	}
	
	@Override
	public String toString() {
		return this.printMap();
	}
	
}
