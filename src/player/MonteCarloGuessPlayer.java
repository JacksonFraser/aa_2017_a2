package player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import world.World;
import world.World.ShipLocation;

public class MonteCarloGuessPlayer implements Player {

	private LinkedList<Guess> possibleGuesses;
	private LinkedList<Guess> FiveGuesses;
	private LinkedList<Guess> FourGuesses;
	private LinkedList<Guess> otherGuesses;

	private ArrayList<ShipLocation> playerShipList = new ArrayList<>();
	private LinkedList<Guess> targetModeList = new LinkedList<Guess>();
	private boolean start = false;
	private boolean foundFive = false;

	@Override
	public void initialisePlayer(World world) {
		playerShipList = world.shipLocations;
    	possibleGuesses = getGuesses();
    	System.out.println(possibleGuesses.size());
    	getFiveGuesses();
    	System.out.println(FiveGuesses.size());

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
					 * */
					if (playerShipList.get(i).coordinates.size() == 0) {
						a.shipSunk = playerShipList.get(i).ship;
						playerShipList.remove(i);
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
		// Search through target mode unless it is empty
		if (targetModeList.size() != 0) {
			Guess g = targetModeList.pop();
			return g;
		} else if(FiveGuesses.size()!=0){
			Guess g = FiveGuesses.pop();
			System.out.println("five start");
			return g;
		} else if(FourGuesses.size()!=0){
			Guess g = FourGuesses.pop();
			System.out.println("five start");
			return g;
		}
		else{
			Guess g = otherGuesses.pop();
			System.out.println("other");
			return g;
		}
			
		
	} // end of makeGuess()

	@Override
	public void update(Guess guess, Answer answer) {

		if (answer.isHit == true) {
			targetModeList.addAll(createTargetModeList(guess.column, guess.row));
		}

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
		Collections.shuffle(guessList);
    	//FiveGuesses = getFiveGuesses(guessList);

		return guessList;
	}

	public void getFiveGuesses() {
		LinkedList<Guess> fiveList = new LinkedList<Guess>();
		LinkedList<Guess> fourList = new LinkedList<Guess>();

		LinkedList<Guess> otherList = new LinkedList<Guess>();

        int max = possibleGuesses.size();
    	for(int i = 0;i<max;i++){
    		Guess temp = possibleGuesses.pop();
    	    if(temp.row>=4&&temp.row<=5&&temp.column>=4&&temp.column<=5){
    	    	System.out.println(temp);
    	    	fiveList.add(temp);
    	    }
    	    if(temp.row>=3&&temp.row<=6&&temp.column>=3&&temp.column<=6){
    	    	System.out.println(temp);
    	    	fourList.add(temp);
    	    }
    	    
    	    else{otherList.add(temp);}
    	}
        otherGuesses = new LinkedList<Guess>(otherList);
        FourGuesses = new LinkedList<Guess>(fourList);
        FiveGuesses = new LinkedList<Guess>(fiveList);

	    
		//Collections.shuffle(guessList);

	}
	
	
	/**
	 * Creates new guesses to add to the targetModeList and removes the guesses
	 * from the possibleGuessesList
	 * */
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
		return list;
	}

	private LinkedList<Guess> removeGuess(int col, int row) {
		LinkedList<Guess> guesses = possibleGuesses;
		Guess g = new Guess();
		g.row = row;
		g.column = col;
		for (int i = 0; i < guesses.size(); ++i) {
			if (guesses.get(i).column == col && guesses.get(i).row == row) {
				guesses.remove(i);
			}
		}
		Collections.shuffle(guesses);
		return guesses;
	}

}

// end of class RandomGuessPlayer
