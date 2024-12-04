import { render } from "vitest-browser-react";
import { describe, it, expect, vi } from "vitest";
import { userEvent } from "@vitest/browser/context";
import Dashboard from "../pages/Dashboard.tsx";

const mocks = vi.hoisted(() => {
  return {
    useFetchEvents: vi.fn(),
    useFetchGyms: vi.fn(),
  };
});

vi.mock(import("../hooks/useFetchEvents.ts"), () => ({
  useFetchEvents: mocks.useFetchEvents,
}));

vi.mock(import("../hooks/useFetchGyms.ts"), () => ({
  useFetchGyms: mocks.useFetchGyms,
}));

describe("Dashboard", () => {
  it("renders loading state for events and gyms", () => {
    // Mocking hooks to return loading states
    mocks.useFetchEvents.mockReturnValue({
      events: [],
      loading: true,
      error: null,
    });
    mocks.useFetchGyms.mockReturnValue({
      gyms: [],
      fetchNearbyGyms: vi.fn(),
      loading: true,
      error: null,
    });

    const screen = render(<Dashboard />);

    expect(screen.getByText("Loading events...").element()).toBeInTheDocument();
  });

  it("renders error state for events", () => {
    // Mocking hooks to return error states
    mocks.useFetchEvents.mockReturnValue({
      events: [],
      loading: false,
      error: "Failed to fetch events",
    });
    mocks.useFetchGyms.mockReturnValue({
      gyms: [],
      fetchNearbyGyms: vi.fn(),
      loading: false,
      error: null,
    });

    const screen = render(<Dashboard />);

    expect(
      screen.getByText("Error: Failed to fetch events").element(),
    ).toBeInTheDocument();
  });

  it("renders event list and allows event selection", async () => {
    // Mocking hooks to return mock data
    const mockEvents = [
      {
        id: 1,
        summary: "Event 1",
        location: "Rua Arquiteto Vital Lobao",
        start: { dateTime: new Date() },
        end: { dateTime: new Date() },
      },
      {
        id: 2,
        summary: "Event 2",
        location: "Rua do Amial",
        start: { dateTime: new Date() },
        end: { dateTime: new Date() },
      },
    ];
    mocks.useFetchEvents.mockReturnValue({
      events: mockEvents,
      loading: false,
      error: null,
    });
    mocks.useFetchGyms.mockReturnValue({
      gyms: [],
      fetchNearbyGyms: vi.fn(),
      loading: false,
      error: null,
    });

    const screen = render(<Dashboard />);

    // Ensure events are rendered
    expect(screen.getByText("Upcoming Events").element()).toBeInTheDocument();
    expect(screen.getByText("Event 1").element()).toBeInTheDocument();
    expect(screen.getByText("Event 2").element()).toBeInTheDocument();

    // Simulate event selection
    await userEvent.click(screen.getByText("Event 1").element());
    await vi.waitFor(() => {
      expect(
        screen.getByText("Nearby Gyms for Event 1").element(),
      ).toBeInTheDocument();
    });
  });

  it("displays nearby gyms after selecting an event", async () => {
    // Mocking hooks to return mock data
    const mockEvents = [
      {
        id: 1,
        summary: "Event 1",
        location: "Location 1",
        start: { dateTime: new Date() },
        end: { dateTime: new Date() },
      },
    ];
    const mockGyms = [
      { name: "Gym 1", distance: 1 },
      { name: "Gym 2", distance: 2 },
    ];
    mocks.useFetchEvents.mockReturnValue({
      events: mockEvents,
      loading: false,
      error: null,
    });
    mocks.useFetchGyms.mockReturnValue({
      gyms: mockGyms,
      fetchNearbyGyms: vi.fn(),
      loading: false,
      error: null,
    });

    const screen = render(<Dashboard />);

    await userEvent.click(screen.getByText("Event 1"));

    await vi.waitFor(() => {
      // Ensure gyms are rendered after selecting an event
      expect(screen.getByText("Gym 1").element()).toBeInTheDocument();
      expect(screen.getByText("Gym 2").element()).toBeInTheDocument();
    });
  });

  it("shows message when no gyms are found", async () => {
    // Mocking hooks to return no gyms
    const mockEvents = [
      {
        id: 1,
        summary: "Event 1",
        location: "Location 1",
        start: { dateTime: new Date() },
        end: { dateTime: new Date() },
      },
    ];
    mocks.useFetchEvents.mockReturnValue({
      events: mockEvents,
      loading: false,
      error: null,
    });
    mocks.useFetchGyms.mockReturnValue({
      gyms: [],
      fetchNearbyGyms: vi.fn(),
      loading: false,
      error: null,
    });

    const screen = render(<Dashboard />);

    await userEvent.click(screen.getByText("Event 1"));

    await vi.waitFor(() => {
      // Ensure message is displayed when no gyms are found
      expect(
        screen.getByText("No nearby gyms found.").element(),
      ).toBeInTheDocument();
    });
  });

  it("handles error state for gyms", async () => {
    // Mocking hooks to return error state for gyms
    const mockEvents = [
      {
        id: 1,
        summary: "Event 1",
        location: "Location 1",
        start: { dateTime: new Date() },
        end: { dateTime: new Date() },
      },
    ];
    mocks.useFetchEvents.mockReturnValue({
      events: mockEvents,
      loading: false,
      error: null,
    });
    mocks.useFetchGyms.mockReturnValue({
      gyms: [],
      fetchNearbyGyms: vi.fn(),
      loading: false,
      error: "Failed to fetch gyms",
    });

    const screen = render(<Dashboard />);

    await userEvent.click(screen.getByText("Event 1"));

    await vi.waitFor(() => {
      // Ensure error message is displayed for gyms
      expect(
        screen.getByText("Error: Failed to fetch gyms").element(),
      ).toBeInTheDocument();
    });
  });
});
