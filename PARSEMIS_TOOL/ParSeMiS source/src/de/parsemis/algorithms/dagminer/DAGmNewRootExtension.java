/**
 * created 31.07.2006
 *
 * @by Tobias Werth (sitowert@i2.informatik.uni-erlangen.de)
 *
 * Copyright 2006 Tobias Werth
 * 
 * This file is part of parsemis.
 *
 * Licence: 
 *  LGPL: http://www.gnu.org/licenses/lgpl.html
 *   EPL: http://www.eclipse.org/org/documents/epl-v10.php
 *   See the LICENSE file in the project's top-level directory for details.
 */
package de.parsemis.algorithms.dagminer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import de.parsemis.algorithms.dagminer.DAGmFragment.LastAction;
import de.parsemis.graph.HPGraph;
import de.parsemis.graph.HPMutableGraph;
import de.parsemis.graph.Node;
import de.parsemis.miner.chain.Extension;
import de.parsemis.miner.chain.MiningStep;
import de.parsemis.miner.chain.SearchLatticeNode;
import de.parsemis.miner.environment.LocalEnvironment;
import de.parsemis.miner.general.HPEmbedding;
import de.parsemis.utils.IntIterator;

/**
 * @author Tobias Werth (sitowert@i2.informatik.uni-erlangen.de)
 * 
 * @param <NodeType>
 *            the type of the node labels (will be hashed and checked with
 *            .equals(..))
 * @param <EdgeType>
 *            the type of the edge labels (will be hashed and checked with
 *            .equals(..))
 */
public class DAGmNewRootExtension<NodeType, EdgeType> extends
		MiningStep<NodeType, EdgeType> {

	public DAGmNewRootExtension(final MiningStep<NodeType, EdgeType> next) {
		super(next);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.parsemis.miner.MiningStep#call(de.parsemis.miner.SearchLatticeNode,
	 *      java.util.Collection)
	 */
	@Override
	public void call(final SearchLatticeNode<NodeType, EdgeType> node,
			final Collection<Extension<NodeType, EdgeType>> extensions) {
		final DAGmFragment<NodeType, EdgeType> actFragment = (DAGmFragment<NodeType, EdgeType>) node
				.toHPFragment();
		final HashMap<NodeType, DAGmFragment<NodeType, EdgeType>> extendedFragments = new HashMap<NodeType, DAGmFragment<NodeType, EdgeType>>();
		final Node<NodeType, EdgeType> lastNode = actFragment.getLastNode();
		final int lastLevel = actFragment.getLevel(lastNode);

		if (lastLevel != 1) {
			// extend at topological level 1, insert new roots
			callNext(node, extensions);
			return;
		}

		final int lastNodeIdx = LocalEnvironment.env(actFragment)
				.getNodeLabelIndex(lastNode.getLabel());
		final Iterator<HPEmbedding<NodeType, EdgeType>> embeddingIt = actFragment
				.iterator();
		while (embeddingIt.hasNext()) {
			final DAGmHPEmbedding<NodeType, EdgeType> actEmbedding = (DAGmHPEmbedding<NodeType, EdgeType>) embeddingIt
					.next();
			final HPGraph<NodeType, EdgeType> superGraph = actEmbedding
					.getHPSuperGraph();

			final IntIterator nodeIt = superGraph.nodeIndexIterator();
			while (nodeIt.hasNext()) {
				final int nextNode = nodeIt.next();
				final NodeType nextNodeLabel = superGraph
						.getNodeLabel(nextNode);

				if (!actEmbedding.isUsed(nextNode)) {
					final int nodeTypeIdx = LocalEnvironment.env(actFragment)
							.getNodeLabelIndex(nextNodeLabel);

					if (nodeTypeIdx >= 0 && nodeTypeIdx <= lastNodeIdx) {
						// useful extension found
						final DAGmHPEmbedding<NodeType, EdgeType> newEmbedding = new DAGmHPEmbedding<NodeType, EdgeType>();
						DAGmFragment<NodeType, EdgeType> newFragment;

						if (extendedFragments.containsKey(nextNodeLabel)) {
							newFragment = extendedFragments.get(nextNodeLabel);
						} else {
							final int nodeLevels[] = new int[actFragment
									.getNodeLevels().length + 1];
							for (int i = 0; i < nodeLevels.length; i++) {
								nodeLevels[i] = 1; // all nodes at level 1
							}
							final HPMutableGraph<NodeType, EdgeType> newGraph = (HPMutableGraph<NodeType, EdgeType>) actEmbedding
									.getSubGraph().clone();
							newGraph.addNodeIndex(nextNodeLabel);

							newFragment = new DAGmFragment<NodeType, EdgeType>(
									newGraph, nodeLevels);
							newFragment.setLastAction(LastAction.INSERTED_NODE);
							newFragment.setLastCreatingNode(0); // FIXME
							newFragment.setLastEdgeCreatingNode(0); // FIXME

							extendedFragments.put(nextNodeLabel, newFragment);
							LocalEnvironment.env(this).stats.newRoot++;
						}

						final int superNodes[] = new int[actFragment
								.getNodeLevels().length + 1];
						System.arraycopy(actEmbedding.getSuperNodes(), 0,
								superNodes, 0, superNodes.length - 1);
						superNodes[superNodes.length - 1] = nextNode;

						newEmbedding.set(
								(DAGmGraph<NodeType, EdgeType>) actEmbedding
										.getDataBaseGraph(), newFragment
										.getHPSubGraph(), superNodes);

						newFragment.add(newEmbedding);
					}
				}
			}
		}

		// add these new found fragments to search lattice - but only frequent
		// one's
		for (final DAGmFragment<NodeType, EdgeType> newFragment : extendedFragments
				.values()) {
			if (LocalEnvironment.env(newFragment).minFreq.compareTo(newFragment
					.frequency()) <= 0) {
				extensions.add(new DAGmSearchLatticeNode<NodeType, EdgeType>(
						newFragment));
			}
		}

		callNext(node, extensions);
	}

}