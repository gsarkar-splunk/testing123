# DuckDB-Wasm + Plotly Dashboard

A static web application that uses DuckDB-Wasm to query data from a DuckDB database and visualize it with Plotly.js charts.

## Features

- **DuckDB-Wasm**: Client-side SQL querying without a server
- **Plotly.js**: Interactive data visualization
- **Dynamic Filtering**: Filter data by category with dropdown selection
- **Static Hosting**: No backend required - runs entirely in the browser

## Demo

The site is automatically deployed to GitHub Pages: [https://gsarkar-splunk.github.io/testing123/](https://gsarkar-splunk.github.io/testing123/)

## Setup for GitHub Pages

This repository is configured to automatically deploy to GitHub Pages using GitHub Actions. To enable it:

1. Go to your repository settings
2. Navigate to "Pages" in the left sidebar
3. Under "Source", select "GitHub Actions"
4. The workflow will automatically deploy on every push to the `main` branch

## Local Development

To run locally, you need to serve the files over HTTP (not file://) due to CORS restrictions:

### Using Python (from project root):
```bash
python -m http.server 8000
# Then open http://localhost:8000/web/ in your browser
```

## File Structure

```
web/
├── index.html          # Main HTML page
├── script.js           # JavaScript application logic
├── styles.css          # CSS styling
└── README.md           # This file
```

## Database Requirements

The application expects a DuckDB database at `../data/mydata.db` (relative to the web directory) with the following structure:

```sql
CREATE TABLE data (
    category TEXT,
    x_value NUMERIC,
    y_value NUMERIC
);
```

> **Note**: Use the Java backend (`../core/java/`) to generate this database with sample data.

## Technologies Used

- **DuckDB-Wasm**: For client-side SQL querying
- **Plotly.js**: For interactive charts
- **GitHub Pages**: For static site hosting
- **GitHub Actions**: For automated deployment

## Browser Compatibility

- Modern browsers with ES6 module support
- WebAssembly support required for DuckDB-Wasm
- HTTPS required for some features (automatically provided by GitHub Pages)

## How It Works

1. **Database Loading**: DuckDB-Wasm loads the database file from `../data/mydata.db`
2. **Data Querying**: SQL queries are executed client-side in the browser
3. **Visualization**: Query results are rendered as interactive Plotly.js charts
4. **Filtering**: Users can filter data by category using the dropdown selector

## Development Notes

- The web application is completely static - no backend server required
- All SQL processing happens in the browser via WebAssembly
- CORS restrictions require serving files over HTTP (not file://)
- The database file must be accessible relative to the web directory