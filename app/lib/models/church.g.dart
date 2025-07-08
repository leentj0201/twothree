// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'church.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Church _$ChurchFromJson(Map<String, dynamic> json) => Church(
  id: (json['id'] as num?)?.toInt(),
  name: json['name'] as String,
  description: json['description'] as String?,
  address: json['address'] as String?,
  phone: json['phone'] as String?,
  email: json['email'] as String?,
  website: json['website'] as String?,
  logoUrl: json['logoUrl'] as String?,
  status:
      $enumDecodeNullable(_$ChurchStatusEnumMap, json['status']) ??
      ChurchStatus.active,
  createdAt: json['createdAt'] == null
      ? null
      : DateTime.parse(json['createdAt'] as String),
  updatedAt: json['updatedAt'] == null
      ? null
      : DateTime.parse(json['updatedAt'] as String),
  createdBy: json['createdBy'] as String?,
  updatedBy: json['updatedBy'] as String?,
);

Map<String, dynamic> _$ChurchToJson(Church instance) => <String, dynamic>{
  if (instance.id case final value?) 'id': value,
  'name': instance.name,
  if (instance.description case final value?) 'description': value,
  if (instance.address case final value?) 'address': value,
  if (instance.phone case final value?) 'phone': value,
  if (instance.email case final value?) 'email': value,
  if (instance.website case final value?) 'website': value,
  if (instance.logoUrl case final value?) 'logoUrl': value,
  'status': instance.status.toJson(),
  if (instance.createdAt?.toIso8601String() case final value?)
    'createdAt': value,
  if (instance.updatedAt?.toIso8601String() case final value?)
    'updatedAt': value,
  if (instance.createdBy case final value?) 'createdBy': value,
  if (instance.updatedBy case final value?) 'updatedBy': value,
};

const _$ChurchStatusEnumMap = {
  ChurchStatus.active: 'active',
  ChurchStatus.inactive: 'inactive',
  ChurchStatus.pending: 'pending',
};
