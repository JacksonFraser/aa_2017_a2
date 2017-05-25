package player;

import java.util.ArrayList;
import java.util.Scanner;

import world.World;
import world.World.ShipLocation;

/**
 * Monte Carlo guess player (task C).
 * Please implement this class.
 *
 * @author Youhan, Jeffrey
 */
public class MonteCarloGuessPlayer  implements Player{
	
	private ArrayList<ShipLocation> playerShipList = new ArrayList<>();

	@Override
    public void initialisePlayer(World world) {
        playerShipList = world.shipLocations;
		
	} // end of initialisePlayer()

    @Override
    public Answer getAnswer(Guess guess) {
        // To be implemented.

        // dummy return
        return null;
    } // end of getAnswer()


    @Override
    public Guess makeGuess() {
        // To be implemented.

        // dummy return
        return null;
    } // end of makeGuess()


    @Override
    public void update(Guess guess, Answer answer) {
        
    } // end of update()


    @Override
    public boolean noRemainingShips() {
        // To be implemented.

        // dummy return
        return true;
    } // end of noRemainingShips()

} // end of class MonteCarloGuessPlayer
