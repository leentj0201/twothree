// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'department.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Department _$DepartmentFromJson(Map<String, dynamic> json) => Department(
  id: (json['id'] as num?)?.toInt(),
  name: json['name'] as String,
  description: json['description'] as String?,
  color: json['color'] as String?,
  icon: json['icon'] as String?,
  churchId: (json['churchId'] as num).toInt(),
  category: $enumDecode(_$DepartmentCategoryEnumMap, json['category']),
  status:
      $enumDecodeNullable(_$DepartmentStatusEnumMap, json['status']) ??
      DepartmentStatus.active,
  createdAt: json['createdAt'] == null
      ? null
      : DateTime.parse(json['createdAt'] as String),
  updatedAt: json['updatedAt'] == null
      ? null
      : DateTime.parse(json['updatedAt'] as String),
  createdBy: json['createdBy'] as String?,
  updatedBy: json['updatedBy'] as String?,
);

Map<String, dynamic> _$DepartmentToJson(Department instance) =>
    <String, dynamic>{
      if (instance.id case final value?) 'id': value,
      'name': instance.name,
      if (instance.description case final value?) 'description': value,
      if (instance.color case final value?) 'color': value,
      if (instance.icon case final value?) 'icon': value,
      'churchId': instance.churchId,
      'category': instance.category.toJson(),
      'status': instance.status.toJson(),
      if (instance.createdAt?.toIso8601String() case final value?)
        'createdAt': value,
      if (instance.updatedAt?.toIso8601String() case final value?)
        'updatedAt': value,
      if (instance.createdBy case final value?) 'createdBy': value,
      if (instance.updatedBy case final value?) 'updatedBy': value,
    };

const _$DepartmentCategoryEnumMap = {
  DepartmentCategory.education: 'education',
  DepartmentCategory.evangelism: 'evangelism',
  DepartmentCategory.worship: 'worship',
  DepartmentCategory.service: 'service',
  DepartmentCategory.youth: 'youth',
  DepartmentCategory.children: 'children',
  DepartmentCategory.prayer: 'prayer',
  DepartmentCategory.finance: 'finance',
  DepartmentCategory.administration: 'administration',
  DepartmentCategory.other: 'other',
};

const _$DepartmentStatusEnumMap = {
  DepartmentStatus.active: 'active',
  DepartmentStatus.inactive: 'inactive',
};
