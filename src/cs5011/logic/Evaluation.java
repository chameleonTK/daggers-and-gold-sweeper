package cs5011.logic;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import cs5011.logic.agent.*;
import cs5011.logic.game.Game;
import cs5011.logic.game.WorldGenerator;

public class Evaluation {

	public static void printArr(Agent[] agents, String s, int[] values) {
		System.err.print(s);
		for (int j=0; j< agents.length; j++) {
			System.err.print(values[j]+" ");
		}
		System.err.println();
	}
	
	public static void evaluateWinningRatio(Agent[] agents) {
		int N = 50;
		int[] win = new int[agents.length];
		int[] lose = new int[agents.length];
		for (int i=0; i<N; i++) {
			System.err.println("EASY"+i);
			//EASY
			String[][] map = WorldGenerator.generate(8, 5, 1);
			for (int j=0; j< agents.length; j++) {
				if (agents[j].run(new Game(map))) {
					win[j]++;
				} else {
					lose[j]++;
				}
			}
			
			
			printArr(agents, "Win: ", win);
			printArr(agents, "Lose: ", lose);
		}
		
		
		win = new int[3];
		lose = new int[3];
		for (int i=0; i<N; i++) {
			//MEDIUM
			String[][] map = WorldGenerator.generate(8, 10, 1);
			System.err.println("MEDIUM"+i);
			for (int j=0; j< agents.length; j++) {
				if (agents[j].run(new Game(map))) {
					win[j]++;
				} else {
					lose[j]++;
				}
			}
			
			
			printArr(agents, "Win: ", win);
			printArr(agents, "Lose: ", lose);
		}
		
		
		win = new int[3];
		lose = new int[3];
		for (int i=0; i<N; i++) {
			//HARD
			System.err.println("HARD"+i);
			String[][] map = WorldGenerator.generate(8, 30, 1);
			for (int j=0; j< agents.length; j++) {
				if (agents[j].run(new Game(map))) {
					win[j]++;
				} else {
					lose[j]++;
				}
			}
			
			
			printArr(agents, "Win: ", win);
			printArr(agents, "Lose: ", lose);
		}
	}
	
	
	public static void evaluateTime(Agent agent) {
		
		
		for (int size=5; size < 50; size += 2) {
			long startTime = System.currentTimeMillis();
			for (int i=0; i<10; i++) {
				String[][] map = WorldGenerator.generate(size, (int)(size*size*0.125), 1);
				agent.run(new Game(map));
			}
			
			long endTime = System.currentTimeMillis();
			long timeElapsed = endTime - startTime;
			System.err.println("size: "+size+", time: "+timeElapsed);
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		Agent rps = new RPSAgent(1);
		Agent sps = new SPSAgent(1);
		Agent ats = new SATSAgent(1);
		
		rps.verbose = false;
		sps.verbose = false;
		ats.verbose = false;
		
		FileOutputStream faccess = null;
		faccess = new FileOutputStream("access_log.txt");
		System.setOut(new PrintStream(faccess));
		
		
//		evaluateWinningRatio(new Agent[] {rps, sps});
		evaluateTime(ats);
		
		faccess.close();
		
	}

}
