import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
 
 
/**
 * Board Game
 *
 * Version 1.0.0
 *
 * This class involves creating a game board, positioning insects and food points,
 * checking for various exceptions related to invalid inputs or game conditions,
 * and determining the movement and visibility of insects based on the direction they move.
 * @author Ahmed Baha Eddine Alimi
 */
 
public class Main {
    private static Board gameBoard;
    private static List<Insect> presentInsects;
    private static final int X_COORDINATE = 2;
    private static final int Y_COORDINATE = 3;
    private static final int MIN_BOARD_SIZE = 4;
    private static final int MAX_BOARD_SIZE = 1000;
    private static final int MIN_FOOD_POINTS = 1;
    private static final int MAX_FOOD_POINTS = 200;
    private static final int MIN_INSECTS = 1;
    private static final int MAX_INSECTS = 16;
    private static final int INPUT_OFFSET = 3;
 
    /**
     * Checks if the provided input for the board size is within the valid range.
     *
     * @param s The input string representing the board size.
     * @return The parsed integer value of the board size.
     * @throws InvalidBoardSizeException if the input is not within the valid range.
     */
    private static int boardSizeCheck(String s) throws Exception {
        int input;
        try {
            input = Integer.parseInt(s);
            if (input < MIN_BOARD_SIZE || input > MAX_BOARD_SIZE) {
                throw  new InvalidBoardSizeException();
            }
        } catch (NumberFormatException e) {
            throw new InvalidBoardSizeException();
        }
        return input;
    }
 
    /**
     * Checks if the provided input for the number of food points is within the valid range.
     *
     * @param s The input string representing the number of food points.
     * @return The parsed integer value of the number of food points.
     * @throws InvalidNumberOfFoodPointsException if the input is not within the valid range.
     */
    private static int foodPointCheck(String s) throws Exception {
        int input;
        try {
            input = Integer.parseInt(s);
            if (input < MIN_FOOD_POINTS || input > MAX_FOOD_POINTS) {
                throw  new InvalidNumberOfFoodPointsException();
            }
        } catch (NumberFormatException e) {
            throw new InvalidNumberOfFoodPointsException();
        }
        return input;
    }
    /**
     * Checks if the provided input for the number of insects is within the valid range.
     *
     * @param s The input string representing the number of insects.
     * @return The parsed integer value of the number of insects.
     * @throws InvalidNumberOfInsectsException if the input is not a valid integer
     * or falls outside the acceptable range.
     */
    private static int insectNumberCheck(String s) throws Exception {
        int input;
        try {
            input = Integer.parseInt(s);
            if (input < MIN_INSECTS || input > MAX_INSECTS) {
                throw  new InvalidNumberOfInsectsException();
            }
        } catch (NumberFormatException e) {
            throw new InvalidNumberOfInsectsException();
        }
        return input;
    }
 
 
    /**
     * Creates an instance of an insect based on the provided insect type, position, and color.
     *
     * @param insectType The type of insect to be created.
     * @param position   The position of the insect.
     * @param color      The color of the insect.
     * @return An instance of the specific insect type created.
     */
    private static Insect createInsect(String insectType, EntityPosition position, InsectColor color) {
        switch (insectType) {
            case "Grasshopper":
                return new Grasshopper(position, color);
            case "Butterfly":
                return new Butterfly(position, color);
            case "Ant":
                return new Ant(position, color);
            case "Spider":
                return new Spider(position, color);
            default:
                return null;
        }
    }
 
    /**
     * Checks for the presence of a duplicate insect based on color and type.
     *
     * @param insect The insect to be checked for duplication.
     * @throws DuplicateInsectException if a duplicate insect with the same color and type is found.
     */
    private static void duplicateInsectCheck(Insect insect) throws DuplicateInsectException {
        for (Insect currentInsect : presentInsects) {
            if (currentInsect.color == insect.color && currentInsect.toString().equals(insect.toString())) {
                throw new DuplicateInsectException();
            }
        }
    }
 
