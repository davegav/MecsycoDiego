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
import mecsycoscholar.application.highway.model.event.EventModelArtifact;
import mecsycoscholar.application.highway.model.event.TrafficModel;
import mecsycoscholar.helper.Utils;

public class EventBasedHighwayInterchangeLauncher {

	public static final double event_length = 50;

	public static final double max_speed = 5;

	public static final double min_speed = 1;

	public static final double maxSimulationTime = 1000000;

	public static final boolean headless = false;

	public static void main(String args[]) {

		TrafficModel model1 = new TrafficModel("Event-Based Traffic Model 1", event_length, max_speed, min_speed, 1, 10,
				headless);

		EventMAgent agent1 = new EventMAgent("agent_1", event_length / max_speed, maxSimulationTime);
		EventModelArtifact art1 = new EventModelArtifact(model1);
		agent1.setModelArtifact(art1);

		TrafficModel model2 = new TrafficModel("Event-Based Traffic Model 2", event_length, max_speed, min_speed, 1, 10,
				headless);

		EventMAgent agent2 = new EventMAgent("agent_2", event_length / max_speed, maxSimulationTime);
		EventModelArtifact art2 = new EventModelArtifact(model2);
		agent2.setModelArtifact(art2);

		TrafficModel model3 = new TrafficModel("Event-Based Traffic Model 3", event_length, max_speed, min_speed, 2, 10,
				headless);

		EventMAgent agent3 = new EventMAgent("agent_3", event_length / max_speed, maxSimulationTime);
		EventModelArtifact art3 = new EventModelArtifact(model3);
		agent3.setModelArtifact(art3);

		TrafficModel model4 = new TrafficModel("Event-Based Traffic Model 4", event_length, max_speed, min_speed, 1, 10,
				headless);

		EventMAgent agent4 = new EventMAgent("agent_4", event_length / max_speed, maxSimulationTime);
		EventModelArtifact art4 = new EventModelArtifact(model4);
		agent4.setModelArtifact(art4);

		TrafficModel model5 = new TrafficModel("Event-Based Traffic Model 5", event_length, max_speed, min_speed, 1, 10,
				headless);

		EventMAgent agent5 = new EventMAgent("agent_5", event_length / max_speed, maxSimulationTime);
		EventModelArtifact art5 = new EventModelArtifact(model5);
		agent5.setModelArtifact(art5);

		Utils.connect(agent1, agent3, "out0", "in0");
		// No impact of the port name, we avoid to using the same name to avoid
		// overwriting
		Utils.connect(agent2, agent3, "out0", "in00");
		Utils.connect(agent3, agent4, "out0", "in0");
		Utils.connect(agent3, agent5, "out1", "in0");
		Utils.connect(agent4, agent1, "out0", "in0");
		Utils.connect(agent5, agent2, "out0", "in0");

		agent1.startModelSoftware();
		agent2.startModelSoftware();
		agent3.startModelSoftware();
		agent4.startModelSoftware();
		agent5.startModelSoftware();

		System.out.println(model1.totalNumberOfCars() + model2.totalNumberOfCars() + model3.totalNumberOfCars()
				+ model4.totalNumberOfCars() + model5.totalNumberOfCars());

		agent1.start();
		agent2.start();
		agent3.start();
		agent4.start();
		agent5.start();

		try {
			agent1.join();
			agent2.join();
			agent3.join();
			agent4.join();
			agent5.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Check the number of vehicles at the end
		System.out.println(model1.totalNumberOfCars() + model2.totalNumberOfCars() + model3.totalNumberOfCars()
				+ model4.totalNumberOfCars() + model5.totalNumberOfCars());

	}
}
