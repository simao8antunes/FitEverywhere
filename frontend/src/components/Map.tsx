import React from "react";

// Assuming you're using a library like Google Maps or Leaflet for the map
import { GoogleMap, LoadScript, Marker } from "@react-google-maps/api";

interface Gym {
  name: string;
  latitude: number;
  longitude: number;
}

interface MapProps {
  gyms: Gym[];
}

const Map: React.FC<MapProps> = ({ gyms }) => {
  const mapCenter = gyms.length > 0 ? { lat: gyms[0].latitude, lng: gyms[0].longitude } : { lat: 0, lng: 0 };

  return (
    <LoadScript googleMapsApiKey="AIzaSyAjEzYhZoH1GHZ_LrBXo7tjKTYzHOB7Cqs">
      <GoogleMap
        mapContainerStyle={{ width: "400px", height: "400px" }}
        center={mapCenter}
        zoom={12}
      >
        {gyms.map((gym, index) => (
          <Marker
            key={index}
            position={{ lat: gym.latitude, lng: gym.longitude }}
            label={gym.name}
          />
        ))}
      </GoogleMap>
    </LoadScript>
  );
};

export default Map;
