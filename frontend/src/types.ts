interface User {
  username: string;
  role: "gym_manager" | "client" | "personal_trainer";
  email: string;
}

interface Client extends User {
  workoutsPerWeek: number;
  preferredTime: string;
  role: "client";
}

interface GymManager extends User {
  role: "gym_manager";
}

interface PersonalTrainer extends User {
  linkedGym: string;
  role: "personal_trainer";
}

type UserOptions = Client | GymManager | PersonalTrainer;

interface UseFetchUserResult {
  isAuthenticated: boolean;
  user: UserOptions | null;
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
  distance?: string;
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
  Event,
  Gym,
  GymResponse,
  NearbyGymsProps,
  UseFetchEventsResult,
  UseFetchUserResult,
  User,
  UserOptions,
  Client,
  GymManager,
  PersonalTrainer,
};
