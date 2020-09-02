package app.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSqlConnect {

    public static Connection connectToDatabaseOrDie() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://THE_HOST/THE_DATABASE";
            conn = DriverManager.getConnection("jdbc:postgresql://172.17.30.208:5432/atma_nirbhar_db", "postgres", "Agri@dmin123");
            //conn = DriverManager.getConnection("jdbc:postgresql://10.195.97.206:5433/atma_nirbhar_db_2020_08_25", "postgres", "postgres");
            //conn = DriverManager.getConnection("jdbc:postgresql://172.16.59.132:2433/atma_nirbhar_db_2020_08_25", "postgres", "GMC#$@R$guJlWR$");
            //conn = DriverManager.getConnection("jdbc:postgresql://10.195.96.32:5432/atma_db_test_1", "postgres", "postgres");
            //conn = DriverManager.getConnection("jdbc:postgresql://10.195.96.32:5432/atma_nirbhar_db", "postgres", "postgres");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(2);
        }
        return conn;
    }
}
