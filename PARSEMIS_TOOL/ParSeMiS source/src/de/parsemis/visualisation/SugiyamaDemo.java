/**
 * Created Aug 23, 2007
 *
 * @by Sebastian Lenz (siselenz@stud.informatik.uni-erlangen.de)
 * 
 * Copyright 2007 Sebastian Lenz
 * 
 * This file is part of parsemis.
 *
 * Licence: 
 *  LGPL: http://www.gnu.org/licenses/lgpl.html
 *   EPL: http://www.eclipse.org/org/documents/epl-v10.php
 *   See the LICENSE file in the project's top-level directory for details.
 */
package de.parsemis.visualisation;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.border.EtchedBorder;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.activity.Activity;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.util.GraphicsLib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;
import de.parsemis.MainFrame;
import de.parsemis.graph.Graph;
import de.parsemis.parsers.GraphmlParser;
import de.parsemis.visualisation.prefuseVisualisation.PrefuseVisualisationSettings;
import de.parsemis.visualisation.prefuseVisualisation.SyncActionList;

/**
 * 
 * @author Sebastian Lenz (siselenz@stud.informatik.uni-erlangen.de)
 * 
 * @param <NodeType>
 *            the type of the node labels (will be hashed and checked with
 *            .equals(..))
 * @param <EdgeType>
 *            the type of the edge labels (will be hashed and checked with
 *            .equals(..))
 */
