package player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import world.World;
import world.World.ShipLocation;

public class MonteCarloGuessPlayer implements Player {

	private LinkedList<Guess> passGuesses = new LinkedList<Guess>();
	private LinkedList<Guess> possibleGuesses;
	private LinkedList<Guess> huntList = new LinkedList<Guess>() ;
	private LinkedList<Guess> FiveGuesses;
	private LinkedList<Guess> otherGuesses;
	private LinkedList<GuessRisk> risklevel;

	// count which round make new monte carlo
	private int count = 0;
	// check if new hunt is from a target point
	private boolean fromTragetGuess = false;
	// save that targetGuess
	private Guess targetGuess;
	// the biggest ship a player have
	private int Maxshipsize = 3;

	private ArrayList<ShipLocation> playerShipList = new ArrayList<>();
	//private LinkedList<Guess> targetModeList = new LinkedList<Guess>();

	@Override
	public void initialisePlayer(World world) {
		playerShipList = world.shipLocations;
		possibleGuesses = getGuesses();
		risklevel = setRisk();
		System.out.println("riskList" + risklevel.size());
		/*
		for (int i = 0; i <= 30; i++) {
			System.out.println("riskList a" + risklevel.get(i));
		}
		*/
		// System.out.println(possibleGuesses.size());
		//createGuessesLevel();
		// System.out.println(FiveGuesses.size());

	} // end of initialisePlayer()

	@Override
	public Answer getAnswer(Guess guess) {
		Answer a = new Answer();
		// Searches individual ship coordinates
		for (int i = 0; i < playerShipList.size(); ++i) {
			for (int j = 0; j < playerShipList.get(i).coordinates.size(); ++j) {
				int col = playerShipList.get(i).coordinates.get(j).column;
				int row = playerShipList.get(i).coordinates.get(j).row;

				// Remove this point from the ship
				if (col == guess.column && row == guess.row) {
					playerShipList.get(i).coordinates.remove(j);
					/**
					 * if there are no more coordinates, then the ship has been
					 * sunk and we remove it from the playerShipList
					 */
					if (playerShipList.get(i).coordinates.size() == 0) {
						a.shipSunk = playerShipList.get(i).ship;
						playerShipList.remove(i);
						for (i = 0; i < playerShipList.size(); i++) {
							if (playerShipList.get(i).ship.len() >= Maxshipsize) {
								Maxshipsize = playerShipList.size();
							}
						}
						System.out.println("ship suck" + a.shipSunk.name());
						a.isHit = true;
						return a;
					}
					a.isHit = true;
				}
			}
		}
		return a;
	} // end of getAnswer()

	@Override
	public Guess makeGuess() {
		//System.out.println("risklevel = "+ risklevel.size());
		// Search through target mode unless it is empty
		    if (risklevel.size() != 0) {
			GuessRisk R = new GuessRisk();
			int risk = risklevel.get(0).risk;
			for (int i = 0;i<risklevel.size();i++)
			{
				if(risk < risklevel.get(i).risk){
					risk = risklevel.get(i).risk;
					R = risklevel.get(i);
				}
				
			}
			for (int j = 0;j<risklevel.size();j++)
			{
			 if(R.column==risklevel.get(j).column&&R.row==risklevel.get(j).row){
				 risklevel.get(j).risk=0;
			 }
			}
			Guess g = new Guess();
			g.column = R.column;
			g.row = R.row;
			return g;
		}
		return null; 

	} // end of makeGuess()

	@Override
	public void update(Guess guess, Answer answer) {
		// if that guess find first hit point of a ship

		if (answer.isHit != true) {
			createReduceModeList(guess.column,guess.row);
		}
		
		
		
		if (answer.isHit == true) {
			createIncreaseModeList(guess.column, guess.row);
		}
		// if new target point is from a target has been searched
		// chenck same line with out make a four point check;

		
		// To be implemented.
	} // end of update()

	@Override
	public boolean noRemainingShips() {
		if (playerShipList.size() == 0) {
			return true;
		} else {
			return false;
		}

	} // end of noRemainingShips()

