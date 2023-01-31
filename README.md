# Piazza Panic
Piazza Panic is the Engineering 1 module game that we have been tasked with making.

We are Cohort 1, Team 9 and this is our game!

## How to build and run
In order to build this project then you will need to have Java JDK version 11.

Import the gradle project then run <code>PiazzaPanicLauncher.java</code> and if you're on macOS then you'll need to add <code>-XstartOnFirstThread</code> as a Java JVM argument.

To run this, we recommend that you execute it from a terminal for Windows/Linux users use this command: <code>java -jar PiazzaPanic.jar</code> but if you're on macOS then you need to run this command: <code>java -jar -XstartOnFirstThread PiazzaPanic.jar</code>

### Optional arguments
The program has some optional arguments that can be appended in order to change the functionality, they are listed below.

<code>--help</code> to display the list of arguments from the command line

<code>--assessment=[assessment number]</code> This will determine what assessment to initialise, it will default to 1 if none is specified. Valid options are: <code>1</code> or <code>2</code>

<code>--difficulty=[difficulty]</code> This will set the difficulty level of the game, the valid options are: <code>easy</code>, <code>medium</code> and <code>hard</code>. Easy will have 3 customers, medium has 5 and hard will have 8 customers. Defaults to medium and is case sensitive.

<code>--loci=[distance]</code> This sets the distance that you can be within of a station in order to interact with it, the default is 40 (in pixels). Valid options are from 5 until the limit of a float variable. P.S. I know this isn't a true loci so to the mathematicians I apologise.

<code>-v</code> Verbose mode, spits out everything it's doing.