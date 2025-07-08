import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/church.dart';
import '../models/member.dart';
import '../models/department.dart';

class ApiService {
  static const String baseUrl = 'http://localhost:8080/api';
  
  // Church APIs
  static Future<List<Church>> getChurches() async {
    final response = await http.post(
      Uri.parse('$baseUrl/churches/list'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({}),
    );
    
    if (response.statusCode == 200) {
      List<dynamic> jsonList = jsonDecode(response.body);
      return jsonList.map((json) => Church.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load churches');
    }
  }
  
  static Future<Church> createChurch(Church church) async {
    final response = await http.post(
      Uri.parse('$baseUrl/churches/create'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(church.toJson()),
    );
    
    if (response.statusCode == 201) {
      return Church.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to create church');
    }
  }
  
  static Future<Church> updateChurch(int id, Church church) async {
    final response = await http.post(
      Uri.parse('$baseUrl/churches/update'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'churchId': id,
        'churchDto': church.toJson(),
      }),
    );
    
    if (response.statusCode == 200) {
      return Church.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to update church');
    }
  }
  
  static Future<bool> deleteChurch(int id) async {
    final response = await http.post(
      Uri.parse('$baseUrl/churches/delete'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'churchId': id}),
    );
    
    return response.statusCode == 204;
  }
  
  // Member APIs
  static Future<List<Member>> getMembersByChurch(int churchId) async {
    final response = await http.post(
      Uri.parse('$baseUrl/members/church/members'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'churchId': churchId}),
    );
    
    if (response.statusCode == 200) {
      List<dynamic> jsonList = jsonDecode(response.body);
      return jsonList.map((json) => Member.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load members');
    }
  }
  
  static Future<Member> createMember(Member member) async {
    final response = await http.post(
      Uri.parse('$baseUrl/members/create'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(member.toJson()),
    );
    
    if (response.statusCode == 201) {
      return Member.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to create member');
    }
  }
  
  static Future<Member> updateMember(int id, Member member) async {
    final response = await http.post(
      Uri.parse('$baseUrl/members/update'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'memberId': id,
        'memberDto': member.toJson(),
      }),
    );
    
    if (response.statusCode == 200) {
      return Member.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to update member');
    }
  }
  
  static Future<bool> deleteMember(int id) async {
    final response = await http.post(
      Uri.parse('$baseUrl/members/delete'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'memberId': id}),
    );
    
    return response.statusCode == 204;
  }
  
  // Department APIs
  static Future<List<Department>> getDepartmentsByChurch(int churchId) async {
    final response = await http.post(
      Uri.parse('$baseUrl/departments/church/departments'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'churchId': churchId}),
    );
    
    if (response.statusCode == 200) {
      List<dynamic> jsonList = jsonDecode(response.body);
      return jsonList.map((json) => Department.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load departments');
    }
  }
  
  static Future<Department> createDepartment(Department department) async {
    final response = await http.post(
      Uri.parse('$baseUrl/departments/create'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(department.toJson()),
    );
    
    if (response.statusCode == 201) {
      return Department.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to create department');
    }
  }
  
  static Future<Department> updateDepartment(int id, Department department) async {
    final response = await http.post(
      Uri.parse('$baseUrl/departments/update'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'departmentId': id,
        'departmentDto': department.toJson(),
      }),
    );
    
    if (response.statusCode == 200) {
      return Department.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to update department');
    }
  }
  
  static Future<bool> deleteDepartment(int id) async {
    final response = await http.post(
      Uri.parse('$baseUrl/departments/delete'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'departmentId': id}),
    );
    
    return response.statusCode == 204;
  }
} 