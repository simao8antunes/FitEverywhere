import { render } from "vitest-browser-react";
import { describe, it, expect, vi, afterEach } from "vitest";
import { MemoryRouter } from "react-router-dom";
import MyGyms from "../pages/MyGyms.tsx";

const mocks = vi.hoisted(() => ({
  useAuth: vi.fn(),
}));

// Mock the `useAuth` hook
vi.mock(import("../hooks/useAuth.ts"), () => ({
  useAuth: mocks.useAuth,
}));

vi.mock(import("../components/gyms/GymForm.tsx"), () => ({
  default: vi.fn(() => <div data-testid="gym-form">GymForm Component</div>),
}));

afterEach(() => {
  vi.clearAllMocks(); // Clean up mocks after each test
});

describe("MyGyms Component", () => {
  it("renders the title and description", () => {
    mocks.useAuth.mockReturnValue({
      user: null,
    });

    render(
      <MemoryRouter>
        <MyGyms />
      </MemoryRouter>,
    );

    expect(document.querySelector("h1")?.textContent).toBe("My Gyms");
    expect(document.querySelector("p")?.textContent).toBe(
      "Here you can see all the gyms you manage.",
    );
  });

  it("displays gyms for a gym manager user", () => {
    const mockGyms = [
      { id: "1", name: "Gym A" },
      { id: "2", name: "Gym B" },
    ];

    mocks.useAuth.mockReturnValue({
      user: {
        role: "gym_manager",
        linkedGyms: mockGyms,
      },
    });

    render(
      <MemoryRouter>
        <MyGyms />
      </MemoryRouter>,
    );

    const gymElements = Array.from(
      document.querySelectorAll(".card-title"),
    ).map((el) => el.textContent);

    expect(gymElements).toContain("Gym A");
    expect(gymElements).toContain("Gym B");
  });

  it("renders GymForm component", () => {
    mocks.useAuth.mockReturnValue({
      user: {
        role: "gym_manager",
        linkedGyms: [],
      },
    });

    render(
      <MemoryRouter>
        <MyGyms />
      </MemoryRouter>,
    );

    expect(document.querySelector('[data-testid="gym-form"]')).toBeTruthy();
  });

  it("handles gym selection", () => {
    const mockGyms = [
      { id: "1", name: "Gym A" },
      { id: "2", name: "Gym B" },
    ];

    mocks.useAuth.mockReturnValue({
      user: {
        role: "gym_manager",
        linkedGyms: mockGyms,
      },
    });

    render(
      <MemoryRouter>
        <MyGyms />
      </MemoryRouter>,
    );

    const gymAElement = document.querySelector(".card-title") as HTMLElement;
    expect(gymAElement.textContent).toBe("Gym A");

    // Simulate clicking Gym A
    gymAElement.click();

    expect(document.querySelector('[data-testid="gym-form"]')).toBeTruthy();
  });

  it("does not render gyms for non-gym manager users", () => {
    mocks.useAuth.mockReturnValue({
      user: {
        role: "client",
        linkedGyms: [],
      },
    });

    render(
      <MemoryRouter>
        <MyGyms />
      </MemoryRouter>,
    );

    const gymElements = Array.from(
      document.querySelectorAll(".card-title"),
    ).map((el) => el.textContent);

    expect(gymElements).not.toContain("Gym A");
    expect(gymElements).not.toContain("Gym B");
  });
});
