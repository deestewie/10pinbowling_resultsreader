
# Ten(10) Pin Bowling Results Reader & Scoreboard

## Project Details
The project takes as input a file, reads the content and outputs the scoreboard per players

## Setup the project

1. cd to your project root folder in command line.
2. Run the command => `mvn compile`. Ensure that you have internet connection as external plugins were used in this project.
3. Run the following command => `mvn exec:java -Dexec.mainClass=com.ten_ball_bowling_game.app.App`

## How to run the project from command line
1. Open a command prompt window and go to the directory where you saved the java program (App.java). Assume it's C:\.
2. Type 'javac App.java' and press enter to compile your code. If there are no errors in your code, the command prompt will take you to the next line (Assumption: The path variable is set).
3. Now, type ' java App ' to run your program.
4. Follow the instructions under the Steps to execute project header

## Running the jar version of the project
1.  Download the jar file from the release
2.  cd to the folder where you place the download jar in your command line.
3.  Run the following command `java -cp ten-ball-bowling-game-repo-1.0-SNAPSHOT.jar com.ten_ball_bowling_game.app.App`
4.  Follow the instructions under the Steps to execute project header

## Steps to execute project
1. Enter the absolute file path at the following message: `Enter file path below => : `
2. Enter the expected column separator for the file entered in step 4. The default is space separated.
