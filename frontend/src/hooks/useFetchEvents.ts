import { useEffect, useState } from "react";
import type { Event, UseFetchEventsResult } from "../types";

export function useFetchEvents(): UseFetchEventsResult {
  const [events, setEvents] = useState<Event[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchEvents = async () => {
    return await fetch("/api/calendar/events", {
      mode: "no-cors",
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
        setError("Failed to load events.");
        throw new Error(`Error fetching calendar events: ${err}`);
      })
      .finally(() => setLoading(false));
  }, []);

  return { events, loading, error };
}