    /**
     * Generates an output string representing the information about an insect, its color, type, direction,
     * and the visible food points for that insect.
     *
     * @param visibleFoodPoints The number of visible food points for the insect.
     * @param insect            The insect for which the output is being generated.
     * @param direction         The direction in which the insect is moving.
     * @return A formatted string containing information about the insect's color, type,direction,
     * and visible food points.
     */
    private static String output(int visibleFoodPoints, Insect insect, Direction direction) {
        String insectColor = insect.color.capitalizeFirstLetter();
        String insectInfo = insect.toString();
        String directionText = direction.getTextRepresentation();
        return insectColor + ' ' + insectInfo + ' ' + directionText + ' ' + visibleFoodPoints + ' ' + '\n';
    }
 
 
 
    /**
     * This method reads input from a file, processes the inputs to create a game board with insects and food points,
     * determines the movement of insects, their visibility of food points,
     * and outputs the game state to an output file.
     *
     * @param args The command-line arguments passed to the program.
     * @throws IOException If an I/O error occurs while reading or writing files.
     */
 
    public static void main(String[] args) throws IOException {
        FileOutputStream outputFile = null;
        FileInputStream inputFile = null;
        try {
            outputFile = new FileOutputStream("output.txt");
            inputFile = new FileInputStream("input.txt");
 
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputFile))) {
                List<String> inputList = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    inputList.add(line);
                }
                presentInsects = new ArrayList<>();
                String[] input = inputList.toArray(new String[0]);
                int boardSize = boardSizeCheck(input[0]);
                gameBoard = new Board(boardSize);
                int foodNumber = foodPointCheck(input[2]);
                int insectNumber = insectNumberCheck(input[1]);
 
                for (int i = 0; i < insectNumber; i++) {
                    String[] info = input[i + INPUT_OFFSET].split(" ");
                    InsectColor color;
                    try {
                        color = InsectColor.valueOf(info[0].toUpperCase());
 
                    } catch (Exception exception) {
                        throw new InvalidInsectColorException();
                    }
                    switch (info[1]) {
                        case "Grasshopper":
                        case "Butterfly":
                        case "Ant":
                        case "Spider":
                            break;
                        default:
                            throw new InvalidInsectTypeException();
                    }
                    int x = Integer.parseInt(info[X_COORDINATE]);
                    int y = Integer.parseInt(info[Y_COORDINATE]);
                    EntityPosition position = EntityPosition.getPosition(x, y);
 
                    if (!gameBoard.checkPosition(position)) {
                        throw new InvalidEntityPositionException();
                    }
 
                    Insect insect = createInsect(info[1], position, color);
                    duplicateInsectCheck(insect);
 
                    gameBoard.addEntity(insect);
                    presentInsects.add(insect);
                    insect.setBoard(gameBoard);
                }
 
                for (int i = 0; i < foodNumber; i++) {
                    String[] foodInfo = input[i + INPUT_OFFSET + insectNumber].split(" ");
                    int amount = Integer.parseInt(foodInfo[0]);
                    int x = Integer.parseInt(foodInfo[1]);
                    int y = Integer.parseInt(foodInfo[2]);
                    EntityPosition position = EntityPosition.getPosition(x, y);
 
 
                    if (!(position.getX() >= 1 && position.getX() <= gameBoard.getSize()
                            && position.getY() >= 1 && position.getY() <= gameBoard.getSize())) {
                        throw new InvalidEntityPositionException();
                    }
                    FoodPoint foodPoint = new FoodPoint(position, amount);
                    gameBoard.addEntity(foodPoint);
                    foodPoint.setBoard(gameBoard);
                }
 
                for (int i = 0; i < presentInsects.size(); i++) {
                    Insect insect = presentInsects.get(i);
                    Direction direction = gameBoard.getDirection(insect);
                    int visibleFoodPoints = gameBoard.getDirectionVisibleFoodPoints(insect);
                    outputFile.write(output(visibleFoodPoints, insect, direction).getBytes());
                }
                outputFile.write("\n".getBytes());
                outputFile.close();
                inputFile.close();
            }
        } catch (IOException e) {
            outputFile.write(e.getMessage().getBytes());
            outputFile.write("\n".getBytes());
            outputFile.close();
            inputFile.close();
 
        } catch (Exception e) {
            outputFile.write(e.getMessage().getBytes());
            outputFile.write("\n".getBytes());
            inputFile.close();
        }
    }
 
}
 
 
 
