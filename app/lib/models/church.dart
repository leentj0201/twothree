import 'package:json_annotation/json_annotation.dart';
import '../enums/church_status.dart';

part 'church.g.dart';

@JsonSerializable()
class Church {
  final int? id;
  final String name;
  final String? description;
  final String? address;
  final String? phone;
  final String? email;
  final String? website;
  final String? logoUrl;
  final ChurchStatus status;
  final DateTime? createdAt;
  final DateTime? updatedAt;
  final String? createdBy;
  final String? updatedBy;

  Church({
    this.id,
    required this.name,
    this.description,
    this.address,
    this.phone,
    this.email,
    this.website,
    this.logoUrl,
    this.status = ChurchStatus.active,
    this.createdAt,
    this.updatedAt,
    this.createdBy,
    this.updatedBy,
  });

  factory Church.fromJson(Map<String, dynamic> json) => _$ChurchFromJson(json);
  Map<String, dynamic> toJson() => _$ChurchToJson(this);
} 