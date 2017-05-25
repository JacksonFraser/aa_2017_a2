package player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import world.World;
import world.World.ShipLocation;

public class GreedyGuessPlayer implements Player {

	private LinkedList<Guess> possibleGuesses;
	private LinkedList<Guess> passGuesses = new LinkedList<Guess>();

	private ArrayList<ShipLocation> playerShipList = new ArrayList<>();
	private LinkedList<Guess> targetModeList = new LinkedList<Guess>();
	private boolean start = false;

	@Override
	public void initialisePlayer(World world) {
		playerShipList = world.shipLocations;
    	possibleGuesses = getGuesses();
    	//System.out.println(possibleGuesses.size());


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
		} else {
			if(passGuesses.size()!=0){
				passGuesses.removeAll(passGuesses);
			}
			Guess g = possibleGuesses.pop();
			
			return g;

		}
	} // end of makeGuess()

	@Override
	public void update(Guess guess, Answer answer) {
		
		if (answer.isHit == true) {
			possibleGuesses.add(guess);
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
		for (int i = 0; i <= max; i++) {
			for (int j = i % 2; j <= max; j += 2) {
				Guess guess = new Guess();
				guess.row = i;
				guess.column = j;
				guessList.add(guess);
			}
		}
		Collections.shuffle(guessList);

		return guessList;
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
			if(guessExists(g)){
				
			}
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
		System.out.println("size of pass guess:"+passGuesses.size());
		for(int i =0;i<list.size();i++){
			if(guessExists(list.get(i))){
				list.remove(i);
			};
			
		}
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
	private boolean guessExists(Guess g){
		for(int i = 0; i < passGuesses.size(); ++i){
			if(passGuesses.get(i)== g){
				return true;
			}
		}
		return false;
	}

}

// end of class RandomGuessPlayer
