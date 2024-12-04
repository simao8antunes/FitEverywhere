import { render } from "vitest-browser-react";
import { describe, it, expect, vi, afterEach } from "vitest";
import { MemoryRouter } from "react-router-dom";
import AuthProvider from "../providers/AuthProvider.tsx";

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

describe("AuthProvider", () => {
  afterEach(() => {
    vi.clearAllMocks();
  });

  it("renders children correctly", () => {
    const screen = render(
      <MemoryRouter>
        <AuthProvider>
          <div>Child Component</div>
        </AuthProvider>
      </MemoryRouter>,
    );

    expect(screen.getByText("Child Component").element()).toBeInTheDocument();
  });

  it("fetches and provides user data on mount", async () => {
    globalThis.fetch = vi.fn().mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        user: { email: "test@example.com", userSpecs: {} },
      }),
    });

    render(
      <MemoryRouter>
        <AuthProvider>
          <div>Child Component</div>
        </AuthProvider>
      </MemoryRouter>,
    );

    await vi.waitFor(() => {
      expect(sessionStorage.getItem("user")).toEqual("test@example.com");
    });
  });

  it("handles unauthenticated state (401)", async () => {
    globalThis.fetch = vi.fn().mockResolvedValueOnce({
      ok: false,
      status: 401,
    });

    render(
      <MemoryRouter>
        <AuthProvider>
          <div>Child Component</div>
        </AuthProvider>
      </MemoryRouter>,
    );

    await vi.waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith("/login");
    });
  });

  it("handles user not found (404)", async () => {
    globalThis.fetch = vi.fn().mockResolvedValueOnce({
      ok: false,
      status: 404,
    });

    render(
      <MemoryRouter>
        <AuthProvider>
          <div>Child Component</div>
        </AuthProvider>
      </MemoryRouter>,
    );

    await vi.waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith("/select-role");
    });
  });

  it("handles fetch errors", async () => {
    globalThis.fetch = vi
      .fn()
      .mockRejectedValueOnce(new Error("Network Error"));

    render(
      <MemoryRouter>
        <AuthProvider>
          <div>Child Component</div>
        </AuthProvider>
      </MemoryRouter>,
    );

    await vi.waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith("/login");
    });
  });
});
