import { render } from "vitest-browser-react";
import { describe, expect, it } from "vitest";
import Login from "../pages/Login.tsx";

describe("Login Component", () => {
  it("renders the login page correctly", () => {
    const screen = render(<Login />);

    // Verify that the logo is displayed
    const logo = screen.getByAltText("FitEverywhere Logo");
    expect(logo.element()).toBeInTheDocument();

    // Verify the description text is displayed
    const description = screen.getByText(
      /With FitEverywhere, maintaining a fitness routine becomes a rewarding part of the travel experience/i,
    );
    expect(description.element()).toBeInTheDocument();

    // Verify the login button is displayed
    const loginButton = screen.getByTestId("login-button");
    expect(loginButton.element()).toBeInTheDocument();
    expect(loginButton.element()).toHaveTextContent("Login with Google");
  });
});
