
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage; 

/**
 * This will be the main class for the game.
 * @author pkll1g19
 *
 */
public class Game extends Application {
  /**
   * Width of the window
   */
  private int WINDOWSIZE = 800;
  /**
   * Size of the grid.
   */
  private int GRIDSIZE = (int) (WINDOWSIZE * 0.7);
  /**
   * Grid will be size type X type cells.
   */
  private int type = 6;
  /**
   * The pane containing the grid.
   */
  private Pane gridPane;
  /**
   * The border pane.
   */
  private BorderPane pane;
  /**
   * The array for storing all the rectangles.
   */
  private GCell[][] grid;
  /** 
   * The array list for all the cages created. 
   */
  private List<Cage> cages = new ArrayList<Cage>();
  /**
   * Object that will be used to highlight rows and columns.
   */
  private MistakeChecker mistakeChecker;
  /**
   * Contains the history of the action performed.
   */
  private History history;
  /**
   * The cell that has been currently selected.
   */
  private GCell selected;

    
  /**
   * Launches the application.
   * @param args  command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
  
  /**
   * This will create the main user interface.
   */
  public void start(Stage primaryStage) {
    /** Creates the primary pane and scene. */
    pane = new BorderPane();
    pane.setPrefSize(WINDOWSIZE, WINDOWSIZE);
    pane.setPadding(new Insets(30));
    primaryStage.setScene(new Scene(pane));


    /** Creates the history object to keep tracks of actions performed. */
    history = new History(type);
    
    /** Creates a highlighter object to highlight rows/columns */
    mistakeChecker = new MistakeChecker(pane, type);
    
    /** Adds the buttons around the grid. */
    addControls(pane);
    createGrid(true);   
    
    /** Adds some finishing touches to and shows the pane. */
    primaryStage.setTitle("Mathdoku");
    primaryStage.show();   
  }
  
  /**
   * Creates the buttons around the grid.
   */
  private void addControls(BorderPane primaryPane) {

    /** Adds the top pane. */
    HBox topPane = new HBox();
    topPane.setPrefSize(600, 100);
    topPane.setPadding(new Insets(20, 20, 20, 0));
    topPane.setSpacing(20);
    primaryPane.setTop(topPane);
    
    /** Adds the side panel for the saving and loading. */
    VBox sidePane = new VBox();
    sidePane.setPrefSize(100, 600);
    sidePane.setPadding(new Insets(20));
    sidePane.setSpacing(20);
    primaryPane.setRight(sidePane);
    
    /**
     * Create the buttons for undoing, redoing, and clearing.
     */
    Image undoImage = new Image("undo.png", 50, 50, false, false);
    Button undoButton = new Button("", new ImageView(undoImage));   
    undoButton.setPrefSize(50, 50);
    undoButton.setOnAction(e -> history.undo(grid, mistakeChecker, cages));
    
    Image redoImage = new Image("redo.png", 50, 50, false, false);
    Button redoButton = new Button("", new ImageView(redoImage));
    redoButton.setPrefSize(50, 50);
    redoButton.setOnAction(e -> history.redo(grid, mistakeChecker, cages));
    history.setButtons(undoButton, redoButton);
    
    Button clearAllButton = new Button("Clear All");  
    clearAllButton.setOnAction(new EventHandler<ActionEvent>() {
      /** When the button is clicked, the grid will be cleared. */
      @Override
      public void handle(ActionEvent event) {
        history.addHistory(grid);
        ClearAll.clearAll(type, grid, cages, mistakeChecker);
      }     
    });
    
    /** Creates a button that will clear the selected cell. */
    Button clearButton = new Button("Clear");
    clearButton.setPrefSize(100, 100);
    clearButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                                new EventHandler<MouseEvent>() {
      /** Clears the selected cell and records this in history. */
      @Override
      public void handle(MouseEvent event) {
        history.addHistory(grid);
        selected.clear();
      }     
    });
    
