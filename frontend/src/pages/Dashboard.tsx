import React, { useEffect } from "react";
import EventList from "../components/EventList";
import NearbyGyms from "../components/NearbyGyms";
import { useFetchEvents } from "../hooks/useFetchEvents";
import { useFetchGyms } from "../hooks/useFetchGyms";

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

  useEffect(() => {
    if (events.length > 0 && events[0].location) {
      fetchNearbyGyms(events[0].location).then(() => {
        console.log("Fetched nearby gyms");
      });
    }
  }, [events, fetchNearbyGyms]);

  return (
    <div className="container mx-auto px-4 py-6">
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Events Section */}
        <div className="bg-white rounded-lg shadow-lg p-6">
          <h2 className="text-xl font-bold mb-4">Upcoming Events</h2>
          <EventList
            events={events}
            loading={eventsLoading}
            error={eventsError}
          />
        </div>

        {/* Nearby Gyms Section */}
        <div className="bg-white rounded-lg shadow-lg p-6">
          <h3 className="text-xl font-bold mb-4">Nearby Gyms</h3>
          <NearbyGyms gyms={gyms} loading={gymsLoading} error={gymsError} />
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
