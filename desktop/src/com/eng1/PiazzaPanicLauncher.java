package com.eng1;

import com.badlogic.gdx.backends.lwjgl3.*;
import com.eng1.enums.Difficulty;
import com.eng1.enums.Mode;

import java.util.Arrays;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class PiazzaPanicLauncher {

	static Mode mode = Mode.ASSESSMENT_1;
	static Difficulty difficulty = Difficulty.MEDIUM;
	static float loci = 40f;
	static boolean verbose = false;

    public static void main(String[] args) {
        final int width = 1280, height = 720;
        boolean run = argumentHandler(args);
		if (verbose)
			System.out.println(Arrays.toString(args));
		System.out.printf("Assessment mode - %s\nDifficulty setting - %s\nLoci set %f\nVerbose mode - %s\n", mode.toString(), difficulty.toString(), loci, verbose ? "Enabled" : "Disabled");
        if (run) {
            Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
            config.setForegroundFPS(60);
            config.useVsync(true);
            config.setWindowedMode(width, height);
            config.setTitle("Piazza Panic");
            new Lwjgl3Application(new PiazzaPanic(width, height, mode, loci, difficulty, verbose), config);
        }
    }

	private static boolean argumentHandler(String[] args) {
		for (String arg : args) {
			arg = arg.replaceAll("-", "");
			String[] split = arg.split("=");
			if (split.length > 2)
				throw new RuntimeException("Invalid program argument!");
			switch (split[0]) {
				case "help":
					System.out.println("\nWelcome to Kitchen Tossups' Piazza Panic!\n" +
							"This is the help page and will assist you with what you will need in terms of arguments.\n\n" +
							"List of valid arguments are:\n\n" +
							"-------------------------------------------------------------------------------------------\n" +
							"  --help                            | Displays the current help screen you are on.\n" +
							"                                    |\n" +
							"  --assessment=<assessment number>  | This will determine what assessment to initialise,\n" +
							"                                    | it will default to 1 if none is specified.\n" +
							"                                    | Valid options are: 1 or 2\n" +
							"                                    |\n" +
							"  --difficulty=<difficulty>         | This will set the difficulty level of the game,\n" +
							"                                    | the valid options are: \"easy\", \"medium\" and \"hard\".\n" +
							"                                    | Easy will have 3 customers, medium has 5 and\n" +
							"                                    | hard will have 8 customers. Defaults to medium\n" +
							"                                    | and is case sensitive.\n" +
							"                                    |\n" +
							"  --loci=<distance>                 | This sets the distance that you can be within of\n" +
							"                                    | a station in order to interact with it,\n" +
							"                                    | the default is 40 (in pixels).\n" +
							"                                    | Valid options are from 5 until the limit of\n" +
							"                                    | a float variable. P.S. I know this isn't a true\n" +
							"                                    | loci so to the mathematicians I apologise.\n" +
							"                                    |\n" +
							"  -v                                | Verbose mode, spits out everything it's doing.\n" +
							"                                    |\n" +
							"  -XstartOnFirstThread              | This one is relevant if you're on macOS...\n" +
							"                                    | if you are getting an error (look below) then add\n" +
							"                                    | this to the JVM arguments and not the\n" +
							"                                    | program arguments. Below is the mentioned error.\n" +
							"                                    | \"GLFW may only be used on the main thread and that\n" +
							"                                    | thread must be the first thread in the process.\"\n" +
							"-------------------------------------------------------------------------------------------\n");
					return false;
				case "assessment":
					if (split.length != 2)
						throw new RuntimeException("Invalid use of assessment, use --help in order to find correct usage");
					try {
						int assessmentNumber = Integer.parseInt(split[1]);
						switch (assessmentNumber) {
							case 1:
								mode = Mode.ASSESSMENT_1;
								break;
							case 2:
								mode = Mode.ASSESSMENT_2;
								break;
							default:
								throw new RuntimeException("Invalid use of assessment, use --help in order to find correct usage");
						}
					} catch (Exception e) {
						throw new RuntimeException("Invalid use of assessment, use --help in order to find correct usage");
					}
					break;
				case "difficulty":
					if (split.length != 2)
						throw new RuntimeException("Invalid use of difficulty, use --help in order to find correct usage");
					switch (split[1]) {
						case "easy":
							difficulty = Difficulty.EASY;
							break;
						case "medium":
							difficulty = Difficulty.MEDIUM;
							break;
						case "hard":
							difficulty = Difficulty.HARD;
							break;
						default:
							throw new RuntimeException("Invalid use of difficulty, use --help in order to find correct usage");
					}
					break;
				case "loci":
					if (split.length != 2)
						throw new RuntimeException("Invalid use of loci, use --help in order to find correct usage");
					try {
						float lociHere = Float.parseFloat(split[1]);
						if (lociHere < 5f)
							throw new RuntimeException("Invalid use of assessment, use --help in order to find correct usage");
						loci = lociHere;
					} catch (Exception e) {
						throw new RuntimeException("Invalid use of assessment, use --help in order to find correct usage");
					}
					break;
				case "v":
					verbose = true;
					break;
				default:
					throw new RuntimeException("Invalid use of arguments, use --help in order to find correct usage");
			}
		}
		return true;
	}
}
