/*
V. Chevrier, L. Ciarletta, J. Siebert,
Copyright (c) Université de Lorraine & INRIA, Affero GPL license v3, 2014-2015


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

package mecsycoscholar.application.utillauncher;

import mecsyco.core.agent.EventMAgent;
import mecsyco.core.model.ModelArtifact;
import mecsyco.world.netlogo.type.NetLogoColorConstants;
import mecsycoscholar.application.pedestrian.model.NetLogoTurtlePedestrianShape;
import mecsycoscholar.application.pedestrian.model.PedestrianModelArtifact;
import mecsycoscholar.helper.Utils;

public class PedestrianLauncher {
// Display Settings
	/**
	 * First world name.
	 */
	public final static String firstWorldName = "World 1";

	/**
	 * Second world name.
	 */
	public final static String secondWorldName = "World 2";

	/**
	 * Third world name.
	 */
	public final static String thirdWorldName = "World 3";

// Model settings
	/**
	 * Maximum simulation time.
	 */
	public final static double maxSimulationTime = 1000;

	/**
	 * Path to the first NetLogo model.
	 */
	public final static String firstModelPath = "My Models/netlogo/pedestrian.nlogo";

	/**
	 * Path to the first NetLogo model.
	 */
	public final static String secondModelPath = "My Models/netlogo/pedestrian.nlogo";

	/**
	 * Path to the first NetLogo model.
	 */
	public final static String thirdModelPath = "My Models/netlogo/pedestrian.nlogo";

// Entry point
	public static void main (String... args) {
		EventMAgent agent1 = new EventMAgent ("agent_1", maxSimulationTime);
		ModelArtifact modelArtefacct1 = new PedestrianModelArtifact (firstModelPath, firstWorldName);
		agent1.setModelArtifact (modelArtefacct1);

		EventMAgent agent2 = new EventMAgent ("agent_2", maxSimulationTime);
		ModelArtifact modelArtefacct2 = new PedestrianModelArtifact (secondModelPath, secondWorldName, NetLogoColorConstants.BLUE, NetLogoTurtlePedestrianShape.butterfly);
		agent2.setModelArtifact (modelArtefacct2);

		EventMAgent agent3 = new EventMAgent ("agent_3", maxSimulationTime);
		ModelArtifact modelArtefacct3 = new PedestrianModelArtifact (thirdModelPath, thirdWorldName, NetLogoColorConstants.YELLOW, NetLogoTurtlePedestrianShape.turtle);
		agent3.setModelArtifact (modelArtefacct3);

		//links
		Utils.connect(agent3, agent1, "right_out","left_in");
		Utils.connect(agent1, agent2, "right_out","left_in");
		Utils.connect(agent2, agent3, "right_out","left_in");

		agent1.startModelSoftware ();
		agent2.startModelSoftware ();
		agent3.startModelSoftware ();
		
		agent1.setModelParameters();
		agent2.setModelParameters();
		agent3.setModelParameters();

		agent1.start ();
		agent2.start ();
		agent3.start ();
	}

}