class InvalidBoardSizeException extends Exception {
    @Override
    public String getMessage() {
        return ("Invalid board size");
    }
}
 
class InvalidNumberOfInsectsException extends Exception {
    @Override
    public String getMessage() {
        return ("Invalid number of insects");
    }
}
 
class InvalidNumberOfFoodPointsException extends Exception {
    @Override
    public String getMessage() {
        return ("Invalid number of food points");
    }
}
 
class InvalidInsectColorException extends Exception {
    @Override
    public String getMessage() {
        return ("Invalid insect color");
    }
}
 
class InvalidInsectTypeException extends Exception {
    @Override
    public String getMessage() {
        return ("Invalid insect type");
    }
}
 
class InvalidEntityPositionException extends Exception {
    @Override
    public String getMessage() {
        return ("Invalid entity position");
    }
}
 
class DuplicateInsectException extends Exception {
    @Override
    public String getMessage() {
        return ("Duplicate insects");
    }
}
 
class TwoEntitiesOnSamePositionException extends Exception {
    @Override
    public String getMessage() {
        return ("Two entities in the same position");
    }
}
/**
 * Enum representing directions.
 */
enum Direction {
    N("North"),
    E("East"),
    S("South"),
    W("West"),
    NE("North-East"),
    SE("South-East"),
    SW("South-West"),
    NW("North-West");
    private String textRepresentation;
 
    /**
     * Constructor for Direction enum.
     *
     * @param text The text representation of the direction.
     */
    Direction(String text) {
        this.textRepresentation = text;
    }
 
    /**
     * Gets the text representation of the direction.
     *
     * @return The string representation of the direction.
     */
    public String getTextRepresentation() {
        return textRepresentation;
    }
}
/**
 * Enum representing various colors that can be attributed to insects.
 */
enum InsectColor {
    RED,
    GREEN,
    BLUE,
    YELLOW;
    /**
     * Converts a string representation of color to the corresponding InsectColor enum.
     *
     * @param s The string representation of the color.
     * @return The InsectColor enum equivalent to the input string.
     * @throws InvalidInsectColorException If the provided string does not match any valid color.
     */
    public InsectColor toColor(String s) throws InvalidInsectColorException {
        switch (s.toUpperCase()) {
            case "RED":
                return InsectColor.RED;
            case "BLUE":
                return InsectColor.BLUE;
            case "YELLOW":
                return InsectColor.YELLOW;
            case "GREEN":
                return InsectColor.GREEN;
            default:
                throw new InvalidInsectColorException();
 
        }
    }
    public String capitalizeFirstLetter() {
        return this.name().substring(0, 1).toUpperCase() + this.name().substring(1).toLowerCase();
    }
}
/**
 * Interface representing the movement behavior for entities that move orthogonally (up, down, left, right).
 */
interface OrthogonalMoving {
 
    /**
     * Retrieves the value visible in the orthogonal direction from the current entity position.
     *
     * @param dir             The direction in which the visibility is being calculated.
     * @param entityPosition  The position of the entity.
     * @param boardData       The map of board entities for position lookup.
     * @param boardSize       The size of the game board.
     * @return The value visible in the specified orthogonal direction.
     */
    int getOrthogonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                           Map<String, BoardEntity> boardData, int boardSize);
 
    /**
     * Moves the entity orthogonally (up, down, left, right) and retrieves the cumulative visible value.
     *
     * @param dir             The direction in which the entity is moving.
     * @param entityPosition  The position of the entity.
     * @param color           The color of the entity.
     * @param boardData       The map of board entities for position lookup.
     * @param boardSize       The size of the game board.
     * @return The cumulative value of food points visible and collected while moving orthogonally.
     */
    int travelOrthogonally(Direction dir, EntityPosition entityPosition, InsectColor color,
                           Map<String, BoardEntity> boardData, int boardSize);
}
/**
 * Interface representing the movement behavior for entities that move diagonally.
 */
