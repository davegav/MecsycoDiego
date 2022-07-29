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

package mecsycoscholar.application.ode.launcher.lotkavolterra;

import mecsyco.core.agent.EventMAgent;
import mecsyco.core.agent.ObservingMAgent;
import mecsyco.core.coupling.CentralizedEventCouplingArtifact;
import mecsyco.core.coupling.multiplexer.AggregationMultiplexer;
import mecsyco.core.operation.aggregation.XYStateAggregationOperation;
import mecsyco.observing.jfreechart.xy.LiveTXGraphic;
import mecsyco.observing.jfreechart.xy.LiveXYGraphic;
import mecsyco.observing.jfreechart.xy.Renderer;
import mecsyco.observing.swing.dispatcher.SwingDispatcherArtifact;
import mecsycoscholar.application.ode.model.NetLogoModelArtifact;

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
	public final static double maxSimulationTime = 1000;

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
		ObsModelArtifact.addObservingArtifact(new LiveXYGraphic("XY", "X", "Y", Renderer.Line, "X,Y", "X,Y"));

		// create the coupling artifacts
		CentralizedEventCouplingArtifact couplingFromPredatorToPrey = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact couplingFromPreyToPredator = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact couplingFromPredatorToObs = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact couplingFromPreyToObs = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact couplingFromPredatorToMult = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact couplingFromPreyToMult = new CentralizedEventCouplingArtifact();

		CentralizedEventCouplingArtifact couplingFromMultToObs = new CentralizedEventCouplingArtifact();

		// create the multiplexor
		XYStateAggregationOperation XYState = new XYStateAggregationOperation("X", "Y");
		AggregationMultiplexer mult = new AggregationMultiplexer("XYStateMultiplexor", XYState);

		// link the coupling artifacts with the m-agents
		preyAgent.addInputCouplingArtifact(couplingFromPredatorToPrey, "Y");
		preyAgent.addOutputCouplingArtifact(couplingFromPreyToPredator, "X");
		preyAgent.addOutputCouplingArtifact(couplingFromPreyToObs, "X");
		preyAgent.addOutputCouplingArtifact(couplingFromPreyToMult, "X");

		predatorAgent.addOutputCouplingArtifact(couplingFromPredatorToPrey, "Y");
		predatorAgent.addInputCouplingArtifact(couplingFromPreyToPredator, "X");
		predatorAgent.addOutputCouplingArtifact(couplingFromPredatorToObs, "Y");
		predatorAgent.addOutputCouplingArtifact(couplingFromPredatorToMult, "Y");

		mult.addInput(couplingFromPredatorToMult, "Y");
		mult.addInput(couplingFromPreyToMult, "X");
		mult.addOutput(couplingFromMultToObs, "X,Y");

		obsAgent.addInputCouplingArtifact(couplingFromPreyToObs, "X");
		obsAgent.addInputCouplingArtifact(couplingFromPredatorToObs, "Y");
		obsAgent.addInputCouplingArtifact(couplingFromMultToObs, "X,Y");

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
		mult.start();

		predatorAgent.coInitialize();
		preyAgent.coInitialize();

		preyAgent.start();
		predatorAgent.start();
		obsAgent.start();

	}
}
