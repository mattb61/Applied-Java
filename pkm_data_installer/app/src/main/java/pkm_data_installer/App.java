package pkm_data_installer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.Yaml;

public class App {
    private final static Yaml yaml = new Yaml();
    private final static String URL = "jdbc:mariadb://localhost:3306/pkmdb";
    private final static String USER = "root";
    private final static String PW = "root_password";
    private final static Map<String, Integer> releaseIdLookupTable = new HashMap<>();
    private final static Map<String, Integer> gameIdLookupTable = new HashMap<>();
    private final static Map<String, Integer> typeIdLookupTable = new HashMap<>();
    private final static Map<Region, Integer> regionIdLookupTable = new HashMap<>();

    public static Map<String, Object> readYaml(String resourcePathString) {
        try(InputStream in = App.class.getClassLoader().getResourceAsStream(resourcePathString)){
            return yaml.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return null; // required to compile, but should never be reached
        }
    }

    private static Set<Integer> getGenerationsFromReleases(Map<String,Object> yamlMap) {
        Set<Integer> set = new HashSet<>();
        for (String key : yamlMap.keySet()) {
            Map<String, Object> release = (Map<String, Object>)yamlMap.get(key);
            set.add((Integer)release.get("gen"));
        }
        return set;
    }

    private static void pushGenerations(Statement statement) throws SQLException {
        Map<String, Object> yamlMap = readYaml("releases.yaml");
        Set<Integer> generations = getGenerationsFromReleases(yamlMap);
        int rows_affected = 0;
        for (Integer gen_id : generations) {
            String insert_sql = "INSERT INTO generations (id) VALUES (" + gen_id + ");";
            rows_affected += statement.executeUpdate(insert_sql);
        }
        System.out.println("pushGenerations rows affected: " + rows_affected);
    }

    private static void pushData(
        Connection conn, 
        String filePath, 
        String preparedSQL, 
        Map<String, Integer> idTable,
        TablePushLogic pushLogic
    ) throws SQLException {
        Map<String, Object> yamlMap = readYaml(filePath);
        int nextId = 1;
        try (
            PreparedStatement statement = 
                conn.prepareStatement(preparedSQL)
        ) {
            for (String key : yamlMap.keySet()) {
                Map<String, Object> dataSource = 
                    (Map<String, Object>)yamlMap.get(key);
                pushLogic.configure(statement, dataSource);
                statement.addBatch();
                if(idTable != null)
                    idTable.put(key, nextId++);
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw e;
        }
    }

    private static void pushReleases(Connection connection) throws SQLException {
        String filePath = "releases.yaml";
        String sql = "INSERT INTO releases (name, gen_id) VALUES (?, ?);";
        pushData(connection, filePath, sql, releaseIdLookupTable, (statement, release) -> {
            String name = (String)release.get("name");
            int gen = (Integer)release.get("gen");
            statement.setString(1, name);
            statement.setInt(2, gen);
        });
    }

    private static void pushGames(Connection connection) throws SQLException {
        String filePath = "games.yaml";
        String sql = "INSERT INTO games (name, release_id) VALUES (?, ?);";
        pushData(connection, filePath, sql, gameIdLookupTable, (statement, game) -> {
            String name = (String)game.get("name");
            int release = releaseIdLookupTable.get((String)game.get("release"));
            statement.setString(1, name);
            statement.setInt(2, release);
        });
    }

    private static void pushTypes(Connection connection) throws SQLException {
        String filePath = "types.yaml";
        String sql = "INSERT INTO types (name, gen_id) VALUES (?, ?)";
        pushData(connection, filePath, sql, typeIdLookupTable, (statement, type) -> {
            String name = (String)type.get("name");
            int gen = (Integer)type.get("gen");
            statement.setString(1, name);
            statement.setInt(2, gen);
        });
    } 

    private static void pushAbilities(Connection connection) throws SQLException {
        String filePath = "abilities.yaml";
        String sql = "INSERT INTO abilities (name, release_id) VALUES (?, ?)";
        pushData(connection, filePath, sql, null, (statement, abilities) -> {
            String name = (String)abilities.get("name");
            int releaseId = releaseIdLookupTable.get((String)abilities.get("release"));
            statement.setString(1, name);
            statement.setInt(2, releaseId);
        });
    } 

    private static void pushEggGroups(Connection connection) throws SQLException {
        String filePath = "egg-groups.yaml";
        String sql = "INSERT INTO egg_groups (name) VALUES (?)";
        pushData(connection, filePath, sql, null, (statement, eggGroup) -> {
            String name = (String)eggGroup.get("name");
            statement.setString(1, name);
        });
    }

    private static void pushItems(Connection connection) throws SQLException {
        String filePath = "items.yaml";
        String sql = "INSERT INTO items (name, cost, release_id) VALUES (?, ?, ?)";
        pushData(connection, filePath, sql, null, (statement, item) -> {
            String name = (String)item.get("name");
            int cost = (Integer)item.get("cost");
            int release = releaseIdLookupTable.get((String)item.get("release"));
            statement.setString(1, name);
            statement.setInt(2, cost);
            statement.setInt(3, release);
        });
    }

    private static class Region {
        private String name;
        private String release;
        public Region(String name, String release) {
            this.name = name;
            this.release = release;
        }
        public String getName() {
            return name;
        }
        public String getRelease() {
            return release;
        }
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((release == null) ? 0 : release.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Region other = (Region) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (release == null) {
                if (other.release != null)
                    return false;
            } else if (!release.equals(other.release))
                return false;
            return true;
        }
    }

    private static Set<Region> getRegionsFromLocations(Map<String,Object> yamlMap) {
        Set<Region> set = new HashSet<>();
        for (String key : yamlMap.keySet()) {
            Map<String, Object> location = (Map<String, Object>)yamlMap.get(key);
            String region = (String)location.get("region");
            String release = (String)location.get("release");
            set.add(new Region(region, release));
        }
        return set;
    }

    private static void pushRegions(Connection conn) throws SQLException {
        Map<String, Object> yamlMap = readYaml("locations.yaml");
        Set<Region> regions = getRegionsFromLocations(yamlMap);
        try(PreparedStatement stmt = conn.prepareStatement(
            "INSERT INTO regions (name, release_id) VALUES (?, ?)"
        )) {
            int nextId = 1;
            for (Region region : regions) {
                stmt.setString(1, region.getName());
                stmt.setInt(2, releaseIdLookupTable.get(region.getRelease()));
                stmt.addBatch();
                regionIdLookupTable.put(region, nextId++);
            }
            stmt.executeBatch();
        }
    }

    private static void pushLocations(Connection conn) throws SQLException {
        String filePath = "locations.yaml";
        String sql = "INSERT INTO locations (name, region_id) VALUES (?, ?)";
        pushData(conn, filePath, sql, null, (statement, location) -> {
            String name = (String)location.get("name");
            String regionName = (String)location.get("region");
            String release = (String)location.get("release");
            int regionId = regionIdLookupTable.get(new Region(regionName, release));
            statement.setString(1, name);
            statement.setInt(2, regionId);
        });
    }

    public static void main(String[] args) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(2);
        }
        try (
            Connection conn = DriverManager.getConnection(URL, USER, PW);
            Statement stmt  = conn.createStatement();
        ) {
            pushGenerations(stmt);
            pushReleases(conn);
            pushGames(conn);
            pushTypes(conn);
            pushAbilities(conn);
            pushEggGroups(conn);
            pushItems(conn);
            pushRegions(conn);
            pushLocations(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(3);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(4);
        }
    }
}