interface DiagonalMoving {
 
    /**
     * Retrieves the value visible in the diagonal direction from the current entity position.
     *
     * @param dir             The direction in which the visibility is being calculated.
     * @param entityPosition  The position of the entity.
     * @param boardData       The map of board entities for position lookup.
     * @param boardSize       The size of the game board.
     * @return The value visible in the specified diagonal direction.
     */
    int getDiagonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                         Map<String, BoardEntity> boardData, int boardSize);
 
    /**
     * Moves the entity diagonally and retrieves the cumulative visible value.
     *
     * @param dir             The direction in which the entity is moving.
     * @param entityPosition  The position of the entity.
     * @param color           The color of the entity.
     * @param boardData       The map of board entities for position lookup.
     * @param boardSize       The size of the game board.
     * @return The cumulative value of food points visible and collected while moving diagonally.
     */
    int travelDiagonally(Direction dir, EntityPosition entityPosition, InsectColor color,
                         Map<String, BoardEntity> boardData, int boardSize);
}
 
/**
 * Abstract class representing an insect on the game board.
 */
abstract class Insect extends BoardEntity {
    protected InsectColor color;
 
    /**
     * Constructor for creating an insect.
     *
     * @param position The position of the insect on the board.
     * @param color    The color of the insect.
     */
    public Insect(EntityPosition position, InsectColor color) {
        this.entityPosition = position;
        this.color = color;
    }
 
    /**
     * Retrieves the best direction for the insect to move based on board data.
     *
     * @param boardData The map of board entities for position lookup.
     * @param boardSize The size of the game board.
     * @return The recommended direction for the insect to move.
     */
    public abstract Direction getBestDirection(Map<String, BoardEntity> boardData, int boardSize);
 
    /**
     * Moves the insect in a specified direction and retrieves the cumulative visible value.
     *
     * @param dir       The direction in which the insect is moving.
     * @param boardData The map of board entities for position lookup.
     * @param boardSize The size of the game board.
     * @return The cumulative value of food points visible and collected while moving.
     */
    public abstract int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int boardSize);
}
 
/**
 * Represents a Grasshopper insect on the game board.
 */
class Grasshopper extends Insect implements  OrthogonalMoving {
 
    /**
     * Constructor for creating a Grasshopper.
     *
     * @param entityPosition The position of the Grasshopper on the board.
     * @param color          The color of the Grasshopper.
     */
    public Grasshopper(EntityPosition entityPosition, InsectColor color) {
        super(entityPosition, color);
    }
 
    @Override
    public Direction getBestDirection(Map<String, BoardEntity> boardData, int boardSize) {
        int maxVisibleFoodPoints = -1;
            Direction direction = Direction.N;
        List<Direction> directions = new ArrayList<>(Arrays.asList(
                Direction.N, Direction.E, Direction.S, Direction.W
        ));
        for (Direction dir: directions) {
            EntityPosition position = entityPosition;
            position = position.newPosition(position, dir);
            position = position.newPosition(position, dir);
            int visibleFoodPoints = 0;
            while (board.checkPosition(position)) {
                BoardEntity entity = boardData.get(position.toString());
                if (entity instanceof FoodPoint) {
                    visibleFoodPoints += ((FoodPoint) entity).getValue();
                }
                position = position.newPosition(position, dir);
                position = position.newPosition(position, dir);
            }
            if (visibleFoodPoints > maxVisibleFoodPoints) {
                direction = dir;
                maxVisibleFoodPoints = visibleFoodPoints;
            }
        }
        return direction;
    }
    @Override
    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int boardSize) {
        EntityPosition position = entityPosition;
        position = position.newPosition(position, dir);
        position = position.newPosition(position, dir);
        int visibleFoodPoints = 0;
        while (board.checkPosition(position)) {
            BoardEntity entity = boardData.get(position.toString());
            if (entity instanceof FoodPoint) {
                visibleFoodPoints += ((FoodPoint) entity).getValue();
                board.removeEntity(position);
            }
            if (entity instanceof Insect) {
                if (((Insect) entity).color != color) {
                    break;
                }
            }
            position = position.newPosition(position, dir);
            position = position.newPosition(position, dir);
        }
        board.removeEntity(entityPosition);
        return visibleFoodPoints;
    }
    @Override
    public String toString() {
        return "Grasshopper";
    }
    @Override
    public int getOrthogonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                                  Map<String, BoardEntity> boardData, int boardSize) {
        return 0;
    }
 
    @Override
    public int travelOrthogonally(Direction dir, EntityPosition entityPosition, InsectColor color,
                                  Map<String, BoardEntity> boardData, int boardSize) {
        return 0;
    }
}
/**
 * Represents an Ant insect on the game board.
 */
