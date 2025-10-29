# DuckDB Multi-Component Data Platform

A comprehensive data platform demonstrating modern data engineering practices with Java backend data generation, SQL-based data operations, and interactive web visualization using DuckDB and Plotly.js.

## Project Overview

This project showcases a complete data pipeline with:

- **Java Backend** (`core/java/`): Enterprise-grade data generation with ECS logging
- **SQL Scripts** (`sql/`): Reusable database operations with SQLFluff linting support
- **Web Frontend** (`web/`): Interactive data visualization with DuckDB-Wasm and Plotly.js
- **Data Management** (`data/`): Structured data storage with Data Package specification
- **Centralized Logging** (`logs/`): JSON-structured application logs

## Quick Start

1. **Generate Data (Java)**:
   ```bash
   cd core/java
   mvn compile exec:java
   ```

2. **View Data (Web)**:
   ```bash
   # Serve from project root
   python -m http.server 8000
   # Open http://localhost:8000/
   ```

3. **Explore Database**:
   ```bash
   # The generated database is at data/mydata.db
   ```

## Project Components

### Java Backend (`core/java/`)
- **Purpose**: Generate structured test data for the DuckDB database
- **Features**: ECS-compliant logging, Maven project structure, external SQL files
- **Technologies**: Java, DuckDB JDBC driver, Log4j2 with ECS layout
- **Output**: Creates `data/mydata.db` with structured sample data

### SQL Operations (`sql/`)
- **Purpose**: Reusable SQL scripts for database operations
- **Features**: Table creation, data verification, record counting
- **Linting**: SQLFluff-ready for code quality
- **Usage**: Consumed by Java application and available for other tools

### Web Visualization (`web/`)
- **Purpose**: Interactive data exploration and visualization
- **Features**: Client-side SQL querying, dynamic filtering, responsive charts
- **Technologies**: DuckDB-Wasm, Plotly.js, vanilla JavaScript
- **Demo**: [Live on GitHub Pages](https://gsarkar-splunk.github.io/testing123/)

### Data Management (`data/`)
- **Purpose**: Centralized data storage with metadata
- **Features**: Data Package specification, structured schema documentation
- **Format**: DuckDB database with JSON metadata

## Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Java Backend  │───▶│   DuckDB File   │◀───│  Web Frontend   │
│  (Data Creator) │    │  (data/*.db)    │    │ (Visualization) │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       ▲
         ▼                       │
┌─────────────────┐              │
│   SQL Scripts   │──────────────┘
│  (Operations)   │
└─────────────────┘
```

## Development Workflow

1. **Data Generation**: Use Java application to create/populate database
2. **Data Verification**: Use SQL scripts to validate and explore data
3. **Web Development**: Build visualizations with live database queries
4. **Quality Assurance**: Lint SQL with SQLFluff, format Java with Maven

## Technologies & Standards

- **Backend**: Java 11+, Maven, DuckDB JDBC, Log4j2
- **Database**: DuckDB (embedded analytical database)
- **Frontend**: DuckDB-Wasm, Plotly.js, ES6 modules
- **Logging**: Elastic Common Schema (ECS) compliance
- **Data**: Frictionless Data Package specification
- **Linting**: SQLFluff (SQL), Eclipse formatter (Java)

## Database Schema

```sql
CREATE TABLE data (
    category TEXT,      -- Categories: A, B, C
    x_value INTEGER,    -- Values: 1-20
    y_value DOUBLE      -- Generated using mathematical formula
);
```

## Deployment

- **GitHub Pages**: Web frontend automatically deploys on push to `main`
- **Local Development**: Use HTTP server for CORS compliance
- **Database**: Portable DuckDB file, no server required

## Getting Started

See individual component READMEs for detailed setup:
- [Java Backend Setup](core/java/README.md)
- [SQL Scripts Documentation](sql/README.md)
- [Web Frontend Guide](web/README.md)