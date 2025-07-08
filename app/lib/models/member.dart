import 'package:json_annotation/json_annotation.dart';
import '../enums/member_status.dart';
import '../enums/member_role.dart';

part 'member.g.dart';

@JsonSerializable()
class Member {
  final int? id;
  final String name;
  final String? email;
  final String? phone;
  final String? address;
  final DateTime? birthDate;
  final String? gender;
  final String? profileImageUrl;
  final int churchId;
  final int? departmentId;
  final MemberStatus status;
  final MemberRole role;
  final DateTime? createdAt;
  final DateTime? updatedAt;
  final String? createdBy;
  final String? updatedBy;

  Member({
    this.id,
    required this.name,
    this.email,
    this.phone,
    this.address,
    this.birthDate,
    this.gender,
    this.profileImageUrl,
    required this.churchId,
    this.departmentId,
    this.status = MemberStatus.active,
    this.role = MemberRole.member,
    this.createdAt,
    this.updatedAt,
    this.createdBy,
    this.updatedBy,
  });

  factory Member.fromJson(Map<String, dynamic> json) => _$MemberFromJson(json);
  Map<String, dynamic> toJson() => _$MemberToJson(this);
} 