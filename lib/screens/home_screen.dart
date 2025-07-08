import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/church_provider.dart';
import '../providers/member_provider.dart';
import '../providers/department_provider.dart';
import '../models/church.dart';
import '../enums/church_status.dart';
import 'church_list_screen.dart';
import 'member_list_screen.dart';
import 'department_list_screen.dart';

// ... 기존 코드 ...
// (Icons.department → Icons.business로 교체) 

// ... 기존 코드 ...
// leading: const Icon(Icons.department, size: 40, color: Colors.orange),
leading: const Icon(Icons.business, size: 40, color: Colors.orange),
// ... 기존 코드 ... 