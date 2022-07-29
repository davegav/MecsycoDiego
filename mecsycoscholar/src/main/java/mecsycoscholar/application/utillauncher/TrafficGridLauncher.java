/*
C. Bourjot, B. Camus, V. Chevrier, L. Ciarletta, M. Urbanski Copyright
(c) Université de Lorraine & INRIA, Affero GPL license v3, 2014-2015


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
import mecsycoscholar.application.trafficgrid.model.GridModelArtifact;
import mecsycoscholar.helper.Utils;

public class TrafficGridLauncher {
	//the maximum simulation time
	public static final double maxSimulationTime=1000;
	
	public static void main(String args[]){
		// create the model agent for the X model
		EventMAgent GridAgent1 = new EventMAgent("agent_grid_1", maxSimulationTime);
		// create the model artifact and link it with the agent
		GridModelArtifact MicroModelArt1 = new GridModelArtifact("My Models/netlogo/Traffic-Grid.nlogo","Grid 1");
		GridAgent1.setModelArtifact(MicroModelArt1);
		
		// create the model agent for the X model
		EventMAgent GridAgent2 = new EventMAgent("agent_grid_2", maxSimulationTime);
		// create the model artifact and link it with the agent
		GridModelArtifact MicroModelArt2 = new GridModelArtifact("My Models/netlogo/Traffic-Grid.nlogo","Grid 2");
		GridAgent2.setModelArtifact(MicroModelArt2);
		
		// create the model agent for the X model
		EventMAgent GridAgent3 = new EventMAgent("agent_grid_3", maxSimulationTime);
		// create the model artifact and link it with the agent
		GridModelArtifact MicroModelArt3 = new GridModelArtifact("My Models/netlogo/Traffic-Grid.nlogo","Grid 3");
		GridAgent3.setModelArtifact(MicroModelArt3);
		
		// create the model agent for the X model
		EventMAgent GridAgent4 = new EventMAgent("agent_grid_4", maxSimulationTime);
		// create the model artifact and link it with the agent
		GridModelArtifact MicroModelArt4 = new GridModelArtifact("My Models/netlogo/Traffic-Grid.nlogo","Grid 4");
		GridAgent4.setModelArtifact(MicroModelArt4);		

		//links
		Utils.connect(GridAgent1, GridAgent2, "right_out","left_in");
		Utils.connect(GridAgent1, GridAgent4, "down_out","up_in");
		
		Utils.connect(GridAgent2, GridAgent1, "right_out","left_in");	
		Utils.connect(GridAgent2, GridAgent3, "down_out","up_in");
		
		Utils.connect(GridAgent3, GridAgent4, "right_out","left_in");	
		Utils.connect(GridAgent3, GridAgent2, "down_out","up_in");
		
		Utils.connect(GridAgent4, GridAgent3, "right_out","left_in");	
		Utils.connect(GridAgent4, GridAgent1, "down_out","up_in");		
		
		
		GridAgent1.startModelSoftware();
		GridAgent2.startModelSoftware();
		GridAgent4.startModelSoftware();
		GridAgent3.startModelSoftware();
		
		GridAgent1.setModelParameters();
		GridAgent2.setModelParameters();
		GridAgent4.setModelParameters();
		GridAgent3.setModelParameters();
		
		GridAgent1.start();
		GridAgent2.start();
		GridAgent3.start();
		GridAgent4.start();
	}
}
