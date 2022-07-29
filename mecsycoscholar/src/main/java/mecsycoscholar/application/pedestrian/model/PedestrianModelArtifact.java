/*
V. Chevrier, L. Ciarletta, J. Siebert,
Copyright (c) Université de Lorraine & INRIA, Affero GPL license v3, 2014-2015


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

package mecsycoscholar.application.pedestrian.model;

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

import mecsyco.core.exception.UnexpectedTypeException;
import mecsyco.core.model.ModelArtifact;
import mecsyco.core.type.ArrayedSimulVector;
import mecsyco.core.type.SimulData;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.SimulVector;
import mecsyco.observing.swing.util.InitJFrame;
import mecsyco.world.netlogo.type.NetLogoColorConstants;
import mecsyco.world.netlogo.type.NetLogoTurtle;

public class PedestrianModelArtifact extends ModelArtifact {

	// Implementation
	/**
	 * Frame for NetLogo pedestrian.
	 */
	private final JFrame frame;

	/**
	 * Frame width.
	 */
	private static final int WINDOW_WIDTH = 440;

	/**
	 * Frame height.
	 */
	private static final int WINDOW_HEIGHT = 580;

	/**
	 * Time step.
	 */
	private double timeDiscretization;

	/**
	 * Time of the last internal event processed.
	 */
	private double eventTime;

	/**
	 * Model name.
	 */
	private final String modelName;

	/**
	 * Model path.
	 *
	 */
	private final String modelAccessDescription;

	/**
	 * Interface with the NetLogo model instance.
	 */
	private final InterfaceComponent comp;

	/**
	 * Shape of turtles
	 */
	private NetLogoTurtlePedestrianShape shape;

	/**
	 * Color of turtles
	 */
	private int color;

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
	 * Construct a Pedestrian model artifact.
	 *
	 * @param aModelPath Path to the NetLogo model.
	 * @param aFrameName
	 * @param aColor
	 * @param aShape
	 */
	public PedestrianModelArtifact(String aModelPath, String aFrameName, int aColor,
			NetLogoTurtlePedestrianShape aShape) {
		super(aFrameName);
		modelAccessDescription = aModelPath;
		modelName = aFrameName;
		eventTime = 0;
		timeDiscretization = 1;
		shape = aShape;
		color = aColor;

		guiStop = false;
		autoGuiStop = false;
		waitGui = new Semaphore(0);
		// create a new "light" instance
		frame = new JFrame(aFrameName);
		comp = new InterfaceComponent(frame);
	}

	/**
	 * Construct a Pedestrian model artifact.
	 *
	 * @param aModelPath Path to the NetLogo model.
	 * @param aFrameName
	 */
	public PedestrianModelArtifact(String aModelPath, String aFrameName) {
		this(aModelPath, aFrameName,
				NetLogoColorConstants.GREEN,
				NetLogoTurtlePedestrianShape.bug);
	}

	// Deferred initialization
	@Override
	public void initialize() {
		openModel();
	}

	@Override
	public void setInitialParameters(String[] args) {
		execCmd("set g-shape \"" + shape + "\"");
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
			double ticks = ((Double) comp.report("ticks")) * timeDiscretization;
			// in order to schedule an event at simulation time 0
			if (ticks != 0) {
				ticks = ticks + timeDiscretization;
			}
			return ticks;
		} else {
			return Double.MAX_VALUE;
		}
	}

	@Override
	public void processInternalEvent(double time) {
		// run the go procedure (here our policy is to run only one-by-one)
		execCmd("go");
		eventTime = time;
	}

	@Override
	public void processExternalInputEvent(SimulEvent event, String port) {
		if ("left_in".equals(port)) {
			final SimulData data = event.getData();

			if (data instanceof SimulVector) {
				final SimulVector<?> candidates = (SimulVector<?>) data;

				// Avoid to interpret a command when there is no turtles.
				if (!candidates.isEmpty()) {
					String cmd = "";

					for (Object candidate : candidates) {
						if (candidate instanceof NetLogoTurtle) {
							final NetLogoTurtle t = (NetLogoTurtle) candidate;

							cmd = cmd + "create-pedestrians 1 [set shape g-shape set color " + t.getColor()
									+ " setxy 0 "
									+ t.getY() + " set heading "
									+ t.getHeading() + " set size "
									+ t.getSize() + "] ";
						}
					}
					execCmd(cmd);
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
	public SimulEvent getExternalOutputEvent(String port) {
		SimulEvent result = null;

		if ("right_out".equals(port)) {
			AgentSet pedestrians;
			pedestrians = (AgentSet) comp.report("pedestrians with [hidden?]");
			int nbPedestrian = pedestrians.count();
			if (nbPedestrian > 0) {
				final NetLogoTurtle[] turtles = new NetLogoTurtle[nbPedestrian];
				Turtle item;

				Iterator<Agent> iterator = pedestrians.agents().iterator();
				while (iterator.hasNext()) {
					nbPedestrian--;
					item = (Turtle) iterator.next();
					turtles[nbPedestrian] = new NetLogoTurtle(item.id(), item.xcor(), item.ycor(),
							(Double) item.color(), item
									.heading(),
							item.size(), item.shape());
				}

				result = new SimulEvent(new ArrayedSimulVector<>(turtles), eventTime);
			}
		} else {
			getLogger().warn("The output port '{}' is not supported", port);
		}

		return result;
	}

	@Override
	public void finishSimulation() {
		getLogger().info("Model {} end simulation at {}", modelName, getLastEventTime());
		if (autoGuiStop) {
			closeWindows();
		}
		try {
			waitGui.acquire(); // Wait the gui closure to release all resources.
			comp.workspace().dispose();
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
	 * Attempt to run `aCommand'.
	 *
	 * @param cmd - NetLogo command
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
							guiStop = true;
							waitGui.release();
						}
					});
					frame.setLayout(new BorderLayout());
					frame.setContentPane(comp);
					frame.setVisible(true);
					try {
						comp.open(modelAccessDescription);
						// this does not work when in jar file ....
						TickCounterLabel t = new TickCounterLabel(comp.world());
						SpeedSliderPanel s = new SpeedSliderPanel(comp.workspace());
						frame.getContentPane().add(s, BorderLayout.NORTH);
						frame.getContentPane().add(t, BorderLayout.SOUTH);
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (InvalidVersionException e1) {
						e1.printStackTrace();
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
