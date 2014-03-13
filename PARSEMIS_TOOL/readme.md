Binaries for the parsemis tool (java binary).
===================

Parsemis is a quite complete tool with 3 different algorithms for sub-graph matching: gSpan, Gaston and Dagma.
Parsemis incorporates methods to filter by frequency and the minimum number of nodes which we want the fragments to be.

For the moment the script calls the tool with the following options:

--graphFile=input_graphs\example1.lg --minimumFrequency=2 --outputFile=results\test.txt --storeEmbeddings=true --minimumNodeCount=2 --algorithm=gspan

* graphFile is the input file.
* outputFile is the output file.
* minimum frequency is the minimum frequency we want the fragment to appear (could be a percentage or an integer). In this case, 
I use 2 because we want fragment with at least 2 occurrences.
* storeEmbeddings is set to true in order to store where the fragment has been found in the input file graph (and how many times).
* minimumNodeCount is set to 2 in order to avoid fragments with only one node. The minimum must be two.
* algorithm specifies the algorithm to be used: gspan, gaston or dagma. On gaston, the hierarchical clustering could be activated.

Other options that could be used to play with are storeHierarchicalEmbeddings (which stores the hierarchical relationships among the fragments), 
minimumEdgeCount, etc.