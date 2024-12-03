import { render } from "vitest-browser-react";
import { describe, it, expect, vi } from "vitest";
import { MemoryRouter } from "react-router-dom";
import App from "../src/App.tsx";
import { useAuth } from "../src/hooks/useAuth";

const mocks = vi.hoisted(() => {
  return {
    useAuth: vi.fn(),
  };
});

// Mock the `useAuth` hook
vi.mock(import("../src/hooks/useAuth.ts"), () => ({
  useAuth: mocks.useAuth,
}));

describe("App Component", () => {
  it("renders Login page for unauthenticated users", () => {
    vi.mocked(useAuth).mockReturnValue({
      error: null,
      logout(): void {},
      isAuthenticated: false,
      user: null,
    });

    const screen = render(
      <MemoryRouter initialEntries={["/login"]}>
        <App />
      </MemoryRouter>,
    );

    expect(screen.getByText("Login Page Content")).toBeInTheDocument();
  });

  it("redirects to SelectRole for unauthenticated users on protected routes", () => {
    (useAuth as vi.Mock).mockReturnValue({
      isAuthenticated: false,
      user: null,
    });

    const screen = render(
      <MemoryRouter initialEntries={["/profile"]}>
        <App />
      </MemoryRouter>,
    );

    expect(screen.getByText("SelectRole Page Content")).toBeInTheDocument();
  });

  it("renders Dashboard for gym_manager", () => {
    (useAuth as vi.Mock).mockReturnValue({
      isAuthenticated: true,
      user: { role: "gym_manager" },
    });

    const screen = render(
      <MemoryRouter initialEntries={["/"]}>
        <App />
      </MemoryRouter>,
    );

    expect(screen.getByText("Dashboard Content")).toBeInTheDocument();
  });

  it("renders GymProfile for gym_manager", () => {
    (useAuth as vi.mock).mockReturnValue({
      isAuthenticated: true,
      user: { role: "gym_manager" },
    });

    const screen = render(
      <MemoryRouter initialEntries={["/profile"]}>
        <App />
      </MemoryRouter>,
    );

    expect(screen.getByText("GymProfile Content")).toBeInTheDocument();
  });

  it("renders Profile for client", () => {
    (useAuth as vi.Mock).mockReturnValue({
      isAuthenticated: true,
      user: { role: "client" },
    });

    const screen = render(
      <MemoryRouter initialEntries={["/profile"]}>
        <App />
      </MemoryRouter>,
    );

    expect(screen.getByText("Profile Page Content")).toBeInTheDocument();
  });
});
