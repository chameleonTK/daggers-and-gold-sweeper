package cs5011.logic.agent;
import java.util.ArrayList;
import java.util.Iterator;

import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Literal;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.sat4j.core.VecInt;
import org.sat4j.pb.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;

import java.util.List;

import cs5011.logic.game.Game;

public class SATSAgent extends SPSAgent {
	public SATSAgent(int L) {
		super(L);
	}
	
	public String convertCoordinateToNumber(int i, int j){
		return "D10"+(i+"0"+j);
	}
	
	private List<Coordinate> getAroundCoveredTile(Game g, int x, int y) {
		List<Coordinate> choices = this.getAroundTile(g, x, y);
		List<Coordinate> neighbours = new ArrayList<Coordinate>();
		
		for(Coordinate c: choices) {
			if (g.isCovered(c.x, c.y) && !g.isFlaged(c.x, c.y)) {
				neighbours.add(c);
			}
		}
		
		return neighbours;
	}
	
	private List<Coordinate> getAroundDaggerTile(Game g, int x, int y) {
		List<Coordinate> choices = this.getAroundTile(g, x, y);
		List<Coordinate> neighbours = new ArrayList<Coordinate>();
		
		for(Coordinate c: choices) {
			if (g.isDagger(c.x, c.y) || g.isFlaged(c.x, c.y)) {
				neighbours.add(c);
			}
		}
		
		return neighbours;
	}
	
	public String formulateKnowledgeBase(Game g) throws ContradictionException{
		List<String> kb = new ArrayList<String>();
		boolean debug = false;
		for(int i=0; i< g.getHeight(); i++) {
			for(int j=0; j< g.getWidth(); j++) {
				if (g.isCovered(i, j)) {
					continue;
				}
				
				if (g.isDagger(i, j)) {
					if (debug) {
						System.out.println("("+this.convertCoordinateToNumber(i, j)+")");
					}
					kb.add("("+this.convertCoordinateToNumber(i, j)+")");
				} else {
					if (debug) {
						System.out.println("(~"+this.convertCoordinateToNumber(i, j)+")");
					}
					kb.add("(~"+this.convertCoordinateToNumber(i, j)+")");
					
					
					List<Coordinate> covered = this.getAroundCoveredTile(g, i, j);
					List<Coordinate> daggers = this.getAroundDaggerTile(g, i, j);
					List<Coordinate> neignbour = this.getAroundTile(g, i, j);
					
					int value = Coordinate.getValue(g.getInfo(i, j));
					
					if (value == daggers.size()) {
						// AFN rule
						List<String> newKb = new ArrayList<String>();
						for(Coordinate p: neignbour) {
							if (g.isDagger(p.x, p.y) || g.isFlaged(p.x, p.y)) {
								newKb.add(""+this.convertCoordinateToNumber(p.x, p.y));
							} else {
								newKb.add("~"+this.convertCoordinateToNumber(p.x, p.y));
							}
						}							
						if (debug) {
							System.out.println("("+String.join(" & ", newKb)+")");
						}
						kb.add("("+String.join(" & ", newKb)+")");
					} else if (value < daggers.size()) {
						// *** if the game configuration is consistency, it is impossible that value < daggers.size
						throw new ContradictionException("Game configuration is inconsistency.");
					} else if (covered.size() == value - daggers.size()) {
						// AMN rule
						List<String> newKb = new ArrayList<String>();
						for(Coordinate p: neignbour) {							
							if (g.isDagger(p.x, p.y) || g.isFlaged(p.x, p.y) || g.isCovered(p.x, p.y)) {
								newKb.add(""+this.convertCoordinateToNumber(p.x, p.y));
							} else {
								newKb.add("~"+this.convertCoordinateToNumber(p.x, p.y));
							}
						}	
						
						if (debug) {
							System.out.println("("+String.join(" & ", newKb)+")");
						}
						kb.add("("+String.join(" & ", newKb)+")");
					} else if (covered.size() > value - daggers.size()) {
						List<List<Coordinate>> comb = this.getCombination(covered, value - daggers.size());
						List<String> newKb = new ArrayList<String>();
						for(List<Coordinate> selectedTile: comb) {
							if (selectedTile.size() <= 0) {
								continue;
							}
							List<String> subKb = new ArrayList<String>();
							for(Coordinate p: covered) {							
								if (selectedTile.contains(p)) {
									subKb.add(""+this.convertCoordinateToNumber(p.x, p.y));
								} else {
									subKb.add("~"+this.convertCoordinateToNumber(p.x, p.y));
								}
							}	
							
							newKb.add("("+String.join(" & ", subKb)+")");
						}
						
						if (newKb.size() <= 0) {
							continue;
						}
						
						if (debug) {
							System.out.println("("+String.join(" | ", newKb)+")");
						}
						kb.add("("+String.join(" | ", newKb)+")");
					} else {
						// *** if the game configuration is consistency, it is impossible that #coveredTiles < #uncoveredDaggers
						throw new ContradictionException("Game configuration is inconsistency.");
					}
				}
				
			} 
		}
		
		return String.join(" & ", kb);
	}
	
