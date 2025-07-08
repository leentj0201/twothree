import 'package:flutter/foundation.dart';
import '../models/member.dart';
import '../services/api_service.dart';

class MemberProvider with ChangeNotifier {
  List<Member> _members = [];
  bool _isLoading = false;
  String? _error;
  int? _currentChurchId;

  List<Member> get members => _members;
  bool get isLoading => _isLoading;
  String? get error => _error;
  int? get currentChurchId => _currentChurchId;

  Future<void> loadMembersByChurch(int churchId) async {
    _isLoading = true;
    _error = null;
    _currentChurchId = churchId;
    notifyListeners();

    try {
      _members = await ApiService.getMembersByChurch(churchId);
    } catch (e) {
      _error = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> createMember(Member member) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      final newMember = await ApiService.createMember(member);
      _members.add(newMember);
    } catch (e) {
      _error = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> updateMember(int id, Member member) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      final updatedMember = await ApiService.updateMember(id, member);
      final index = _members.indexWhere((m) => m.id == id);
      if (index != -1) {
        _members[index] = updatedMember;
      }
    } catch (e) {
      _error = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> deleteMember(int id) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      final success = await ApiService.deleteMember(id);
      if (success) {
        _members.removeWhere((m) => m.id == id);
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

  void clearMembers() {
    _members.clear();
    _currentChurchId = null;
    notifyListeners();
  }
} 