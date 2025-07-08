enum DepartmentStatus {
  active,
  inactive;

  static DepartmentStatus fromString(String value) {
    return DepartmentStatus.values.firstWhere(
      (status) => status.name == value.toLowerCase(),
      orElse: () => DepartmentStatus.active,
    );
  }

  String toJson() => name;
} 