class Ant extends Insect implements OrthogonalMoving, DiagonalMoving {
 
    /**
     * Constructor for creating an Ant.
     *
     * @param entityPosition The position of the Ant on the board.
     * @param insectColor    The color of the Ant.
     */
    public Ant(EntityPosition entityPosition, InsectColor insectColor) {
        super(entityPosition, insectColor);
    }
 
    @Override
    public Direction getBestDirection(Map<String, BoardEntity> boardData, int boardSize) {
        int maxVisibleFoodPoints = -1;
        Direction direction = Direction.N;
        EntityPosition position;
        List<Direction> directions = new ArrayList<>(Arrays.asList(
                Direction.N, Direction.E, Direction.S, Direction.W,
                Direction.NE, Direction.SE, Direction.SW, Direction.NW
        ));
        for (Direction dir: directions) {
            position = entityPosition;
            int visibleFoodPoints = getOrthogonalDirectionVisibleValue(dir, position, boardData, boardSize);
            if (visibleFoodPoints > maxVisibleFoodPoints) {
                direction = dir;
                maxVisibleFoodPoints = visibleFoodPoints;
            }
        }
 
 
        for (Direction dir: directions) {
            position = entityPosition;
            int visibleFoodPoints = getDiagonalDirectionVisibleValue(dir, position, boardData, boardSize);
            if (visibleFoodPoints > maxVisibleFoodPoints) {
                direction = dir;
                maxVisibleFoodPoints = visibleFoodPoints;
            }
        }
        return direction;
    }
 
    @Override
    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int boardSize) {
        switch (dir) {
            case N:
            case S:
            case W:
            case E:
            return travelOrthogonally(dir, entityPosition, color, boardData, boardSize);
            case NW:
            case NE:
            case SW:
            case SE:
            return travelDiagonally(dir, entityPosition, color, boardData, boardSize);
            default:
            return 0;
        }
    }
 
    @Override
    public int getOrthogonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition, Map<String,
            BoardEntity> boardData, int boardSize) {
        int visibleFoodPoints = 0;
        EntityPosition position = entityPosition.newPosition(entityPosition, dir);
        while (board.checkPosition(position)) {
            BoardEntity entity = boardData.get(position.toString());
            if (entity instanceof FoodPoint) {
                visibleFoodPoints += ((FoodPoint) entity).getValue();
            }
            position = position.newPosition(position, dir);
        }
        return visibleFoodPoints;
    }
 
    @Override
    public int travelOrthogonally(Direction dir, EntityPosition entityPosition, InsectColor color, Map<String,
            BoardEntity> boardData, int boardSize) {
        int visibleFoodPoints = 0;
        EntityPosition position = entityPosition.newPosition(entityPosition, dir);
        while (board.checkPosition(position)) {
            BoardEntity entity = boardData.get(position.toString());
            if (entity instanceof FoodPoint) {
                visibleFoodPoints += ((FoodPoint) entity).getValue();
                board.removeEntity(position);
            }
            if (entity instanceof Insect) {
                if (((Insect) entity).color != color) {
                    break;
                }
            }
            position = position.newPosition(position, dir);
        }
        board.removeEntity(entityPosition);
        return visibleFoodPoints;
    }
 
    @Override
    public int getDiagonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition, Map<String,
            BoardEntity> boardData, int boardSize) {
        int visibleFoodPoints = 0;
        EntityPosition position = entityPosition.newPosition(entityPosition, dir);
        while (board.checkPosition(position)) {
            BoardEntity entity = boardData.get(position.toString());
            if (entity instanceof FoodPoint) {
                visibleFoodPoints += ((FoodPoint) entity).getValue();
            }
            position = position.newPosition(position, dir);
        }
        return visibleFoodPoints;
    }
 
    @Override
    public int travelDiagonally(Direction dir, EntityPosition entityPosition, InsectColor color, Map<String,
            BoardEntity> boardData, int boardSize) {
        int visibleFoodPoints = 0;
        EntityPosition position = entityPosition.newPosition(entityPosition, dir);
        while (board.checkPosition(position)) {
            BoardEntity entity = boardData.get(position.toString());
            if (entity instanceof FoodPoint) {
                visibleFoodPoints += ((FoodPoint) entity).getValue();
                board.removeEntity(position);
            }
            if (entity instanceof Insect) {
                if (((Insect) entity).color != color) {
                    break;
                }
            }
            position = position.newPosition(position, dir);
        }
        board.removeEntity(entityPosition);
        return visibleFoodPoints;
    }
 
    @Override
    public String toString() {
        return "Ant";
    }
}
 
