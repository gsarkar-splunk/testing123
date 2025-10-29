# SQL Files Documentation

This folder contains reusable SQL statements.

## File Structure

### Table Management
- **`create_data_table.sql`** - Creates the main data table
- **`check_table_exists.sql`** - Checks if the data table exists in the database

### Data Operations
- **`insert_data.sql`** - Parameterized insert statement for adding data records
- **`count_records.sql`** - Counts total records in the data table

### Data Verification
- **`verify_data_by_category.sql`** - Groups and counts records by category for verification

## Usage

These SQL files are read by the Java application using the `readSQLFile()` utility method. The files use standard SQL syntax compatible with DuckDB.

## Table Schema

The `data` table created by `create_data_table.sql` has the following structure:

```sql
CREATE TABLE data (
    category TEXT,      -- Category identifier (A, B, C)
    x_value INTEGER,    -- X coordinate (1-20)
    y_value DOUBLE      -- Calculated Y value using formula
);
```