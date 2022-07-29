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

package mecsycoscholar.application.highway.model.mas;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

import javax.annotation.Nullable;
import javax.swing.JFrame;

import org.nlogo.agent.AgentSet;
import org.nlogo.agent.Turtle;
import org.nlogo.api.Agent;
import org.nlogo.api.AgentException;
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
 * from the left, 2 from the right, 3 from the top and 4 from the bottom.
 *
 *
 * Alternative
 * {@link mecsyco.world.netlogo.NetLogoModelArtifact.NetLogoModelArtifact}
 */
public class MicroModelArtifact extends ModelArtifact {

	// Implementation
	/**
	 * Width of the frame.
	 */
	private final int windowWidth;

	/**
	 * Height of the frame.
	 */
	private final int windowHeight;

	/**
	 * Time step between each internal event.
	 */
	private double timeDiscretization;

	/**
	 * a path or a string corresponding to the model according to the kind of
	 * "launch"
	 */
	private final String modelAccessDescription;

	/**
	 * Name of the frame.
	 */
	private final String frameName;

	/**
	 * Interface with NetLogo.
	 */
	private final InterfaceComponent comp;

	/**
	 * Frame.
	 */
	private final JFrame frame;

	/**
	 *
	 */
	private double eventTime;

	/**
	 * X position of the next frame.
	 */
	private static int windowX = 0;

	/**
	 * Y position of the next frame.
	 */
	private static int windowY = 0;

	/**
	 * Variable set to true when the frame is closed, enable to stop simulation.
	 */
	private boolean guiStop;

	/**
	 * To wait frame closure before releasing all resources.
	 */
	private Semaphore waitGui;

	// Creation
	/**
	 *
	 * @param modelPath
	 * @param name
	 * @param height
	 * @param width
	 */
	public MicroModelArtifact(String modelPath, String name, int height, int width) {
		super(name);
		modelAccessDescription = modelPath;
		frameName = name;
		windowHeight = height;
		windowWidth = width;
		eventTime = 0;
		timeDiscretization = 1;

		// create a new "light" instance
		frame = new JFrame();
		comp = new InterfaceComponent(frame);
		guiStop = false;
		waitGui = new Semaphore(0);
	}

	/**
	 *
	 * @param modelPath
	 * @param name
	 * @param height
	 * @param width
	 * @param x
	 * @param y
	 */
	public MicroModelArtifact(String modelPath, String name, int height, int width, int x, int y) {
		this(modelPath, name, height, width);
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
		if (!guiStop) {
			double ticks;
			ticks = ((Double) (comp.report("ticks"))) * timeDiscretization;
			// in order to schedule an event at simulation time 0;
			if (ticks != 0) {
				ticks = ticks + timeDiscretization;
			}
			return ticks;
		} else {
			// GUI is closed, so close the model by sending a value > stopTime
			// The the fiishSimulation() method will be called to release all resources.
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
		final SimulData data = event.getData();

		if ("in".equals(port)) {
			if (data instanceof SimulVector) {
				final SimulVector<?> candidates = (SimulVector<?>) data;
				String cmd = "";
				for (final Object candidate : candidates) {
					if (candidate instanceof SimulVector) {
						final SimulVector<?> car = (SimulVector<?>) candidate;

						// Only one port (x, y, heading, speed)
						cmd += "create-cars 1 [set shape \"car\" setxy min-pxcor 0"
								+ " set heading " + car.item(0) + " set color blue set speed  "
								+ car.item(1) + " set speed-limit  1 set speed-min  0 ] ";
					}
				}
				if (!cmd.isEmpty()) {
					execCmd(cmd);
				}
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
	public @Nullable SimulEvent getExternalOutputEvent(String port) {
		// the sheep positions
		SimulVector<SimulVector<String>> turtleList = new ArrayedSimulVector<SimulVector<String>>();

		// the list of agents (the sheep)
		AgentSet listWalkers;
		@Nullable
		SimulEvent result = null;
		try {

			// get the list of sheep from netlogo
			String cmd = "";
			if ("out".contentEquals(port)) {
				cmd = "turtles with [not hidden?]";
			}

			listWalkers = (AgentSet) comp.report(cmd);

			// check if the agent set is not empty
			if (!listWalkers.isEmpty()) {
				// for each turtle create a SimulVector
				Iterator<Agent> iterator = listWalkers.agents().iterator();
				while (iterator.hasNext()) {
					// get the agent
					Turtle agt = (Turtle) iterator.next();
					// get the agent speed
					Double speed = (Double) agt.getVariable("SPEED");

					SimulVector<String> tortuga = new ArrayedSimulVector<String>("" + agt.xcor(), "" + agt.ycor(),
							"" + agt.heading(),
							"" + speed);
					turtleList = turtleList.appended(tortuga);
				}
			}

			result = new SimulEvent(turtleList, eventTime);
		} catch (AgentException e) {
			getLogger().error("", e);
		}

		return result;
	}

	/**
	 * Do something when simulation is done
	 */
	@Override
	public void finishSimulation() {
		try {
			waitGui.acquire(); // Only the gui releases this when closed. Then gui is updated until closure.
			// Ensure that all related threads are closed.
			comp.workspace().dispose();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		getLogger().info("Model " + frameName + " end simulation at time : " + getLastEventTime());
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
	/**
	 *
	 */
	protected void openModel() {
		try {
			EventQueue.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					frame.setSize(windowWidth, windowHeight);
					frame.setDefaultCloseOperation(InitJFrame.getDefaultCloseOperationJFrame());
					frame.addWindowListener(new WindowAdapter() {

						@Override
						public void windowClosing(WindowEvent e) {
							// We call the current default operation
							super.windowClosing(e);
							guiStop = true;
							waitGui.release();
						}
					});
					frame.setLayout(new BorderLayout());
					frame.setContentPane(comp);
					frame.setLocation(windowX, windowY);
					windowX += windowWidth;
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
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
