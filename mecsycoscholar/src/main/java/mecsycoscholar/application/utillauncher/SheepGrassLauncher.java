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
import mecsycoscholar.application.sheepgrass.model.GrassModelArtifact;
import mecsycoscholar.application.sheepgrass.model.SheepModelArtifact;
import mecsycoscholar.helper.Utils;

/**
* The first world represents grass and sheep eating it. When a sheep eat a grass, the
* grass patch disappears and the sheep gains energy. 
* The second world represents Shepherds and sheep. 
* When a sheep has a energy equals to zero, it dead.
* 
* Sheep eat in world 1, but the movements are done in world 2.
* World 1 sends the energy of each sheep to world 2.
* World 2 sends position of each sheep to world 1.
*
*/
public class SheepGrassLauncher {

// Display settings
	/**
	 * First world name.
	 */
	public final static String firstWorldName = "grass-world";

	/**
	 * Second world name.
	 */
	public final static String secondWorldName = "sheep-world";

	/**
	 * Frame width.
	 */
	public final static int width = 650;

	/**
	 * Frame height.
	 */
	public final static int height = 690;

// Model settings
	/**
	 * Maximum simulation time.
	 */
	public final static double maxSimulationTime = 1000;

	/**
	 * Path to the NetLogo model.
	 */
	public final static String firstModelPath = "My Models/netlogo/sheep_grass_weeds.nlogo";

	/**
	 * Path to the NetLogo model.
	 */
	public final static String secondModelPath = "My Models/netlogo/sheep_shepherds.nlogo";

// Entry point
	public static void main (String... args) {
		EventMAgent agent1 = new EventMAgent("agent_grass", maxSimulationTime);
		ModelArtifact model1 = new GrassModelArtifact(firstModelPath, firstWorldName, width, height);
		agent1.setModelArtifact(model1);

		EventMAgent agent2 = new EventMAgent("agent_sheeps", maxSimulationTime);
		ModelArtifact model2 = new SheepModelArtifact(secondModelPath, secondWorldName, width, height);
		agent2.setModelArtifact(model2);

		//links
		Utils.connect(agent1, agent2, "out","in");
		Utils.connect(agent2, agent1,  "out","in");

		agent1.startModelSoftware();
		agent2.startModelSoftware();
		
		agent1.setModelParameters();
		agent2.setModelParameters();

		agent1.start();
		agent2.start();
	}

}
