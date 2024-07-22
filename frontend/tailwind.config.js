/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'grey': '#b1b1b1',
        'blue' : '#1814f3',
      },
    },
  },
  plugins: [
    require('daisyui', '@tailwindcss/forms'),
  ],
  daisyui: {
		themes: ['light', 'dark']
	}
}
