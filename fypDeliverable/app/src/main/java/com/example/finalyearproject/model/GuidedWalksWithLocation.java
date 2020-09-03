package com.example.finalyearproject.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class GuidedWalksWithLocation {
    @Embedded
    public GuidedWalk guidedWalk;
    @Relation(
            parentColumn = "GuidedWalkID",
            entityColumn = "LocationID",
            associateBy = @Junction(LocationGuidedWalkCrossRef.class)
    )
    public List<Location> locations;
}


