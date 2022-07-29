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

package mecsycoscholar.application.trafficgrid.model;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

import javax.swing.JFrame;

import org.nlogo.agent.AgentSet;
import org.nlogo.agent.Turtle;
import org.nlogo.api.Agent;
import org.nlogo.lite.InterfaceComponent;
import org.nlogo.window.InvalidVersionException;
import org.nlogo.window.SpeedSliderPanel;
import org.nlogo.window.TickCounterLabel;

import mecsyco.core.model.ModelArtifact;
import mecsyco.core.type.ArrayedSimulVector;
import mecsyco.core.type.SimulData;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.SimulVector;
import mecsyco.observing.swing.util.InitJFrame;

/**
 * This class represents a Netlogo Model Artifact which: - adds turtles in the
 * model. They are four input ports 1 is for adding turtles from the left, 2
 * from the right, 3 from the top and 4 from the bottom - gets hidden turtles of
 * the model (the model's hidden turtles corresponds to turtles getting outside
 * of the model). They are four output ports 1 is for getting turtles coming
 * from the left, 2 from the right, 3 from the top and 4 from the bottom
 *
 *
 *
 */
public class GridModelArtifact extends ModelArtifact {

	// Implementation
	/**
	 * Frame for NetLogo.
	 */
	private final JFrame frame;

	/**
	 * Frame width.
	 */
	private static final int WINDOW_WIDTH = 400;

	/**
	 * Frame height.
	 */
	private static final int WINDOW_HEIGHT = 400;

	/**
	 * Time between each NetLogo step.
	 */
	private double timeDiscretization;

	/**
	 * a path or a string corresponding to the model according to the kind of
	 * "launch"
	 *
	 */
	private String modelAccessDescription;

	/**
	 * Name of the frame.
	 */
	private final String frameName;

	/**
	 * Interface component with NetLogo simulator.
	 */
	private final InterfaceComponent comp;

	/**
	 * Time of the current event.
	 */
	private double eventTime;

	/**
	 * Set it to true to automatically close the windows at the end of simulation.
	 */
	private boolean autoGuiStop;

	/**
	 * To wait frame closure before releasing all resources.
	 */
	private final Semaphore waitGui;

	// Creation
	/**
	 *
	 * @param modelPath  Path to the traffic grid NetLogo model
	 * @param aFrameName Name of the frame.
	 */
	public GridModelArtifact(String modelPath, String aFrameName) {

		super("grid." + aFrameName);
		modelAccessDescription = modelPath;
		frameName = aFrameName;
		eventTime = 0;
		timeDiscretization = 1;

		// create a new "light" instance
		frame = new JFrame(frameName);
		comp = new InterfaceComponent(frame);
		waitGui = new Semaphore(0);
	}

	// Deferred initialization
	/**
	 * STEP0 :Create a simulator instance, load the model and initiate it.
	 */
	@Override
	public void initialize() {
		openModel();
		execCmd("setup");
	}

	/**
	 * STEP1 : Set the initial parameters.
	 *
	 * @param args the initial parameters.
	 */
	@Override
	public void setInitialParameters(String[] args) {
	}

	// Model
	/**
	 * @return Time of the last event
	 */
	@Override
	public double getLastEventTime() {
		return eventTime;
	}

	/**
	 *
	 * @return Time of the next event
	 */
	@Override
	public double getNextInternalEventTime() {
		if (frame.isDisplayable()) {
			double ticks = ((Double) (comp.report("ticks"))) * timeDiscretization;
			// in order to schedule an event at simulation time 0;
			if (ticks != 0) {
				ticks = ticks + timeDiscretization;
			}
			return ticks;
		} else {
			return Double.MAX_VALUE;
		}

	}

	/**
	 * Run one simulation step or event
	 */
	@Override
	public void processInternalEvent(double time) {
		// run the go procedure (here our policy is to run only one-by-one)
		execCmd("go");
		eventTime = time;
	}

	/**
	 * Set the model input data into the corresponding port.
	 *
	 * @param event the external event
	 * @param port  the port where the event has to be send (here we assume that
	 *              there are ports 1,2 and 3 respectively correspond to the value
	 *              of variables "X","Y" and "Z")
	 */
	@Override
	public void processExternalInputEvent(SimulEvent event, String port) {
		if (frame.isDisplayable()) {
			final SimulData data = event.getData();

			if (data instanceof SimulVector) {
				final SimulVector<?> candidates = (SimulVector<?>) data;

				String cmd = "";
				for (final Object candidate : candidates) {
					if (candidate instanceof SimulVector<?>) {
						final SimulVector<?> ct = (SimulVector<?>) candidate;

						switch (port) {
						case ("left_in"):
							cmd += "create-cars 1 [set shape \"car\" setxy min-pxcor "
									+ ct.item(1) + " set heading "
									+ ct.item(3) + " set color "
									+ ct.item(4) + " set up-car? false ] ";
							break;
						case ("right_in"):
							cmd += "create-cars 1 [set shape \"car\" setxy max-pxcor "
									+ ct.item(1) + " set heading "
									+ ct.item(3) + " set color "
									+ ct.item(4) + " set up-car? false ] ";
							break;
						case ("up_in"):
							cmd += "create-cars 1 [set shape \"car\" setxy "
									+ ct.item(0) + " max-pycor set heading "
									+ ct.item(3) + " set color "
									+ ct.item(4) + " set up-car? true] ";
							break;
						case ("down_in"):
							cmd += "create-cars 1 [set shape \"car\" setxy "
									+ ct.item(0) + " min-pycor set heading "
									+ ct.item(3) + " set color "
									+ ct.item(4) + " set up-car? true] ";
							break;
						default:
							getLogger().warn("The input port '{" + port + "}' is not supported");
						}
					}
				}

				if (!cmd.isEmpty()) {
					execCmd(cmd);
				} else {
					getLogger().warn("The input port '{" + port + "}' is not supported");
				}
			} else {
				getLogger().warn("Data type is not supported", port);
			}
		}
	}

