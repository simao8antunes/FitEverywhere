import { render } from "vitest-browser-react";
import { afterEach, describe, expect, it, vi } from "vitest";
import { MemoryRouter } from "react-router-dom";
import App from "../App.tsx";

const mocks = vi.hoisted(() => {
  return {
    useAuth: vi.fn(),
    useFetchGyms: vi.fn(),
  };
});

// Mock the `useAuth` hook
vi.mock(import("../hooks/useAuth.ts"), () => ({
  useAuth: mocks.useAuth,
}));
vi.mock(import("../hooks/useFetchGyms.ts"), () => ({
  useFetchGyms: mocks.useFetchGyms,
}));

afterEach(() => {
  vi.clearAllMocks(); // Clean up mocks after each test
});

describe("App Component", () => {
  it("renders Login page for unauthenticated users", () => {
    mocks.useAuth.mockReturnValue({
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

    expect(screen.getByTestId("login-button").element()).toBeInTheDocument();
  });

  it("redirects to SelectRole for unauthenticated users on protected routes", () => {
    mocks.useAuth.mockReturnValue({
      error: null,
      logout(): void {},
      isAuthenticated: true,
      user: null,
    });

    const screen = render(
      <MemoryRouter initialEntries={["/select-role"]}>
        <App />
      </MemoryRouter>,
    );

    expect(screen.getByTestId("gym-button").element()).toBeInTheDocument();
    expect(screen.getByTestId("client-button").element()).toBeInTheDocument();
    expect(
      screen.getByRole("button", { name: "continue" }).element(),
    ).toBeInTheDocument();
  });

  it("renders Dashboard for users", () => {
    mocks.useAuth.mockReturnValue({
      error: null,
      logout(): void {},
      isAuthenticated: true,
      user: {
        role: "client",
        username: "test",
        email: "test@gmail.com",
        userSpecs: {
          picture:
            "https://www.google.com/logos/doodles/2024/seasonal-holidays-2024-6753651837110333.4-s.png",
        },
      },
    });
    vi.mock(import("../hooks/useFetchEvents.ts"), () => ({
      useFetchEvents: vi.fn(() => ({
        events: [],
        loading: false,
        error: null,
      })),
    }));
    const mockFetchOwnGyms = vi
      .fn()
      .mockResolvedValue([{ name: "Gym A" }, { name: "Gym B" }]);
    mocks.useFetchGyms.mockReturnValue({
      gyms: [{ name: "Gym A" }, { name: "Gym B" }],
      fetchNearbyGyms: vi.fn(),
      loading: true,
      error: null,
      fetchOwnGyms: mockFetchOwnGyms,
    });

    const screen = render(
      <MemoryRouter initialEntries={["/"]}>
        <App />
      </MemoryRouter>,
    );

    expect(screen.getByTestId("dashboard").element()).toBeInTheDocument();
  });

  it("renders GymProfile for gym_manager", () => {
    mocks.useAuth.mockReturnValue({
      error: null,
      logout(): void {},
      isAuthenticated: true,
      user: {
        role: "gym_manager",
        username: "test",
        email: "test@gmail.com",
        userSpecs: {
          picture:
            "https://www.google.com/logos/doodles/2024/seasonal-holidays-2024-6753651837110333.4-s.png",
        },
      },
    });

    const screen = render(
      <MemoryRouter initialEntries={["/profile"]}>
        <App />
      </MemoryRouter>,
    );

    expect(screen.getByTestId("gym-profile").element()).toBeInTheDocument();
  });

  it("renders Profile for client", () => {
    mocks.useAuth.mockReturnValue({
      error: null,
      logout(): void {},
      isAuthenticated: true,
      user: {
        role: "client",
        username: "test",
        email: "test@gmail.com",
        userSpecs: {
          picture:
            "https://www.google.com/logos/doodles/2024/seasonal-holidays-2024-6753651837110333.4-s.png",
        },
      },
    });

    const screen = render(
      <MemoryRouter initialEntries={["/profile"]}>
        <App />
      </MemoryRouter>,
    );

    expect(screen.getByTestId("client-profile").element()).toBeInTheDocument();
  });
});
