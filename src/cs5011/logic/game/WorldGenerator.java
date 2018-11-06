package cs5011.logic.game;

import java.util.Random;

public class WorldGenerator {
	
	public static String[][] generate(String s) {
		switch(s) {
			case "EASY1":
			case "EASY2":
			case "EASY3":
			case "EASY4":
			case "EASY5":
			case "EASY6":
			case "EASY7":
			case "EASY8":
			case "EASY9":
			case "EASY10":
			case "MEDIUM1":
			case "MEDIUM2":
			case "MEDIUM3":
			case "MEDIUM4":
			case "MEDIUM5":
			case "MEDIUM6":
			case "MEDIUM7":
			case "MEDIUM8":
			case "MEDIUM9":
			case "MEDIUM10":
			case "HARD1":
			case "HARD2":
			case "HARD3":
			case "HARD4":
			case "HARD5":
			case "HARD6":
			case "HARD7":
			case "HARD8":
			case "HARD9":
			case "HARD10":
			    return World.valueOf(s).map;
			case "TEST":
			    return (new String[][] {
			    	 {"1","d"},
					 {"2","2"},
					 {"d","1"},
			    });
		default: 
			return generate(5, 4, 1);
	}
	}
	
	public static String[][] generate(int n, int d, int g) {
		String[][] map = new String[n][n];
		Random ran = new Random();
		while(d>0) {
			int pos = ran.nextInt(n*n);
			int x = (int)(pos/n);
			int y = (int)(pos%n);
			
			if (map[x][y] == null) {
				map[x][y] = "d";
				d--;
			}
		}
		
		while(g>0) {
			int pos = ran.nextInt(n*n);
			int x = (int)(pos/n);
			int y = (int)(pos%n);
			if (map[x][y] == null) {
				map[x][y] = "g";
				g--;
			}
		}
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				if (map[i][j]!=null && map[i][j].equals("d")) {
					continue;
				}
				
				int nDaggers = 0;
				if (isDagger(n, map, i-1, j-1)) { nDaggers++;}
				if (isDagger(n, map, i-1, j)) { nDaggers++;}
				if (isDagger(n, map, i-1, j+1)) { nDaggers++;}
				if (isDagger(n, map, i, j-1)) { nDaggers++;}
				if (isDagger(n, map, i, j+1)) { nDaggers++;}
				if (isDagger(n, map, i+1, j-1)) { nDaggers++;}
				if (isDagger(n, map, i+1, j)) { nDaggers++;}
				if (isDagger(n, map, i+1, j+1)) { nDaggers++;}
				
				if (map[i][j] == null) {
					map[i][j] = "" + nDaggers;
				} else {
					if (nDaggers>0) {
						map[i][j] = nDaggers+map[i][j];
					}
					
				}
			}
		}
		
		return map;
	}
	
	private static boolean isDagger(int n, String[][] map, int i, int j) {
		return checkBorder(n, i, j) && (map[i][j]!=null) && (map[i][j].equals("d"));
	}
	
	private static boolean checkBorder(int n, int i, int j) {
		return (i>=0 && i<n) && (j>=0 && j < n);
	}

}