	/**
	 * Get the model output event (to exchange with another model)
	 *
	 * @param port the model output port where data has to be collected (here we
	 *             assume that there are ports 1,2 and 3 respectively correspond to
	 *             the value of variables "X","Y" and "Z")
	 * @return the model event for the port
	 */
	@Override
	public SimulEvent getExternalOutputEvent(String port) {
		// the sheep positions
		SimulVector<SimulVector<String>> turtleList = new ArrayedSimulVector<SimulVector<String>>();
		Turtle agt;

		// the list of agents (the sheep)
		AgentSet listWalkers;
		SimulEvent ret = null;

		// get the list of sheep from netlogo
		final String cmd;
		switch (port) {
		case ("left_out"):
			cmd = "turtles with [(hidden?)  and (not up-car?)]";
			break;
		case ("right_out"):
			cmd = "turtles with [(hidden?)  and (not up-car?)]";
			break;
		case ("up_out"):
			cmd = "turtles with [(hidden?)  and (up-car?)]";
			break;
		case ("down_out"):
			cmd = "turtles with [(hidden?)  and (up-car?)]";
			break;
		default:
			cmd = "";
			break;
		}

		if (!cmd.isEmpty()) {

			listWalkers = (AgentSet) comp.report(cmd);

			// check if the agent set is not empty
			if (!listWalkers.isEmpty()) {

				// // for each sheep get the position and set the
				// // SheepPosition object
				Iterator<Agent> iterator = listWalkers.agents().iterator();
				while (iterator.hasNext()) {
					// get the agent
					agt = (Turtle) iterator.next();
					turtleList = turtleList.appended(new ArrayedSimulVector<String>("" + agt.xcor(), "" + agt.ycor(),
							"" + agt.id(), "" + agt.heading(),
							"" + agt.color()));
				}
				ret = new SimulEvent(turtleList, eventTime);
			}
		} else {
			getLogger().warn("The output port '{}' is not supported", port);
		}

		return ret;
	}

	/**
	 * Do something when simulation is done
	 */
	@Override
	public void finishSimulation() {
		getLogger().info("Model " + frameName + " end simulation at time : " + getLastEventTime());
		if (autoGuiStop) {
			closeWindows();
		}
		try {
			waitGui.acquire(); // Wait the closure of the GUI to release resources.
			comp.workspace().jobManager.die(); // Prevent from blocking in case of NetLogo error
			comp.workspace().dispose(); // release resources
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to close a frame, it is used to end JUnit test of this model artifact.
	 * Send a window closing event to the frame.
	 */
	public void closeWindows() {
		frame.dispatchEvent(new WindowEvent(this.frame, WindowEvent.WINDOW_CLOSING));
	}

	// Service
	/**
	 * Execute the NetLogo command
	 *
	 * @param cmd the command to be executed in NetLogo
	 */
	protected void execCmd(String cmd) {
		comp.command(cmd);
	}

	// Model displaying
	protected void openModel() {
		try {
			EventQueue.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
					frame.setDefaultCloseOperation(InitJFrame.getDefaultCloseOperationJFrame());
					frame.addWindowListener(new WindowAdapter() {

						@Override
						public void windowClosing(WindowEvent e) {
							// We call the current default operation
							super.windowClosing(e);
							waitGui.release();
						}
					});
					frame.setLayout(new BorderLayout());
					frame.setContentPane(comp);
					frame.setTitle(frameName);
					frame.setVisible(true);

					// Add the tick counter and the speed slider to the gui.
					try {
						comp.open(modelAccessDescription);
						TickCounterLabel t = new TickCounterLabel(comp.world());
						SpeedSliderPanel s = new SpeedSliderPanel(comp.workspace());
						frame.getContentPane().add(s, BorderLayout.NORTH);
						frame.getContentPane().add(t, BorderLayout.SOUTH);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InvalidVersionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} catch (InterruptedException e) {
			getLogger().error("", e);
		} catch (InvocationTargetException e) {
			getLogger().error("", e);
		}
	}

	/**
	 *
	 * @return True if the GUI will automatically be closed at the end.
	 */
	public boolean isAutoGuiStop() {
		return autoGuiStop;
	}

	/**
	 *
	 * @param autoStop Set to true to automatically close the GUI at the end of
	 *                 simulation. It will call {@link #closeWindows()} in
	 *                 {@link #finishSimulation()}.
	 */
	public void setAutoGuiStop(boolean autoStop) {
		autoGuiStop = autoStop;
	}

}