    /** Adds a button that will generate a new game randomly. */
    Button generateGameButton = new Button("Generate a New Game");
    generateGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, 
                                       new GenerationHandler());
    
    topPane.getChildren().addAll(undoButton, redoButton, clearAllButton,
                                 clearButton, generateGameButton);
    
    /** Creates the buttons for loading. */  
    Button loadButtonFile = new Button("Load\nfrom\nFile");
    loadButtonFile.setPrefSize(150, 200);   
    loadButtonFile.setOnAction(e -> new LoadFile());
    
    Button loadButtonText = new Button("Load\nfrom\nText\nInput");
    loadButtonText.setPrefSize(150, 200);
    loadButtonText.addEventHandler(MouseEvent.MOUSE_CLICKED, 
                                   new LoadTextHandler());   
    
    /** Create button for adjusting the font size. */
    Button fontIncreaseButton = new Button("+Font");
    fontIncreaseButton.addEventHandler(MouseEvent.MOUSE_CLICKED, 
                                       e -> changeFontSize(true));
    Button fontDecreaseButton = new Button("-Font");
    fontDecreaseButton.addEventHandler(MouseEvent.MOUSE_CLICKED, 
                                       e -> changeFontSize(false));
    
    sidePane.getChildren().addAll(loadButtonFile, loadButtonText, 
                                  fontIncreaseButton, fontDecreaseButton);
    
    /** Will be used to store buttons on the bottom pane. */
    HBox bottomPane = new HBox();
    primaryPane.setBottom(bottomPane);
    bottomPane.setSpacing(10);
    
    /** Check box for toggling whether mistakes should be shown. */
    CheckBox showMistakeBox = new CheckBox("Show mistakes");
    showMistakeBox.setStyle("-fx-font-size: 20; -fx-border-insets: -5;");
    showMistakeBox.setPadding(new Insets(20));
    showMistakeBox.addEventHandler(MouseEvent.MOUSE_CLICKED, 
                                   e -> mistakeChecker.toggleShowMistake(cages, 
                                                                         grid));
   
    BorderPane.setAlignment(showMistakeBox, Pos.CENTER);
    bottomPane.getChildren().add(showMistakeBox);
    
    /** Buttons the user can press to enter numbers into the boxes. */
    for (int number = 1; number <= 9; number++) {
      /** The number that will be entered when the button is clicked. */
      final int finalNumber = number;
      Button numberButton = new Button(Integer.toString(finalNumber));
      numberButton.addEventHandler(MouseEvent.MOUSE_CLICKED, 
                                   e -> selected.enterNumber(finalNumber, 
                                                             type));
      numberButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                                   e -> mistakeChecker.checkMistakes(cages, 
                                                                     grid));
      numberButton.setPrefSize(40, 40);
      bottomPane.getChildren().add(numberButton);
    }
    
    /** Adds the event listener for keys pressed by the user. */
    primaryPane.addEventHandler(KeyEvent.KEY_RELEASED, new KeyBoardHandler());
  } 
  
  /**
   * Creates the grid to be played on.
   * @param isDefault   whether a default grid is being drawn.
   */
  private void createGrid(boolean isDefault) {
    gridPane = new Pane();
    gridPane.setPrefSize(GRIDSIZE, GRIDSIZE);
    pane.setCenter(gridPane);
    BorderPane.setAlignment(gridPane, Pos.TOP_CENTER);
    
    /** Initialises the grid. */
    grid = new GCell[type][type];
    
    /** Draws squares for each cell and arranges then  */
    int cellWidth = GRIDSIZE / type;
    for (int i = 0; i < type; i++) {
        for (int j = 0; j < type; j++) {
            GCell cell = new GCell(i, j, cellWidth, gridPane, selected);
            gridPane.getChildren().add(cell);
            grid[j][i] = cell;                          
            /** Adds the event listener to check whether it has been selected. */
            cell.addEventHandler(MouseEvent.MOUSE_CLICKED, 
                                 new EventHandler<MouseEvent>() {
              /** Sets selected to the cell clicked. */
              @Override
              public void handle(MouseEvent event) {
                cell.select(selected);
                selected = cell;
              }        
            });
              
        }
    }
    
    if (isDefault) {
      /** Draws the cages. */
      cages.add(new AdditionCage(new GCell[] {grid[0][0], grid[1][0]}, gridPane, 11));
      cages.add(new DivisionCage(new GCell[] {grid[0][1], grid[0][2]}, gridPane, 2));
      cages.add(new SubtractionCage(new GCell[] {grid[1][1], grid[1][2]}, gridPane, 3));
      cages.add(new MultiplicationCage(new GCell[] {grid[0][3], grid[1][3]}, gridPane, 20));
      cages.add(new MultiplicationCage(new GCell[] {grid[0][4], grid[0][5], grid[1][5],
                         grid[2][5]}, gridPane, 6));
      cages.add(new MultiplicationCage(new GCell[] {grid[2][0], grid[2][1], grid[3][0], 
                                       grid[3][1]}, gridPane, 240));
      cages.add(new MultiplicationCage(new GCell[] {grid[4][0], grid[4][1]}, gridPane, 6));
      cages.add(new AdditionCage(new GCell[] {grid[5][0], grid[5][1], grid[5][2]}, gridPane,
                                 8));
      cages.add(new MultiplicationCage(new GCell[] {grid[2][2], grid[2][3]}, gridPane, 6));
      cages.add(new DivisionCage(new GCell[] {grid[1][4], grid[2][4]}, gridPane, 3));
      cages.add(new MultiplicationCage(new GCell[] {grid[3][2], grid[4][2]}, gridPane, 6));
      cages.add(new AdditionCage(new GCell[] {grid[3][3], grid[4][3], grid[4][4]}, gridPane, 
                                 7));
      cages.add(new MultiplicationCage(new GCell[] {grid[3][4], grid[3][5]}, gridPane, 30));
      cages.add(new DivisionCage(new GCell[] {grid[5][3], grid[5][4]}, gridPane, 2));
      cages.add(new AdditionCage(new GCell[] {grid[4][5], grid[5][5]}, gridPane, 9));
    }
  }  
 
  /**
   * Resets the state of the game.
   * @param gameData    used to extract data from a text area/file.
   */
  private void resetGame(GameData gameData) {
    /** Removes the grid from the pane. */
    gridPane.getChildren().clear();
    grid = null;
    /** Removes all the cages. */
    cages.clear();
    /** Resets the history. */
    history.clear();
    /** Unselects a cell. */
    selected = null;
    
    /** Removes everything from the mistake checker. */
    mistakeChecker.resetMistakeChecker();   
    
    GameData data = gameData;
    /** Recreates the grid. */
    type = data.getSize();

    createGrid(false);
    
    /** Updates the size of the history object and mistake checker. */
    history.setSize(type);
    mistakeChecker.setType(type);
    
    /** Creates the cages. */
    for (int i = 0; i < data.getCages().size(); i++) {
      /** 
       * Finds the cells within the cage.
       */
      GCell[] cageCells = new GCell[data.getCages().get(i).size()];
      int index = 0;
      for (Integer cellNo : data.getCages().get(i)) {
        cageCells[index] = grid[Math.floorDiv(cellNo - 1, type)][(cellNo - 1) % type];
        index++;
      }
      switch (data.getOperators().get(i)) {
        case ADD:
          cages.add(new AdditionCage(cageCells, gridPane, 
                                     data.getTargets().get(i)));
          break;
        case MINUS:
          cages.add(new SubtractionCage(cageCells, gridPane, 
                                        data.getTargets().get(i)));
          break;
        case DIVIDE:
          cages.add(new DivisionCage(cageCells, gridPane, 
                                     data.getTargets().get(i)));
          break;
        case TIMES:
          cages.add(new MultiplicationCage(cageCells, gridPane, 
                                           data.getTargets().get(i))); 
      }
    }
  }
  
  /** 
   * Will detect if there's any keys pressed; if so then it will enter a 
   * number, clear the selected cell, change the selected cell etc.
   */
  private class KeyBoardHandler implements EventHandler<KeyEvent> {
    /** 
     * When it detects a key press, it checks which key press it then 
     * performs the suitable action.
     */
    @Override
    public void handle(KeyEvent event) {
      selected = selected == null ? grid[0][0] : selected;
      GCell newSelected = selected;
     
      switch (event.getCode()) {
        /** Selects the cell around the one currently selected. */
        case D:
          /** Checks that there is a right cell. */
          if (selected.getRow() < type - 1) {
            newSelected = grid[selected.getColumn()][selected.getRow() + 1];
          }
          break;
        case A:
          /** Checks that there is a left cell. */
          if (selected.getRow() > 0) {
            newSelected = grid[selected.getColumn()][selected.getRow() - 1];
          }
          break;
        case W:
          /** Checks that there is a cell above that can be selected. */
          if (selected.getColumn() > 0) {
            newSelected = grid[selected.getColumn() - 1][selected.getRow()];
          }
          break;
        case S:
          /** Checks that there is a cell below that can be selected. */
          if (selected.getColumn() < type - 1) {
            newSelected = grid[selected.getColumn() + 1][selected.getRow()];
          }
          break;
        /** Clears a cell. */
        case BACK_SPACE:
          history.addHistory(grid);
          selected.clear();
          break;        
      default:
        break;
      }
      
      /** Checks if the user has used awsd to select a new cell. */
      if (newSelected != selected) {
        newSelected.select(selected);
        selected = newSelected;
      }
      
      /** If a new number is entered then it is recorded. */
      if (event.getCode().isDigitKey() || event.getCode().isKeypadKey()) {
        /** New value entered. */
        int value = 0;
        /** Enters the corresponding number when number key pressed. */
        switch (event.getCode()) {
          case NUMPAD1:
          case DIGIT1:
            value = 1;          
            break;
          case NUMPAD2:
          case DIGIT2:
            value = 2;
            break;
          case NUMPAD3:
          case DIGIT3:
            value = 3;
            break;
          case NUMPAD4:
          case DIGIT4:
            value = 4;
            break;
          case NUMPAD5:
          case DIGIT5:
            value = 5;
            break;
          case NUMPAD6:
          case DIGIT6:
            value = 6;
            break;
          case NUMPAD7:
          case DIGIT7:
            value = 7;
            break;
          case NUMPAD8:
          case DIGIT8:
            value = 8;
            break;
          case NUMPAD9:
          case DIGIT9:
            value = 9;
            break;
        default:
          break;
        }
        history.addHistory(grid);
        selected.enterNumber(value, type);
      }
      
      /** Checks if there are any mistakes. */
      mistakeChecker.checkMistakes(cages, grid);
    }    
  }
  
  /**
   * This object will be created when the user wants to load a file.
   * This essentially opens up window where they can choose a file.
   * @author starp
   *
   */
  private class LoadFile {
    /**
     * @overrides loadStage() creates the file name.
     * Creates the stage, and adds the file chooser.
     */
    public LoadFile() { 
      Stage loadFileStage = new Stage();
      
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open Game File");
      File file = fileChooser.showOpenDialog(loadFileStage);
      
      /** If the file has chosen a file. */
      if (file != null) {
        /** Uses a file loader to interpret the file. */
        FileLoader loader = new FileLoader(file.getAbsolutePath());
        /** If the file has been succesfully loaded. */
        if (loader.getGameData() != null) {
          /** Resets the game. */
          resetGame(loader.getGameData()); 
        }
      }
    }         
  }
  
  /**
   * Event handler for the load from text input event handler.
   * @author starp
   *
   */
  private class LoadTextHandler implements EventHandler<MouseEvent> {
    private VBox loadPane;
    
    /**
     * @param event   the mouse event.
     */
    public void handle(MouseEvent event) {
      loadStage();
    }
            
    /**
    * Creates the stage.
    * @ Creates the text area. 
    */
    private void loadStage() {
      Stage loadStage = new Stage();
      
      loadPane = new VBox();
      loadStage.setTitle("Load from file");
      loadStage.setScene(new Scene(loadPane));            
      loadPane.setPrefSize(300, 150);
      loadPane.setPadding(new Insets(10));
      loadPane.setSpacing(10);
      
      
      Button submitButton = new Button("Submit"); 
      loadPane.setAlignment(Pos.CENTER);
      loadPane.getChildren().add(submitButton);
      loadStage.show();        
      
      loadPane.setPrefSize(800, 800);
      
      /** File name label. */
      Label fileNameLabel = new Label();
      fileNameLabel.setText("Enter data for the new game:");
      loadPane.getChildren().add(fileNameLabel);
      
      /** HBox is for storing the text field. */
      HBox inputDataPane = new HBox();
      loadPane.getChildren().add(inputDataPane);
      inputDataPane.setPrefSize(800, 800);
      /** Creates the text field to hold the file name. */
      TextArea inputDataField = new TextArea();
      inputDataField.setPrefWidth(40);
      inputDataField.setPrefColumnCount(5);
      inputDataField.setMaxWidth(Double.POSITIVE_INFINITY);
      inputDataField.setMaxHeight(Double.POSITIVE_INFINITY);
      inputDataPane.getChildren().add(inputDataField);
      
      /** Allows the input field to grow. */
      VBox.setVgrow(inputDataPane, Priority.ALWAYS);
      HBox.setHgrow(inputDataField, Priority.ALWAYS);
      

      submitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          FileLoader loader = new FileLoader(inputDataField);
          /** If the file has been successfully loaded. */
          if (loader.getGameData() != null) {
            /** Resets the game. */
            resetGame(loader.getGameData()); 
          }
      }       
      });
    } 
  }
  
  /**
   * Class for opening up a form which allows the user to select settings for
   * generating a new game.
   * @author starp
   *
   */
  private class GenerationHandler implements EventHandler<MouseEvent> {
    /**
     * Will be used to select the dimensions.
     */
    private ChoiceBox<String> dimensionChoiceBox = new ChoiceBox<String>();
    /**
     * The random seed that will be used in the random number generator.
     */
    private TextField seedField;
    /**
     * The maximum number of cells in a cage. 
     */
    private Slider maxSlider;
    /**
     * Closes the stage.
     */
    private Stage loadStage;
    
    /**
     * @param event     the mouse event.
     */
    @Override
    public void handle(MouseEvent event) {
      loadStage();
    } 
    
    /** 
     * Loads a window that allows the user to select whether they want to 
     * generate a new game.
     */
    private void loadStage() {
      loadStage = new Stage();
      VBox loadPane = new VBox();
      loadPane.setPadding(new Insets(20));
      loadPane.setPrefSize(500, 200);
      loadPane.setSpacing(20);
      loadStage.setScene(new Scene(loadPane));
      loadStage.setTitle("Generate a new game");
            
      /** Label and box for specifying the maximum size of a cage. */
      Label maxLabel = new Label("Cage size: ");
      Label maxValueLabel = new Label("4");
      maxSlider = new Slider(2, 4, 4);
      /** Updates the label each time the slider is changed. */
      maxSlider.valueProperty().addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, 
                            Number oldValue, Number newValue) {
          maxValueLabel.setText(Double.toString(maxSlider.getValue()));
        }
      });
      maxSlider.setMajorTickUnit(1);
      maxSlider.setMinorTickCount(0);
      maxSlider.setSnapToTicks(true);
      HBox maxBox = new HBox();
      maxBox.setSpacing(10);
      maxBox.getChildren().addAll(maxLabel, maxSlider, maxValueLabel);
      
      /** Label and text box that allows the user to choose a random seed. */
      Label seedLabel = new Label("Seed for random generation:");
      seedField = new TextField();
      seedField.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, 
                            String oldValue, String newValue) {
          /** If the value entered by the user isn't an integer. */
          if (!newValue.matches("\\d{0,8}")) {
            seedField.setText(oldValue);
          }
        }  
      });
      HBox seedBox = new HBox();
      seedBox.setSpacing(10);
      seedBox.getChildren().addAll(seedLabel, seedField);
     
      /** Label and box for selecting the dimension of the new game. */
      Label dimensionLabel = new Label("Grid size:");
      dimensionChoiceBox = new ChoiceBox<String>();
      /** Adds the different dimensions to the box. */
      for (int i = 2; i <= 9; i++) {
        dimensionChoiceBox.getItems().add(String.format("%d x %d", i, i));
      }
      dimensionChoiceBox.setValue("2 x 2");
      dimensionChoiceBox.valueProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, 
                            String oldValue, String newValue) {
          int size = Integer.parseInt((newValue.split(" x "))[0]);
          maxSlider.setMax(size * size);
        }
      });
      HBox dimensionBox = new HBox();
      dimensionBox.setSpacing(10);
      dimensionBox.getChildren().addAll(dimensionLabel, dimensionChoiceBox);
      
      /** Submit button. */
      Button submitButton = new Button("Submit");
      submitButton.setOnAction(e -> generateNewGame());
      
      loadPane.getChildren().addAll(dimensionBox, seedBox, maxBox, 
                                    submitButton);
      
      loadStage.show();
    }
    
    /**
     * Generates a new game and restarts.
     */
    private void generateNewGame() {
      try {
        PuzzleGenerator generator;
        
        String seedString = seedField.getText();
        /** Uses the correct puzzle generator constructor. */
        if (!seedString.equals(new String(""))) {
          generator = new PuzzleGenerator(Integer.parseInt(seedString));
        }
        else {
          generator = new PuzzleGenerator();
        }
        
        /** Size of the grid. */
        int size = Integer.parseInt((dimensionChoiceBox.getValue().split(" x "))[0]); 
        /** Sees if the user has entered a maximum cage size. */
        
        resetGame(generator.createGameData(size, (int)maxSlider.getValue()));
        loadStage.close();
      }
      catch (Exception e) {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setContentText("Failed to generate a new game.");
        errorAlert.show();
        System.err.println(e);
        e.printStackTrace(System.out);
      }
    }
  }
  
  /**
   * Class that changes the size of the font. 
   * @param isIncreasing    whether they want to increase the font. 
   */
  public void changeFontSize(boolean isIncreasing) {
    /** Changes the size of the label for each cage. */
    for (Cage cage : cages) {
      cage.changeLabel(isIncreasing);
    }
    /** Changes the font size in each cell. */
    for (int i = 0; i < type; i++) { 
      for (int j = 0; j < type; j++) {
        grid[i][j].changeFontSize(isIncreasing, type);
      }
    }
  }
}
