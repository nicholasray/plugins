package io.flutter.plugins.googlemaps;

import android.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradientLines {
  private final Map<String, GradientLine> gradientLineIdToGradientLine;

  public GradientLines() {
    this.gradientLineIdToGradientLine = new HashMap<>();
  }

  public Collection<GradientLine> getGradientLines() {
    return gradientLineIdToGradientLine.values();
  }

  public GradientLine getGradientLineById(String id) {
    return gradientLineIdToGradientLine.get(id);
  }

  public void addGradientLinesFromObjects(List<Object> gradientLinesToAdd) {
    if (gradientLinesToAdd != null) {
      for (Object gradientLineToAdd : gradientLinesToAdd) {
        addGradientLineFromObject(gradientLineToAdd);
      }
    }
  }

  private void addGradientLineFromObject(Object gradientLine) {
    if (gradientLine == null) {
      return;
    }

    GradientLine typedGradientLine = Convert.makeGradientLine(gradientLine);
    gradientLineIdToGradientLine.put(typedGradientLine.getId(), typedGradientLine);
  }

  public boolean changeGradientLinesFromObjects(List<Object> gradientLinesToChange) {
    if (gradientLinesToChange == null) {
      return false;
    }

    boolean didChange = false;
    for (Object gradientLine : gradientLinesToChange ) {
      didChange = changeGradientLineFromObject(gradientLine) || didChange;
    }


    return didChange;
  }

  private boolean changeGradientLineFromObject(Object gradientLine) {
    if (gradientLine == null) {
      return false;
    }
    Log.d("Gradient Line to change", gradientLine.toString());
    GradientLine typedGradientLine = Convert.makeGradientLine(gradientLine);
    gradientLineIdToGradientLine.put(typedGradientLine.getId(), typedGradientLine);

    return true;
  }

  public boolean removeGradientLines(List<Object> gradientLineIdsToRemove) {
    if (gradientLineIdsToRemove == null) {
      return false;
    }

    boolean didChange = false;
    for (Object rawGradientLineId: gradientLineIdsToRemove) {
      if (rawGradientLineId == null) {
        continue;
      }

      GradientLine removed = gradientLineIdToGradientLine.remove(rawGradientLineId);
      didChange = removed != null || didChange;
    }

    return didChange;
  }
}
