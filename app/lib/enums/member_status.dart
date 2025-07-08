enum MemberStatus {
  active,
  inactive,
  pending;

  static MemberStatus fromString(String value) {
    return MemberStatus.values.firstWhere(
      (status) => status.name == value.toLowerCase(),
      orElse: () => MemberStatus.active,
    );
  }

  String toJson() => name;
} 