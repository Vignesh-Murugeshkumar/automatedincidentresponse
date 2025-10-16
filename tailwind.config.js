/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/main/resources/templates/**/*.html",
    "./src/main/resources/static/**/*.js"
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        'incident-purple': '#6f42c1',
        'incident-blue': '#0d6efd',
        'incident-green': '#198754',
        'incident-red': '#dc3545',
        'incident-orange': '#fd7e14',
        'incident-teal': '#20c997'
      }
    }
  },
  plugins: [],
}