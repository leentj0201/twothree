import 'package:flutter/foundation.dart';
import '../models/department.dart';
import '../services/api_service.dart';

class DepartmentProvider with ChangeNotifier {
  List<Department> _departments = [];
  bool _isLoading = false;
  String? _error;
  int? _currentChurchId;

  List<Department> get departments => _departments;
  bool get isLoading => _isLoading;
  String? get error => _error;
  int? get currentChurchId => _currentChurchId;

  Future<void> loadDepartmentsByChurch(int churchId) async {
    _isLoading = true;
    _error = null;
    _currentChurchId = churchId;
    notifyListeners();

    try {
      _departments = await ApiService.getDepartmentsByChurch(churchId);
    } catch (e) {
      _error = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> createDepartment(Department department) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      final newDepartment = await ApiService.createDepartment(department);
      _departments.add(newDepartment);
    } catch (e) {
      _error = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> updateDepartment(int id, Department department) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      final updatedDepartment = await ApiService.updateDepartment(id, department);
      final index = _departments.indexWhere((d) => d.id == id);
      if (index != -1) {
        _departments[index] = updatedDepartment;
      }
    } catch (e) {
      _error = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> deleteDepartment(int id) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      final success = await ApiService.deleteDepartment(id);
      if (success) {
        _departments.removeWhere((d) => d.id == id);
      }
    } catch (e) {
      _error = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  void clearError() {
    _error = null;
    notifyListeners();
  }

  void clearDepartments() {
    _departments.clear();
    _currentChurchId = null;
    notifyListeners();
  }
} 