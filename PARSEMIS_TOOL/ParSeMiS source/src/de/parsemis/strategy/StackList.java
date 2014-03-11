/**
 * Created on Jun 26, 2006
 * 
 * @by Marc Woerlein (woerlein@informatik.uni-erlangen.de)
 *
 * Copyright 2006 Marc Woerlein
 * 
 * This file is part of parsemis.
 *
 * Licence: 
 *  LGPL: http://www.gnu.org/licenses/lgpl.html
 *   EPL: http://www.eclipse.org/org/documents/epl-v10.php
 *   See the LICENSE file in the project's top-level directory for details.
 */
package de.parsemis.strategy;

import java.util.Collection;

/**
 * This interface encapsulate the required abilities of a list of stacks.
 * 
 * @author Marc Woerlein (woerlein@informatik.uni-erlangen.de)
 * 
 * @param <NodeType>
 *            the type of the node labels (will be hashed and checked with
 *            .equals(..))
 * @param <EdgeType>
 *            the type of the edge labels (will be hashed and checked with
 *            .equals(..))
 */
public interface StackList<NodeType, EdgeType> extends
		Collection<MiningStack<NodeType, EdgeType>> {

	/**
	 * trys to refill the given stack with new work form the other MiningStacks
	 * 
	 * @param empty
	 * @return true if the given stack is refilled
	 */
	public boolean split(MiningStack<NodeType, EdgeType> empty);

}
