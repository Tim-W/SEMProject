#Testing Document Sprint Week 4

##Total branch coverage: 65%-65%
For our project we mainly looked at branch coverage. We did this because a lot of our code
is GUI code which cant be tested. Usually GUI code does not consist of a lot of branches so
by measuring branch coverage we thought we had the most trustworthy metric.
We went up 1 percent. This time we had a full MVC-model between GameController and GameScene, 
which left one of our test classes unusable, but we tested other classes which raised it again.

###nl.tudelft.sem.group2 74%-70%
We went down 4 percent, mainly because we moved some classes.
AreaTracker and Logger are still at a nice 79% and 83%.

###nl.tudelft.sem.group2.collisions 85%
This package's main class is the collisionhandler, with a nice 85% branch coverage

###nl.tudelft.sem.group2.global.globals 0%
This class is just a list of constants, no need for testing.

###nl.tudelft.sem.group2.scenes 0%
We haven't made any test classes for hits yet, since these are GUI classes.

###nl.tudelft.sem.group2.controllers 0%
We actaully had a test suite for this class, but the split to MVC made it unusable 
and we did not have the time to rewrite it yet.

###nl.tudelft.sem.group2.units 97%-89%
The units package has been tested for 89% branch coverage. We tested the draw methods
using Mockito, this is why the percentage is so high. Some classes even tested with 100%.
The Stix class is still untested, so we dropped to 89.

###nl.tudelft.sem.group2.sound 0%
This class is just a sound handler, so we did not test it.