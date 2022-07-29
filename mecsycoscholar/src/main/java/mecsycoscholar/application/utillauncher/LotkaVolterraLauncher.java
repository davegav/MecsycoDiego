/*
C. Bourjot, B. Camus, V. Chevrier, L. Ciarletta Copyright(c)
Université de Lorraine & INRIA, Affero GPL license v3, 2014-2015


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
import mecsyco.core.agent.ObservingMAgent;
import mecsyco.core.coupling.multiplexer.AggregationMultiplexer;
import mecsyco.core.operation.aggregation.XYStateAggregationOperation;
import mecsyco.observing.jfreechart.xy.LiveTXGraphic;
import mecsyco.observing.jfreechart.xy.PostMortemXYGraphic;
import mecsyco.observing.jfreechart.xy.Renderer;
import mecsyco.observing.swing.dispatcher.SwingDispatcherArtifact;
import mecsycoscholar.application.ode.model.NetLogoModelArtifact;
import mecsycoscholar.helper.Utils;

/**
 * Launch a distributed simulation of a Lotka-Volterra system : Prey population:
 * dx/dt = a*x-b*x*y Predator population: dy/dt = c*x*y-d*y
 *
 * Each ODE is simulated by one NetLogo instance: Prey population: prey.nlogo
 * Predator population: predator.nlogo
 * 
 * A monolithic Netlogo simulation of the system can be found in the file
 * prey-predator.nlogo
 *
 *
 *
 */

public class LotkaVolterraLauncher {

	// set the simulation time
	public final static double maxSimulationTime = 20;

	// set the step size of the prey model (between {0.01,0.001,0.0001})
	public final static double time_discretization_prey = 0.01;

	// set the step size of the prey model (between {0.01,0.001,0.0001})
	public final static double time_discretization_predator = 0.01;

	public static void main(String[] args) {
		// create the model agent for the prey model
		EventMAgent preyAgent = new EventMAgent("agent_prey", maxSimulationTime);
		// create the model artifact and link it with the agent
		NetLogoModelArtifact preyModelArtifact = new NetLogoModelArtifact("My Models/netlogo/prey.nlogo", "Prey Model");
		preyAgent.setModelArtifact(preyModelArtifact);

		// create the model agent for the predator model
		EventMAgent predatorAgent = new EventMAgent("agent_predator", maxSimulationTime);
		// create the model artifact and link it with the agent
		NetLogoModelArtifact predatorModelArtifact = new NetLogoModelArtifact("My Models/netlogo/predator.nlogo",
				"Predator Model");
		predatorAgent.setModelArtifact(predatorModelArtifact);

		// create the model agent for the observing model
		ObservingMAgent obsAgent = new ObservingMAgent("obs", maxSimulationTime);
		// create the observing model artifact and link it with the agent
		SwingDispatcherArtifact ObsModelArtifact = new SwingDispatcherArtifact();
		obsAgent.setDispatcherArtifact(ObsModelArtifact);

		// Create graphics
		ObsModelArtifact.addObservingArtifact(
				new LiveTXGraphic("title", "y", Renderer.Step, new String[] { "X", "Y" }, new String[] { "X", "Y" }));
		ObsModelArtifact
				.addObservingArtifact(new PostMortemXYGraphic("XY", "prey", "predator", Renderer.Step, "X,Y", "X,Y"));

		// create the new multiplexor
		XYStateAggregationOperation ope = new XYStateAggregationOperation("X", "Y");
		AggregationMultiplexer Mult = new AggregationMultiplexer("Mult", ope);

		// links between agent
		Utils.connect(predatorAgent, preyAgent, "Y");
		Utils.connect(preyAgent, predatorAgent, "X");

		// links with obs
		Utils.connect(preyAgent, obsAgent, "X");
		Utils.connect(predatorAgent, obsAgent, "Y");

		// link with obs using multiplexer
		Utils.connect(new EventMAgent[] { preyAgent, predatorAgent }, new String[] { "X", "Y" }, Mult, obsAgent, "X,Y");

		// initialize the model
		preyAgent.startModelSoftware();
		predatorAgent.startModelSoftware();
		obsAgent.startModelSoftware();

		// set model parameters
		String arg_prey[] = { String.valueOf(time_discretization_prey) };
		String arg_predator[] = { String.valueOf(time_discretization_predator) };
		preyAgent.setModelParameters(arg_prey);
		predatorAgent.setModelParameters(arg_predator);

		// first simulation step
		predatorAgent.coInitialize();
		preyAgent.coInitialize();

		preyAgent.start();
		predatorAgent.start();
		obsAgent.start();
		Mult.start();

	}
}
