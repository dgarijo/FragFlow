MotifGraphProcessor
===================

Project desinged for finding internal macro and composite workflow motifs in scientific workflow, defined according to 

 http://purl.org/net/wf-motifs#InternalMacro and http://purl.org/net/wf-motifs#CompositeWorkflow.
 
The project finds a set of workflow fragments from workflow specifications and/or workflow workflow executions and links 
the results to the corpus. The results are linked according to the Workflow Fragment Description Ontology: http://purl.org/net/wf-fd

In order to achieve the results, this project defines diverse operations for graph manipulation and formatting. In particular:

* Generic readers and writers that can read and write different workflow specifications and traces (currently supported: OPMW, OPM)
* Inference and abstraction of a workfow collection or individual workflows.
* Remote querying and adaptation to process RDF workflows exposed as Linked Data.
* Formatting output to be read by the SUBDUE tool, a too which does hierarchical clustering on the graph inputs.
* Capability of saving the results as RDF.
* Computation of statistics on the results obtained, and binding the fragments proposed by SUDUE to the results.

The project is configured as a Netbeans project right now. All the libraries and dependencies are jar files contained in the /lib folder.

Current ongoing work:
* Adapt the framework to different types of graph mining algorithms. Currently supported: SUBDUE
* Adapt the framework to read from different types of workflows. Currently supported: OPMW
