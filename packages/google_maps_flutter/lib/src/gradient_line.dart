part of google_maps_flutter;

@immutable
class GradientLineId {
  GradientLineId(this.value) : assert(value != null);

  /// value of the [GradientLineId].
  final String value;

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other.runtimeType != runtimeType) return false;
    final GradientLineId typedOther = other;
    return value == typedOther.value;
  }

  @override
  int get hashCode => value.hashCode;

  @override
  String toString() {
    return 'GradientLineId{value: $value}';
  }
}

@immutable
class GradientLine {
  const GradientLine(
      {@required this.gradientLineId, this.points, this.width = 10});

  final GradientLineId gradientLineId;

  final BuiltList<ColorPoint> points;

  final int width;

  dynamic _toJson() {
    final Map<String, dynamic> json = <String, dynamic>{};

    void addIfPresent(String fieldName, dynamic value) {
      if (value != null) {
        json[fieldName] = value;
      }
    }

    addIfPresent('id', gradientLineId.value);
    addIfPresent(
        'points',
        points
            .map<dynamic>((ColorPoint point) => point._toJson())
            .toList(growable: false));
    addIfPresent('width', width);

    return json;
  }

  @override
  String toString() {
    return _toJson().toString();
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other.runtimeType != runtimeType) return false;
    final GradientLine typedOther = other;

    return gradientLineId == typedOther.gradientLineId &&
        width == typedOther.width &&
        points == typedOther.points;
  }

  @override
  int get hashCode {
    return hashValues(
        gradientLineId.hashCode, hashList(points), width.hashCode);
  }
}

Map<GradientLineId, GradientLine> _keyByGradientLineId(
    Iterable<GradientLine> gradientLines) {
  if (gradientLines == null) {
    return <GradientLineId, GradientLine>{};
  }
  return Map<GradientLineId, GradientLine>.fromEntries(gradientLines.map(
      (GradientLine gradientLine) => MapEntry<GradientLineId, GradientLine>(
          gradientLine.gradientLineId, gradientLine)));
}

List<Map<String, dynamic>> _serializeGradientLineSet(
    Set<GradientLine> gradientLines) {
  if (gradientLines == null) {
    return null;
  }
  return gradientLines
      .map<Map<String, dynamic>>((GradientLine p) => p._toJson())
      .toList();
}
