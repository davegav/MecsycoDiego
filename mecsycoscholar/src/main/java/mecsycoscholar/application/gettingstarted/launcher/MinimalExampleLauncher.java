/*
V. Chevrier, L. Ciarletta, V. Elvinger, Copyright (c) Université de
Lorraine & INRIA, Affero GPL license v3, 2014-2015

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package mecsycoscholar.application.gettingstarted.launcher;

import mecsyco.core.agent.EventMAgent;
import mecsyco.core.coupling.CentralizedEventCouplingArtifact;
import mecsyco.core.model.ModelArtifact;
import mecsyco.core.operation.event.EventOperation;
import mecsyco.world.netlogo.type.NetLogoColorConstants;
import mecsycoscholar.application.gettingstarted.model.SimpleRandomWalkModelArtifact;
import mecsycoscholar.application.gettingstarted.operation.TurtleColorSetter;

/**
 * Single simulator with a filter as operation.
 *
 */
public final class MinimalExampleLauncher {

	// Display Settings
	/**
	 * First world name.
	 */
	public static final String FIRST_WORLD_NAME = "world1";

	/**
	 * Second world name.
	 */
	public static final String SECOND_WORLD_NAME = "world2";

	/**
	 * Frame width.
	 */
	public static final int WIDTH = 460;

	/**
	 * Frame height.
	 */
	public static final int HEIGHT = 520;

	// Model settings
	/**
	 * Maximum simulation time.
	 */
	public static final double MAXSIMULATIONTIME = 1000;

	/**
	 * Path to the NetLogo model.
	 */
	public static final String FIRST_MODEL_PATH = "My Models/netlogo/random_walk.nlogo";

	/**
	 * Turtles color
	 */
	public static final double GREEN = 55;

	/**
	 * Turtles color
	 */
	public static final double COLOR = 15;

	/**
	 * Turtles size
	 */
	public static final double SIZE = 2;

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		EventMAgent agent1 = new EventMAgent("world-1", MAXSIMULATIONTIME);

		ModelArtifact modelArtifact = new SimpleRandomWalkModelArtifact(FIRST_MODEL_PATH, FIRST_WORLD_NAME, WIDTH,
				HEIGHT, SIZE, COLOR);
		agent1.setModelArtifact(modelArtifact);
		
		EventOperation colorSetter = new TurtleColorSetter (NetLogoColorConstants.GREEN);
		
		CentralizedEventCouplingArtifact couplingArtifact = new CentralizedEventCouplingArtifact();
		couplingArtifact.addEventOperation(colorSetter);
		agent1.addInputCouplingArtifact(couplingArtifact, "in");
		agent1.addOutputCouplingArtifact(couplingArtifact, "out");

		// Start NetLogo
		agent1.startModelSoftware();

		// Model initialization
		agent1.setModelParameters();

		// Start the simulation
		agent1.start();
	}

	/**
	 * Prevent instantiation.
	 */
	private MinimalExampleLauncher() {

	}

}
