package com.example.finalyearproject.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class LocationsWithGuidedWalks {
    @Embedded
    public Location location;
    @Relation(
            parentColumn = "LocationID",
            entityColumn = "GuidedWalkID",
            associateBy = @Junction(LocationGuidedWalkCrossRef.class)
    )
    public List<GuidedWalk> guidedWalk;
}


