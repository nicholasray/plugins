package io.flutter.plugins.googlemaps;

import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;

public class ColorPoint {
    private LatLng location;
    private int color;

    public ColorPoint(LatLng location, int color) {
        this.location = location;
        this.color = color;
    }

    public LatLng getLocation() {
        return location;
    }

    public int getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ColorPoint)) return false;
        ColorPoint that = (ColorPoint) o;
        return color == that.color &&
            location.equals(that.location);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{location, color});
    }
}