public class SugiyamaDemo<NodeType, EdgeType> extends Display implements
		GraphPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String GRAPH = "graph";

	public static final String NODES = "graph.nodes";

	public static final String EDGES = "graph.edges";

	private Dimension oldDimension = null;

	private Dimension newDimension = null;

	private prefuse.data.Graph prefuseGraph = null;

	private boolean isFragment = true;

	private final LabelRenderer m_nodeRenderer;

	private final EdgeRenderer m_edgeRenderer;

	@SuppressWarnings("unused")
	private Activity layoutActivity;

	@SuppressWarnings("unused")
	private Activity repaintActivity;

	@SuppressWarnings("unused")
	private Activity colorActivity;

	private final SugiyamaLayout layout;

	private final SyncActionList color;

	private final SyncActionList repaint;

	public @SuppressWarnings("unchecked")
	SugiyamaDemo(final Dimension d, final Graph graph, final boolean frag) {
		// initialize display and data
		super(new Visualization());
		isFragment = frag;
		oldDimension = d;
		setBorder(new EtchedBorder(EtchedBorder.RAISED));
		// this.setSize(d);

		String datafile = null;
		final Collection graphs = new ArrayList<Graph<NodeType, EdgeType>>();
		graphs.add(graph);
		OutputStream out;
		try {
			out = new FileOutputStream("graphml.xml");
			datafile = "graphml.xml";
			new GraphmlParser(MainFrame.settings.parser.getNodeParser(),
					MainFrame.settings.parser.getEdgeParser()).serialize(out,
					graphs);
		} catch (final FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		try {
			prefuseGraph = new GraphMLReader().readGraph(datafile);
		} catch (final DataIOException e) {
			e.printStackTrace();
		}
		m_vis.add(GRAPH, prefuseGraph);

		m_nodeRenderer = new LabelRenderer("name");
		m_nodeRenderer.setRoundedCorner(8, 8); // round the corners
		m_edgeRenderer = new EdgeRenderer();
		m_edgeRenderer.setArrowHeadSize(12, 16);
		m_edgeRenderer.setDefaultLineWidth(1.5);
		final DefaultRendererFactory rf = new DefaultRendererFactory(
				m_nodeRenderer);
		m_vis.setRendererFactory(rf);
		rf.add(new InGroupPredicate(EDGES), m_edgeRenderer);
		// create our nominal color palette
		// blue is the node color
		final int[] palette = new int[] { PrefuseVisualisationSettings.nodeColor };
		// create an action list containing all color assignments
		color = new SyncActionList();
		// map nominal data values to colors using our provided palette
		color.add(new DataColorAction(NODES, "color", Constants.NOMINAL,
				VisualItem.FILLCOLOR, palette));
		// use black for node text
		color.add(new ColorAction(NODES, VisualItem.TEXTCOLOR,
				PrefuseVisualisationSettings.nodeTextColor));
		// use black for edges
		color.add(new ColorAction(EDGES, VisualItem.STROKECOLOR,
				PrefuseVisualisationSettings.edgeColor));
		color.add(new ColorAction(EDGES, VisualItem.FILLCOLOR,
				PrefuseVisualisationSettings.edgeColor));
		// repaint
		repaint = new SyncActionList();
		repaint.add(new RepaintAction());

		// create the layout action
		layout = new SugiyamaLayout(GRAPH);
		// layout.setAutoScale(true); nur bei radialtreelayout?

		// add the actions to the visualization
		m_vis.putAction("color", color);
		m_vis.putAction("layout", layout);
		m_vis.putAction("repaint", repaint);

		m_vis.run("color"); // assign the colors
		m_vis.run("layout"); // start up the animated layout
		m_vis.run("repaint");

		// TODO: get initial value from settings
		this.setHighQuality(PrefuseVisualisationSettings.setHighQuality);
		this.setSize(d.width, d.height);
		if (isFragment) {
			// this.zoom(new Point2D.Double(0, 0), 0.5);
		} else {
			// this.panTo(new Point2D.Double(0, (double) this.getHeight() /
			// 2.0));
			addControlListener(new FocusControl(1));
			addControlListener(new DragControl());
			addControlListener(new PanControl());
			addControlListener(new ZoomControl());
			addControlListener(new WheelZoomControl());
			addControlListener(new ZoomToFitControl());
			addControlListener(new NeighborHighlightControl());
		}
	}

	public void addToPropertyChangeListener(final JComponent propertyChanger) {
		if (propertyChanger != null) {
			propertyChanger.addPropertyChangeListener("high quality", this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SugiyamaDemo<NodeType, EdgeType> clone()
			throws CloneNotSupportedException {
		return (SugiyamaDemo<NodeType, EdgeType>) super.clone();
	}

	public JComponent getComponent() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see prefuse.Display#getVisualization()
	 */
	@Override
	public synchronized Visualization getVisualization() {
		return super.getVisualization();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see prefuse.Display#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(final Graphics g) {
		newDimension = getSize();
		System.out.println("ist angekommen");
		// Wenn die Groesse des Components geaendert worden ist, dann den
		// Graphen scalieren
		// if ((newDimension.height != oldDimension.height)
		// || (newDimension.width != oldDimension.width)) {
		// scaleDemo(newDimension);
		// }
		if (m_offscreen == null) {
			m_offscreen = getNewOffscreenBuffer(newDimension.width,
					newDimension.height);
			damageReport();
		}
		final Graphics2D g2D = (Graphics2D) g;
		final Graphics2D buf_g2D = (Graphics2D) m_offscreen.getGraphics();
		// paint the visualization
		paintDisplay(buf_g2D, newDimension);
		paintBufferToScreen(g2D);

		// fire post-paint events to any painters
		firePostPaint(g2D);
		buf_g2D.dispose();
	}

	public void paintOffscreen(final Graphics g, final Dimension dim) {
		try {
			// Dimension d = new Dimension((int) (scale * getWidth()),
			// (int) (scale * getHeight()));
			// BufferedImage img = (BufferedImage) createImage(d.width,
			// d.height);
			// Graphics2D g = (Graphics2D) img.getGraphics();
			// Point2D p = new Point2D.Double(0, 0);
			// zoom(p, scale);
			final boolean q = isHighQuality();
			System.err.println("PrefuseDemo:: davor");
			paintDisplay((Graphics2D) g, dim);
			System.err.println("PrefuseDemo:: danach");
			setHighQuality(q);
			// zoom(p, 1 / scale);

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void propertyChange(final PropertyChangeEvent event) {
		final String propertyName = event.getPropertyName();
		// Qualitaet des Bildes aendern
		if (propertyName.equals("high quality")) {
			// TODO: unnecesarry if initial value correct
			PrefuseVisualisationSettings.setHighQuality = ((Boolean) event
					.getNewValue()).booleanValue();
			setHighQuality(((Boolean) event.getNewValue()).booleanValue());
			this.repaint();
		}
	}

	// TODO funktioniert gar nicht
	public void scaleDemo(final Dimension d) {
		final int newWidth = d.width;
		final int newHeight = d.height;
		final double oldWidth = oldDimension.getWidth();
		final double oldHeight = oldDimension.getHeight();
		final double oldValue = (oldWidth * oldHeight);
		final double newValue = (newWidth * newHeight);
		this.zoom(new Point2D.Double(0, 0), newValue / oldValue);
		this.panAbs(0, 0);
		oldDimension = d;
	}

	/**
	 * Set the Visualiztion associated with this Display. This Display will
	 * render the items contained in the provided visualization. If this Display
	 * is already associated with a different Visualization, the Display
	 * unregisters itself with the previous one.
	 * 
	 * @param vis
	 *            the backing {@link Visualization} to use.
	 */
	@Override
	public synchronized void setVisualization(final Visualization vis) {
		super.setVisualization(vis);
	}

	public void zoomToFit() {
		while (!layout.ready()) {
			;
		}
		synchronized (repaint) {
			synchronized (color) {

				// long a=System.currentTimeMillis()+5000;
				// while (System.currentTimeMillis()<a);
				// MouseEvent e = new MouseEvent(this, 0, 0,
				// Control.RIGHT_MOUSE_BUTTON,
				// 0, 0, 0, false);
				// for (MouseListener m : this.getMouseListeners()) {
				// m.mouseClicked(e);
				// }
				final Rectangle2D bounds = getVisualization().getBounds(
						Visualization.ALL_ITEMS);
				System.err.println(bounds + " " + this.getScale() + " "
						+ this.getSize());

				GraphicsLib.expand(bounds, 50 + (int) (1 / this.getScale()));

				System.err.println(bounds + " " + this.getScale() + " "
						+ this.getSize());

				// DisplayLib.fitViewToBounds(this, bounds, 1);
				// init variables
				final double w = getWidth(), h = getHeight();
				final double cx = bounds.getCenterX();
				final double cy = bounds.getCenterY();
				System.err.println("w = " + w + " h = " + h + " cx = " + cx
						+ " cy = " + cy);

				// compute half-widths of final bounding box around
				// the desired center point
				final double wb = Math.max(cx - bounds.getMinX(), bounds
						.getMaxX()
						- cx);
				final double hb = Math.max(cy - bounds.getMinY(), bounds
						.getMaxY()
						- cy);

				// compute scale factor
				// - figure out if z or y dimension takes priority
				// - then balance against the current scale factor
				final double scale = Math.min(w / (2 * wb), h / (2 * hb))
						/ getScale();
				System.err.println("wb = " + wb + " hb = " + hb + " scale = "
						+ scale);

				// animate to new display settings
				final Point2D center = new Point2D.Double(cx, cy);
				panToAbs(center);
				zoomAbs(center, scale);
			}
		}
		m_vis.run("repaint");

	}

}
