# DuckDB Java Project

A comprehensive Java application for creating and managing DuckDB databases with enterprise-grade logging and Maven project structure.

## Overview

This application creates and populates a DuckDB database with structured data, featuring:
- **ECS-compliant logging** using Elastic Common Schema (ECS)
- **External SQL files** for maintainable database operations  
- **Data Package specification** following Frictionless Data standards
- **Code formatting** with Eclipse Java formatter

## Features

### Database Operations
1. **Connects to persistent database**: Uses `../../data/mydata.db` 
2. **Creates data table**: Creates table with structure:
   - `category` (TEXT): Categories A, B, C
   - `x_value` (INTEGER): Values from 1 to 20  
   - `y_value` (DOUBLE): Generated using mathematical formula
3. **Populates sample data**: 60 records total (20 per category)
4. **Smart detection**: Checks if database/table already exists and has data
5. **Verifies results**: Displays count of records per category

### Logging Architecture
- **Console Logging**: Human-readable colorized output for development
- **JSON Logging**: ECS-compliant structured logs in root-level `logs/app.json`
- **Dual Appenders**: Simultaneous console and JSON file output

### External SQL Management
SQL statements are externalized in the root-level `sql/` directory:
- `create_data_table.sql` - Table creation DDL
- `insert_data.sql` - Data insertion statements  
- `check_table_exists.sql` - Table existence validation
- `count_records.sql` - Record counting
- `verify_data_by_category.sql` - Category validation

## Quick Start

### Prerequisites
- Java 11 or higher
- Maven 3.6+

### Running the Application

```bash
# Clone and navigate to the project
cd core/java

# Compile and run (automatic formatting included)
mvn clean compile
mvn exec:java

# Format code manually
mvn formatter:format

# Run tests
mvn test
```

The application uses project-specific Maven settings in `.mvn/` to ensure Maven Central repository access.

## Project Structure

```
testing123/                    # Root directory
├── core/java/
│   ├── .mvn/
│   │   ├── settings.xml       # Project-specific Maven settings (Maven Central only)
│   │   └── maven.config       # Automatic settings file reference
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/
│   │   │   │   └── Main.java  # Application entry point with ECS logging
│   │   │   └── resources/
│   │   │       └── log4j2.xml # Logging configuration (Console + ECS JSON)
│   │   └── test/
│   │       └── java/com/example/
│   │           └── MainTest.java # Unit tests
│   ├── pom.xml               # Maven project configuration
│   └── README.md             # This file
├── sql/                      # External SQL files (root level)
├── logs/                     # Generated log files (root level)
│   └── app.json             # ECS-compliant JSON logs
└── data/                     # Database storage (root level)
    ├── mydata.db            # DuckDB database file
    └── datapackage.json     # Frictionless Data Package specification
```

## Dependencies

### Core Dependencies
- **DuckDB JDBC** (1.4.1.0): Database connectivity
- **Log4j 2** (2.21.1): Logging framework (API + Core)  
- **Elastic ECS Layout** (1.5.0): ECS-compliant JSON logging
- **JUnit 5** (5.9.2): Unit testing framework

### Build Plugins
- **Maven Compiler**: Java 11 compilation
- **Maven Surefire**: Test execution  
- **Exec Maven Plugin**: Application execution
- **Formatter Plugin**: Eclipse Java code formatting

## Configuration

### Maven Settings
The project uses `.mvn/settings.xml` to ensure dependencies are resolved from Maven Central only, avoiding corporate repository authentication issues.

### Logging Configuration
- **Console**: Colorized human-readable format with timestamps and structured fields
- **JSON File**: ECS-compliant structured logging in root-level `logs/app.json`
- **Log Levels**: INFO for application, ERROR for third-party libraries

### ECS Field Mapping
The application generates logs with proper Elastic Common Schema fields:
- `@timestamp`: ISO 8601 UTC timestamps
- `log.level`: Log severity level  
- `log.logger`: Logger class name
- `event.action`: Database operation type
- `event.category`: Operation category (database, process)
- `event.outcome`: success/failure status
- `database.*`: Database-specific fields
- `service.*`: Service identification

## Data Generation Formula

The `y_value` is calculated using a mathematical formula:
```
y = random(5, 15) + (categoryOffset * 3) + (0.2 * x)
where categoryOffset: A=0, B=1, C=2
```

This generates realistic test data with:
- Random base values between 5-15
- Category-based offsets creating distinct clusters  
- Linear trend based on x_value

## Troubleshooting

### Maven Issues
- **401 Unauthorized**: Corporate repositories may require authentication. The project uses Maven Central only via `.mvn/settings.xml`
- **Plugin Resolution**: All plugins are configured to use Maven Central repositories

### Database Issues  
- **File Not Found**: Database file is created automatically in `../../data/mydata.db`
- **Permission Errors**: Ensure write access to the data directory

### Logging Issues
- **Missing JSON Logs**: Check that root-level `logs/` directory is writable

## Development

### Code Style
The project uses Eclipse Java formatter with default settings. Code is automatically formatted during compilation.

### Adding SQL Statements
1. Create `.sql` file in root-level `sql/` directory
2. Use `readSQLFile("filename")` in Java code (automatically prepends `sql/` and appends `.sql`)
3. Follow existing naming conventions

### Testing
Unit tests validate:
- Database connectivity
- Table creation and population
- Data integrity
- Logging functionality

Run tests with: `mvn test`

## Data Package Integration

The application integrates with a Frictionless Data Package specification located at `../../data/datapackage.json`, providing:
- Schema definitions for the database structure
- Metadata about data generation and validation
- Field constraints and data types
- Integration documentation

This enables standardized data documentation and validation workflows.