package com.redispro.geospatialindex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class GeoSpatialService {

    private static final String GEO_KEY = "locations";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void addLocation(String locationName, double longitude, double latitude) {
        redisTemplate.opsForGeo().add(GEO_KEY, new Point(longitude, latitude), locationName);
    }

    public GeoResults<RedisGeoCommands.GeoLocation<String>> findLocationsNearby(double longitude, double latitude, double distanceInKm) {
        Point point = new Point(longitude, latitude);
        Distance distance = new Distance(distanceInKm, RedisGeoCommands.DistanceUnit.KILOMETERS);
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(50);
        return redisTemplate.opsForGeo().radius(GEO_KEY, point, distance, args);
    }
}

