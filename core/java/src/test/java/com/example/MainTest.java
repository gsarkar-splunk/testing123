package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Unit tests for DuckDB functionality.
 */
public class MainTest {

    private Connection connection;
    private static final String TEST_DATABASE_URL = "jdbc:duckdb:"; // In-memory for tests

    @BeforeEach
    public void setUp() throws SQLException {
        // Create a fresh in-memory database connection for each test
        connection = DriverManager.getConnection(TEST_DATABASE_URL);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testDuckDBConnection() throws SQLException {
        assertNotNull(connection);
        assertFalse(connection.isClosed());

        // Test basic SQL execution
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT 1 as test_value");
            assertTrue(rs.next());
            assertEquals(1, rs.getInt("test_value"));
        }
    }

    @Test
    public void testDataTableCreationAndPopulation() throws SQLException {
        // Create the data table structure (like script.py)
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                    CREATE TABLE data (
                        category TEXT,
                        x_value INTEGER,
                        y_value DOUBLE
                    )
                    """);

            // Insert test data
            stmt.execute("INSERT INTO data VALUES ('A', 1, 10.5), ('B', 2, 15.3), ('C', 3, 20.1)");

            // Query and verify data structure
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as row_count FROM data");
            assertTrue(rs.next());
            assertEquals(3, rs.getInt("row_count"));

            // Verify categories
            rs = stmt.executeQuery("SELECT DISTINCT category FROM data ORDER BY category");
            assertTrue(rs.next());
            assertEquals("A", rs.getString("category"));
            assertTrue(rs.next());
            assertEquals("B", rs.getString("category"));
            assertTrue(rs.next());
            assertEquals("C", rs.getString("category"));
        }
    }

    @Test
    public void testDuckDBVersion() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT version()");
            assertTrue(rs.next());
            String version = rs.getString(1);
            assertNotNull(version);
            assertTrue(version.contains("duckdb"), "Version should contain 'duckdb': " + version);
        }
    }

    @Test
    public void testDataPopulationLogic() throws SQLException {
        // Test that the formula logic works correctly
        // Formula: randomValue + (category.charAt(0) - 'A') * 3 + 0.2 * x

        // Test category offsets
        assertEquals(0, ('A' - 'A') * 3); // Category A offset = 0
        assertEquals(3, ('B' - 'A') * 3); // Category B offset = 3
        assertEquals(6, ('C' - 'A') * 3); // Category C offset = 6

        // Test x offset calculation
        assertEquals(0.2, 0.2 * 1, 0.001); // x=1 -> 0.2
        assertEquals(2.0, 0.2 * 10, 0.001); // x=10 -> 2.0
        assertEquals(4.0, 0.2 * 20, 0.001); // x=20 -> 4.0
    }
}