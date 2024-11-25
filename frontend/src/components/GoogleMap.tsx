import React from "react";

// Assuming you're using a library like Google Maps or Leaflet for the map
import {
  AdvancedMarker,
  APIProvider,
  Map,
  Pin,
} from "@vis.gl/react-google-maps";
import { Gym } from "../types.ts";

interface GoogleMapProps {
  gyms: Gym[];
}

const PoiMarkers = (props: { pois: Gym[] }) => {
  return (
    <>
      {props.pois.map((poi: Gym) => (
        <AdvancedMarker
          key={poi.name}
          position={{ lat: poi.latitude, lng: poi.longitude }}
        >
          <Pin
            background={"#FBBC04"}
            glyphColor={"#000"}
            borderColor={"#000"}
          />
        </AdvancedMarker>
      ))}
    </>
  );
};

const GoogleMap: React.FC<GoogleMapProps> = ({ gyms }) => {
  const mapCenter =
    gyms.length > 0
      ? { lat: gyms[0].latitude, lng: gyms[0].longitude }
      : { lat: 0, lng: 0 };

  console.log("Gyms:", gyms);
  return (
    <APIProvider
      apiKey="AIzaSyAjEzYhZoH1GHZ_LrBXo7tjKTYzHOB7Cqs"
      onLoad={() => console.log("Maps API has loaded.")}
    >
      <Map
        className="w-full aspect-video"
        defaultCenter={mapCenter}
        mapId="NearbyGymsMap"
        defaultZoom={12}
        defaultHeading={2}
      >
        <PoiMarkers pois={gyms} />
      </Map>
    </APIProvider>
  );
};

export default GoogleMap;
