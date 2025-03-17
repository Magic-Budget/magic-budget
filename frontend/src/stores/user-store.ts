import { UUID } from "crypto";
import { create } from "zustand";

export interface UserStore {
  id: UUID;
  username: string;
  firstName: string;
  lastName: string;
  bearerToken: string;
  updateFirstName: (firstName: string) => void;
  updateLastName: (lastName: string) => void;
  updateUsername: (username: string) => void;
  updateId: (id: UUID) => void;
  updateBearerToken: (bearerToken: string) => void;
}

export const useUserStore = create<UserStore>((set) => ({
  id: "00000000-0000-0000-0000-000000000000",
  username: "",
  firstName: "",
  lastName: "",
  bearerToken: "",
  updateFirstName: (firstName: string) => set(() => ({ firstName: firstName })),
  updateLastName: (lastName: string) => set(() => ({ lastName: lastName })),
  updateUsername: (username: string) => set(() => ({ username: username })),
  updateId: (id: UUID) => set(() => ({ id: id })),
  updateBearerToken: (bearerToken: string) =>
    set(() => ({ bearerToken: bearerToken })),
}));
