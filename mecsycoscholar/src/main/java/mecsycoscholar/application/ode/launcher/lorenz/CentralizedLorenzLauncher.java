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
package mecsycoscholar.application.ode.launcher.lorenz;

import javax.swing.JFrame;

import mecsyco.core.agent.EventMAgent;
import mecsyco.core.coupling.CentralizedEventCouplingArtifact;
import mecsyco.observing.swing.util.InitJFrame;
import mecsycoscholar.application.ode.model.NetLogoModelArtifact;

/**
 * Launch a distributed simulation of a Lorenz system : dx/dt = a*(y-x) dy/dt =
 * x*(b-z) dz/dt = x*y - c*z
 *
 * Each ODE is simulated by one NetLogo instance: dx/dt: Lorenz-X.nlogo dy/dt:
 * Lorenz-Y.nlogo dz/dt: Lorenz-Z.nlogo
 *
 * A monolithic Netlogo simulation of the system can be found in the file
 * Lorenz.nlogo
 *
 *
 */

public class CentralizedLorenzLauncher {

	// set the global simulation time
	public final static double maxSimulationTime = 80;

	// set the step size of the X model (between {0.01,0.001,0.0001})
	public final static double time_discretization_X = 0.01;

	// set the step size of the Y model (between {0.01,0.001,0.0001})
	public final static double time_discretization_Y = 0.01;

	// set the step size of the Z model (between {0.01,0.001,0.0001})
	public final static double time_discretization_Z = 0.01;

	public static void main(String args[]) {
		InitJFrame.setDefaultCloseOperationJFrame(JFrame.DISPOSE_ON_CLOSE);

		// create the model agent for the X model
		EventMAgent XAgent = new EventMAgent("agent_x", maxSimulationTime);
		// create the model artifact and link it with the agent
		NetLogoModelArtifact XModelArtifact = new NetLogoModelArtifact("My Models/netlogo/Lorenz-X.nlogo",
				"Evolution of the intensity of the convection motion");
		XAgent.setModelArtifact(XModelArtifact);

		// create the model agent for the Y model
		EventMAgent YAgent = new EventMAgent("agent_y", maxSimulationTime);
		// create the model artifact and link it with the agent
		NetLogoModelArtifact YModelArtifact = new NetLogoModelArtifact("My Models/netlogo/Lorenz-Y.nlogo",
				"Evolution of the temperature difference between the ascending and descending currents");
		YAgent.setModelArtifact(YModelArtifact);

		// create the model agent for the Z model
		EventMAgent ZAgent = new EventMAgent("agent_z", maxSimulationTime);
		// create the model artifact and link it with the agent
		NetLogoModelArtifact ZModelArtifact = new NetLogoModelArtifact("My Models/netlogo/Lorenz-Z.nlogo",
				"Evolution of the distortion of the vertical temperature from linearity");
		ZAgent.setModelArtifact(ZModelArtifact);

		// create the coupling artifacts
		CentralizedEventCouplingArtifact XOutputToZ = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact XOutputToY = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact YOutputToX = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact YOutputToZ = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact ZOutput = new CentralizedEventCouplingArtifact();

		// set the output link
		XAgent.addOutputCouplingArtifact(XOutputToZ, "X");
		XAgent.addOutputCouplingArtifact(XOutputToY, "X");

		YAgent.addOutputCouplingArtifact(YOutputToX, "Y");
		YAgent.addOutputCouplingArtifact(YOutputToZ, "Y");

		ZAgent.addOutputCouplingArtifact(ZOutput, "Z");

		// set the input link
		XAgent.addInputCouplingArtifact(YOutputToX, "Y");

		YAgent.addInputCouplingArtifact(XOutputToY, "X");
		YAgent.addInputCouplingArtifact(ZOutput, "Z");

		ZAgent.addInputCouplingArtifact(XOutputToZ, "X");
		ZAgent.addInputCouplingArtifact(YOutputToZ, "Y");

		// initialize the model
		XAgent.startModelSoftware();
		YAgent.startModelSoftware();
		ZAgent.startModelSoftware();

		// set model parameters
		String argX[] = { String.valueOf(time_discretization_X) };
		String argY[] = { String.valueOf(time_discretization_Y) };
		String argZ[] = { String.valueOf(time_discretization_Z) };
		XAgent.setModelParameters(argX);
		ZAgent.setModelParameters(argZ);
		YAgent.setModelParameters(argY);

		// send the initial value of each models
		XAgent.coInitialize();
		YAgent.coInitialize();
		ZAgent.coInitialize();

		XAgent.start();
		YAgent.start();
		ZAgent.start();

	}
}
