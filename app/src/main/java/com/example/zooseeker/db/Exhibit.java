package com.example.zooseeker.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "exhibits")
public class Exhibit implements Parcelable {
    /**
     * Load ZooNode's from a JSON file (such as vertex_info.json).
     *
     * @param infoReader a reader from which to read the JSON input.
     * @return a list
     */
    public static List<Exhibit> fromJson(Reader infoReader) {
        var gson = new Gson();
        var type = new TypeToken<List<Exhibit>>() {
        }.getType();
        return gson.fromJson(infoReader, type);
    }

    public static void toJson(List<Exhibit> infos, Writer writer) throws IOException {
        var gson = new Gson();
        var type = new TypeToken<List<Exhibit>>() {
        }.getType();
        gson.toJson(infos, type, writer);
        writer.flush();
        writer.close();
    }

    public enum Kind {
        // The SerializedName annotation tells GSON how to convert
        // from the strings in our JSON to this Enum.
        @SerializedName("gate") GATE,
        @SerializedName("exhibit") EXHIBIT,
        @SerializedName("intersection") INTERSECTION,
        @SerializedName("exhibit_group") EXHIBIT_GROUP;
    }

    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @NonNull
    public String id;

    @ColumnInfo(name = "group_id")
    @SerializedName("group_id")
    @Nullable
    public String groupId;

    @ColumnInfo(name = "kind")
    @SerializedName("kind")
    @NonNull
    public Kind kind;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    @NonNull
    public String name;

    @ColumnInfo(name = "tags")
    @SerializedName("tags")
    @NonNull
    public ArrayList<String> tags;

    @ColumnInfo(name = "lat")
    @SerializedName("lat")
    public Double lat;

    @ColumnInfo(name = "lng")
    @SerializedName("lng")
    public Double lng;

    public boolean isExhibit() {
        return kind.equals(Kind.EXHIBIT);
    }

    public boolean isIntersection() {
        return kind.equals(Kind.INTERSECTION);
    }

    public boolean isGroup() {
        return kind.equals(Kind.EXHIBIT_GROUP);
    }

    public boolean hasGroup() {
        return groupId != null;
    }

    public Exhibit(@NonNull String id,
                   @Nullable String groupId,
                   @NonNull Kind kind,
                   @NonNull String name,
                   @NonNull ArrayList<String> tags,
                   @Nullable Double lat,
                   @Nullable Double lng) {
        this.id = id;
        this.groupId = groupId;
        this.kind = kind;
        this.name = name;
        this.tags = tags;
        this.lat = lat;
        this.lng = lng;

        if (!this.hasGroup() && (lat == null || lng == null)) {
            throw new RuntimeException("Nodes must have a lat/long unless they are grouped.");
        }
    }

    @Override
    public String toString() {
        return "Exhibit{" +
                "id='" + id + '\'' +
                ", groupId='" + groupId + '\'' +
                ", kind=" + kind +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exhibit)) return false;
        Exhibit exhibit = (Exhibit) o;
        return id.equals(exhibit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.groupId);
        dest.writeInt(this.kind == null ? -1 : this.kind.ordinal());
        dest.writeString(this.name);
        dest.writeStringList(this.tags);
        if (this.lat != null || this.lng != null){
            dest.writeDouble(this.lat);
            dest.writeDouble(this.lng);
        } else {
            dest.writeDouble(0.0);
            dest.writeDouble(0.0);
        }

    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.groupId = source.readString();
        int tmpKind = source.readInt();
        this.kind = tmpKind == -1 ? null : Kind.values()[tmpKind];
        this.name = source.readString();
        this.tags = source.createStringArrayList();
        this.lat = source.readDouble();
        this.lng = source.readDouble();
    }

    protected Exhibit(Parcel in) {
        this.id = in.readString();
        this.groupId = in.readString();
        int tmpKind = in.readInt();
        this.kind = tmpKind == -1 ? null : Kind.values()[tmpKind];
        this.name = in.readString();
        this.tags = in.createStringArrayList();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
    }

    public static final Creator<Exhibit> CREATOR = new Creator<Exhibit>() {
        @Override
        public Exhibit createFromParcel(Parcel source) {
            return new Exhibit(source);
        }

        @Override
        public Exhibit[] newArray(int size) {
            return new Exhibit[size];
        }
    };
}
