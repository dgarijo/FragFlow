/**
 * created May 31, 2006
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
package de.parsemis.graph;

import java.io.Serializable;
import java.util.BitSet;

import de.parsemis.miner.environment.Relabler;
import de.parsemis.utils.Cloneable;
import de.parsemis.utils.Generic;
import de.parsemis.utils.IntIterator;

/**
 * This interface represents a HighPerformance Graph which avoids costly object
 * generation by indexing.
 * <p>
 * The transformation from normal Graph to HPGraph is straight forward, so
 * development with objects and afterwards transformation to higher performance
 * is possible.
 * <p>
 * There are at least consistency checks as possible, so correct usage is
 * necessary!
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
public interface HPGraph<NodeType, EdgeType> extends
		Cloneable<HPGraph<NodeType, EdgeType>>, Generic<NodeType, EdgeType>,
		Serializable {

	/** constant that describes that there is no node */
	public static final int NO_NODE = -1;

	/** constant that describes that there is no edge */
	public static final int NO_EDGE = -1;

	/** @return an iterator over all edges (indices) in this graph */
	public IntIterator edgeIndexIterator();

	/**
	 * @param nodeIdx
	 *            the index of the node
	 * @return the degree of the node
	 */
	public int getDegree(final int nodeIdx);

	/**
	 * @param edgeIdx
	 *            the index of the edge
	 * @return the direction (in respect to nodeA) of the edge
	 */
	public int getDirection(final int edgeIdx);

	/**
	 * @param edgeIdx
	 *            the index of the edge
	 * @param nodeIdx
	 *            the index of the node
	 * @return the direction (in respect to the given node) of the edge
	 * @throws IllegalArgumentException
	 *             if the node is not a node of the edge
	 */
	public int getDirection(final int edgeIdx, final int nodeIdx)
			throws IllegalArgumentException;

	/**
	 * @param nodeAIdx
	 *            the index of the start node
	 * @param nodeBIdx
	 *            the index of the end node
	 * @return the index of the edge started in nodeA and ended in nodeB, or -1
	 *         if none is available
	 */
	public int getEdge(final int nodeAIdx, final int nodeBIdx);

	/** @return the count of edges */
	public int getEdgeCount();

	/**
	 * @param nodeIdx
	 *            the index of the node
	 * @return an iterator over all edges (indices) connected with the given
	 *         node
	 */
	public IntIterator getEdgeIndices(final int nodeIdx);

	/**
	 * @param edgeIdx
	 *            the index of the edge
	 * @return the label connected with the given edge
	 */
	public EdgeType getEdgeLabel(final int edgeIdx);
	
	/**
	 * determine the index of the label of the given edge according to the given
	 * relabler shall be equal to or more efficient than
	 * rel.getEdgeLabelIndex(this.getEdgeLabel(edgeIdx))
	 * 
	 * @param edgeIdx
	 *            the index of the edge
	 * @param relabler
	 * @return the index of the corresponding label
	 */
	public int getEdgeLabelIndex(final int edgeIdx,
			final Relabler<NodeType, EdgeType> relabler);

	/**
	 * @return a BitSet which setted bits describe that the corresponding edge
	 *         is available in the HPGraph
	 */
	public BitSet getEdges();

	/** @return the unique ID of the graph */
	public int getID();

	/**
	 * @param nodeIdx
	 *            the index of the node
	 * @return the number of (directed) incoming edges for the node
	 */
	public int getInDegree(final int nodeIdx);

	/**
	 * @param nodeIdx
	 *            the index of the node
	 * @return an iterator over all directed incoming edges (indices) connected
	 *         with the given node
	 */
	public IntIterator getInEdgeIndices(final int nodeIdx);

	/** @return the maximal used edge index */
	public int getMaxEdgeIndex();

	/** @return the maximal used node index */
	public int getMaxNodeIndex();

	/** @return the associated name of the graph */
	public String getName();
	
	// added
	/** @return the associated class of the graph */
	public int getClassNumber();
	public void setClassNumber(int classNumber);
	
	/**
	 * @param edgeIdx
	 *            the index of the edge
	 * @return the first node of the edge
	 */
	public int getNodeA(final int edgeIdx);

	/**
	 * @param edgeIdx
	 *            the index of the edge
	 * @return the second node of the edge
	 */
	public int getNodeB(final int edgeIdx);

	/** @return the count of nodes */
	public int getNodeCount();

	/**
	 * @param nodeIdx
	 *            the index of the node
	 * @param i
	 * @return the i.th edge connected to the given node
	 */
	public int getNodeEdge(final int nodeIdx, final int i);

	/**
	 * @param nodeIdx
	 *            the index of the node
	 * @return the label connected with the given node
	 */
	public NodeType getNodeLabel(final int nodeIdx);

	/**
	 * determine the index of the label of the given node according to the given
	 * relabler shall be equal to or more efficient than
	 * rel.getNodeLabelIndex(this.getNodeLabel(nodeIdx))
	 * 
	 * @param nodeIdx
	 *            the index of the node
	 * @param relabler
	 * @return the index of the corresponding label
	 */
	public int getNodeLabelIndex(final int nodeIdx,
			final Relabler<NodeType, EdgeType> relabler);

	/**
	 * @param nodeIdx
	 *            the index of the node
	 * @param i
	 * @return the node connected to the given node by the i.th edge
	 */
	public int getNodeNeigbour(final int nodeIdx, final int i);

	/**
	 * use and buffer the GraphTools.computePartitions2(HPGraph<NodeType,
	 * EdgeType>, Relabler<NodeType, EdgeType>) method
	 * 
	 * @param rel
	 * @return the node partitioning according to the above partitioning
	 */
	public int[] getNodePartions(Relabler<NodeType, EdgeType> rel);

	/**
	 * @return a BitSet which setted bits describe that the corresponding node
	 *         is available in the HPGraph
	 */
	public BitSet getNodes();

	/**
	 * @param edgeIdx
	 *            the index of the edge
	 * @param nodeIdx
	 *            the index of the node
	 * @return the other node connected with the edge
	 */
	public int getOtherNode(final int edgeIdx, final int nodeIdx);

	/**
	 * @param nodeIdx
	 *            the index of the node
	 * @return the number of (directed) outgoing edges for the node
	 */
	public int getOutDegree(final int nodeIdx);

	/**
	 * @param nodeIdx
	 *            the index of the node
	 * @return an iterator over all directed outgoing edges (indices) connected
	 *         with the given node
	 */
	public IntIterator getOutEdgeIndices(final int nodeIdx);

	/**
	 * checks if the given edgeIdx is a valid edge in the graph
	 * 
	 * @param edgeIdx
	 *            the index of the node
	 * @return <code>true</code>, if the given edge is available
	 */
	public boolean isValidEdge(final int edgeIdx);

	/**
	 * checks if the given nodeIdx is a valid node in the graph
	 * 
	 * @param nodeIdx
	 *            the index of the node
	 * @return <code>true</code>, if the given node is available
	 */
	public boolean isValidNode(final int nodeIdx);

	/** @return an iterator over all nodes (indices) in this graph */
	public IntIterator nodeIndexIterator();

	/**
	 * sets the label for the given edge
	 * 
	 * @param edgeIdx
	 *            the index of the edge
	 * @param label
	 */
	public void setEdgeLabel(final int edgeIdx, final EdgeType label);

	/**
	 * sets the label for the given node
	 * 
	 * @param nodeIdx
	 *            the index of the node
	 * @param label
	 */
	public void setNodeLabel(final int nodeIdx, final NodeType label);
	
	// added
	public int getEdgeWeigth(final int edgeIdx);
	
	public void setEdgeRanking(final int edgeIdx, double r);
	public double getEdgeRanking(int edgeIdx);

	/** @return an objected version of this HPGraph */
	public Graph<NodeType, EdgeType> toGraph();
}
