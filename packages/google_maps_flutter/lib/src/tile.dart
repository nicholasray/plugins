part of google_maps_flutter;

class Tile {
  const Tile(this.width, this.height, this.image);

  final int width;
  final int height;
  final Uint8List image;


  Map<String, dynamic> _toJson() {
    final Map<String, dynamic> json = <String, dynamic>{};

    void addIfPresent(String fieldName, dynamic value) {
      if (value != null) {
        json[fieldName] = value;
      }
    }

    addIfPresent('width', width);
    addIfPresent('height', height);
    addIfPresent('image', image);

    return json;
  }
}