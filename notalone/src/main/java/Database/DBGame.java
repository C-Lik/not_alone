package Database;

import Exceptions.CriticalExceptionHandler;
import MainGame.Game;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Vector;

public class DBGame {

    private static DBGame instance = null;
    private final LinkedHashMap<String, Vector<EnemyEntity>> enemiesList = new LinkedHashMap<>();
    private final LinkedHashMap<String, Vector<ItemEntity>> itemsList = new LinkedHashMap<>();
    private final DBManager dbManager;
    private Integer mapId;
    private Integer won;

    private DBGame() {
        dbManager = new DBManager("res/gameDB.db");
    }

    public static DBGame getInstance() {
        if (instance == null)
            instance = new DBGame();
        return instance;
    }

    public static boolean isInstanceNull() {
        return instance == null;
    }

    public void loadLevel() {
        try {
            clearOldData();

            ResultSet resultSet = dbManager.executeQuery("SELECT * FROM current_map");
            if (resultSet.next()) {
                mapId = resultSet.getInt("map_id");
                won = resultSet.getInt("won");
            } else {
                mapId = 1;
                won = 0;
                dbManager.executeUpdate("INSERT INTO current_map (map_id, won) VALUES (1, 0)");
            }
            resultSet.close();
            this.loadEnemies();
            this.loadItems();

        } catch (Exception e) {
            CriticalExceptionHandler.handle(e);
        }
    }

    private void loadEnemies() throws SQLException {
        ResultSet resultSet = dbManager.executeQuery("SELECT me.index_x, me.index_y, me.left_limit, me.right_limit, e.name FROM maps_enemies me INNER JOIN enemy e ON  e.id = me.enemy_id WHERE map_id =" + mapId + ";");
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            Integer x = resultSet.getInt("index_x");
            Integer y = resultSet.getInt("index_y");
            Integer left = resultSet.getInt("left_limit");
            Integer right = resultSet.getInt("right_limit");

            Vector<EnemyEntity> auxiliary = enemiesList.get(name);
            if (auxiliary == null) {
                auxiliary = new Vector<>();
                auxiliary.add(new EnemyEntity(x, y, left, right));
                enemiesList.put(name, auxiliary);
            } else {
                auxiliary.add(new EnemyEntity(x, y, left, right));
            }
        }
        resultSet.close();
    }

    private void loadItems() throws SQLException {
        ResultSet resultSet = dbManager.executeQuery("SELECT mi.index_x, mi.index_y, i.name FROM maps_items mi INNER JOIN item i ON  i.id = mi.item_id WHERE map_id =" + mapId + ";");
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            Integer x = resultSet.getInt("index_x");
            Integer y = resultSet.getInt("index_y");

            Vector<ItemEntity> auxiliary = itemsList.get(name);
            if (auxiliary == null) {
                auxiliary = new Vector<>();
                auxiliary.add(new ItemEntity(x, y));
                itemsList.put(name, auxiliary);
            } else {
                auxiliary.add(new ItemEntity(x, y));
            }
        }
        resultSet.close();
    }

    public void updateWin() {
        dbManager.executeUpdate("UPDATE current_map SET won = 1;");
    }

    public void updateLevel() {
        String command = String.format("UPDATE current_map SET map_id = %d, won = 0;", Game.currentMapNumber);
        dbManager.executeUpdate(command);
        loadLevel();
    }

    public void clearOldData() {
        itemsList.clear();
        enemiesList.clear();
    }

    public Integer getMapId() {
        return mapId;
    }

    public Boolean won() {
        return won == 1;
    }

    public LinkedHashMap<String, Vector<EnemyEntity>> getEnemiesList() {
        return enemiesList;
    }

    public LinkedHashMap<String, Vector<ItemEntity>> getItemsList() {
        return itemsList;
    }

    public void close() {
        dbManager.close();
    }
}