/**
 * Represents a Butterfly insect on the game board.
 */
class Butterfly extends Insect implements OrthogonalMoving {
    /**
     * Constructor for creating a Butterfly.
     *
     * @param entityPosition The position of the Butterfly on the board.
     * @param insectColor    The color of the Butterfly.
     */
    public Butterfly(EntityPosition entityPosition, InsectColor insectColor) {
        super(entityPosition, insectColor);
    }
 
    @Override
    public Direction getBestDirection(Map<String, BoardEntity> boardData, int boardSize) {
        EntityPosition position;
        int maxVisibleFoodPoints = -1;
        Direction direction = Direction.N;
        List<Direction> directions = new ArrayList<>(Arrays.asList(
                Direction.N, Direction.E, Direction.S, Direction.W
        ));
        for (Direction dir: directions) {
            position = entityPosition;
            int visibleFoodPoints = getOrthogonalDirectionVisibleValue(dir, position, boardData, boardSize);
            if (visibleFoodPoints > maxVisibleFoodPoints) {
                direction = dir;
                maxVisibleFoodPoints = visibleFoodPoints;
            }
        }
        return direction;
    }
 
    @Override
    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int boardSize) {
        return travelOrthogonally(dir, entityPosition, color, boardData, boardSize);
    }
 
    @Override
    public int getOrthogonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition, Map<String,
            BoardEntity> boardData, int boardSize) {
        int visibleFoodPoints = 0;
        EntityPosition position = entityPosition.newPosition(entityPosition, dir);
        while (board.checkPosition(position)) {
            BoardEntity entity = boardData.get(position.toString());
            if (entity instanceof FoodPoint) {
                visibleFoodPoints += ((FoodPoint) entity).getValue();
            }
            position = position.newPosition(position, dir);
        }
        return visibleFoodPoints;
    }
 
    @Override
    public int travelOrthogonally(Direction dir, EntityPosition entityPosition, InsectColor color, Map<String,
            BoardEntity> boardData, int boardSize) {
        int visibleFoodPoints = 0;
        EntityPosition position = entityPosition.newPosition(entityPosition, dir);
        while (board.checkPosition(position)) {
            BoardEntity entity = boardData.get(position.toString());
            if (entity instanceof FoodPoint) {
                visibleFoodPoints += ((FoodPoint) entity).getValue();
                board.removeEntity(position);
            }
            if (entity instanceof Insect) {
                if (((Insect) entity).color != color) {
                    break;
                }
            }
            position = position.newPosition(position, dir);
        }
        board.removeEntity(entityPosition);
        return visibleFoodPoints;
    }
 
    @Override
    public String toString() {
        return "Butterfly";
    }
}
 
