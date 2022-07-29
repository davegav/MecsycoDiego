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

package mecsycoscholar.application.sheepgrass.model;

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

import mecsyco.core.model.ModelArtifact;
import mecsyco.core.type.ArrayedSimulVector;
import mecsyco.core.type.SimulData;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.SimulVector;
import mecsyco.observing.swing.util.InitJFrame;
import mecsycoscholar.application.sheepgrass.type.SheepEnergy;
import mecsycoscholar.application.sheepgrass.type.SheepShepherd;

/**
 *
 *
 *
 *
 */
public class SheepModelArtifact extends ModelArtifact {

	// Implementation
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
	 * Set it to true to automatically close the windows at the end of simulation.
	 */
	private boolean autoGuiStop;

	/**
	 * To wait frame closure before releasing all resources.
	 */
	private final Semaphore waitGui;

	// Creation
	public SheepModelArtifact(String aModelPath, String aFrameName, int aWindowWidth, int aWindowHeight) {

		super("sheep." + aFrameName);
		modelAccessDescription = aModelPath;
		frameName = aFrameName;
		windowHeight = aWindowHeight;
		windowWidth = aWindowWidth;
		eventTime = 0;
		timeDiscretization = 1;

		// create a new "light" instance
		frame = new JFrame();
		comp = new InterfaceComponent(frame);

		waitGui = new Semaphore(0);
	}

	// Deferred initialization
	@Override
	public void initialize() {
		openModel();
		execCmd("setup");
	}

	@Override
	public void setInitialParameters(String[] args) {
	}

	// Model
	@Override
	public double getLastEventTime() {
		return eventTime;
	}

	@Override
	public double getNextInternalEventTime() {
		// Stop the influence of this model when the frame is closed
		if (frame.isDisplayable()) {
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
		if (frame.isDisplayable()) {
			final SimulData data = event.getData();

			if (data instanceof SimulVector) {
				final SimulVector<?> candidates = (SimulVector<?>) data;

				String cmd = "";
				for (final Object candidate : candidates) {
					if (candidate instanceof SheepEnergy) {
						final SheepEnergy s = (SheepEnergy) candidate;

						cmd = cmd + "ask a-sheep  " + s.getId() + " [ set energy " + s.getEnergy() + "] ";
					}
				}

				execCmd(cmd);
			}
		}
	}

	@Override
	public @Nullable SimulEvent getExternalOutputEvent(String port) {
		SimulVector<SheepShepherd> sheeps;
		final @Nullable SimulEvent result;

		@Nullable
		AgentSet items = null;
		items = (AgentSet) comp.report("sheep");

		if (items != null) {
			Turtle item;

			sheeps = new ArrayedSimulVector<>();
			Iterator<Agent> iterator = items.agents().iterator();
			while (iterator.hasNext()) {
				item = (Turtle) iterator.next();
				sheeps = sheeps.appended(asSheepShepherdModelInfo(item));
			}

			result = new SimulEvent(sheeps, eventTime);
		} else {
			result = null;
		}

		return result;
	}

	@Override
	public void finishSimulation() {
		getLogger().info("end simulation at time {}", getLastEventTime());
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
	 * Attempt to run `aCommand'.
	 *
	 * @param cmd - NetLogo command
	 */
	protected void execCmd(String cmd) {
		comp.command(cmd);
	}

	protected SheepShepherd asSheepShepherdModelInfo(Turtle aTarget) {
		Boolean isCarried = ((Boolean) aTarget.getVariable(14)).booleanValue();
		return new SheepShepherd(aTarget.id(), aTarget.xcor(), aTarget.ycor(), (Double) aTarget.color(),
				aTarget.heading(), aTarget.size(), isCarried.booleanValue());
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