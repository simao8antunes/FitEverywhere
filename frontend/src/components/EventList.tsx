import React from "react";
import type { Event, UseFetchEventsResult } from "../types";

interface EventListProps extends UseFetchEventsResult {
  onEventClick: (event: Event) => void; // Replace `any` with your event type
}

const EventList: React.FC<EventListProps> = ({
  events,
  loading,
  error,
  onEventClick,
}) => {
  // Render loading state
  if (loading) {
    return <div>Loading events...</div>;
  }

  // Render error state
  if (error) {
    return <div>Error: {error}</div>;
  }

  // Render no events message
  if (events.length === 0) {
    return <p>No upcoming events.</p>;
  }

  // Sort events by their start time (closest to farthest)
  const sortedEvents = [...events].sort(
    (a, b) =>
      new Date(a.start.dateTime).getTime() -
      new Date(b.start.dateTime).getTime(),
  );

  // Render list of events
  return (
    <div>
      <div className="space-y-4">
        {sortedEvents.map((event) => (
          <div
            key={event.id}
            id={event.id}
            className="card bg-base-200 hover:bg-neutral hover:text-neutral-content cursor-pointer transition-colors event"
            onClick={() => onEventClick(event)}
          >
            <div className="card-body">
              <h3 className="card-title">{event.summary}</h3>
              <p className="text-sm">
                {new Date(event.start.dateTime).toLocaleString()} -{" "}
                {new Date(event.end.dateTime).toLocaleString()}
                <label>{event.location}</label>
              </p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default EventList;
