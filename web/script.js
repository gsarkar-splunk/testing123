import * as duckdb from "https://cdn.jsdelivr.net/npm/@duckdb/duckdb-wasm@1.31.0/+esm";

// --- Initialize DuckDB-Wasm ---
const JSDELIVR_BUNDLES = duckdb.getJsDelivrBundles();
const bundle = await duckdb.selectBundle(JSDELIVR_BUNDLES);
const worker = await duckdb.createWorker(bundle.mainWorker);
const db = new duckdb.AsyncDuckDB(new duckdb.ConsoleLogger(), worker);
await db.instantiate(bundle.mainModule, bundle.pthreadWorker);

// --- Load the DuckDB .db file ---
const response = await fetch("../data/mydata.db");
if (!response.ok) {
  throw new Error(`Failed to fetch mydata.db: ${response.status}`);
}
const buffer = await response.arrayBuffer();
await db.registerFileBuffer("/mydata.db", new Uint8Array(buffer));

const conn = await db.connect();
await conn.query("ATTACH '/mydata.db' AS mydb (READ_ONLY);");

// --- Use the known table and columns ---
const tableName = "data";       // The table we know exists
const categoryCol = "category"; // Text column
const xCol = "x_value";         // Numeric column
const yCol = "y_value";         // Numeric column

// --- Populate category dropdown ---
const categoriesResult = await conn.query(`
  SELECT DISTINCT ${categoryCol} AS cat FROM mydb.${tableName} ORDER BY cat;
`);
const categories = categoriesResult.toArray().map(r => r.cat);

const select = document.getElementById("category");
for (const cat of categories) {
  const option = document.createElement("option");
  option.value = cat;
  option.textContent = cat;
  select.appendChild(option);
}

// --- Initial plot ---
await updatePlot(categories[0]);

select.addEventListener("change", async (e) => {
  await updatePlot(e.target.value);
});

async function updatePlot(selectedCategory) {
  const query = `
    SELECT ${xCol} AS x, ${yCol} AS y
    FROM mydb.${tableName}
    WHERE ${categoryCol} = '${selectedCategory}'
    ORDER BY ${xCol};
  `;
  const result = await conn.query(query);
  const rows = result.toArray();

  const x = rows.map(r => r.x);
  const y = rows.map(r => r.y);

  // Define trace data
  const trace = {
    x,
    y,
    mode: "lines+markers",
    type: "scatter",
    name: selectedCategory
  };

  // Define reusable font configuration
  const fontFamily = "'Lato', 'Gill Sans', 'Lato', sans-serif";
  const titleFont = { size: 18 };
  const axisFont = { size: 14 };
  const tickFont = { size: 12, color: '#666' };

  // Define layout configuration
  const layout = {
    title: {
      text: `Plot of ${yCol} vs ${xCol} for ${selectedCategory}`,
      font: titleFont
    },
    xaxis: { 
      title: { text: xCol, font: axisFont },
      tickfont: tickFont
    },
    yaxis: { 
      title: { text: yCol, font: axisFont },
      tickfont: tickFont
    },
    font: {
      family: fontFamily,
      size: 12,
      color: '#333'
    },
    margin: { t: 50 },
    paper_bgcolor: "rgba(0,0,0,0)",
    plot_bgcolor: "rgba(0,0,0,0)",
  };

  // Define config options (if needed)
  const config = {
    displayModeBar: false,       // show the modebar
    displaylogo: false,          // remove "Produced with Plotly" button
    scrollZoom: true,            // enables scroll wheel zooming
    responsive: true             // optional, makes it responsive
  };

  // Create the plot
  Plotly.newPlot("plot", [trace], layout, config);
}
