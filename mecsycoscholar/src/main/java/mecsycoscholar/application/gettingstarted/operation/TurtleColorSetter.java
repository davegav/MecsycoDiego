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

package mecsycoscholar.application.gettingstarted.operation;

import java.util.Map;

import javax.annotation.Nullable;

import mecsyco.core.helper.ObjectHelpers;
import mecsyco.core.operation.event.EventOperation;
import mecsyco.core.type.ArrayedSimulVector;
import mecsyco.core.type.SimulData;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.SimulVector;

/**
 * Operation to set NetLogo color for the example.
 *
 *
 */
public class TurtleColorSetter extends EventOperation {

	// Implementation
	/**
	 * {@link #getColor()}
	 */
	private final Number color;

	// Creation
	/**
	 *
	 * @param aColor {@link #getColor()}
	 */
	public TurtleColorSetter(Number aColor) {
		color = aColor;
	}

	/**
	 *
	 * @param parameters Map of parameters to be compliant with MECSYCO DSL, only
	 *                   one parameter named "color" of type {@link Number} is
	 *                   allowed
	 */
	public TurtleColorSetter(Map<String, Number> parameters) {
		// There should be only one param
		color = parameters.get("color");
	}

	/**
	 *
	 * @return Color.
	 */
	public Number getColor() {
		return color;
	}

	@Override
	public int hashCode() {
		return color.hashCode();
	}

	// Status
	@Override
	public boolean equals(@Nullable Object o) {
		return ObjectHelpers.haveSameClass(this, o) && color == ((TurtleColorSetter) o).color;
	}

	// Operation
	@Override
	public @Nullable SimulData apply(SimulEvent aEvent) {
		final SimulData data = aEvent.getData();
		final @Nullable SimulData result;
		if (data instanceof SimulVector && ((SimulVector<?>) data).areTyped(SimulVector.class)) {
			SimulVector<?> candidates = (SimulVector<?>) data;

			int count = candidates.count();
			for (int i = 0; i < count; i++) {
				if (candidates.item(i) instanceof SimulVector) {
					final SimulVector<?> t = (SimulVector<?>) candidates.item(i);
					SimulVector<String> attributes = new ArrayedSimulVector<String>((String) t.item(0), "" + color);
					candidates = candidates.replacedAny(i, attributes);
				}
			}

			result = candidates;
		} else {
			result = null;
		}

		return result;
	}

}
