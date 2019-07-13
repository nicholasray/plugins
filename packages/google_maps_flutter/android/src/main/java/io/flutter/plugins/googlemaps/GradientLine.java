package io.flutter.plugins.googlemaps;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;

public class GradientLine {
  private final String id;
  private final List<ColorPoint> points;
  private final int width;

  GradientLine(String id, List<ColorPoint> points, int width) {
    this.id = id;
    this.points = points;
    this.width = width;
  }

  public String getId() {
    return id;
  }

  public List<ColorPoint> getPoints() {
    return points;
  }

  public int getWidth() {
    return width;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (!(other instanceof GradientLine)) return false;
    GradientLine otherTyped = (GradientLine) other;

    return
        id.equals(otherTyped.id) &&
          width == otherTyped.width &&
          CollectionUtils.isEqualCollection(this.points, otherTyped.points);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(new Object[]{id, points, width});
  }
}
