enum DepartmentCategory {
  education,
  evangelism,
  worship,
  service,
  youth,
  children,
  prayer,
  finance,
  administration,
  other;

  static DepartmentCategory fromString(String value) {
    return DepartmentCategory.values.firstWhere(
      (category) => category.name == value.toLowerCase(),
      orElse: () => DepartmentCategory.other,
    );
  }

  String toJson() => name;
} 