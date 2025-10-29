package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.MapMessage;

/**
 * A Java application that creates/populates the DuckDB database stored in the data folder.
 */
public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    private static final String DATABASE_PATH = "../../data/mydata.db";
    private static final String DATABASE_URL = "jdbc:duckdb:" + DATABASE_PATH;
    private static final String SQL_DIR = "../../data/sql/";

    // SQL file names (readSQLFile will handle the directory path)
    private static final String SQL_CREATE_TABLE = "create_data_table.sql";
    private static final String SQL_INSERT_DATA = "insert_data.sql";
    private static final String SQL_CHECK_TABLE_EXISTS = "check_table_exists.sql";
    private static final String SQL_COUNT_RECORDS = "count_records.sql";
    private static final String SQL_VERIFY_BY_CATEGORY = "verify_data_by_category.sql";

    public static void main(String[] args) {
        logger.info(new MapMessage().with("event.category", "process").with("event.action", "start")
                .with("event.outcome", "success").with("message", "Starting DuckDB Java Application")
                .with("service.name", "duckdb-java-app").with("service.version", "1.0.0"));

        try {
            // Create a connection to DuckDB (persistent database file)
            Connection connection = DriverManager.getConnection(DATABASE_URL);
            logger.info(new MapMessage().with("event.category", "database").with("event.action", "connect")
                    .with("event.outcome", "success").with("message", "Connected to DuckDB successfully")
                    .with("database.type", "duckdb").with("database.instance", "mydata.db")
                    .with("url.full", DATABASE_URL).with("file.path", DATABASE_PATH));

            // Create and populate the database if needed
            createAndPopulateDatabase(connection);

            // Close the connection
            connection.close();
            logger.info(new MapMessage().with("event.category", "database").with("event.action", "disconnect")
                    .with("event.outcome", "success").with("message", "Database connection closed successfully")
                    .with("database.type", "duckdb").with("database.instance", "mydata.db"));

        } catch (SQLException e) {
            logger.error(new MapMessage().with("event.category", "database").with("event.action", "connect")
                    .with("event.outcome", "failure").with("message", "Database error occurred")
                    .with("database.type", "duckdb").with("error.message", e.getMessage())
                    .with("error.type", e.getClass().getSimpleName()), e);
        }
    }

    /**
     * Utility method to read SQL content from a file in the SQL directory.
     *
     * @param sqlFileName
     *            The name of the SQL file (without path)
     *
     * @return The SQL content as a string
     *
     * @throws IOException
     *             if the file cannot be read
     */
    private static String readSQLFile(String sqlFileName) throws IOException {
        Path path = Paths.get(SQL_DIR + sqlFileName);
        return Files.readString(path).trim();
    }

    private static void createAndPopulateDatabase(Connection connection) throws SQLException {
        try {
            // Check if the 'data' table already exists
            boolean tableExists = false;
            try (Statement stmt = connection.createStatement()) {
                String checkTableSQL = readSQLFile(SQL_CHECK_TABLE_EXISTS);
                ResultSet rs = stmt.executeQuery(checkTableSQL);
                tableExists = rs.next();
            }

            if (tableExists) {
                // Check if table has data
                try (Statement stmt = connection.createStatement()) {
                    String countSQL = readSQLFile(SQL_COUNT_RECORDS);
                    ResultSet rs = stmt.executeQuery(countSQL);
                    rs.next();
                    int count = rs.getInt("count");
                    if (count > 0) {
                        logger.info(new MapMessage().with("event.category", "database").with("event.action", "validate")
                                .with("event.outcome", "success")
                                .with("message", "Database already exists and has records")
                                .with("database.type", "duckdb").with("database.instance", "mydata.db")
                                .with("database.query.count", count));
                        verifyData(connection);
                        return;
                    }
                }
            }

            // Create the table (replicating script.py structure)
            createDataTable(connection);

            // Populate with sample data (replicating script.py logic)
            populateSampleData(connection);

            // Verify the data
            verifyData(connection);

            logger.info(new MapMessage().with("event.category", "database").with("event.action", "create")
                    .with("event.outcome", "success").with("message", "Database created and populated successfully")
                    .with("database.type", "duckdb").with("database.instance", "mydata.db")
                    .with("file.name", "mydata.db").with("file.extension", "db"));

        } catch (IOException e) {
            logger.error(new MapMessage().with("event.category", "file").with("event.action", "read")
                    .with("event.outcome", "failure").with("message", "Error reading SQL files")
                    .with("file.extension", "sql").with("file.directory", SQL_DIR).with("error.message", e.getMessage())
                    .with("error.type", e.getClass().getSimpleName()), e);
        }
    }

    private static void createDataTable(Connection connection) throws SQLException, IOException {
        try (Statement stmt = connection.createStatement()) {
            String createTableSQL = readSQLFile(SQL_CREATE_TABLE);
            stmt.execute(createTableSQL);
            logger.info(new MapMessage().with("event.category", "database").with("event.action", "create")
                    .with("event.outcome", "success").with("message", "Data table created successfully")
                    .with("database.type", "duckdb").with("database.table", "data").with("file.name", SQL_CREATE_TABLE)
                    .with("file.extension", "sql"));
        }
    }

    private static void populateSampleData(Connection connection) throws SQLException, IOException {
        String[] categories = { "A", "B", "C" };
        Random random = new Random();

        String insertSQL = readSQLFile(SQL_INSERT_DATA);
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {

            for (String category : categories) {
                for (int x = 1; x <= 20; x++) { // 20 data points per category
                    // Replicate Python formula: random.uniform(5, 15) + (ord(cat) - 65) * 3 + 0.2 * x
                    double randomValue = 5 + random.nextDouble() * 10; // uniform between 5 and 15
                    double categoryOffset = (category.charAt(0) - 'A') * 3; // (ord(cat) - 65) * 3
                    double xOffset = 0.2 * x;
                    double y = randomValue + categoryOffset + xOffset;

                    pstmt.setString(1, category);
                    pstmt.setInt(2, x);
                    pstmt.setDouble(3, y);
                    pstmt.addBatch();
                }
            }

            pstmt.executeBatch();
            // No need to commit as DuckDB uses autocommit by default
            logger.info(new MapMessage().with("event.category", "database").with("event.action", "insert")
                    .with("event.outcome", "success").with("message", "Sample data inserted successfully")
                    .with("database.type", "duckdb").with("database.table", "data")
                    .with("database.query.count", categories.length * 20)
                    .with("database.operation.batch_size", categories.length * 20));
        }
    }

    private static void verifyData(Connection connection) throws SQLException, IOException {
        try (Statement stmt = connection.createStatement()) {
            String verifySQL = readSQLFile(SQL_VERIFY_BY_CATEGORY);
            ResultSet rs = stmt.executeQuery(verifySQL);

            logger.info(new MapMessage().with("event.category", "database").with("event.action", "query")
                    .with("event.outcome", "success").with("message", "Starting data verification by category")
                    .with("database.type", "duckdb").with("database.table", "data")
                    .with("database.operation.type", "select"));

            while (rs.next()) {
                String category = rs.getString("category");
                int count = rs.getInt("count");
                logger.info(new MapMessage().with("event.category", "database").with("event.action", "count")
                        .with("event.outcome", "success")
                        .with("message", String.format("Category %s has %d records", category, count))
                        .with("database.type", "duckdb").with("database.table", "data")
                        .with("database.query.count", count).with("labels.category", category));
            }

            logger.info(new MapMessage().with("event.category", "database").with("event.action", "validate")
                    .with("event.outcome", "success").with("message", "Data verification completed successfully")
                    .with("database.type", "duckdb").with("database.table", "data"));
        }
    }
}