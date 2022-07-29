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
import mecsycoscholar.application.gettingstarted.model.SimpleRandomWalkModelArtifact;

/**
 * Interacting models.
 *
 */
public final class InteractingModelExampleLauncher {

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
	 * Path to the NetLogo model.
	 */
	public static final String SECOND_MODEL_PATH = "My Models/netlogo/random_walk.nlogo";

	/**
	 * Turtles color
	 */
	public static final double GREEN = 55;

	/**
	 * Turtles color
	 */
	public static final double RED = 15;

	/**
	 * Turtles size
	 */
	public static final double SIZE1 = 2;

	/**
	 * Turtles size
	 */
	public static final double SIZE2 = 4;

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		// Model 1
		EventMAgent agent1 = new EventMAgent("world-1", MAXSIMULATIONTIME);
		ModelArtifact modelArtefacct1 = new SimpleRandomWalkModelArtifact(FIRST_MODEL_PATH, FIRST_WORLD_NAME, WIDTH,
				HEIGHT, SIZE1, GREEN);
		agent1.setModelArtifact(modelArtefacct1);
		CentralizedEventCouplingArtifact couplingArtifact1 = new CentralizedEventCouplingArtifact();

		// Model 2
		EventMAgent agent2 = new EventMAgent("world-2", MAXSIMULATIONTIME);
		ModelArtifact modelArtifact2 = new SimpleRandomWalkModelArtifact(SECOND_MODEL_PATH, SECOND_WORLD_NAME, WIDTH,
				HEIGHT, SIZE2, RED);
		agent2.setModelArtifact(modelArtifact2);
		CentralizedEventCouplingArtifact couplingArtifact2 = new CentralizedEventCouplingArtifact();

		// Connections
		agent1.addInputCouplingArtifact(couplingArtifact1, "in");
		agent1.addOutputCouplingArtifact(couplingArtifact2, "out");
		agent2.addInputCouplingArtifact(couplingArtifact2, "in");
		agent2.addOutputCouplingArtifact(couplingArtifact1, "out");

		// Start NetLogo
		agent1.startModelSoftware();
		agent2.startModelSoftware();

		// Model initializations
		agent1.setModelParameters();
		agent2.setModelParameters();

		// Simulation launching
		agent1.start();
		agent2.start();
	}

	private InteractingModelExampleLauncher() {

	}

}
