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

 