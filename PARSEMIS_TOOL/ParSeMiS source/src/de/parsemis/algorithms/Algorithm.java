/**
 * created May 2, 2006
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
package de.parsemis.algorithms;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import de.parsemis.graph.Graph;
import de.parsemis.graph.GraphFactory;
import de.parsemis.miner.chain.Extender;
import de.parsemis.miner.chain.SearchLatticeNode;
import de.parsemis.miner.environment.Settings;
import de.parsemis.miner.general.Fragment;
import de.parsemis.utils.Generic;

/**
 * This interface encapsulate the required abilities of a mining algorithm.
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
public interface Algorithm<NodeType, EdgeType> extends
		Generic<NodeType, EdgeType>, Serializable {

	/**
	 * @param threadIdx
	 * @return a (new) Extender Object for the given thread (index)
	 */
	public Extender<NodeType, EdgeType> getExtender(final int threadIdx);

	/**
	 * Initialize the algorithm
	 * 
	 * @param graphs
	 *            the set of graphs that will be search for frequent fragments
	 * @param factory
	 *            the factory new graphs will be created with
	 * @param settings
	 *            the settings for the search
	 * @return a collection with all fragments that will not be found by the
	 *         algorithm
	 */
	public Collection<Fragment<NodeType, EdgeType>> initialize(
			final Collection<Graph<NodeType, EdgeType>> graphs,
			final GraphFactory<NodeType, EdgeType> factory,
			final Settings<NodeType, EdgeType> settings);

	/**
	 * @return an iterator over the initial nodes for the search
	 */
	public Iterator<SearchLatticeNode<NodeType, EdgeType>> initialNodes();

}
