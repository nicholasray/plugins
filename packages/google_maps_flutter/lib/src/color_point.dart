part of google_maps_flutter;

@immutable
class ColorPoint {
  const ColorPoint(this.location, this.color);

  final LatLng location;

  final Color color;

  dynamic _toJson() {
    final Map<String, dynamic> json = <String, dynamic>{};

    void addIfPresent(String fieldName, dynamic value) {
      if (value != null) {
        json[fieldName] = value;
      }
    }

    addIfPresent('location', location._toJson());
    addIfPresent('color', color.value);

    return json;
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other.runtimeType != runtimeType) return false;
    final ColorPoint typedOther = other;

    return location == typedOther.location && color == typedOther.color;
  }

  @override
  int get hashCode => hashValues(location, color);
}
