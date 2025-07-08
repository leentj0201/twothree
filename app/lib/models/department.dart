import 'package:json_annotation/json_annotation.dart';
import '../enums/department_status.dart';
import '../enums/department_category.dart';

part 'department.g.dart';

@JsonSerializable()
class Department {
  final int? id;
  final String name;
  final String? description;
  final String? color;
  final String? icon;
  final int churchId;
  final DepartmentCategory category;
  final DepartmentStatus status;
  final DateTime? createdAt;
  final DateTime? updatedAt;
  final String? createdBy;
  final String? updatedBy;

  Department({
    this.id,
    required this.name,
    this.description,
    this.color,
    this.icon,
    required this.churchId,
    required this.category,
    this.status = DepartmentStatus.active,
    this.createdAt,
    this.updatedAt,
    this.createdBy,
    this.updatedBy,
  });

  factory Department.fromJson(Map<String, dynamic> json) => _$DepartmentFromJson(json);
  Map<String, dynamic> toJson() => _$DepartmentToJson(this);
} 