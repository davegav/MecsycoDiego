package mecsycoscholar.application.highway.operation;

import java.util.Map;
import java.util.Objects;

import mecsyco.core.operation.event.EventOperation;
import mecsyco.core.type.SimulData;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.Tuple1;

public class Density2Cars extends EventOperation {

	// Implementation
		/**
		 * {@link #getmacroLength()}
		 */
		private final double macroLength;

		/**
		 * {@link #gettrackCount()}
		 */
		private final double trackCount;
		
		/**
		 * {@link #getCellCount()}
		 */
		private final double cellCount;

	// Creation
		/**
		 * 
		 * @param aMacroLength {@link #getmacroLength()}
		 * @param atrackCount {@link #gettrackCount()}
		 * @param aCellCount {@link #getCellCount()}
		 */
		public Density2Cars (double aMacroLength, double atrackCount, double aCellCount) {
			macroLength = aMacroLength;
			trackCount = atrackCount;
			cellCount = aCellCount;
		}
		
		/**
		 * Map containing all the parameters in the way (name, value)
		 * @param parameters
		 */
		public Density2Cars (Map<String, Object> parameters) {
			macroLength = parameters.get("macroLength")!=null?(Double) parameters.get("macroLength"):0.;
			trackCount = parameters.get("trackCount")!=null?(Double) parameters.get("trackCount"):0.;
			cellCount = parameters.get("cellCount")!=null?(Double) parameters.get("cellCount"):0.;
		}

	// Access
		@Override
		public SimulData apply (SimulEvent aEvent) {
			Number density = (Double)((Tuple1<?>) aEvent.getData()).getItem1();
			// System.out.println(density + " " + macro_length + " " + density * macro_length * nbVoie);
			return new Tuple1<>(density.doubleValue() * (1/(macroLength * cellCount)) * trackCount);
		}

		@Override
		public int hashCode () {
			return Objects.hash(macroLength, trackCount, cellCount);
		}

		/**
		 * 
		 * @return Length of a macro cells (km).
		 */
		public double getmacroLength () {
			return macroLength;
		}

		/**
		 * 
		 * @return Number of track in the macro road.
		 */
		public double gettrackCount () {
			return trackCount;
		}

		/**
		 * 
		 * @return Number of cells.
		 */
		public double getCellCount () {
			return cellCount;
		}

	// Status
		@Override
		public boolean equals (Object o) {
			return o instanceof Density2Cars && 
					((Density2Cars) o).cellCount == cellCount && 
					((Density2Cars) o).trackCount == trackCount && 
					((Density2Cars) o).macroLength == macroLength;
		}
}
