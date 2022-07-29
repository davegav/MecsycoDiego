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

package mecsycoscholar.application.sheepgrass.type;

import mecsyco.world.netlogo.type.NetLogoTurtle;

/**
 * represents the position and color of sheep.
 * 
 * 
 *
 */
public class SheepShepherd extends NetLogoTurtle {

// Version
	private static final long serialVersionUID = 1L;

// Creation
	/**
	 * 
	 * @param aId
	 *            - {@link #getId()}
	 * @param aX
	 *            - {@link #getX()}
	 * @param aY
	 *            - {@link #getY()}
	 * @param aColor
	 *            - {@link #getColor()}
	 * @param aHeading
	 *            - {@link #getHeading()}
	 * @param aSize
	 *            - {@link #getSize()}
	 * @param aIsCarried
	 *            - {@link #isCarried()}
	 */
	public SheepShepherd (long aId, double aX, double aY, double aColor, double aHeading, double aSize, boolean aIsCarried) {
		super (aId, aX, aY, aColor, aHeading, aSize, "sheep");
		isCarried = aIsCarried;
	}

// Status
	public boolean isCarried () {
		return isCarried;
	}

// Status change
	public void carry () {
		isCarried = true;
	}

	public void leave () {
		isCarried = false;
	}

	/**
	 * 
	 * @param aIsCarried - {@link #isCarried()}
	 */
	public void setIsCarried (boolean aIsCarried) {
		isCarried = aIsCarried;
	}

// Implementation
	/**
	 * {@link #isCarried()}
	 */
	protected boolean isCarried;

}
