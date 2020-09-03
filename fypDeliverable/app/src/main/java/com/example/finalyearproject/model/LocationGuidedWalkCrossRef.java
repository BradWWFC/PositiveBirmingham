package com.example.finalyearproject.model;

import androidx.room.Entity;

@Entity(primaryKeys = {"LocationID", "GuidedWalkID"})
public class LocationGuidedWalkCrossRef {
    public long LocationID;
    public long GuidedWalkID;
}
