/*
V. Chevrier, L. Ciarletta, V. Elvinger, Copyright (c) Université de
Lorraine & INRIA, Affero GPL license v3, 2014-2015

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

package mecsycoscholar.application.gettingstarted.model;

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
import org.nlogo.lite.InterfaceComponent;
import org.nlogo.window.InvalidVersionException;
import org.nlogo.window.SpeedSliderPanel;
import org.nlogo.window.TickCounterLabel;

import mecsyco.core.exception.UnexpectedTypeException;
import mecsyco.core.model.ModelArtifact;
import mecsyco.core.type.ArrayedSimulVector;
import mecsyco.core.type.SimulData;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.SimulVector;
import mecsyco.observing.swing.util.InitJFrame;
import mecsyco.world.netlogo.type.NetLogoTurtle;

/**
 * Simple artifact dedicated to a random walk example.
 *
 *
 */
public class SimpleRandomWalkModelArtifact extends ModelArtifact {

	// Implementation
	/**
	 * X position of the next frame.
	 */
	private static int windowX = 0;

	/**
	 * Y position of the next frame.
	 */
	private static int windowY = 0;

	/**
	 * Frame width.
	 */
	private final int windowWidth;

	/**
	 * Frame height.
	 */
	private final int windowHeight;

	/**
	 * Frame.
	 */
	private final JFrame frame;

	/**
	 * Time step.
	 */
	private double timeDiscretization;

	/**
	 * Time of the last internal event processed.
	 */
	private double eventTime;

	/**
	 * Model path.
	 *
	 */
	private final String modelAccessDescription;

	/**
	 * Frame name.
	 */
	private final String frameName;

	/**
	 * Interface with the NetLogo model instance.
	 */
	private final InterfaceComponent comp;

	/**
	 * Color parameter
	 */
	private double color;

	/**
	 * Size parameter
	 */
	private double size;

	/**
	 * Variable set to true when the frame is closed, enable to stop simulation.
	 */
	private boolean guiStop;

	/**
	 * Set it to true to automatically close the windows at the end of simulation.
	 */
	private boolean autoGuiStop;

	/**
	 * To wait frame closure before releasing all resources.
	 */
	private Semaphore waitGui;

	// Creation
	/**
	 *
	 * @param aModelPath    Non null path to model
	 * @param aFrameName    Non null simulator name (use for frame name too)
	 * @param aWindowWidth  natural value
	 * @param aWindowHeight natural value
	 * @param aColor        color of the turtles
	 * @param aSize         size of the turtles
	 */
	public SimpleRandomWalkModelArtifact(String aModelPath, String aFrameName, int aWindowWidth, int aWindowHeight,
			double aSize, double aColor) {
		super(aFrameName);
		modelAccessDescription = aModelPath;
		frameName = aFrameName;
		windowHeight = aWindowHeight;
		windowWidth = aWindowWidth;
		eventTime = 0;
		timeDiscretization = 1; // Relation between NetLogo tick, and co-simulation time

		// create a new "light" instance
		frame = new JFrame();
		comp = new InterfaceComponent(frame);

		color = aColor;
		size = aSize;
		guiStop = false;
		waitGui = new Semaphore(0);
	}
	
	// Deferred initialization
	@Override
	public void initialize() {
		openModel();
	}

	@Override
	public void setInitialParameters(String[] args) {
		// Set the parameters before calling the setup method of this model
		execCmd("set g-size " + size);
		execCmd("set g-color " + color);
		execCmd("setup");
	}

	// Model
	@Override
	public double getLastEventTime() {
		return eventTime;
	}

	@Override
	public double getNextInternalEventTime() {
		if (!guiStop) {
			// in order to schedule an event at simulation time 0
			double ticks = ((Double) comp.report("ticks")) * timeDiscretization;
			if (ticks > 0) {
				return ticks + timeDiscretization;
			}
		} else {
			return Double.MAX_VALUE; // Stop the simulation
		}
		return 0;
	}

	@Override
	public void processInternalEvent(double time) {
		// run the go procedure (here our policy is to run only one-by-one)
		execCmd("go");
		eventTime = time;
	}

	@Override
	public void processExternalInputEvent(SimulEvent event, String port) {
		if ("in".equals(port)) {
			final SimulData data = event.getData();

			if (data instanceof SimulVector) {
				final SimulVector<?> candidates = (SimulVector<?>) data;

				// Avoid to interpret a command when there is no turtles.
				if (!candidates.isEmpty()) {
					for (Object candidate : candidates) {
						if (candidate instanceof SimulVector) {
							final SimulVector<?> t = (SimulVector<?>) candidate;
							// For each received sub SimulVector, we create a turtle
							execCmd("create-turtles 1 [set size " + t.item(0) + " set color " + t.item(1) + "] ");
						}
					}
				}
			} else {
				getLogger().warn("Input port 'in' ",
						new UnexpectedTypeException(SimulVector.of(NetLogoTurtle.class), data.getClass()));
			}
		} else {
			getLogger().warn("The input port '{}' is not supported", port);
		}
	}

	@Override
	public @Nullable SimulEvent getExternalOutputEvent(String port) {
		SimulEvent result = null;

		if ("out".equals(port)) {
			// We want to send all the hidden turtles in the middle another model,
			// then we will send a list of turtles' attributes requested to re-create them
			// (size, color)

			// We get all the hidden turtles
			AgentSet walkers = (AgentSet) comp.report("turtles with [hidden?]");
			if (walkers.count() > 0) {
				// We create a SilulVector, a Mecsyco type corresponding to a list.
				// Each element will be another SimulVector containing the size of a turtle, and
				// its color
				SimulVector<SimulVector<String>> data = new ArrayedSimulVector<SimulVector<String>>();
				Turtle item;
				Iterator<Agent> iterator = walkers.agents().iterator();
				while (iterator.hasNext()) {
					item = (Turtle) iterator.next();
					SimulVector<String> attributes = new ArrayedSimulVector<String>("" + item.size(),
							"" + item.color());
					data = data.appended(attributes);
				}
				result = new SimulEvent(data, eventTime);
			}
		} else {
			getLogger().warn("The output port '{}' is not supported", port);
		}

		return result;
	}

	@Override
	public void finishSimulation() {
		getLogger().info("end simulation at {}", getLastEventTime());
		if (autoGuiStop) {
			closeWindows();
		}
		try {
			waitGui.acquire(); // Only the gui releases this when closed. Then gui is updated until closure.
			// Ensure that all related threads are closed.
			// Clear everything, seems to prevent blocking when Netlogo error is raised.
			comp.world().clearAll();
			comp.workspace().clearAll();
			// Release resource, doesn't work on debug due to JVM bug.
			comp.workspace().dispose();
			frame.dispose(); // Force the frame to dispose if something blocked it (normally no effect).
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Service
	/**
	 * Attempt to run {@code aCommand}.
	 *
	 * @param cmd NetLogo command
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
					frame.setTitle(frameName);
					frame.setLocation(windowX, windowY);
					windowX += windowWidth;
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

	/**
	 * Method to close a frame, it is used to end JUnit test of this model artifact.
	 * Send a window closing event to the frame.
	 */
	public void closeWindows() {
		frame.dispatchEvent(new WindowEvent(this.frame, WindowEvent.WINDOW_CLOSING));
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
