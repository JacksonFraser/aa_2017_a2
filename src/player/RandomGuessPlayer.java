package player;

import java.util.Random;

import world.World;

/**
 * Random guess player (task A). Please implement this class.
 *
 * @author Youhan, Jeffrey
 */
public class RandomGuessPlayer implements Player {

	@Override
	public void initialisePlayer(World world) {
		// To be implemented.
	} // end of initialisePlayer()

	@Override
	public Answer getAnswer(Guess guess) {
		// To be implemented.

		// dummy return
		return null;
	} // end of getAnswer()

	@Override
	public Guess makeGuess() {
		Guess g = new Guess();
		Random random = new Random();
		int max = 9;
		int min = 0;

		int row = random.nextInt(max - min + 1) + min;
		g.row = row;

		int col = random.nextInt(max - min + 1) + min;
		g.column = col;
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

} // end of class RandomGuessPlayer
