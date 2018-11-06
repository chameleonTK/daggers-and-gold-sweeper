package cs5011.logic.agent;

import java.util.ArrayList;
import java.util.List;

import cs5011.logic.game.Game;

/**
 * The Single Point Strategy Agent 
 * @author 180008901
 *
 */
public class SPSAgent extends Agent {

	public SPSAgent(int L) {
		super(L);
	}
	
	protected Coordinate[] listAroundOffset() {
		return (new Coordinate[] {
			new Coordinate(-1, 0), new Coordinate(1, 0), 
			new Coordinate(0, -1), new Coordinate(0, 1),
			new Coordinate(-1, -1), new Coordinate(-1, 1), 
			new Coordinate(1, -1), new Coordinate(1, 1)
		});
	}
	
	protected List<Coordinate> getAroundTile(Game g, int x, int y) {
		List<Coordinate> neighbours = new ArrayList<Coordinate>();
		
		Coordinate[] offsets = this.listAroundOffset();
		for(int i=0; i< offsets.length; i++) {
			Coordinate o = offsets[i];
			
			if (g.inBorder(x+o.x, y+o.y)) {
				neighbours.add(new Coordinate(x+o.x, y+o.y, g.getInfo(x+o.x, y+o.y)));
			}
		} 
		
		return neighbours; 
	}
	
	protected List<Coordinate> getAroundHintTile(Game g, int x, int y) {
		List<Coordinate> choices = this.getAroundTile(g, x, y);
		List<Coordinate> neighbours = new ArrayList<Coordinate>();
		
		for(Coordinate c: choices) {
			if (!g.isCovered(c.x, c.y) && !g.isFlaged(c.x, c.y) && !g.isDagger(c.x, c.y)) {
				neighbours.add(c);
			}
		}
		
		return neighbours; 
	}
	
	public boolean allFreeNeighbours(Game g, int x, int y) {
		List<Coordinate> neighbours = this.getAroundHintTile(g, x, y);
		for(Coordinate point: neighbours) {
			int marked = 0;
			Coordinate[] offsets = this.listAroundOffset();
			for(int i=0; i< offsets.length; i++) {
				if (!g.inBorder(point.x+offsets[i].x, point.y+offsets[i].y)) {
					continue;
				}
				
				if (g.isFlaged(point.x+offsets[i].x, point.y+offsets[i].y) || g.isDagger(point.x+offsets[i].x, point.y+offsets[i].y)) {
					marked++;
				}
			}
			
			if (marked == point.value) {
				return true;
			}
		}
		return false;
	}
	
	public boolean allMarkedNeighbours(Game g, int x, int y) {
		List<Coordinate> neighbours = this.getAroundHintTile(g, x, y);
		for(Coordinate point: neighbours) {
			int covered = 0, marked = 0;
			Coordinate[] offsets = this.listAroundOffset();
			
			for(int i=0; i< offsets.length; i++) {
				if (!g.inBorder(point.x+offsets[i].x, point.y+offsets[i].y)) {
					continue;
				}
				
				if (g.isFlaged(point.x+offsets[i].x, point.y+offsets[i].y) || g.isDagger(point.x+offsets[i].x, point.y+offsets[i].y)) {
					marked++;
				} else if (g.isCovered(point.x+offsets[i].x, point.y+offsets[i].y)) {
					covered++;
				}
			}

			if (covered == point.value - marked) {
				return true;
			}
		}
		return false;
	}
	
	public boolean applyRules(Game g) {
		for(int i=0; i< g.getHeight(); i++) {
			for(int j=0; j< g.getWidth(); j++) {
				if (g.isCovered(i, j) && !g.isFlaged(i, j)) {
					if (allFreeNeighbours(g, i, j)) {
						g.probe(i, j, this);
						this.printStatus(g);
						return true;
					} else if (allMarkedNeighbours(g, i, j)) {
						g.flag(i, j);
						this.printStatus(g);
						return true;
					}
				}
			} 
		}

		return false;
	}

	public void probeNext(Game g) {
		if (this.applyRules(g)) {
			return;
		}
		
		// if it is impossible to infer by AFN, AMN, it will start guessing
		this.guess(g);
	}

}
