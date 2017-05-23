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

	private Deque<Guess> possibleGuesses = getShuffledGuesses();
	private ArrayList<ShipLocation> playerShipList = new ArrayList<>();

	@Override
	public void initialisePlayer(World world) {
		playerShipList = world.shipLocations;
		// To be implemented.
	} // end of initialisePlayer()

	@Override
	public Answer getAnswer(Guess guess) {
		Answer a = new Answer();
		for(int i = 0; i < playerShipList.size(); ++i){
			for(int j = 0; j < playerShipList.get(i).coordinates.size(); ++j) {
				int col = playerShipList.get(i).coordinates.get(j).column;
				int row = playerShipList.get(i).coordinates.get(j).row;
				if(col == guess.column && row == guess.row){
					playerShipList.get(i).coordinates.remove(j);
					if(playerShipList.get(i).coordinates.size() == 0){
						System.out.println(playerShipList.get(i).ship.name()+ " has been sunk");
						a.shipSunk = playerShipList.get(i).ship;
					}
					a.isHit = true;
				}
			}
		}
		// dummy return
		return a;
	} // end of getAnswer()

	@Override
	public Guess makeGuess() {
		Guess g = possibleGuesses.pop();
		return g;
		// dummy return
	} // end of makeGuess()

	@Override
	public void update(Guess guess, Answer answer) {
		// To be implemented.
	} // end of update()

	@Override
	public boolean noRemainingShips() {
		// To be implemented.

		// dummy return
		return false;
	} // end of noRemainingShips()

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
