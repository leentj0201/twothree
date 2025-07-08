import 'package:flutter/foundation.dart';
import '../models/church.dart';
import '../services/api_service.dart';

class ChurchProvider with ChangeNotifier {
  List<Church> _churches = [];
  bool _isLoading = false;
  String? _error;

  List<Church> get churches => _churches;
  bool get isLoading => _isLoading;
  String? get error => _error;

  Future<void> loadChurches() async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      _churches = await ApiService.getChurches();
    } catch (e) {
      _error = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> createChurch(Church church) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      final newChurch = await ApiService.createChurch(church);
      _churches.add(newChurch);
    } catch (e) {
      _error = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> updateChurch(int id, Church church) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      final updatedChurch = await ApiService.updateChurch(id, church);
      final index = _churches.indexWhere((c) => c.id == id);
      if (index != -1) {
        _churches[index] = updatedChurch;
      }
    } catch (e) {
      _error = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> deleteChurch(int id) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      final success = await ApiService.deleteChurch(id);
      if (success) {
        _churches.removeWhere((c) => c.id == id);
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
} 