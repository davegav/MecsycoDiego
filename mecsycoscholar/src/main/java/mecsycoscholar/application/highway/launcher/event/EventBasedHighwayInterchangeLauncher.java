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

package mecsycoscholar.application.highway.launcher.event;

import mecsyco.core.agent.EventMAgent;
import mecsyco.core.coupling.CentralizedEventCouplingArtifact;
import mecsycoscholar.application.highway.model.event.EventModelArtifact;
import mecsycoscholar.application.highway.model.event.TrafficModel;

public class EventBasedHighwayInterchangeLauncher {

	public final static double event_length = 50;

	public final static double max_speed = 5;

	public final static double min_speed = 1;

	public static final double maxSimulationTime = 1000000;

	public final static boolean headless = false;

	public static void main (String args[]) {

		TrafficModel model1 = new TrafficModel("Event-Based Traffic Model 1", event_length, max_speed, min_speed, 1,
				10, headless);

		EventMAgent agent1 = new EventMAgent("agent_1", event_length / max_speed, maxSimulationTime);
		EventModelArtifact art1 = new EventModelArtifact(model1);
		agent1.setModelArtifact(art1);

		TrafficModel model2 = new TrafficModel("Event-Based Traffic Model 2", event_length, max_speed, min_speed, 1,
				10, headless);

		EventMAgent agent2 = new EventMAgent("agent_2", event_length / max_speed, maxSimulationTime);
		EventModelArtifact art2 = new EventModelArtifact(model2);
		agent2.setModelArtifact(art2);

		TrafficModel model3 = new TrafficModel("Event-Based Traffic Model 3", event_length, max_speed, min_speed, 2,
				10, headless);

		EventMAgent agent3 = new EventMAgent("agent_3", event_length / max_speed, maxSimulationTime);
		EventModelArtifact art3 = new EventModelArtifact(model3);
		agent3.setModelArtifact(art3);

		TrafficModel model4 = new TrafficModel("Event-Based Traffic Model 4", event_length, max_speed, min_speed, 1,
				10, headless);

		EventMAgent agent4 = new EventMAgent("agent_4", event_length / max_speed, maxSimulationTime);
		EventModelArtifact art4 = new EventModelArtifact(model4);
		agent4.setModelArtifact(art4);

		TrafficModel model5 = new TrafficModel("Event-Based Traffic Model 5", event_length, max_speed, min_speed, 1,
				10, headless);

		EventMAgent agent5 = new EventMAgent("agent_5", event_length / max_speed, maxSimulationTime);
		EventModelArtifact art5 = new EventModelArtifact(model5);
		agent5.setModelArtifact(art5);

		CentralizedEventCouplingArtifact artFrom1To3 = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact artFrom2To3 = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact artFrom3To4 = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact artFrom3To5 = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact artFrom4To1 = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact artFrom5To2 = new CentralizedEventCouplingArtifact();

		agent1.addOutputCouplingArtifact(artFrom1To3, "out0");
		agent3.addInputCouplingArtifact(artFrom1To3, "in0");
		
		agent2.addOutputCouplingArtifact(artFrom2To3, "out0");
		agent3.addInputCouplingArtifact(artFrom2To3, "in00"); //No impact of the port name, we avoid to using the same name to avoid overwriting

		agent3.addOutputCouplingArtifact(artFrom3To4, "out0");
		agent4.addInputCouplingArtifact(artFrom3To4, "in0");
		
		agent3.addOutputCouplingArtifact(artFrom3To5, "out1");
		agent5.addInputCouplingArtifact(artFrom3To5, "in0");

		agent4.addOutputCouplingArtifact(artFrom4To1, "out0");
		agent1.addInputCouplingArtifact(artFrom4To1, "in0");
		
		agent5.addOutputCouplingArtifact(artFrom5To2, "out0");
		agent2.addInputCouplingArtifact(artFrom5To2, "in0");
		

		agent1.startModelSoftware();
		agent2.startModelSoftware();
		agent3.startModelSoftware();
		agent4.startModelSoftware();
		agent5.startModelSoftware();
		
		agent1.setModelParameters();
		agent2.setModelParameters();
		agent3.setModelParameters();
		agent4.setModelParameters();
		agent5.setModelParameters();
		
		agent1.start();
		agent2.start();
		agent3.start();
		agent4.start();
		agent5.start();

	}
}
