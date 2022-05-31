package com.example.zooseeker.db;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.Locale;
import java.util.Objects;

/**
 * Represents a group, with an optional group.
 */
public class ExhibitWithGroup implements Parcelable {
    @Embedded
    public Exhibit exhibit;
    @Relation(
            parentColumn = "group_id",
            entityColumn = "id"
    )
    public Exhibit group = null;

    public String getExhibitName() {
        return exhibit.name;
    }

    public String getExhibitID() { return exhibit.id; }

    public String getGroupName() {
        if (group == null) return " ";
        return group.name;
    }

    public String getCoordString() {
        var coords = getCoords();
        return String.format(Locale.getDefault(), "%3.6f, %3.6f", coords.first, coords.second);
    }

    public Pair<Double, Double> getCoords() {
        if (group != null) {
            return Pair.create(group.lat, group.lng);
        } else {
            return Pair.create(exhibit.lat, exhibit.lng);
        }
    }

    public boolean isCloseTo(Pair<Double, Double> otherCoords) {
        return isCloseTo(otherCoords, 0.001);
    }

    public boolean isCloseTo(Pair<Double, Double> otherCoords, double delta) {
        var coords = getCoords();
        if (coords == null
                || otherCoords == null
                || coords.first == null || coords.second == null
                || otherCoords.first == null || otherCoords.second == null) return false;
        var dLat = coords.first - otherCoords.first;
        var dLng = coords.second - otherCoords.second;
        return Math.sqrt(Math.pow(dLat, 2) + Math.pow(dLng, 2)) < delta;
    }

    @Override
    public String toString() {
        return "ExhibitWithGroup{" +
                "exhibit=" + exhibit +
                ", group=" + group +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExhibitWithGroup)) return false;
        ExhibitWithGroup exhibit1 = (ExhibitWithGroup) o;
        return Objects.equals(exhibit, exhibit1.exhibit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exhibit);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.exhibit, flags);
        dest.writeParcelable(this.group, flags);
    }

    public void readFromParcel(Parcel source) {
        this.exhibit = source.readParcelable(Exhibit.class.getClassLoader());
        this.group = source.readParcelable(Exhibit.class.getClassLoader());
    }

    public ExhibitWithGroup() {
    }

    protected ExhibitWithGroup(Parcel in) {
        this.exhibit = in.readParcelable(Exhibit.class.getClassLoader());
        this.group = in.readParcelable(Exhibit.class.getClassLoader());
    }

    public static final Creator<ExhibitWithGroup> CREATOR = new Creator<ExhibitWithGroup>() {
        @Override
        public ExhibitWithGroup createFromParcel(Parcel source) {
            return new ExhibitWithGroup(source);
        }

        @Override
        public ExhibitWithGroup[] newArray(int size) {
            return new ExhibitWithGroup[size];
        }
    };
}
