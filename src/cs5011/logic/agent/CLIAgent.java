package cs5011.logic.agent;

import java.io.BufferedReader;
import java.io.IOException;

import cs5011.logic.game.Game;

public class CLIAgent extends Agent {

	String name;
	BufferedReader br;
	public CLIAgent(int L, String name, BufferedReader br) {
		super(L);
		this.name = name;
		this.br = br;
	}
	
	private int getInt(String message) throws IOException {
		while(true) {
			System.out.print(message);
			
			try{
	            int i = Integer.parseInt(br.readLine());
	            return i;
	        }catch(NumberFormatException nfe){
	        	System.err.print("Invalid input. Please try again!");
	        }
		}
	}
	
	public void probeNext(Game g) {
		try {
			int x = getInt("Get X: ");
			int y = getInt("Get Y: ");
			if (!g.inBorder(x, y)) {
				System.err.print("Out of board!! Skip this turn");
			} else if(!g.isCovered(x, y)) {
				System.err.print("Don't be a cheater by probe on uncovered tile!");
				this.takePenalty();
			} else {
				System.out.println("probe "+ (new Coordinate(x, y)));
				g.probe(x, y, this);
			}
			
		} catch (IOException e) {
			System.err.print("Invalid input. Skip this turn");
		}
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}
