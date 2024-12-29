import { Dispatch, SetStateAction } from "react";

interface User {
  username: string;
  role: "gym_manager" | "client" | "personal_trainer";
  email: string;
  userSpecs?: UserSpec;
}

interface Client extends User {
  workoutsPerWeek: number;
  preferredTime: string;
  role: "client";
  purchases: Purchase[];
  ptServices: PTService[];
}

interface GymManager extends User {
  role: "gym_manager";
  linkedGyms: Gym[];
}

interface PersonalTrainer extends User {
  linkedGym?: Gym;
  description: string;
  role: "personal_trainer";
  services?: PTService[];
}

interface PTService {
  id: number;
  name: string;
  description: string;
  price: number;
  duration: number;
  type: string;
  personalTrainer?: PersonalTrainer;
}

interface Purchase {
  id: number;
  client: string;
  purchaseDate: string;
  gym: Gym;
  type: string;
  price: number;
}

type UserOptions = Client | GymManager | PersonalTrainer;

interface UseFetchUserResult {
  isAuthenticated: boolean;
  user: UserOptions | null;
  error: string | null;
  logout: () => Promise<void>;
  fetchUsers: () => Promise<void>;
  updateUserData: (data: UserOptions) => void;
  addServiceToPersonalTrainer: (service: PTService) => void;
  purchaseGymMembership: (purchase: Purchase) => Promise<void>;
  fetchPersonalTrainers: () => Promise<PersonalTrainer[] | undefined>;
  purchasePTService: (serviceId: number) => Promise<void>;
  fetchAvailablePTs: () => Promise<PersonalTrainer[] | undefined>;
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

class Gym {
  id: number;
  name: string;
  dailyFee?: number;
  weeklyMembership?: number;
  description?: string;
  personalTrainers: PersonalTrainer[] = [];
  overpassData?: GymOverpass;
  distance?: number;

  constructor(id: number, name: string) {
    this.id = id;
    this.name = name;
  }
}
interface GymOverpass {
  id: number;
  tags: GymSpec;
  lat: number;
  lon: number;
  name?: string;
}

interface GymSpec {
  addrCity?: string;
  addrHouseNumber?: string;
  addrPostcode?: string;
  addrStreet?: string;
  phone?: string;
  website?: string;
  openingHours?: string;
}

interface GymResponse {
  id: number;
  tags: {
    name: string;
  };
  lat: number;
  lon: number;
}

interface GymsProps {
  error: string | null;
  gyms: Gym[];
  loading: boolean;
  onSelectGym?: Dispatch<SetStateAction<Gym | undefined>>;
}

type UserSpec = {
  email: string;
  email_verified: boolean;
  family_name: string;
  given_name: string;
  hd: string;
  name: string;
  picture: string;
  sub: string;
};

export type {
  Client,
  Event,
  GymManager,
  GymOverpass,
  GymResponse,
  GymsProps,
  PersonalTrainer,
  PTService,
  Purchase,
  UseFetchEventsResult,
  UseFetchUserResult,
  User,
  UserOptions,
};

export { Gym };
