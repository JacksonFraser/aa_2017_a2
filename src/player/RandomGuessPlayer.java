package player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import world.World;
import world.World.Coordinate;
import world.World.ShipLocation;

/**
 * Random guess player (task A). Please implement this class.
 *
 * @author Youhan, Jeffrey
 */
public class RandomGuessPlayer implements Player {

	private World world;
	private Deque<Guess> possibleGuesses = getShuffledGuesses();
	private ArrayList<ShipLocation> playerShipList = new ArrayList<>();

	@Override
	public void initialisePlayer(World world) {
		this.world = world;
		playerShipList = world.shipLocations;
	} // end of initialisePlayer()

	@Override
	public Answer getAnswer(Guess guess) {
		Answer a = new Answer();
		
		//Searches individual ships
		for(int i = 0; i < playerShipList.size(); ++i){
			
			//Searches individual ship coordinates 
			for(int j = 0; j < playerShipList.get(i).coordinates.size(); ++j) {
				int col = playerShipList.get(i).coordinates.get(j).column;
				int row = playerShipList.get(i).coordinates.get(j).row;
				
				//Remove this point from the ship
				if(col == guess.column && row == guess.row){
					playerShipList.get(i).coordinates.remove(j);
					
					/*
					 * if there are no more coordinates, then the ship has been sunk
					 * and we remove it from the playerShipList
					 * */
					if(playerShipList.get(i).coordinates.size() == 0){
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
		Guess g = possibleGuesses.pop();
		return g;
	} // end of makeGuess()

	@Override
	public void update(Guess guess, Answer answer) {
		// No need to implement for RandomPlayer
	} // end of update()

	@Override
	public boolean noRemainingShips() {
		if(playerShipList.size() == 0){
			return true;
		}
		else{
			return false;
		}

	} // end of noRemainingShips()
	
	
	//creates a deque of all possible guesses and then randomises them
	public Deque<Guess> getShuffledGuesses() {
		List<Guess> guessList = new ArrayList<>();
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
		possibleGuesses = new ArrayDeque<>(guessList);

		return possibleGuesses;
	}

		
}

// end of class RandomGuessPlayer
