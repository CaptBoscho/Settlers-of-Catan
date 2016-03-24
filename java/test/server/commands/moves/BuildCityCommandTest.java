package server.commands.moves;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.exceptions.CommandExecutionFailedException;
import server.facade.MockFacade;
import server.main.Config;
import shared.dto.BuildCityDTO;
import shared.dto.CookieWrapperDTO;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit Testing for the "Build City" command.
 *
 * @author Derek Argueta
 */
public class BuildCityCommandTest {

	@Before
	public void setUp() {
		Config.facade = new MockFacade();
	}

	@After
	public void tearDown() {

	}

	/**
	 * Validates that the command checks that the parameters are set before
	 * executing using the `assert` keyword.
	 */
	@Test(expected = AssertionError.class)
	public void testExecuteWithMissingParams() throws CommandExecutionFailedException {
		new BuildCityCommand().execute();
	}

	@Test
	public void testExecute() {
		//build a city where a settlement exists and pass
		CookieWrapperDTO dto = new CookieWrapperDTO(new BuildCityDTO(0, new VertexLocation(new HexLocation(0,2), VertexDirection.NorthEast)));
		dto.setGameId(MockFacade.DEFAULT_GAME);
		BuildCityCommand command = new BuildCityCommand();
		command.setParams(dto);
		try {
			command.execute();
		} catch (CommandExecutionFailedException e) {
			fail(e.getMessage());
		}

		//try to build city at a location you can't, and fail
		dto.setGameId(MockFacade.EMPTY_GAME);
		command.setParams(dto);
		try {
			command.execute();
			fail("Should not be able to build a city");
		} catch(CommandExecutionFailedException e) {
			//Exception should be thrown
			assertTrue(true);
		}
	}
}
