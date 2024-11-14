interface User {
  username: string;
  role: string;
}

interface UseFetchUserResult {
  isAuthenticated: boolean;
  user: User | null;
  error: string | null;
  logout: () => void;
}

interface Event {
  id: string;
  summary: string;
  start: { dateTime: string };
  end: { dateTime: string };
  location?: string;
}

interface UseFetchEventsResult {
  events: Event[];
  loading: boolean;
  error: string | null;
}

export type { User, UseFetchUserResult, Event, UseFetchEventsResult };
