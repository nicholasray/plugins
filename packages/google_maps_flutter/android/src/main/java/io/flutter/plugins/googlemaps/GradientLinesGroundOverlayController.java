package io.flutter.plugins.googlemaps;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

public class GradientLinesGroundOverlayController {
  private final GradientLinesGroundOverlayGenerator generator;
  private GroundOverlay groundOverlay;
  private GoogleMap googleMap;

  GradientLinesGroundOverlayController(float density) {
    this.generator = new GradientLinesGroundOverlayGenerator(density);
  }

  void setGoogleMap(GoogleMap googleMap) {
    this.googleMap = googleMap;
  }



  void showGradientLines(List<GradientLine> gradientLines) {
    if (!doesContainPoints(gradientLines)) {
      return;
    }

    if (groundOverlay != null) {
      groundOverlay.remove();
    }

    groundOverlay = googleMap.addGroundOverlay(createGroundOverlayOptions(gradientLines));
  }

  private boolean doesContainPoints(List<GradientLine> gradientLines) {
    for (GradientLine gradientLine : gradientLines) {
      if (gradientLine.getPoints().size() > 0 )  {
        return true;
      }
    }

    return false;
  }

  private GroundOverlayOptions createGroundOverlayOptions(List<GradientLine> gradientLines) {
    LatLngBounds groundOverlayBounds = generator.getBounds(gradientLines);
    return new GroundOverlayOptions()
        .image(BitmapDescriptorFactory.fromBitmap(
            generator.generateBitmap(
                gradientLines,
                groundOverlayBounds, (int)
                    googleMap.getCameraPosition().zoom
            )
        ))
        .positionFromBounds(groundOverlayBounds);
  }
}