	// creates a list of all possible guesses and then randomises them
	public LinkedList<GuessRisk> setRisk() {
		LinkedList<GuessRisk> guessList = new LinkedList<>();
		int shipsize = 2;
		int max = 9;
		for (int i = 0; i <= max; ++i) {
			for (int j = 0; j <= max; ++j) {
				GuessRisk guess = new GuessRisk();
				guess.row = i;
				guess.column = j;
				guessList.add(guess);
			}
		}
		for (int round = 0; round < playerShipList.size(); round++) {
			shipsize = playerShipList.get(round).coordinates.size();

			for (int i = 0; i < guessList.size(); i++) {
				int count = 0;
				if (guessList.get(i).row - (shipsize - 1) >= 0
						&& guessList.get(i).row + (shipsize - 1) <= 9) {
					count += shipsize;
				} else if (guessList.get(i).row + (shipsize - 1) <= 9) {
					count += guessList.get(i).row + 1;
				} else if (guessList.get(i).row - (shipsize - 1) >= 0) {
					count += 10 - guessList.get(i).row;
				}

				if (guessList.get(i).column - (shipsize - 1) >= 0
						&& guessList.get(i).column + (shipsize - 1) <= 9) {
					count += shipsize;
				} else if (guessList.get(i).column + (shipsize - 1) <= 9) {
					count += guessList.get(i).column + 1;
				} else if (guessList.get(i).column - (shipsize - 1) >= 0) {
					count += 10 - guessList.get(i).column;
				}
				guessList.get(i).risk += count;
			}

		}
		return guessList;
	}

	public LinkedList<Guess> getGuesses() {
		LinkedList<Guess> guessList = new LinkedList<Guess>();

		int max = 9;
		for (int i = 0; i <= max; ++i) {
			for (int j = 0; j <= max; ++j) {
				Guess guess = new Guess();
				guess.row = i;
				guess.column = j;
				guessList.add(guess);
			}
		}
		return guessList;
	}
	private LinkedList<Guess> createReduceModeList(int col, int row) {
		LinkedList<Guess> list = new LinkedList<>();
		int shipsize = 2 ;
		for (int round = 0; round < playerShipList.size(); round++) {
			shipsize = playerShipList.get(round).coordinates.size();{
				for (int j=1;j<shipsize-1;j++){
		if (col-j >= 0) {
			Guess g = new Guess();
			g.column = col-j;
			g.row = row;
			list.add(g);
			reduceGuessRisk(g);			
		}
		if (col  + j <= 9) {
			Guess g = new Guess();
			g.column = col + j;
			g.row = row;
			list.add(g);
			reduceGuessRisk(g);
		}
		if (row -j >= 0) {
			Guess g = new Guess();
			g.row = row +j ;
			g.column = col;
			list.add(g);
			reduceGuessRisk(g);
		}
		if (row +j <= 9) {
			Guess g = new Guess();
			g.row = row  +j;
			g.column = col;
			list.add(g);
			reduceGuessRisk(g);
		}
		}
			}
		}
		return list;

	}
	private LinkedList<Guess> createIncreaseModeList(int col, int row) {
		LinkedList<Guess> list = new LinkedList<>();
		int shipsize = 2 ;
		for (int round = 0; round < playerShipList.size(); round++) {
			shipsize = playerShipList.get(round).coordinates.size();{
				for (int j=1;j<shipsize-1;j++){
		if (col-j >= 0) {
			Guess g = new Guess();
			g.column = col-j;
			g.row = row;
			list.add(g);
			increaseGuessRisk(g);			
		}
		if (col  + j <= 9) {
			Guess g = new Guess();
			g.column = col + j;
			g.row = row;
			list.add(g);
			increaseGuessRisk(g);
		}
		if (row -j >= 0) {
			Guess g = new Guess();
			g.row = row +j ;
			g.column = col;
			list.add(g);
			increaseGuessRisk(g);
		}
		if (row +j <= 9) {
			Guess g = new Guess();
			g.row = row  +j;
			g.column = col;
			list.add(g);
			increaseGuessRisk(g);
		}
		}
			}
		}
		return list;

	}
	