	public List<List<Coordinate>> getCombination(List<Coordinate> neighbours, int value) {
		List<List<Coordinate>> comb = new ArrayList<List<Coordinate>>();
		if (value == 1) {
			for(int i=0; i< neighbours.size(); i++) {
				List<Coordinate> k = new ArrayList<Coordinate>();
				k.add(neighbours.get(i));
				comb.add(k);
			}
			return comb;
		}
		
		List<Coordinate> duplicated = new ArrayList<Coordinate>(neighbours);
		for(int i=0; i< neighbours.size(); i++) {
			duplicated.remove(0);
			if (duplicated.size() == 0) {
				continue;
			}
			List<List<Coordinate>> c = this.getCombination(duplicated, value-1);
			for(List<Coordinate> l: c) {
				l.add(neighbours.get(i));
				comb.add(l);
			}
		}
		return comb;
	}

	private Formula convertToCNF(String statement) throws ParserException{
		final FormulaFactory f = new FormulaFactory();
		final PropositionalParser p = new PropositionalParser(f);
		final Formula formula = p.parse(statement);
		final Formula cnf = formula.cnf();
		return cnf;
	}
	
	private ISolver generateSolver(Formula cnf) {
		final int MAXVAR = (int) (cnf.numberOfAtoms() + 10); //max number of variables
		final int NBCLAUSES = cnf.numberOfOperands() + 10; // number of clauses
		
		ISolver solver = SolverFactory.newDefault();
		solver.newVar(MAXVAR);
		solver.setExpectedNumberOfClauses(NBCLAUSES);
		solver.setTimeoutOnConflicts(1);
		solver.setTimeout(1);
		return solver;
	}
	
	private IProblem convertToDIMACS(Formula cnf, ISolver solver) throws ContradictionException {
		Iterator<Formula> iterator=cnf.iterator();
		while(iterator.hasNext()){
			List<Integer> dimacs = new ArrayList<Integer>();
			
			Formula clause=iterator.next();
			for(Literal literal:clause.literals()){
				String l = literal.toString();
				try {
					
					l = l.replace("@RESERVED_CNF_", "D11");
					if (l.startsWith("~")) {
						dimacs.add(Integer.parseInt(l.substring(2))*-1);
					} else {
						dimacs.add(Integer.parseInt(l.substring(1)));
					}
				} catch(NumberFormatException e) {
					System.err.println("Cannot convert to int: "+l);
				}
				
			}
			
			if (dimacs.size() == 0) {
				continue;
			}
			solver.addClause(new VecInt(dimacs.stream().mapToInt(Integer::intValue).toArray())); // adapt Array to IVecInt
		}
		
		return solver;
	}
	
	public boolean applyLogic(Game g) {
		try {
			String statement = this.formulateKnowledgeBase(g);
			for(int i=0; i< g.getHeight(); i++) {
				for(int j=0; j< g.getWidth(); j++) {
					if (!g.isCovered(i, j)) {
						continue;
					}
					
					if (g.isFlaged(i, j)) {
						continue;
					}
					
					List<Coordinate> covered = this.getAroundCoveredTile(g, i, j);
					if (covered.size() >= 8) {
						continue;
					}
					
					
					Formula cnf = this.convertToCNF(statement+" & ~"+this.convertCoordinateToNumber(i, j));
					IProblem problem = this.convertToDIMACS(cnf, this.generateSolver(cnf));
					
					if (!problem.isSatisfiable()) {
						g.flag(i, j);
						if (this.verbose) {
							System.out.println("infer "+(new Coordinate(i, j)));
							this.printStatus(g);
						}
						return true;
					}
					
					cnf = this.convertToCNF(statement+" & "+this.convertCoordinateToNumber(i, j));
					problem = this.convertToDIMACS(cnf, this.generateSolver(cnf));
					
					if (!problem.isSatisfiable()) {
						if (this.verbose) {
							System.out.println("infer "+(new Coordinate(i, j)));
						}
						g.probe(i, j, this);
					
						if (this.verbose) {
							this.printStatus(g);
						}
						return true;
					}
				}
			}
			return false;
		} catch(Exception e) {
//			e.printStackTrace();
			return false;
		}
	}
	
	public void probeNext(Game g) {
		// try SPS first;
		if (this.applyRules(g)) {
			return;
		}
		
		// try SATS first;
		if (this.applyLogic(g)) {
			return;
		}
		if (this.verbose) {
			System.out.println("cannot infer");
		}
		
		this.guess(g);
	}
}
