interface User {
  username: string;
  role: string;
  email: string;
  id: string;
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

interface Gym {
  name: string;
  vicinity: string;
  location: {
    lng: number;
    lat: number;
  };
}

interface GymResponse {
  name: string;
  tags: {
    name: string;
  };
  lat: number;
  lon: number;
}
interface NearbyGymsProps {
  gyms: Gym[];
  loading: boolean;
  error: string | null;
}

export type {
  User,
  UseFetchUserResult,
  Event,
  UseFetchEventsResult,
  Gym,
  GymResponse,
  NearbyGymsProps,
};
