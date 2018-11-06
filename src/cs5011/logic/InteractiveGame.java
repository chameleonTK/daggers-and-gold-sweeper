package cs5011.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cs5011.logic.agent.*;
import cs5011.logic.game.Game;
import cs5011.logic.game.WorldGenerator;

public class InteractiveGame {

	public static String selectMode(BufferedReader br) throws IOException {
		String mode = null;
		while(mode==null || (!mode.equals("cvc") && !mode.equals("pvc") && !mode.equals("pvp"))) {
			String prompt = "Play mode options:\n";
			prompt += "  > pvp for Player vs Player mode\n";
			prompt += "  > pvc for Player vs Computer mode\n";
			prompt += "  > cvc for Computer vs Computer mode\n";
			prompt += "Select your option: ";
			System.out.print(prompt);
			
			
	        mode = br.readLine();
	        
	        if (!mode.equals("cvc") && !mode.equals("pvc") && !mode.equals("pvp")) {
	        	System.err.print("Invalid input. Please try again!");
	        }
		}
		
		return mode;
	}
	
	public static Agent selectPlayerStrategy(BufferedReader br, int i) throws IOException {
		String agent = null;
		while(agent==null || (!agent.equals("rps") && !agent.equals("sps") && !agent.equals("ats"))) {
			String prompt = "Agent["+i+"] strategy:\n";
			prompt += "  > rps for Random Probing Strategy\n";
			prompt += "  > sps for Single Point Strategy\n";
			prompt += "  > ats for SAtisfiability Test Strategy\n";
			prompt += "Select your option: ";
			System.out.print(prompt);
			
			
			agent = br.readLine();
	        
	        if (!agent.equals("rps") && !agent.equals("sps") && !agent.equals("ats")) {
	        	System.err.print("Invalid input. Please try again!");
	        }
		}
		
		
		switch(agent) {
			case "rps":
				return new RPSAgent(1);
			case "sps":
				return new SPSAgent(1);
			case "ats":
				return new SATSAgent(1);
			default:
				return new RPSAgent(1);
		}
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String mode = selectMode(br);
		Agent player1 = null, player2 = null;
		if (mode.equals("cvc")) {
			player1 = selectPlayerStrategy(br, 1);
			player2 = selectPlayerStrategy(br, 2);
		} else if (mode.equals("pvc")) {
			player1 = new CLIAgent(1, "Player1", br);
			player2 = selectPlayerStrategy(br, 2);
			player1.verbose = false;
		} else {
			player1 = new CLIAgent(1, "Player1", br);
			player2 = new CLIAgent(1, "Player2", br);
			player1.verbose = false;
			player2.verbose = false;
		}
		
		Game game = new Game(WorldGenerator.generate(5, 4, 1));
		game.verbose = false;
		
		System.out.println("=== START GAME ===");
		System.out.println(game);
		
		int turn = 0;
		while (!game.isWin() && !player1.isLose() && !player2.isLose()) {
			System.out.println("== TURN "+ (turn+1) +" ==");
			
			Agent player = null, opponent = null;
			if (turn%2==0) {
				player = player1;
				opponent = player2;
			} else {
				player = player2;
				opponent = player1;
			}
			
			System.out.println(player+" play\n");
	
			player.probeNext(game);
			System.out.println(game);
			System.out.println("Player1 Life: " + player1.life);
			System.out.println("Player2 Life: " + player2.life);
			System.out.println();
			
			if (game.isWin()) {
				System.out.println(player+" is a winner\n");
			} else if (player.isLose()) {
				System.out.println(opponent+" is a winner\n");
			}
			turn++;
		}
		
		br.close();
		System.out.println("=== END ===");
        
	}

}
