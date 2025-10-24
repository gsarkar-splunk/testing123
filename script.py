import duckdb
import random

# Create DuckDB database file
con = duckdb.connect("data/mydata.db")

# Create example table
con.execute("""
CREATE TABLE data (
    category TEXT,
    x_value INTEGER,
    y_value DOUBLE
);
""")

# Populate sample data
categories = ["A", "B", "C"]
rows = []
for cat in categories:
    for x in range(1, 21):  # 20 data points per category
        y = random.uniform(5, 15) + (ord(cat) - 65) * 3 + 0.2 * x
        rows.append((cat, x, y))

con.executemany("INSERT INTO data VALUES (?, ?, ?)", rows)
con.commit()

# Verify data
print(con.execute("SELECT category, COUNT(*) FROM data GROUP BY category").fetchall())

con.close()
print("✅ mydata.db created successfully!")
