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

package mecsycoscholar.application.trafficgrid.launcher;

import javax.swing.JFrame;

import mecsyco.core.agent.EventMAgent;
import mecsyco.core.coupling.CentralizedEventCouplingArtifact;
import mecsyco.observing.swing.util.InitJFrame;
import mecsycoscholar.application.trafficgrid.model.GridModelArtifact;

public class TrafficGridLauncher {
	//the maximum simulation time
	public static final double maxSimulationTime=1000;
	
	public static void main(String args[]){
		InitJFrame.setDefaultCloseOperationJFrame(JFrame.DISPOSE_ON_CLOSE);
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
		
		CentralizedEventCouplingArtifact artFromR1ToL2=new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact artFromB1ToU4=new CentralizedEventCouplingArtifact();	
		
		CentralizedEventCouplingArtifact artFromR2ToL1=new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact artFromB2ToU3=new CentralizedEventCouplingArtifact();
			
		CentralizedEventCouplingArtifact artFromR3ToL4=new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact artFromB3ToU2=new CentralizedEventCouplingArtifact();
		
		CentralizedEventCouplingArtifact artFromR4ToL3=new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact artFromB4ToU1=new CentralizedEventCouplingArtifact();
		
		
		// set GridAgent 1 output coupling artifacts
		GridAgent1.addOutputCouplingArtifact(artFromR1ToL2, "right_out");
		GridAgent2.addInputCouplingArtifact(artFromR1ToL2, "left_in");		
		
		GridAgent1.addOutputCouplingArtifact(artFromB1ToU4, "down_out");
		GridAgent4.addInputCouplingArtifact(artFromB1ToU4, "up_in");
		
		// Set GridAgent 2 output coupling artifacts
		GridAgent2.addOutputCouplingArtifact(artFromR2ToL1, "right_out");
		GridAgent1.addInputCouplingArtifact(artFromR2ToL1, "left_in");
		
		GridAgent2.addOutputCouplingArtifact(artFromB2ToU3, "down_out");
		GridAgent3.addInputCouplingArtifact(artFromB2ToU3, "up_in");
		
		// Set GridAgent 3 output coupling artifacts
		GridAgent3.addOutputCouplingArtifact(artFromR3ToL4, "right_out");
		GridAgent4.addInputCouplingArtifact(artFromR3ToL4, "left_in");
		
		GridAgent3.addOutputCouplingArtifact(artFromB3ToU2, "down_out");
		GridAgent2.addInputCouplingArtifact(artFromB3ToU2, "up_in");		
	
		// Set GridAgent 4 output coupling artifacts
		GridAgent4.addOutputCouplingArtifact(artFromR4ToL3, "right_out");
		GridAgent3.addInputCouplingArtifact(artFromR4ToL3, "left_in");
		
		GridAgent4.addOutputCouplingArtifact(artFromB4ToU1, "down_out");
		GridAgent1.addInputCouplingArtifact(artFromB4ToU1, "up_in");	
		
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
