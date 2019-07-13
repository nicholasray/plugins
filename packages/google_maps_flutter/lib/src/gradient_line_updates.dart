part of google_maps_flutter;

class _GradientLineUpdates {
  /// Computes [_GradientLineUpdates] given previous and current [GradientLine]s.
  _GradientLineUpdates.from(
      Set<GradientLine> previous, Set<GradientLine> current) {
    if (previous == null) {
      previous = Set<GradientLine>.identity();
    }

    if (current == null) {
      current = Set<GradientLine>.identity();
    }

    print("previous ${previous.first.points.length.toString()}");
    print("current ${current.first.points.length.toString()}");

    final Map<GradientLineId, GradientLine> previousGradientLines =
        _keyByGradientLineId(previous);
    final Map<GradientLineId, GradientLine> currentGradientLines =
        _keyByGradientLineId(current);

    final Set<GradientLineId> prevGradientLineIds =
        previousGradientLines.keys.toSet();
    final Set<GradientLineId> currentGradientLineIds =
        currentGradientLines.keys.toSet();

    GradientLine idToCurrentGradientLine(GradientLineId id) {
      return currentGradientLines[id];
    }

    GradientLine idToPreviousGradientLine(GradientLineId id) {
      return previousGradientLines[id];
    }

    final Set<GradientLineId> _gradientLineIdsToRemove =
        prevGradientLineIds.difference(currentGradientLineIds);

    final Set<GradientLine> _gradientLinesToAdd = currentGradientLineIds
        .difference(prevGradientLineIds)
        .map(idToCurrentGradientLine)
        .toSet();

    final Set<GradientLine> _sameIds = currentGradientLineIds
        .intersection(prevGradientLineIds)
        .map(idToCurrentGradientLine)
        .toSet();

    final Set<GradientLine> _gradientLinesToChange = Set.of(<GradientLine>[]);

    _sameIds.forEach((GradientLine gradientLine) {
      if (gradientLine ==
          idToPreviousGradientLine(gradientLine.gradientLineId)) {
        return;
      }

      _gradientLinesToChange.add(gradientLine);
    });

    gradientLinesToAdd = _gradientLinesToAdd;
    gradientLineIdsToRemove = _gradientLineIdsToRemove;
    gradientLinesToChange = _gradientLinesToChange;
  }

  Set<GradientLine> gradientLinesToAdd;
  Set<GradientLineId> gradientLineIdsToRemove;
  Set<GradientLine> gradientLinesToChange;

  Map<String, dynamic> _toMap() {
    final Map<String, dynamic> updateMap = <String, dynamic>{};

    void addIfNonNull(String fieldName, dynamic value) {
      if (value != null) {
        updateMap[fieldName] = value;
      }
    }

    addIfNonNull(
        'gradientLinesToAdd', _serializeGradientLineSet(gradientLinesToAdd));
    addIfNonNull('gradientLinesToChange',
        _serializeGradientLineSet(gradientLinesToChange));
    addIfNonNull(
        'gradientLineIdsToRemove',
        gradientLineIdsToRemove
            .map<dynamic>((GradientLineId m) => m.value)
            .toList());

    return updateMap;
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other.runtimeType != runtimeType) return false;
    final _GradientLineUpdates typedOther = other;
    return setEquals(gradientLinesToAdd, typedOther.gradientLinesToAdd) &&
        setEquals(
            gradientLineIdsToRemove, typedOther.gradientLineIdsToRemove) &&
        setEquals(gradientLinesToChange, typedOther.gradientLinesToChange);
  }

  @override
  int get hashCode => hashValues(
      gradientLinesToAdd, gradientLineIdsToRemove, gradientLinesToChange);

  @override
  String toString() {
    return '_GradientLineUpdates{polylinesToAdd: $gradientLinesToAdd, '
        'polylineIdsToRemove: $gradientLineIdsToRemove, '
        'polylinesToChange: $gradientLinesToChange}';
  }
}