	/**
	 * Creates new guesses to add to the targetModeList and removes the guesses
	 * from the possibleGuessesList
	 */
	private LinkedList<Guess> createTargetModeList(int col, int row) {
		LinkedList<Guess> list = new LinkedList<>();

		if (col > 0) {
			Guess g = new Guess();
			g.column = col - 1;
			g.row = row;
			list.add(g);
			possibleGuesses = removeGuess(g.column, g.row);
			
		}
		if (col < 9) {
			Guess g = new Guess();
			g.column = col + 1;
			g.row = row;
			list.add(g);
			possibleGuesses = removeGuess(g.column, g.row);
		}
		if (row > 0) {
			Guess g = new Guess();
			g.row = row - 1;
			g.column = col;
			list.add(g);
			possibleGuesses = removeGuess(g.column, g.row);
		}
		if (row < 9) {
			Guess g = new Guess();
			g.row = row + 1;
			g.column = col;
			list.add(g);
			possibleGuesses = removeGuess(g.column, g.row);
		}
		Collections.shuffle(list);

		return list;
	}

	private LinkedList<Guess> removeGuess(int col, int row) {
		LinkedList<Guess> guesses = possibleGuesses;
		LinkedList<Guess> temp = new LinkedList<Guess>();
		Guess g = new Guess();
		g.row = row;
		g.column = col;
		for (int i = 0; i < guesses.size(); ++i) {
			if (guesses.get(i).column == col && guesses.get(i).row == row) {
				temp.add(guesses.get(i));
				guesses.remove(i);
			}
		}
		passGuesses.addAll(temp);

		return guesses;
	}

	private boolean guessExists(Guess g) {
		for (int i = 0; i < possibleGuesses.size(); ++i) {

			if (possibleGuesses.get(i).column == g.column
					&& possibleGuesses.get(i).row == g.row) {
				// System.out.println("here"+g);

				return true;
			}
		}
		return false;
	}

	private boolean guessExistsFromPass(Guess g) {
		for (int i = 0; i < passGuesses.size(); ++i) {

			if (possibleGuesses.get(i).column == g.column
					&& possibleGuesses.get(i).row == g.row) {
				// System.out.println("here"+g);

				return true;
			}
		}
		return false;
	}
	
	
	public LinkedList<Guess> checkMoreOnLine(Guess guess) {
		LinkedList<Guess> highrisk = new LinkedList<>();
		int shipsizecount=0;
		int xRay = (targetGuess.row - guess.row);
		int yRay = (targetGuess.column - guess.column);
		//System.out.println(xRay+" "+yRay);
		int x = guess.row - xRay;
		int y = guess.column - yRay;
		Guess temp = new Guess();
		temp.row = x;
		temp.column = y;
     //   System.out.println("next target point is "+temp);
		do {
			//System.out.println("try="+highrisk.size());
			shipsizecount++;
			if (guessExists(temp)) {		
				highrisk.add(temp);
				//System.out.println("new highrisk first add="+highrisk.size());

				removeGuess(y, x);
				x =x- xRay;
				y =y- yRay;
				temp = new Guess();
				temp.row = x;
				temp.column = y;
			} 
			//if check point not on list, it has been used before,break this check
			  else{
				//	System.out.println("no pass="+highrisk.size());
				break;}
		} while (x == 0 || x == 9 || y == 0 || y == 9||shipsizecount>=Maxshipsize);

		//System.out.println("new highrisk="+highrisk.size());
		// add highrisk to targetlist
		Collections.shuffle(highrisk);
		return highrisk;
	}
	
	
	
	public void setGuessRiskZero(Guess g){
		for (int i = 0; i< risklevel.size();i++){
		if(risklevel.get(i).column==g.column&&risklevel.get(i).row == g.row){
			risklevel.get(i).risk=-5000;
		}
		}
	}
	public void reduceGuessRisk(Guess g){
		for (int i = 0; i< risklevel.size();i++){
		if(risklevel.get(i).column==g.column&&risklevel.get(i).row == g.row){
			risklevel.get(i).risk-=5;
			System.out.println("guesses/fires at row " + risklevel.get(i).row + " column " + risklevel.get(i).column + '.'+ risklevel.get(i).risk);
		}
		}
	}
	public void increaseGuessRisk(Guess g){
		for (int i = 0; i< risklevel.size();i++){
		if(risklevel.get(i).column==g.column&&risklevel.get(i).row == g.row){
			risklevel.get(i).risk+=5;
			System.out.println("guesses/fires at row " + risklevel.get(i).row + " column " + risklevel.get(i).column + '.'+ risklevel.get(i).risk);
		}
		}
	}
	

}

