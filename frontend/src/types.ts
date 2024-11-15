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
  vicinity: string;
  geometry: {
    location: {
      lat: number;
      lng: number;
    };
  };
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
