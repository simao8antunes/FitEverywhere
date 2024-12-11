import { render } from "vitest-browser-react";
import { describe, it, expect, vi, afterEach, beforeEach } from "vitest";
import { userEvent } from "@vitest/browser/context";
import SelectRole from "../pages/SelectRole.tsx";

const mockNavigate = vi.fn();
vi.mock("react-router-dom", async () => {
  const actual = await vi.importActual("react-router-dom");
  return {
    ...actual,
    useNavigate: () => mockNavigate,
    useLocation: () => ({
      state: { userName: "Guest" },
    }),
  };
});

describe("SelectRole", () => {
  const mockFetch = vi.fn();

  beforeEach(() => {
    globalThis.fetch = mockFetch;
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it("renders correctly and disables the Continue button by default", () => {
    const screen = render(<SelectRole />);

    expect(
      screen.getByText("Hello, Guest! Please select your role:").element(),
    ).toBeInTheDocument();
    expect(screen.getByText("Continue").element()).toBeDisabled();
  });

  it("enables the Continue button when a role is selected", async () => {
    const screen = render(<SelectRole />);

    const gymButton = screen.getByText("Gym").element();
    const continueButton = screen.getByText("Continue").element();

    expect(continueButton).toBeDisabled();

    await userEvent.click(gymButton);
    expect(continueButton).not.toBeDisabled();
  });

  it("navigates to the home page with the selected role on success", async () => {
    mockFetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({ message: "Success" }),
    });

    const screen = render(<SelectRole />);

    await userEvent.click(screen.getByText("Gym").element());
    await userEvent.click(screen.getByText("Continue").element());

    expect(mockFetch).toHaveBeenCalledWith("/auth/signup?role=gym", {
      method: "POST",
      credentials: "include",
    });

    await vi.waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith("/", {
        state: { userName: "Guest", role: "gym" },
      });
    });
  });

  it("shows an alert for invalid role selection", async () => {
    mockFetch.mockResolvedValueOnce({
      ok: false,
      status: 400,
      json: async () => ({ error: "Invalid role" }),
    });

    const alertMock = vi.spyOn(window, "alert").mockImplementation(() => {});

    const screen = render(<SelectRole />);

    await userEvent.click(screen.getByText("Client").element());
    await userEvent.click(screen.getByText("Continue").element());

    expect(mockFetch).toHaveBeenCalledWith("/auth/signup?role=client", {
      method: "POST",
      credentials: "include",
    });

    await vi.waitFor(() => {
      expect(alertMock).toHaveBeenCalledWith("Please select a valid role.");
    });

    alertMock.mockRestore();
  });

  it("navigates to the login page for unauthorized user", async () => {
    mockFetch.mockResolvedValueOnce({
      ok: false,
      status: 401,
      json: async () => ({ error: "Authentication required" }),
    });

    const screen = render(<SelectRole />);

    await userEvent.click(screen.getByText("Gym").element());
    await userEvent.click(screen.getByText("Continue").element());

    await vi.waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith("/login");
    });
  });

  it("shows an alert for network errors", async () => {
    mockFetch.mockRejectedValueOnce(new Error("Network error"));

    const alertMock = vi.spyOn(window, "alert").mockImplementation(() => {});

    const screen = render(<SelectRole />);

    await userEvent.click(screen.getByText("Client").element());
    await userEvent.click(screen.getByText("Continue").element());

    await vi.waitFor(() => {
      expect(alertMock).toHaveBeenCalledWith(
        "An unexpected error occurred. Please check your connection and try again.",
      );
    });

    alertMock.mockRestore();
  });
});