/**
 * Represents a Spider insect on the game board.
 */
class Spider extends Insect implements DiagonalMoving {
 
    /**
     * Constructor for creating a Spider.
     *
     * @param entityPosition The position of the Spider on the board.
     * @param insectColor    The color of the Spider.
     */
    public Spider(EntityPosition entityPosition, InsectColor insectColor) {
        super(entityPosition, insectColor);
    }
 
    @Override
    public Direction getBestDirection(Map<String, BoardEntity> boardData, int boardSize) {
        int maxVisibleFoodPoints = -1;
        Direction direction = Direction.N;
        EntityPosition position;
        List<Direction> directions = new ArrayList<>(Arrays.asList(
                Direction.NE, Direction.SE, Direction.SW, Direction.NW
        ));
        for (Direction dir: directions) {
            position = entityPosition;
            int visibleFoodPoints = getDiagonalDirectionVisibleValue(dir, position, boardData, boardSize);
            if (visibleFoodPoints > maxVisibleFoodPoints) {
                direction = dir;
                maxVisibleFoodPoints = visibleFoodPoints;
            }
        }
        return direction;
    }
 
    @Override
    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int boardSize) {
        return travelDiagonally(dir, entityPosition, color, boardData, boardSize);
    }
 
    @Override
    public int getDiagonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition, Map<String,
            BoardEntity> boardData, int boardSize) {
        int visibleFoodPoints = 0;
        EntityPosition position = entityPosition.newPosition(entityPosition, dir);
        while (board.checkPosition(position)) {
            BoardEntity entity = boardData.get(position.toString());
            if (entity instanceof FoodPoint) {
                visibleFoodPoints += ((FoodPoint) entity).getValue();
            }
            position = position.newPosition(position, dir);
        }
        return visibleFoodPoints;
    }
 
    @Override
    public int travelDiagonally(Direction dir, EntityPosition entityPosition, InsectColor color, Map<String,
            BoardEntity> boardData, int boardSize) {
        int visibleFoodPoints = 0;
        EntityPosition position = entityPosition.newPosition(entityPosition, dir);
        while (board.checkPosition(position)) {
            BoardEntity entity = boardData.get(position.toString());
            if (entity instanceof FoodPoint) {
                visibleFoodPoints += ((FoodPoint) entity).getValue();
                board.removeEntity(position);
            }
            if (entity instanceof Insect) {
                if (((Insect) entity).color != color) {
                    break;
                }
            }
            position = position.newPosition(position, dir);
        }
        board.removeEntity(entityPosition);
        return visibleFoodPoints;
    }
 
    @Override
    public String toString() {
        return "Spider";
    }
}
 
/**
 * Represents the game board where entities are placed and managed.
 */
class Board {
    private Map<String, BoardEntity> boardData;
    private int size;
 
    /**
     * Constructor for creating a game board with a specified size.
     *
     * @param boardSize The size of the game board.
     */
    public Board(int boardSize) {
        this.boardData = new HashMap<>();
        this.size = boardSize;
    }
 
    /**
     * Checks if a given position is within the boundaries of the board.
     *
     * @param position The position to check.
     * @return True if the position is within the board boundaries, otherwise false.
     */
 
    public boolean checkPosition(EntityPosition position) {
        int x = position.getX();
        int y = position.getY();
        if (x >= 1 && x <= size) {
            if (y >= 1 && y <= size) {
                return true;
            }
        }
        return false;
    }
    /**
     * Adds an entity to the board.
     *
     * @param entity The entity to be added to the board.
     * @throws TwoEntitiesOnSamePositionException If there are two entities on the same position.
     */
    public void addEntity(BoardEntity entity) throws TwoEntitiesOnSamePositionException {
        if (getEntity(entity.getEntityPosition()) != null) {
            throw new TwoEntitiesOnSamePositionException();
        }
        boardData.put(entity.getEntityPosition().toString(), entity);
    }
 
