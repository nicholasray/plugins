package io.flutter.plugins.googlemaps;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Shader;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Tile;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class GradientLinesTileGenerator {
  private final int tileSize;
  private Paint paint;
  private Canvas canvas;
  private Bitmap bitmap;
  private Path path;

  public GradientLinesTileGenerator(float density) {
    this.tileSize = (int) (256 * density);
    this.paint = new Paint();
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeCap(Paint.Cap.ROUND);
    paint.setStrokeWidth(12);
    paint.setAntiAlias(true);
    this.path = new Path();
    this.bitmap = Bitmap.createBitmap(
        tileSize,
        tileSize,
        Bitmap.Config.ARGB_8888
    );
    this.canvas = new Canvas(bitmap);
  }

  public synchronized Tile generateTile(GradientLines gradientLines, int x, int y, int zoom) {
    boolean didDraw = false;
    int scale = 1 << zoom;
    int tileLeft = tileSize * x;
    int tileTop = tileSize * y;

    for (GradientLine gradientLine : gradientLines.getGradientLines() ) {
      final List<ColorPoint> points = gradientLine.getPoints();

      for (int index = 0; index < points.size() - 1; index++) {
        ColorPoint p = points.get(index);
        ColorPoint next = points.get(index + 1);

        Point pGlobalPoint = project(p.getLocation(), scale, tileSize);
        Point nextGlobalPoint = project(next.getLocation(), scale, tileSize);

        int currentX = pGlobalPoint.x - tileLeft;
        int currentY = pGlobalPoint.y - tileTop;
        int nextX = nextGlobalPoint.x - tileLeft;
        int nextY = nextGlobalPoint.y - tileTop;

        path.rewind();
        path.moveTo(currentX, currentY);
        path.lineTo(nextX, nextY);

        if (canvas.quickReject(path, Canvas.EdgeType.AA)) {
          continue;
        }

        didDraw = true;

        paint.setShader(new LinearGradient(
            currentX, currentY, nextX, nextY, new int[]{p.getColor(), next.getColor()}, null, Shader.TileMode.CLAMP
        ));

        // drawLine appears to be faster than drawPath
        canvas.drawLine(currentX, currentY, nextX, nextY, paint);
      }
    }

    if (!didDraw) {
      return null;
    }

    final ByteArrayOutputStream stream = new ByteArrayOutputStream();
    // still not sure if compression here is helpful or hurts performance
    bitmap.compress(Bitmap.CompressFormat.WEBP, 0, stream);

    Tile tile = new Tile(tileSize, tileSize, stream.toByteArray());

    // reset canvas
    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

    return tile;
  }

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
