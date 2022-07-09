
###Introduction:
- This application is a simple implementation for demand supply matching algorithm.

###Prerequisite:

- Java 11

###How to test this application?
- Just execute the tests present in DemandSupplyMatcherTest.
- You can check console for the Input and Output.

###Design Patterns Used:
- Factory Design pattern.

###Data Structures Used:
- Priority queue as priority is based on lower supply price -  higher demand price.
- If the price is same then consider FIFO like functionality based on the time the order was placed.
- ArrayList to store the input and output.
- ConcurrentHashMap to make thread safe.# online-market-maker
