package io.flutter.plugins.googlemaps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.Collection;
import java.util.List;

public class GradientLinesGroundOverlayGenerator {
  private final int tileSize;
  private Paint paint;
  private Canvas canvas;
  private Bitmap bitmap;
  private Path path;

  public GradientLinesGroundOverlayGenerator(float density) {
    this.tileSize = (int) (256 * density);
    this.paint = new Paint();
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeCap(Paint.Cap.ROUND);
    paint.setStrokeWidth(12);
    paint.setAntiAlias(true);
    this.path = new Path();
  }

  public LatLngBounds getBounds(Collection<GradientLine> gradientLines) {
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    for (GradientLine gradientLine : gradientLines) {
      for (ColorPoint colorPoint : gradientLine.getPoints() ) {
        builder.include(colorPoint.getLocation());
      }
    }
    return builder.build();
  }

  public Bitmap generateBitmap(Collection<GradientLine> gradientLines, LatLngBounds bounds, int zoom) {
    int scale = 1 << 10;


    Point sw = project(bounds.southwest, scale, tileSize);
    Point ne = project(bounds.northeast, scale, tileSize);


    Log.d("BOUNDS IS ", bounds.toString());
    Log.d("SW IS ", sw.toString());
    Log.d("NE IS ", ne.toString());

    Bitmap bitmap = Bitmap.createBitmap(
        ne.x - sw.x,
        sw.y - ne.y,
        Bitmap.Config.ARGB_8888
    );
    Canvas canvas = new Canvas(bitmap);

    boolean didDraw = false;

    for (GradientLine gradientLine : gradientLines ) {
      final List<ColorPoint> points = gradientLine.getPoints();

      for (int index = 0; index < points.size() - 1; index++) {
        ColorPoint currentPoint = points.get(index);
        ColorPoint nextPoint = points.get(index + 1);

        Point currentPixelCoordinate = project(currentPoint.getLocation(), scale, tileSize);
        Point nextPixelCoordinate = project(nextPoint.getLocation(), scale, tileSize);

        int currentX = currentPixelCoordinate.x - sw.x;
        int currentY = currentPixelCoordinate.y - ne.y;
        int nextX = nextPixelCoordinate.x - sw.x;
        int nextY = nextPixelCoordinate.y - ne.y;

        path.rewind();
        path.moveTo(currentX, currentY);
        path.lineTo(nextX, nextY);

        if (canvas.quickReject(path, Canvas.EdgeType.AA)) {
          continue;
        }

        didDraw = true;

        paint.setShader(new LinearGradient(
            currentX, currentY, nextX, nextY, new int[]{currentPoint.getColor(), nextPoint.getColor()}, null, Shader.TileMode.CLAMP
        ));

        // drawLine appears to be faster than drawPath
        canvas.drawLine(currentX, currentY, nextX, nextY, paint);
      }
    }

    if (!didDraw) {
      return null;
    }

    return bitmap;
  }

  /**
   *
   * @param latLng
   * @param scale
   * @param tileSize
   * @return Point in pixel coordinates
   */
  private Point project(LatLng latLng, int scale, int tileSize) {
    double siny = Math.sin(latLng.latitude * Math.PI / 180);

    // Truncating to 0.9999 effectively limits latitude to 89.189. This is
    // about a third of a tile past the edge of the world tile.
    siny = Math.min(Math.max(siny, -0.9999), 0.9999);

    return new Point(
        (int) Math.floor((tileSize * scale * (0.5 + latLng.longitude / 360))),
        (int) Math.floor((tileSize * scale * (0.5 - Math.log((1 + siny) / (1 - siny)) / (4 * Math.PI))))
    );
  }
}
