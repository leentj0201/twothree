enum ChurchStatus {
  active,
  inactive,
  pending;

  static ChurchStatus fromString(String value) {
    return ChurchStatus.values.firstWhere(
      (status) => status.name == value.toLowerCase(),
      orElse: () => ChurchStatus.active,
    );
  }

  String toJson() => name;
} 