------------------------------------------------------------------------------
README.txt
------------------------------------------------------------------------------
To Run the Mathdoku Application:
1. Unzip the zip file into a single folder. Within this should be a sub folder 
   containing all the images, source files and javafx dependencies jars.
2. Compile all the java source code for the mathdoku game using the following 
   in command prompt all in one line:

    javac -cp "javafx-swt.jar;javafx.base.jar;javafx.controls.jar;javafx.fxml.jar;javafx.graphics.jar;javafx.media.jar;javafx.swing.jar;javafx.web.jar" -target 8 -source 8 *.java
3. Then create a runnable executable jar file for the application. A manifest
   file ("manifest.txt") has been included:
    
   jar cmf manifest.txt <name-of-new-jar-file> *.class *.png

4. Then to run the jar, use

   java -jar <name-of-new-jar-file> 

------------------------------------------------------------------------------
User Guide/ Functionalities
------------------------------------------------------------------------------
______________________________________________________________________________

When First Running the Application
______________________________________________________________________________
When the application is started, the first window to appear will contain a 
default grid. Above the grid from left to right are the undo, redo, clear all, 
clear, and generate a new game. On the right are the load from file, load from 
text input, increase font size, and decrease font size button. Finally, there is
the show mistake checkbox, and the number entry buttons.

_______________________________________________________________________________

To Complete the Grid - Entering, Selecting, Clearing Cells, Highlighting 
                       Mistakes, and Winning
_______________________________________________________________________________
Click on the desired cell, or press press the A, W, S, or D keys to select cells
(current selected cells will have a pink box around them).
Then to enter a number press the corresponding key on the keyboard;
alternatively, use the number buttons below the grid. Invalid numbers will
not be entered in the cell.

To clear the current cell, use the "Clear" button positioned above or the 
backspace key; to clear the entire grid, use the "Clear All" button. 
If an action can be undone or redone, the corresponding arrow buttons above 
the grid will be highlighted.

There is also an option to indicate mistakes. If cages aren't completed 
correctly (or at all) they will be highlighted red, whereas incorrect
columns/rows will be highlighted by a magenta shape covering the whole of 
the column/row.

When all the grid has been completed successfully, the animation will be 
started.
________________________________________________________________________________

To Change the Font Size
________________________________________________________________________________
Use the "+font" or "-font" buttons to increase or decrease the size of the cage
labels and contents. There is a mediam, big, or small font.

________________________________________________________________________________

Starting a new game - Loading a file, Reading Text Input, and Random Generation
________________________________________________________________________________
When the "Load from File" button is pressed, a file explorer window will be opened.
A game data file (with the same format specified by the coursework instructions)
can then be loaded from the file system. However, if the file is invalid, an  
error message will pop up.

Alternatively, use the "Load from Text Input" to bring up a new window.
Input game data (with the same format as a text file) can be directly into the 
textbox and press submit.

For random generation of games, press the "Generate New Game Button". Enter the 
seed for the random number generation, select the maximum for the number of 
cells in the cage, and the grid size. Then click submit.
_________________________________________________________________________________

