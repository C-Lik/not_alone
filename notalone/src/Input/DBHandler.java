package Input;

import Exceptions.CriticalExceptionHandler;
import Exceptions.NotCriticalExceptionHandler;
import MainGame.Game;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Vector;

/*!
    \brief Manages the database.

    Provides methods for reading, querying, and updating data from the game's database.
    \note Implements the Singleton design pattern.
 */
public class DBHandler {

    private static DBHandler instance = null;
    private Connection c;
    private Statement stm;
    private String mapName;
    private String items;
    private String enemies;
    private final LinkedHashMap<String, Vector<Vector<Integer>>> enemiesList = new LinkedHashMap<>();
    private final LinkedHashMap<String, Vector<Vector<Integer>>> itemsList = new LinkedHashMap<>();

    /*!
        \brief Private constructor.

        Constructs the connection to the database and creates a new statement.\n
        In case of an exception, it will be written to the error.txt file, and the program will exit with an error
        code of -1.
     */
    private DBHandler() {
        File file = new File("res/gameDB.db");
        if(file.exists()) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:res/gameDB.db");
                stm = c.createStatement();
            } catch (Exception e) {
                CriticalExceptionHandler.handle(e);
            }
        } else {
            CriticalExceptionHandler.handle(new FileNotFoundException("Data Base does not exists!\n"));
        }
    }

    /*!
        \brief Read current level from the database.

        This function utilizes the call to the function \ref executeDBQuery(String).
     */
    public void readLevel() {
        executeDBQuery("SELECT * FROM MAPS where MAP_NAME LIKE 'current_map%';");
    }

    /*!
        \brief Game won update.

        This function performs an update of the information using the function \ref executeDBUpdate(String),
        invalidating the fields of the table corresponding to the row that contains the regular
        expression: "current_map*".
     */
    public void updateWin() {
        executeDBUpdate("UPDATE MAPS SET ITEMS='', ENEMIES='' WHERE MAP_NAME LIKE 'current_map%';");
    }

    /*!
        \brief Game level update.

       This function utilizes calls to the functions \ref executeDBQuery(String) and \ref executeDBUpdate(String) to:
            - Execute a query to retrieve information and save the data of the map to be loaded.
            - Update the data of the row matching the regular expression "current_map*" by deleting existing content
            and inserting a new record with the corresponding values.
     */
    public void updateLevel() {

        String command = String.format("SELECT * FROM MAPS where MAP_NAME LIKE 'map%d';", Game.currentMapNumber);
        executeDBQuery(command);

        // update current map in database
        executeDBUpdate("DELETE from MAPS WHERE MAP_NAME LIKE 'current_map%'");

        command = String.format("INSERT INTO MAPS VALUES('%s', '%s', '%s');",
                "current_map=map" + Game.currentMapNumber, items, enemies);
        executeDBUpdate(command);
    }

    /*!
        \brief Executes a query.

       Executes the query specified by the command parameter, saves the data of the current map, and creates lists
       for enemies and beneficial elements.\n
       In case of an exception, it will be written to the error.txt file, and the program will exit with an
       error code (-1).
     */
    public void executeDBQuery(String command) {
        try {
            ResultSet resultSet = stm.executeQuery(command);

            while (resultSet.next()) {

                String temp = resultSet.getString("MAP_NAME");
                if (temp.contains("="))
                    mapName = temp.split("=")[1];
                else
                    mapName = temp;
                items = resultSet.getString("ITEMS");
                enemies = resultSet.getString("ENEMIES");
            }
            clearLists();
            getElements(items, "item");
            getElements(enemies, "enemy");

            resultSet.close();
        } catch (Exception e) {
            CriticalExceptionHandler.handle(e);
        }
    }

    //! \brief Empties the lists after they have been used.
    public void clearLists() {
        itemsList.clear();
        enemiesList.clear();
    }

    /*! \brief Constructs the lists of elements.

        This function takes two string arguments: one representing the list of entities and the other representing
        the type of entities in the given list. The string will be split into an array of strings using the '|'
        character as a delimiter. Each obtained string will be further split using the '=' character, resulting in an
        array of two elements in the form: ["element_name", "index_X index_Y"] for positions,
        or ["element_name", "index_X index_Y left_margin right_margin"] for enemies. \n
        The resulting structure will be in the form: \n
        {"element_name", [index_X1, index_Y1], [index_X2, index_X2], ...}.
     */
    public void getElements(String s, String type) {
        if (!s.isEmpty()) {
            String[] elements = s.split("\\|");
            String[] temporary;

            for (String element : elements) {
                temporary = element.split("=");
                Vector<Integer> indexes = new Vector<>();
                for (String index : temporary[1].split(" ")) {
                    indexes.add(Integer.parseInt(index));
                }

                if (indexes.size() != 0) {
                    if (type.equals("item")) {
                        if (!itemsList.containsKey(temporary[0])) {
                            itemsList.put(temporary[0], new Vector<>());
                        }
                        itemsList.get(temporary[0]).add(indexes);
                    } else {
                        if (!enemiesList.containsKey(temporary[0]))
                            enemiesList.put(temporary[0], new Vector<>());
                        enemiesList.get(temporary[0]).add(indexes);
                    }
                }
            }
        }
    }

    /*!
        \brief Executes an update command.

        Executes the update command specified by the command parameter. \n
        In case of an exception, it will be written to the error.txt file, and the program will exit with
        an error code (-1).
     */
    public void executeDBUpdate(String command) {
        try {
            stm.executeUpdate(command);
        } catch (Exception e) {
            CriticalExceptionHandler.handle(e);
        }
    }

    //! \brief Retrieves the name of the current map.
    public String getMapName() {
        return mapName;
    }

    //! \brief Gets the enemies list.
    public LinkedHashMap<String, Vector<Vector<Integer>>> getEnemiesList() {
        return enemiesList;
    }

    //! \brief Gets the items list.
    public LinkedHashMap<String, Vector<Vector<Integer>>> getItemsList() {
        return itemsList;
    }

    //! \brief Gets the class instance.
    public static DBHandler getInstance() {
        if (instance == null)
            instance = new DBHandler();
        return instance;
    }

    /*!
      \brief Checks the existence of the class instance.

      @return true - if the instance is null
      @return false - if the instance exists
     */
    public static boolean checkIfInstanceIsNull() {
        return instance == null;
    }

    //! \brief Closes the database connection and the created statement.
    public void close() {
        try {
            c.close();
            stm.close();
        } catch (Exception e) {
            NotCriticalExceptionHandler.handle(e);
        }
    }
}
