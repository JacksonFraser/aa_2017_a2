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
public class GreedyGuessPlayer implements Player {

	private World world;
	private LinkedList<Guess> possibleGuesses = getGuesses();
	private ArrayList<ShipLocation> playerShipList = new ArrayList<>();
	private LinkedList<Guess> targetModeList = new LinkedList<Guess>();
	private String direction = "";

	@Override
	public void initialisePlayer(World world) {
		this.world = world;
		playerShipList = world.shipLocations;
		// To be implemented.
	} // end of initialisePlayer()

	@Override
	public Answer getAnswer(Guess guess) {
		Answer a = new Answer();
		for (int i = 0; i < playerShipList.size(); ++i) {
			for (int j = 0; j < playerShipList.get(i).coordinates.size(); ++j) {
				int col = playerShipList.get(i).coordinates.get(j).column;
				int row = playerShipList.get(i).coordinates.get(j).row;
				if (col == guess.column && row == guess.row) {
					playerShipList.get(i).coordinates.remove(j);
					if (playerShipList.get(i).coordinates.size() == 0) {
						System.out.println(playerShipList.get(i).ship.name()
								+ " has been sunk");
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
		if (targetModeList.size() != 0) {
			Guess g = targetModeList.pop();
			return g;
		} else {
			Guess g = possibleGuesses.pop();
			return g;

		}
	} // end of makeGuess()

	@Override
	public void update(Guess guess, Answer answer) {

		if (answer.isHit == true && targetModeList.size() == 0) {
			targetModeList = createTargetModeList(guess.column, guess.row);
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

		return guessList;
	}

	private LinkedList<Guess> createTargetModeList(int col, int row) {
		LinkedList<Guess> list = new LinkedList<>();
		if (col > 0) {
			Guess g = new Guess();
			g.column = col - 1;
			g.row = row;
			list.add(g);
			possibleGuesses = removeGuess(col, row);
		}
		if (col < 9) {
			Guess g = new Guess();
			g.column = col + 1;
			g.row = row;
			list.add(g);
			possibleGuesses = removeGuess(col, row);
		}
		if (row > 0) {
			Guess g = new Guess();
			g.row = row - 1;
			g.column = col;
			list.add(g);
			possibleGuesses = removeGuess(col, row);
		}
		if (row < 9) {
			Guess g = new Guess();
			g.row = row + 1;
			g.column = col;
			list.add(g);
			possibleGuesses = removeGuess(col, row);
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
				
				System.out.println("got removed "+guesses.size());
				guesses.remove(i);
			}
		}
		return guesses;
	}

}

// end of class RandomGuessPlayer
