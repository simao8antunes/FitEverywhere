import { defineConfig, mergeConfig } from "vitest/config";
import viteConfig from "./vite.config.ts";

export default mergeConfig(
  viteConfig,
  defineConfig({
    test: {
      browser: {
        enabled: true,
        name: "chromium",
        provider: "playwright",
      },
      globals: true,
      coverage: {
        thresholds: {
          branches: 60,
          lines: 60,
          statements: 60,
          functions: 30,
        },
      },
    },
  }),
);
