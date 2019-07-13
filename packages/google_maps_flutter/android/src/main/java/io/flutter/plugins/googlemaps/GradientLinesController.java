package io.flutter.plugins.googlemaps;

import android.util.Log;
import android.util.LruCache;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileProvider;

import java.util.List;

public class GradientLinesController implements TileProvider {
    private final GradientLines gradientLines;
    private final GradientLinesTileGenerator tileGenerator;
    private TileOverlay tileOverlay;
    private final LruCache<String, Tile> tileCache;

    GradientLinesController(float density) {
        this.gradientLines = new GradientLines();
        this.tileGenerator = new GradientLinesTileGenerator(density);

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        this.tileCache = new LruCache<String, Tile>( cacheSize) {
            @Override
            protected int sizeOf(String key, Tile tile) {
              return tile.data.length / 1024;
            }
        };
    }

    void setTileOverlay(TileOverlay tileOverlay) {
      this.tileOverlay = tileOverlay;
    }

    void addGradientLines(List<Object> gradientLinesToAdd) {
        gradientLines.addGradientLinesFromObjects(gradientLinesToAdd);
        clearCache();
    }

    void changeGradientLines(List<Object> gradientLinesToChange) {
      boolean result = gradientLines.changeGradientLinesFromObjects(gradientLinesToChange);
      Log.d("IN JAVA changing lines", "************");
      if (result) {
        clearCache();
      }
    }

    void removeGradientLines(List<Object> gradientLineIdsToRemove) {
      boolean result = gradientLines.removeGradientLines(gradientLineIdsToRemove);
      if (result) {
        clearCache();
      }
    }

    private void clearCache() {
      tileCache.evictAll();
      tileOverlay.clearTileCache();
    }

    @Override
    public Tile getTile(int x, int y, int zoom) {
      try {
          String cacheKey = x + "," + y + "," + zoom;
          Tile cachedTile = tileCache.get(cacheKey);
          if (cachedTile != null) {
            Log.d("CACHE HIT", "*");
            return cachedTile;
          }

          Tile tile = tileGenerator.generateTile(gradientLines, x, y, zoom);
          if (tile == null) {
            return TileProvider.NO_TILE;
          }
          tileCache.put(cacheKey, tile);

          return tile;
      } catch (Exception e) {
          Log.e("exception", e.toString());

          return TileProvider.NO_TILE;
      }
    }
}
