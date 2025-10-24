import duckdb

# Path to your DuckDB file
db_file = "data/mydata.db"

# Connect to the DuckDB database
conn = duckdb.connect(database=db_file, read_only=False)  # read_only=True if you don't want to modify

# Example: list all tables in the database
tables = conn.execute("""
    SELECT table_schema, table_name
    FROM information_schema.tables
    WHERE table_schema='main';
""").fetchall()

print("Tables in mydata.db:")
for schema, table in tables:
    print(f"{schema}.{table}")

# Example: query data from a table named 'data'
# Make sure your table exists in mydata.db
table_name = 'data'

try:
    rows = conn.execute(f"SELECT * FROM {table_name} LIMIT 10;").fetchall()
    print(f"\nFirst 10 rows from {table_name}:")
    for row in rows:
        print(row)
except duckdb.CatalogException:
    print(f"\nTable '{table_name}' does not exist in {db_file}.")

# Close the connection
conn.close()
