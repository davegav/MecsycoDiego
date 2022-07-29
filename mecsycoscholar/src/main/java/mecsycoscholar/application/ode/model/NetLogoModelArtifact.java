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

package mecsycoscholar.application.ode.model;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Semaphore;

import javax.annotation.Nullable;
import javax.swing.JFrame;

import org.nlogo.lite.InterfaceComponent;
import org.nlogo.window.InvalidVersionException;
import org.nlogo.window.SpeedSliderPanel;
import org.nlogo.window.TickCounterLabel;

import mecsyco.core.exception.UnexpectedTypeException;
import mecsyco.core.model.ModelArtifact;
import mecsyco.core.type.SimulData;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.Tuple1;
import mecsyco.core.type.Tuple2;
import mecsyco.core.type.Tuple3;
import mecsyco.observing.swing.util.InitJFrame;

/**
 * This class represents a model-artifact for managing an ODE NetLogo model with
 * 3 input ports 1, 2 and 3 used respectively for settings values of variables
 * called "X", "Y" and "Z" in the model. and 3 output ports 1, 2 and 3 used
 * respectively for getting values of variables called "X", "Y" and "Z" in the
 * model. and having only one parameters timeDiscretization.
 *
 *
 *
 */
public class NetLogoModelArtifact extends ModelArtifact {

	// Implementation
	/**
	 * Frame for NetLogo.
	 */
	private final JFrame frame;

	/**
	 * Frame width.
	 */
	private static final int WINDOW_WIDTH = 480;

	/**
	 * Frame height.
	 */
	private static final int WINDOW_HEIGHT = 550;

	/**
	 * Time between each NetLogo step.
	 */
	private double timeDiscretization;

	/**
	 * Default time between each NetLogo step.
	 */
	private static final double DEFAULT_TIME_DISCRETIZATION = 0.0001;

	/**
	 * a path or a string corresponding to the model according to the kind of
	 * "launch"
	 *
	 */
	private final String modelAccessDescription;

	/**
	 * NetLogo component
	 */
	private final InterfaceComponent comp;

	/**
	 * Time of the last event
	 */
	private double eventTime;

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
	private final Semaphore waitGui;

	// Creation
	/***
	 *
	 * @param modelPath Path to the NetLogo model.
	 * @param name      Name of the frame.
	 */
	public NetLogoModelArtifact(String modelPath, String name) {
		super("ode");
		modelAccessDescription = modelPath;
		timeDiscretization = DEFAULT_TIME_DISCRETIZATION;

		eventTime = 0;

		guiStop = false;
		waitGui = new Semaphore(0);

		frame = new JFrame(name);
		// create a new "light" instance
		comp = new InterfaceComponent(frame);
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
		timeDiscretization = Double.valueOf(args[0]);
		execCmd("set time-discretization " + timeDiscretization);
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
			double tmp = 1 / timeDiscretization;
			double ticks = ((Double) comp.report("ticks")) * timeDiscretization;
			ticks += timeDiscretization;
			ticks = Math.round(tmp * ticks) / tmp;
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
		final SimulData data = event.getData();

		if (data instanceof Tuple1) {
			final Object value = ((Tuple1<?>) event.getData()).getItem1();

			if (value instanceof Number) {
				execCmd("set " + port + " " + ((Number) value).toString());
			} else {
				getLogger().error("processExternalInputEvent",
						new UnexpectedTypeException(Tuple1.of(Number.class), Tuple1.of(value.getClass())));
			}
		} else {
			getLogger().error("processExternalInputEvent",
					new UnexpectedTypeException(Tuple1.of(Number.class), data.getClass()));
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
		@Nullable
		SimulEvent result = null;

		if (port.equalsIgnoreCase("obs")) {
			Double x = (Double) comp.report("X");
			Double y = (Double) comp.report("Y");

			result = new SimulEvent(new Tuple2<>(x, y), eventTime);
		} else if (port.equalsIgnoreCase("obs3d")) {
			Double x = (Double) comp.report("X");
			Double y = (Double) comp.report("Y");
			Double z = (Double) comp.report("Z");

			result = new SimulEvent(new Tuple3<>(x, y, z), eventTime);
		} else {
			SimulData x = new Tuple2<>((Double) comp.report(port), port);
			result = new SimulEvent(x, eventTime);
		}

		return result;
	}

	/**
	 * Do something when simulation is done
	 */
	@Override
	public void finishSimulation() {
		getLogger().info("end simulation at time {", getLastEventTime() + "}");
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

	// Service
	/**
	 * Execute the NetLogo command
	 *
	 * @param cmd the command to be executed in NetLogo
	 */
	protected void execCmd(String cmd) {
		comp.command(cmd);
	}

	/**
	 * Method to close a frame, it is used to end JUnit test of this model artifact.
	 * Send a window closing event to the frame.
	 */
	public void closeWindows() {
		frame.dispatchEvent(new WindowEvent(this.frame, WindowEvent.WINDOW_CLOSING));
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
