import React from "react";

// Assuming you're using a library like Google Maps or Leaflet for the map
import {
  AdvancedMarker,
  APIProvider,
  Map,
  Pin,
} from "@vis.gl/react-google-maps";

interface Gym {
  name: string;
  vicinity: string;
  location: {
    lng: number;
    lat: number;
  };
}

interface GoogleMapProps {
  gyms: Gym[];
}

const PoiMarkers = (props: { pois: Gym[] }) => {
  return (
    <>
      {props.pois.map((poi: Gym) => (
        <AdvancedMarker key={poi.name} position={poi.location}>
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
      ? { lat: gyms[0].location.lat, lng: gyms[0].location.lng }
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
