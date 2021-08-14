package viewer;

import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private static SQLiteDataSource dataSource;

    public static boolean connected(String fileName) {
        File file = new File("C:" + fileName);

        dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:C:" + fileName);
        boolean isConnected = false;
        if (file.exists()) {
            try (Connection con = dataSource.getConnection()) {
                if (con.isValid(5)) {
                    isConnected = true;
                    System.out.println(dataSource.getDatabaseName() + "is connected.");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return isConnected;
    }

    public static List<String> getTables() {
        List<String> tableNames = new ArrayList<>();

        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%';";
            ResultSet resultSet = con.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                tableNames.add(resultSet.getString("name"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return tableNames;
    }

    public static Object[][] getData(String query) {
        try (Connection con = dataSource.getConnection()) {
            ResultSet resultSet = con.createStatement().executeQuery(query);

            ArrayList<Object[]> data = new ArrayList<>();

            while (resultSet.next()) {
                int contact_id = resultSet.getInt("contact_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");

                data.add(new Object[]{contact_id, firstName, lastName, email, phone});
            }

            return data.toArray(new Object[data.size()][5]);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
