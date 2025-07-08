// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'member.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Member _$MemberFromJson(Map<String, dynamic> json) => Member(
  id: (json['id'] as num?)?.toInt(),
  name: json['name'] as String,
  email: json['email'] as String?,
  phone: json['phone'] as String?,
  address: json['address'] as String?,
  birthDate: json['birthDate'] == null
      ? null
      : DateTime.parse(json['birthDate'] as String),
  gender: json['gender'] as String?,
  profileImageUrl: json['profileImageUrl'] as String?,
  churchId: (json['churchId'] as num).toInt(),
  departmentId: (json['departmentId'] as num?)?.toInt(),
  status:
      $enumDecodeNullable(_$MemberStatusEnumMap, json['status']) ??
      MemberStatus.active,
  role:
      $enumDecodeNullable(_$MemberRoleEnumMap, json['role']) ??
      MemberRole.member,
  createdAt: json['createdAt'] == null
      ? null
      : DateTime.parse(json['createdAt'] as String),
  updatedAt: json['updatedAt'] == null
      ? null
      : DateTime.parse(json['updatedAt'] as String),
  createdBy: json['createdBy'] as String?,
  updatedBy: json['updatedBy'] as String?,
);

Map<String, dynamic> _$MemberToJson(Member instance) => <String, dynamic>{
  if (instance.id case final value?) 'id': value,
  'name': instance.name,
  if (instance.email case final value?) 'email': value,
  if (instance.phone case final value?) 'phone': value,
  if (instance.address case final value?) 'address': value,
  if (instance.birthDate?.toIso8601String() case final value?)
    'birthDate': value,
  if (instance.gender case final value?) 'gender': value,
  if (instance.profileImageUrl case final value?) 'profileImageUrl': value,
  'churchId': instance.churchId,
  if (instance.departmentId case final value?) 'departmentId': value,
  'status': instance.status.toJson(),
  'role': instance.role.toJson(),
  if (instance.createdAt?.toIso8601String() case final value?)
    'createdAt': value,
  if (instance.updatedAt?.toIso8601String() case final value?)
    'updatedAt': value,
  if (instance.createdBy case final value?) 'createdBy': value,
  if (instance.updatedBy case final value?) 'updatedBy': value,
};

const _$MemberStatusEnumMap = {
  MemberStatus.active: 'active',
  MemberStatus.inactive: 'inactive',
  MemberStatus.pending: 'pending',
};

const _$MemberRoleEnumMap = {
  MemberRole.admin: 'admin',
  MemberRole.pastor: 'pastor',
  MemberRole.leader: 'leader',
  MemberRole.member: 'member',
};
