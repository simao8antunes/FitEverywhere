import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    port: 80,
    host: true, // Allows Vite to be accessible externally
    watch: {
      usePolling: true, // Enable polling for changes (helpful in Docker)
    },
  },
});
