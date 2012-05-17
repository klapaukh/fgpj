This is the readme for FGPJ. Users who are already familiar with
the RMITGP package will find this to be very similar, as the design
is based off it. However, the code here has has substantial changes
applied in order to move it into Java.

This package is intended for strongly typed Forest Genetic 
Programming. Each node has a fixed return type and expects fixed 
types for its child nodes. The library will automatically ensure
that only correctly typed nodes are joined together, and so it is 
safe to use any number of types in the same run.  

In standard GP there can only be one root node. However, in this 
version there can be any fixed positive integer number of root nodes
(1 and up). This allows for a GP program to return multiple values.
As each program is still a tree the resulting program trees inside 
each program are non overlapping.


----------------------------------

Installation:

Not really important as it is java, so installation isn't 
so much of a thing. You can either just add the jar onto the build
path of your project and gain access to the classes inside of it in 
that manner, or you can import the jar into the project directly.

Option 1: Import the jar (archive file) into eclipse

Option 2: Unzip the jar into some directory

Option 3: Add it to the build path


----------------------------------

Running:

If you wish to write your own code, then you will have to write the 
entry points yourself. On the other hand, to run the symbolic 
regression example:

1) go to the symbolicRegression package

2) Run the SymbMain class

If you wish to modify the parameters you can change settings in the
SymbMain class directly. To change the fitness function you will
need to modify the SymbolicFitness class that can be found in the
same package. 

Both SymbMain and SymbolicFitness contain comments attempting to
explain how to use and modify them.

The test package contains a small set of JUnit tests which can be
run if desired.

The symbolicFitness.bad package contains implementations of X,Y, 
SymbolicFitness and ReturnDouble that while are technically correct,
are not safe to use with the ParallelFitness class due to concurrency
bugs. There is no performance benefit in using them, they are simple
provided as an example of how a sensible sequential idea can break 
with concurrency.

The symbolicFitness.image package contains the additional files needed
to use mathematical functions as used in symbolic regression to
generate images. The fitness function attempts to copy a given image
in ppm P3 format. The image can be of any size. Unfortunately, the
final results of running this, so far have not produced any good
results. Notably it does show off how a GP tree can have multiple 
roots, and has a separate root for each color channel (RGB).

Finally the examples.lines package is an example of using GP to 
copy a given image (ppm image in P3 format or any size). The GP tree
is made up of different shapes, colors, and null nodes to terminate
the tree. This is a less standard example, but comes out of research
this package was originally written for. 
 