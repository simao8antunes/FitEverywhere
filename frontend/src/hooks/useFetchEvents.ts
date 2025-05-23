import { useEffect, useState } from "react";
import type { Event, UseFetchEventsResult } from "../types";

const API_URL = import.meta.env.VITE_API_BASE_URL as string;

export function useFetchEvents(): UseFetchEventsResult {
  const [events, setEvents] = useState<Event[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchEvents = async () => {
    return await fetch(API_URL + "/calendar/events", {
      method: "GET",
      credentials: "include",
    });
  };

  useEffect(() => {
    fetchEvents()
      .then((response) => response.json())
      .then((data) => {
        const upcomingEvents = data.items.filter((event: Event) => {
          const eventStartDate = event.start?.dateTime
            ? new Date(event.start.dateTime)
            : null;
          const currentDate = new Date();

          // Only include events with valid start times, and those that are in the future
          return eventStartDate && eventStartDate > currentDate;
        });

        setEvents(upcomingEvents);
      })
      .catch((err) => {
        console.log("Error fetching events:", err);
        setError("Failed to load events.");
        throw new Error(`Error fetching calendar events: ${err}`);
      })
      .finally(() => setLoading(false));
  }, []);

  return { events, loading, error };
}
