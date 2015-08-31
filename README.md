# COMP90049-P1
COMP90049-P1 for Knowledge Technology at Unimelb

===============================
2015-08-31 19:48
Mark.

I noticed a strange phenomenon that if I use MapMemory to do the process rather than direct scanner IO stream,
the first time the program runs,MapMemory approach will be much slower than direct scanner IO stream

HOWEVER, rerun the program will give MapMemory a huge boost,
and reduce the running time to the scale half as the direct scanner IO stream

see the two test method in Test.java
->public static void testMap(String filePath)
and 
->public static void testScannerIOStream(String filePath)

