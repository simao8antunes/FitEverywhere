/** @type {import('tailwindcss').Config} */
export default {
  darkMode: "class",
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      colors: {
        background: "var(--background-color)",
        secbackground: "var(--secondary-background-color)",
        text: "var(--color-text)",
        primary: "var(--color-primary)",
        secondary: "var(--color-secondary)",
        intense: "var(--color-intense)",
      },
      fontSize: {
        h1: "var(--font-size-h1)",
        h2: "var(--font-size-h2)",
        h3: "var(--font-size-h3)",
        h4: "var(--font-size-h4)",
        h5: "var(--font-size-h5)",
        h6: "var(--font-size-h6)",
        display: "var(--font-size-display)",
        base: "var(--font-size-base)",
        s: "var(--font-size-s)",
        xs: "var(--font-size-xs)",
        label: "var(--font-size-label)",
      },
      spacing: {
        xs: "var(--space-xs)",
        s: "var(--space-s)",
        base: "var(--space-base)",
        m: "var(--space-m)",
        l: "var(--space-l)",
        xl: "var(--space-xl)",
        xxl: "var(--space-xxl)",
      },
      borderRadius: {
        none: "var(--border-radius-none)",
        soft: "var(--border-radius-soft)",
        rounded: "var(--border-radius-rounded)",
        circle: "var(--border-radius-circle)",
      },
    },
  },
  plugins: [require("daisyui")],
  daisyui: {
    themes: [
      {
        myCustomTheme: {
          primary: "#eb9c25", // Customize your colors
          secondary: "#f97316",
          accent: "#1fb2a6",
          neutral: "#3d4451",
          "base-100": "#ffffff", // Light background
          "base-200": "#f3f4f6", // Slightly darker light background
          "base-300": "#d1d5db",
          info: "#2094f3",
          success: "#009485",
          warning: "#ff9900",
          error: "#ff5724",
        },
      },
      "light", // Default light theme
      "dark",
    ],
  },
};