    /**
     * Retrieves an entity at a given position on the board.
     *
     * @param position The position to retrieve the entity from.
     * @return The entity at the specified position, or null if no entity exists.
     */
    public BoardEntity getEntity(EntityPosition position) {
        return this.boardData.get(position.toString());
    }
    /**
     * Gets the best direction for an insect on the board.
     *
     * @param insect The insect for which to determine the best direction.
     * @return The best direction for the given insect.
     */
    public Direction getDirection(Insect insect) {
        return insect.getBestDirection(boardData, size);
    }
 
    /**
     * Gets the visible food points in the direction an insect travels.
     *
     * @param insect The insect to check for visible food points.
     * @return The visible food points in the direction the insect travels.
     */
    public int getDirectionVisibleFoodPoints(Insect insect) {
        Direction direction = getDirection(insect);
        return insect.travelDirection(direction, boardData, size);
    }
 
    /**
     * Removes an entity at a specified position from the board.
     *
     * @param position The position at which to remove the entity.
     */
    public void removeEntity(EntityPosition position) {
        boardData.remove(position.toString());
    }
    /**
     * Gets the size of the board.
     *
     * @return The size of the board.
     */
    public int getSize() {
        return size;
    }
}
 
/**
 * Represents an entity on the game board.
 */
abstract class BoardEntity {
 
    protected EntityPosition entityPosition;
    protected Board board;
 
    /**
     * Gets the position of the entity on the board.
     *
     * @return The position of the entity.
     */
    public EntityPosition getEntityPosition() {
        return entityPosition;
    }
    /**
     * Sets the position of the entity on the board.
     *
     * @param entityPosition The new position of the entity.
     */
    public void setEntityPosition(EntityPosition entityPosition) {
        this.entityPosition = entityPosition;
    }
    /**
     * Sets the board for the entity.
     *
     * @param board The game board where the entity exists.
     */
    public void setBoard(Board board) {
        this.board = board;
    }
}
/**
 * Represents the position of an entity on the game board.
 */
class EntityPosition {
    private int x;
    private int y;
 
    /**
     * Constructs an EntityPosition with given x and y coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public EntityPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Creates a new EntityPosition with the given x and y coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return An EntityPosition object representing the specified coordinates.
     * @throws InvalidEntityPositionException if an invalid position is provided.
     */
    public static EntityPosition getPosition(int x, int y) throws InvalidEntityPositionException {
        try {
            return new EntityPosition(x, y);
        } catch (Exception e) {
            throw new InvalidEntityPositionException();
        }
    }
 
 
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
 
    /**
     * Returns a string representation of the position.
     *
     * @return A string representation of the position.
     */
 
    @Override
    public String toString() {
        return String.valueOf(x) + ' ' + y;
    }
 
    /**
     * Creates a new EntityPosition based on a current position and a direction.
     *
     * @param currentPosition The current position.
     * @param dir             The direction in which to move.
     * @return The new EntityPosition after moving in the specified direction.
     */
 
    public EntityPosition newPosition(EntityPosition currentPosition, Direction dir) {
        int newX = currentPosition.getX();
        int newY = currentPosition.getY();
 
        switch (dir) {
            case N:
                newX--;
                break;
            case S:
                newX++;
                break;
            case W:
                newY--;
                break;
            case E:
                newY++;
                break;
            case NE:
                newX--;
                newY++;
                break;
            case SE:
                newX++;
                newY++;
                break;
            case SW:
                newX++;
                newY--;
                break;
            case NW:
                newX--;
                newY--;
                break;
            default:
                return null;
        }
 
        return new EntityPosition(newX, newY);
    }
 
}
 
/**
 * Represents a food point on the game board.
 */
class FoodPoint extends BoardEntity {
    protected int value;
    /**
     * Constructs a FoodPoint object with a specified position and value.
     *
     * @param position The position of the food point on the board.
     * @param value    The value of the food point.
     */
    public FoodPoint(EntityPosition position, int value) {
        this.value = value;
        this.entityPosition = position;
    }
    /**
     * Retrieves the value of the food point.
     *
     * @return The value of the food point.
     */
    public int getValue() {
        return value;
    }
}