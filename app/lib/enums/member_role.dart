enum MemberRole {
  admin,
  pastor,
  leader,
  member;

  static MemberRole fromString(String value) {
    return MemberRole.values.firstWhere(
      (role) => role.name == value.toLowerCase(),
      orElse: () => MemberRole.member,
    );
  }

  String toJson() => name;
} 