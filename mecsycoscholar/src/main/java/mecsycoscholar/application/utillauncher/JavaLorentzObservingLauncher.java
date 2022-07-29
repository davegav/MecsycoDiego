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
import mecsyco.core.operation.aggregation.XYZStateAggregationOperation;
import mecsyco.observing.jfreechart.xy.LiveTXGraphic;
import mecsyco.observing.jfreechart.xy.Renderer;
import mecsyco.observing.jzy3d.graphic.Live3DGraphic;
import mecsyco.observing.swing.dispatcher.SwingDispatcherArtifact;
import mecsycoscholar.application.ode.model.EquationModelArtifact;
import mecsycoscholar.application.ode.model.LorenzX;
import mecsycoscholar.application.ode.model.LorenzY;
import mecsycoscholar.application.ode.model.LorenzZ;
import mecsycoscholar.helper.Utils;



/**
 * Launch a distributed simulation of a Lorenz system : dx/dt = a*(y-x) dy/dt = x*(b-z) dz/dt = x*y
 * - c*z
 * 
 * Each ODE is simulated by one NetLogo instance: dx/dt: Lorenz-X.nlogo dy/dt: Lorenz-Y.nlogo dz/dt:
 * Lorenz-Z.nlogo
 * 
 * A monolithic Netlogo simulation of the system can be found in the file Lorenz.nlogo
 * 
 * 
 */

public class JavaLorentzObservingLauncher {

	// set the global simulation time
	public final static double maxSimulationTime = 10;

	public final static double init_X = 1;

	public final static double init_Y = 1;

	public final static double init_Z = 4;

	public final static double A = 10;

	public final static double B = 28;

	public final static double C = 2.67;

	// set the step size of the X model (between {0.01,0.001,0.0001})
	public final static double time_discretization_X = 0.01;

	// set the step size of the Y model (between {0.01,0.001,0.0001})
	public final static double time_discretization_Y = 0.01;

	// set the step size of the Z model (between {0.01,0.001,0.0001})
	public final static double time_discretization_Z = 0.01;

	// set the size of the NetLogo Frame.
	public final static int height = 510;

	public final static int width = 460;

	public static void main (String args[]) {

		// create the models
		LorenzX modelX = new LorenzX(init_X, A, time_discretization_X);
		LorenzY modelY = new LorenzY(init_Y, B, time_discretization_Y);
		LorenzZ modelZ = new LorenzZ(init_Z, C, time_discretization_Z);

		// create the model agent for the X model
		EventMAgent XAgent = new EventMAgent("agent_x", maxSimulationTime);
		// create the model artifact and link it with the agent

		EquationModelArtifact XModelArtifact = new EquationModelArtifact(
				"Evolution of the intensity of the convection motion", modelX);
		XAgent.setModelArtifact(XModelArtifact);

		// create the model agent for the Y model
		EventMAgent YAgent = new EventMAgent("agent_y", maxSimulationTime);
		// create the model artifact and link it with the agent
		EquationModelArtifact YModelArtifact = new EquationModelArtifact(
				"Evolution of the temperature difference between the ascending and descending currents", modelY);
		YAgent.setModelArtifact(YModelArtifact);

		// create the model agent for the Z model
		EventMAgent ZAgent = new EventMAgent("agent_z", maxSimulationTime);
		// create the model artifact and link it with the agent
		EquationModelArtifact ZModelArtifact = new EquationModelArtifact(
				"Evolution of the distortion of the vertical temperature from linearity", modelZ);
		ZAgent.setModelArtifact(ZModelArtifact);

		// create the model agent for the observing model
		ObservingMAgent obsAgent = new ObservingMAgent("agent_obs", maxSimulationTime);
		// create the observing model artifact and link it with the agent
		SwingDispatcherArtifact ObsModelArtifact = new SwingDispatcherArtifact();
		obsAgent.setDispatcherArtifact(ObsModelArtifact);

		// create graphics

		ObsModelArtifact.addObservingArtifact(new Live3DGraphic("Lorentz 3D", "Xaxis", "Yaxis", "Zaxis", "X,Y,Z"));

		ObsModelArtifact.addObservingArtifact(new LiveTXGraphic("title", "y", Renderer.Step, new String[] { "Series1",
				"Serie2", "Serie3" },
				new String[] { "X", "Y", "Z" }));

//		ObsModelArtifact.addObservingArtifact(new DataComparator(new String[] { "resources/model_checking/lorenzLog_x.csv", "resources/model_checking/lorenzLog_y.csv", "resources/model_checking/lorenzLog_z.csv" },
//				new String[] { "X", "Y", "Z" }));

		//New multiplexer
		XYZStateAggregationOperation ope=new XYZStateAggregationOperation("X","Y","Z" );
		AggregationMultiplexer Mult = new AggregationMultiplexer("Mult", ope);

		//links between agent
		Utils.connect(XAgent, ZAgent, "X");
		Utils.connect(XAgent, YAgent, "X");
		Utils.connect(YAgent, XAgent, "Y");
		Utils.connect(YAgent, ZAgent, "Y");
		Utils.connect(ZAgent, YAgent, "Z");

		//links with obs
		Utils.connect(XAgent, obsAgent, "X");
		Utils.connect(YAgent, obsAgent, "Y");
		Utils.connect(ZAgent, obsAgent, "Z");

		//link with obs using multiplexer
		Utils.connect(new EventMAgent[]{XAgent,YAgent,ZAgent}, new String[]{"X","Y","Z" }, Mult, obsAgent, "X,Y,Z");

		// initialize the model
		XAgent.startModelSoftware();
		YAgent.startModelSoftware();
		ZAgent.startModelSoftware();
		obsAgent.startModelSoftware();
		
		XAgent.setModelParameters();
		YAgent.setModelParameters();
		ZAgent.setModelParameters();

		// send the initial value of each models
		XAgent.coInitialize();
		YAgent.coInitialize();
		ZAgent.coInitialize();

		XAgent.start();
		YAgent.start();
		ZAgent.start();
		obsAgent.start();
		Mult.start();

	}
}
