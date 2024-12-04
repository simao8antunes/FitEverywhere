import React, { useEffect, useState } from "react";
import EventList from "../components/EventList";
import NearbyGyms from "../components/NearbyGyms";
import { useFetchEvents } from "../hooks/useFetchEvents";
import { useFetchGyms } from "../hooks/useFetchGyms";
import type { Event } from "../types";

const Dashboard: React.FC = () => {
  const {
    events,
    loading: eventsLoading,
    error: eventsError,
  } = useFetchEvents();
  const {
    gyms,
    fetchNearbyGyms,
    loading: gymsLoading,
    error: gymsError,
  } = useFetchGyms();

  const [selectedEvent, setSelectedEvent] = useState<Event | null>(null);

  useEffect(() => {
    if (selectedEvent && selectedEvent.location) {
      fetchNearbyGyms(selectedEvent.location);
    }
  }, [selectedEvent]);

  return (
    <div data-testid="dashboard" className="container mx-auto px-4 py-6">
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Events Section */}
        <div className="bg-intense rounded-lg shadow-lg p-6">
          <h2 className="text-xl font-bold mb-4">Upcoming Events</h2>
          <EventList
            events={events}
            loading={eventsLoading}
            error={eventsError}
            onEventClick={(event) => setSelectedEvent(event)}
          />
        </div>

        {/* Nearby Gyms Section */}
        <div className="bg-intense rounded-lg shadow-lg p-6">
          {selectedEvent ? (
            <>
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-xl font-bold">
                  Nearby Gyms for {selectedEvent.summary}
                </h3>
                <button
                  onClick={() => setSelectedEvent(null)}
                  className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 transition-colors"
                >
                  Close
                </button>
              </div>
              <NearbyGyms gyms={gyms} loading={gymsLoading} error={gymsError} />
            </>
          ) : (
            <p>Select an event to view nearby gyms.</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
