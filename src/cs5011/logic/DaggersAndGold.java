package cs5011.logic;

import cs5011.logic.game.*;
import cs5011.logic.agent.*;

public class DaggersAndGold {

	public static void main(String[] args){
		Game game;
		Agent agent;
		if (args.length <= 0) {
			game = new Game(WorldGenerator.generate(5, 4, 1));
			agent = new RPSAgent(1);
		} else if (args.length == 1) {
			game = new Game(WorldGenerator.generate(args[0].toUpperCase()));
			agent = new RPSAgent(1);
		} else {
			game = new Game(WorldGenerator.generate(args[0].toUpperCase()));
			switch(args[1].toLowerCase()) {
				case "rps":
					agent = new RPSAgent(1); break;
				case "sps":
					agent = new SPSAgent(1); break;
				case "ats":
					agent = new SATSAgent(1); break;
				default:
					agent = new RPSAgent(1); break;
			}
			
		}
		
		agent.run(game);
	}

}
