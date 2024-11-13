import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    host: true, // Allows Vite to be accessible externally
    watch: {
      usePolling: true, // Enable polling for changes (helpful in Docker)
    },
  },
});
