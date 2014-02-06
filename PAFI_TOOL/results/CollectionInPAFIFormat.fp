# *******************************************************
# fsg 1.37 (PAFI 1.0) Copyright 2003, Regents of the University of Minnesota
# 
# Transaction File Information --------------------------
#   Transaction File Name:                       ./Datasets/CollectionInPAFIFormat.txt
#   Number of Input Transactions:                22
#   Number of Distinct Edge Labels:              1
#   Number of Distinct Vertex Labels:            43
#   Average Number of Edges In a Transaction:    3
#   Average Number of Vertices In a Transaction: 12
#   Max Number of Edges In a Transaction:        15
#   Max Number of Vertices In a Transaction:     39
# 
# Options -----------------------------------------------
#   Min Output Pattern Size:                     1
#   Max Output Pattern Size:                     2147483647(INT_MAX)
#   Min Support Threshold:                       9.0% (2 transactions)
#   Generate Only Maximal Patterns:              No
#   Generate PC-List:                            Yes
#   Generate TID-List:                           Yes
# 
# Outputs -----------------------------------------------
#   Frequent Pattern File:                       ./Datasets/CollectionInPAFIFormat.fp
#   PC-List File:                                ./Datasets/CollectionInPAFIFormat.pc
#   TID-List File:                               ./Datasets/CollectionInPAFIFormat.tid
# 
t # 1-0, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#ChiSquared
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection
u 0 1 informBy
t # 1-1, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#ChiSquared
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
u 0 1 informBy
t # 1-2, 3
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#Validator
u 0 1 informBy
t # 1-3, 3
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
u 0 1 informBy
t # 1-4, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#SmallWords
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#TermWeighting
u 0 1 informBy
t # 1-5, 4
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#SmallWords
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#Stemmer
u 0 1 informBy
t # 1-6, 9
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#SmallWords
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#StopWords
u 0 1 informBy
t # 1-7, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatSim
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#TermWeighting
u 0 1 informBy
t # 1-8, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#Multi2Single
u 0 1 informBy
t # 1-9, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#Validator
u 0 1 informBy
t # 1-10, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#CorrelationScore
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection
u 0 1 informBy
t # 1-11, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#CorrelationScore
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
u 0 1 informBy
t # 1-12, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#Stemmer
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#TermWeighting
u 0 1 informBy
t # 1-13, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#PorterStemmer
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#SmallWords
u 0 1 informBy
t # 1-14, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#PorterStemmer
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#TF_IDF
u 0 1 informBy
t # 2-0, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#ChiSquared
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
u 0 1 informBy
u 0 2 informBy
t # 2-1, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#Stemmer
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#SmallWords
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#TermWeighting
u 0 1 informBy
u 0 2 informBy
t # 2-2, 4
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#SmallWords
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#Stemmer
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#StopWords
u 0 1 informBy
u 0 2 informBy
t # 2-3, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#Validator
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
u 0 1 informBy
u 0 2 informBy
t # 2-4, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#Validator
u 0 1 informBy
u 0 2 informBy
t # 2-5, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#SmallWords
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#PorterStemmer
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#StopWords
u 0 1 informBy
u 0 2 informBy
t # 2-6, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#PorterStemmer
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#SmallWords
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#TF_IDF
u 0 1 informBy
u 0 2 informBy
t # 2-7, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#TermWeighting
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatSim
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#SmallWords
u 0 1 informBy
u 0 2 informBy
t # 2-8, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#ChiSquared
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection
u 0 1 informBy
u 0 2 informBy
t # 2-9, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#SmallWords
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#StopWords
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#TermWeighting
u 0 1 informBy
u 0 2 informBy
t # 2-10, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#ChiSquared
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
u 0 1 informBy
u 0 2 informBy
t # 2-11, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#ChiSquared
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#Validator
u 0 1 informBy
u 0 2 informBy
t # 3-0, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#ChiSquared
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
u 0 1 informBy
u 0 2 informBy
u 1 2 informBy
t # 3-1, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#SmallWords
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#TermWeighting
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatSim
v 3 http://www.isi.edu/ac/TextAnalytics/library.owl#StopWords
u 0 1 informBy
u 0 3 informBy
u 1 2 informBy
t # 3-2, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#PorterStemmer
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#SmallWords
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#StopWords
v 3 http://www.isi.edu/ac/TextAnalytics/library.owl#TF_IDF
u 0 1 informBy
u 0 3 informBy
u 1 2 informBy
t # 3-3, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#Validator
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
v 3 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
u 0 1 informBy
u 0 2 informBy
u 1 3 informBy
t # 3-4, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#ChiSquared
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
v 3 http://www.isi.edu/ac/TextAnalytics/library.owl#Validator
u 0 1 informBy
u 0 2 informBy
u 0 3 informBy
t # 3-5, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#ChiSquared
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
v 3 http://www.isi.edu/ac/TextAnalytics/library.owl#Validator
u 0 1 informBy
u 0 2 informBy
u 1 3 informBy
t # 3-6, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#Validator
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#ChiSquared
v 3 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
u 0 1 informBy
u 0 2 informBy
u 1 3 informBy
t # 3-7, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#SmallWords
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#Stemmer
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#StopWords
v 3 http://www.isi.edu/ac/TextAnalytics/library.owl#TermWeighting
u 0 1 informBy
u 0 2 informBy
u 1 3 informBy
t # 3-8, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#ChiSquared
v 3 http://www.isi.edu/ac/TextAnalytics/library.owl#Validator
u 0 1 informBy
u 0 3 informBy
u 1 2 informBy
t # 4-0, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#ChiSquared
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
v 3 http://www.isi.edu/ac/TextAnalytics/library.owl#Validator
u 0 1 informBy
u 0 2 informBy
u 0 3 informBy
u 1 2 informBy
t # 4-1, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#Validator
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#ChiSquared
v 3 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
v 4 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
u 0 1 informBy
u 0 2 informBy
u 0 3 informBy
u 1 4 informBy
t # 4-2, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#Validator
v 3 http://www.isi.edu/ac/TextAnalytics/library.owl#ChiSquared
v 4 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
u 0 1 informBy
u 0 2 informBy
u 1 3 informBy
u 2 4 informBy
t # 4-3, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#ChiSquared
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#Validator
v 3 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
v 4 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
u 0 1 informBy
u 0 3 informBy
u 1 2 informBy
u 2 4 informBy
t # 5-0, 2
v 0 http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection
v 1 http://www.isi.edu/ac/TextAnalytics/library.owl#ChiSquared
v 2 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
v 3 http://www.isi.edu/ac/TextAnalytics/library.owl#Validator
v 4 http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff
u 0 1 informBy
u 0 2 informBy
u 0 3 informBy
u 1 2 informBy
u 3 4 informBy
#   Size Candidates Frequent Patterns 
#   1               15                
#   2               12                
#   3    12         9                 
#   4    4          4                 
#   5    1          1                 
# 
#   Largest Frequent Pattern Size:               5
#   Total Number of Candidates Generated:        17
#   Total Number of Frequent Patterns Found:     41
# 
# Timing Information ------------------------------------
#   Elapsed User CPU Time:                       -0.0[sec]
# *******************************************************