// not use now,backup function

/*
 * public void getLevelGuesses() { LinkedList<Guess> fiveList = new
 * LinkedList<Guess>(); LinkedList<Guess> fourList = new LinkedList<Guess>();
 * LinkedList<Guess> threeList = new LinkedList<Guess>(); LinkedList<Guess>
 * twoList = new LinkedList<Guess>(); LinkedList<Guess> otherList = new
 * LinkedList<Guess>();
 * 
 * int max = possibleGuesses.size(); for (int i = 0; i < max; i++) {
 * System.out.println(possibleGuesses.size());
 * 
 * Guess temp = possibleGuesses.pop(); if (temp.row >= 4 && temp.row <= 5 &&
 * temp.column >= 4 && temp.column <= 5) { System.out.println(temp);
 * fiveList.add(temp); } if (temp.row >= 3 && temp.row <= 6 && temp.column >= 3
 * && temp.column <= 6) { System.out.println(temp); fourList.add(temp); } if
 * (temp.row >= 2 && temp.row <= 7 && temp.column >= 2 && temp.column <= 7) {
 * System.out.println(temp); threeList.add(temp); } if (temp.row >= 1 &&
 * temp.row <= 8 && temp.column >= 1 && temp.column <= 8) {
 * System.out.println(temp); twoList.add(temp); }
 * 
 * else { otherList.add(temp); } } otherGuesses = new
 * LinkedList<Guess>(otherList); FourGuesses = new LinkedList<Guess>(fourList);
 * FiveGuesses = new LinkedList<Guess>(fiveList); ThreeGuesses = new
 * LinkedList<Guess>(threeList); TwoGuesses = new LinkedList<Guess>(twoList);
 * 
 * // Collections.shuffle(guessList);
 * 
 * }
 * 
 * 
 * public void createGuessesLevel() { LinkedList<Guess> highrisk = new
 * LinkedList<>(); LinkedList<Guess> otherList = new LinkedList<>(); int max =
 * possibleGuesses.size(); for (int i = 0; i < max; i++) { //
 * System.out.println(possibleGuesses.size()); // pick high risk guesses from
 * possibleGuess Guess temp = possibleGuesses.pop(); if (temp.row >= 4 - count
 * && temp.row <= 5 + count && temp.column >= 4 - count && temp.column <= 5 +
 * count) { //System.out.println(temp); highrisk.add(temp); } else {
 * otherList.add(temp); } } Collections.shuffle(highrisk);
 * Collections.shuffle(otherList);
 * 
 * // return other guesses back to other list possibleGuesses = new
 * LinkedList<Guess>(otherList); FiveGuesses = new LinkedList<Guess>(highrisk);
 * //System.out.println("after creat possible Guesssize is "+
 * possibleGuesses.size()); }
 * 
 * // check same line , role not all complete // only check 3 more point after
 * once find public LinkedList<Guess> checkMoreOnLine(Guess guess) {
 * LinkedList<Guess> highrisk = new LinkedList<>(); int shipsizecount=0; int
 * xRay = (targetGuess.row - guess.row); int yRay = (targetGuess.column -
 * guess.column); //System.out.println(xRay+" "+yRay); int x = guess.row - xRay;
 * int y = guess.column - yRay; Guess temp = new Guess(); temp.row = x;
 * temp.column = y; // System.out.println("next target point is "+temp); do {
 * //System.out.println("try="+highrisk.size()); shipsizecount++; if
 * (guessExists(temp)) { highrisk.add(temp);
 * //System.out.println("new highrisk first add="+highrisk.size());
 * 
 * removeGuess(y, x); x =x- xRay; y =y- yRay; temp = new Guess(); temp.row = x;
 * temp.column = y; } //if check point not on list, it has been used
 * before,break this check else{ //
 * System.out.println("no pass="+highrisk.size()); break;} } while (x == 0 || x
 * == 9 || y == 0 || y == 9||shipsizecount>=Maxshipsize);
 * 
 * //System.out.println("new highrisk="+highrisk.size()); // add highrisk to
 * targetlist Collections.shuffle(highrisk); return highrisk; }
 */

// end of class MonteCarloGuessPlayer
