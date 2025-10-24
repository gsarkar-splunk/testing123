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

### Manual Setup (Alternative)

If you prefer manual deployment:

1. Go to repository Settings → Pages
2. Under "Source", select "Deploy from a branch"
3. Choose `main` branch and `/ (root)` folder
4. Click Save

## Local Development

To run locally, you need to serve the files over HTTP (not file://) due to CORS restrictions:

### Using Python:
```bash
python -m http.server 8000
```

Then open http://localhost:8000 in your browser.

## File Structure

```
├── index.html          # Main HTML page
├── script.js           # JavaScript application logic
├── styles.css          # CSS styling
├── data/
│   └── mydata.db      # DuckDB database file
└── .github/
    └── workflows/
        └── deploy.yml  # GitHub Actions deployment workflow
```

## Database Schema

The application expects a DuckDB database (`data/mydata.db`) with the following structure:

```sql
CREATE TABLE data (
    category TEXT,
    x_value NUMERIC,
    y_value NUMERIC
);
```

## Technologies Used

- **DuckDB-Wasm**: For client-side SQL querying
- **Plotly.js**: For interactive charts
- **GitHub Pages**: For static site hosting
- **GitHub Actions**: For automated deployment

## Browser Compatibility

- Modern browsers with ES6 module support
- WebAssembly support required for DuckDB-Wasm
- HTTPS required for some features (automatically provided by GitHub Pages)