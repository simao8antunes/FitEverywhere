import React, { useEffect, useState } from "react";
import EventList from "../components/EventList";
import NearbyGyms from "../components/gyms/NearbyGyms";
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
    <div data-testid="dashboard" className="container p-base">
      <div className="flex gap-s">
        {/* Events Section */}
        <div className="card bg-base-100 w-96 shadow-xl">
          <div className="card-body">
            <h2 className="card-title">Upcoming Events</h2>
            <EventList
              events={events}
              loading={eventsLoading}
              error={eventsError}
              onEventClick={(event) => setSelectedEvent(event)}
            />
          </div>
        </div>
        <div className="card bg-base-100 w-[540px] shadow-xl">
          <div className="card-body">
            {selectedEvent ? (
              <>
                <div className="flex justify-between items-center mb-4">
                  <h3 className="text-xl font-bold">
                    Nearby Gyms for {selectedEvent.summary}
                  </h3>
                  <button
                    onClick={() => setSelectedEvent(null)}
                    className="btn btn-outline btn-error transition-colors"
                  >
                    Close
                  </button>
                </div>
                <NearbyGyms
                  gyms={gyms}
                  loading={gymsLoading}
                  error={gymsError}
                />
              </>
            ) : (
              <p>Select an event to view nearby gyms.</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
