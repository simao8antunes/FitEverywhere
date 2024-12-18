import { render } from "vitest-browser-react";
import { describe, it, expect, vi, afterEach } from "vitest";
import { MemoryRouter } from "react-router-dom";
import MyPT from "../pages/MyPT.tsx";

const mocks = vi.hoisted(() => ({
  useAuth: vi.fn(),
}));

// Mock the `useAuth` hook
vi.mock(import("../hooks/useAuth.ts"), () => ({
  useAuth: mocks.useAuth,
}));

afterEach(() => {
  vi.clearAllMocks(); // Clean up mocks after each test
});

describe("MyPT Component", () => {
  it("renders warning for non-client users", () => {
    mocks.useAuth.mockReturnValue({
      user: {
        role: "gym_manager",
        username: "test_manager",
      },
    });

    render(
      <MemoryRouter>
        <MyPT />
      </MemoryRouter>,
    );

    const alertElement = document.querySelector(".alert-warning");
    expect(alertElement).toBeTruthy();
    expect(alertElement?.textContent).toContain(
      "Only clients can view their personal trainers.",
    );
  });

  it("renders PT services for client users", () => {
    const mockServices = [
      {
        personalTrainer: {
          email: "pt1@example.com",
          username: "Trainer One",
          description: "Specialist in strength training.",
        },
        name: "Strength Training",
        duration: 60,
        price: 50,
      },
      {
        personalTrainer: {
          email: "pt2@example.com",
          username: "Trainer Two",
          description: "Expert in cardio and endurance.",
        },
        name: "Cardio Workout",
        duration: 45,
        price: 40,
      },
    ];

    mocks.useAuth.mockReturnValue({
      user: {
        role: "client",
        username: "test_client",
        ptServices: mockServices,
      },
    });

    render(
      <MemoryRouter>
        <MyPT />
      </MemoryRouter>,
    );

    const serviceCards = document.querySelectorAll(".card-title");
    expect(serviceCards.length).toBe(2);

    const serviceTitles = Array.from(serviceCards).map(
      (card) => card.textContent,
    );
    expect(serviceTitles).toContain("Trainer One");
    expect(serviceTitles).toContain("Trainer Two");

    const serviceDescriptions = Array.from(
      document.querySelectorAll(".card-body p"),
    ).map((desc) => desc.textContent);
    expect(serviceDescriptions).toContain("Specialist in strength training.");
    expect(serviceDescriptions).toContain("Expert in cardio and endurance.");
  });

  it("renders no PT services message if client has no services", () => {
    mocks.useAuth.mockReturnValue({
      user: {
        role: "client",
        username: "test_client",
        ptServices: [],
      },
    });

    render(
      <MemoryRouter>
        <MyPT />
      </MemoryRouter>,
    );

    const noServicesMessage = document.querySelector(".alert-info");
    expect(noServicesMessage).toBeTruthy();
    expect(noServicesMessage?.textContent).toContain(
      "You have not purchased any personal trainer services yet.",
    );
  });

  it("renders correct structure for PT services", () => {
    const mockServices = [
      {
        personalTrainer: {
          email: "pt1@example.com",
          username: "Trainer One",
        },
        name: "Yoga Session",
        duration: 30,
        price: 25,
      },
    ];

    mocks.useAuth.mockReturnValue({
      user: {
        role: "client",
        username: "test_client",
        ptServices: mockServices,
      },
    });

    render(
      <MemoryRouter>
        <MyPT />
      </MemoryRouter>,
    );

    const serviceCard = document.querySelector(".card");
    expect(serviceCard).toBeTruthy();

    const serviceDetails = serviceCard?.querySelector(".list-disc");
    expect(serviceDetails?.textContent).toContain("Yoga Session");
    expect(serviceDetails?.textContent).toContain("30 minutes");
    expect(serviceDetails?.textContent).toContain("€25.00");
  });
});
