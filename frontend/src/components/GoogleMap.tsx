import React from "react";

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

const PoiMarkers: React.FC<GoogleMapProps> = ({ gyms }) => {
  return (
    <>
      {gyms.map((poi: Gym) => (
        <AdvancedMarker
          key={poi.name}
          position={{
            lat: poi.overpassData?.lat ?? 0,
            lng: poi.overpassData?.lon ?? 0,
          }}
          className="marker"
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
      ? {
          lat: gyms[0].overpassData?.lat ?? 0,
          lng: gyms[0].overpassData?.lon ?? 0,
        }
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
        <PoiMarkers gyms={gyms} />
      </Map>
    </APIProvider>
  );
};

export default GoogleMap;
