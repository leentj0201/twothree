import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/member_provider.dart';
import '../providers/church_provider.dart';
import '../models/member.dart';
import '../enums/member_role.dart';
import '../enums/member_status.dart';

class MemberListScreen extends StatefulWidget {
  const MemberListScreen({super.key});

  @override
  State<MemberListScreen> createState() => _MemberListScreenState();
}

class _MemberListScreenState extends State<MemberListScreen> {
  int? selectedChurchId;

  @override
  void initState() {
    super.initState();
    // 교회 목록이 로드되지 않았다면 로드
    WidgetsBinding.instance.addPostFrameCallback((_) {
      final churchProvider = context.read<ChurchProvider>();
      if (churchProvider.churches.isEmpty) {
        churchProvider.loadChurches();
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('교인 목록'),
        backgroundColor: Colors.green,
        foregroundColor: Colors.white,
      ),
      body: Consumer2<ChurchProvider, MemberProvider>(
        builder: (context, churchProvider, memberProvider, child) {
          if (churchProvider.churches.isEmpty) {
            return const Center(
              child: Text(
                '먼저 교회를 등록해주세요.',
                style: TextStyle(fontSize: 18),
              ),
            );
          }

          return Column(
            children: [
              // 교회 선택 드롭다운
              Container(
                padding: const EdgeInsets.all(16),
                child: DropdownButtonFormField<int>(
                  value: selectedChurchId,
                  decoration: const InputDecoration(
                    labelText: '교회 선택',
                    border: OutlineInputBorder(),
                  ),
                  items: churchProvider.churches.map((church) {
                    return DropdownMenuItem(
                      value: church.id,
                      child: Text(church.name),
                    );
                  }).toList(),
                  onChanged: (churchId) {
                    setState(() {
                      selectedChurchId = churchId;
                    });
                    if (churchId != null) {
                      memberProvider.loadMembersByChurch(churchId);
                    }
                  },
                ),
              ),

              // 멤버 목록
              Expanded(
                child: _buildMemberList(memberProvider),
              ),
            ],
          );
        },
      ),
      floatingActionButton: selectedChurchId != null
          ? FloatingActionButton(
              onPressed: () => _showAddMemberDialog(context),
              backgroundColor: Colors.green,
              foregroundColor: Colors.white,
              child: const Icon(Icons.add),
            )
          : null,
    );
  }

  Widget _buildMemberList(MemberProvider memberProvider) {
    if (memberProvider.isLoading) {
      return const Center(child: CircularProgressIndicator());
    }

    if (memberProvider.error != null) {
      return Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text('오류: ${memberProvider.error}'),
            const SizedBox(height: 16),
            ElevatedButton(
              onPressed: () => memberProvider.loadMembersByChurch(selectedChurchId!),
              child: const Text('다시 시도'),
            ),
          ],
        ),
      );
    }

    if (memberProvider.members.isEmpty) {
      return const Center(
        child: Text(
          '등록된 교인이 없습니다.\n새 교인을 추가해주세요.',
          textAlign: TextAlign.center,
          style: TextStyle(fontSize: 18),
        ),
      );
    }

    return ListView.builder(
      padding: const EdgeInsets.all(16),
      itemCount: memberProvider.members.length,
      itemBuilder: (context, index) {
        final member = memberProvider.members[index];
        return Card(
          margin: const EdgeInsets.only(bottom: 16),
          child: ListTile(
            leading: CircleAvatar(
              backgroundColor: Colors.green,
              child: Text(
                member.name.isNotEmpty ? member.name[0] : '?',
                style: const TextStyle(
                  color: Colors.white,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
            title: Text(
              member.name,
              style: const TextStyle(fontWeight: FontWeight.bold),
            ),
            subtitle: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                if (member.email != null) ...[
                  Row(
                    children: [
                      const Icon(Icons.email, size: 16),
                      const SizedBox(width: 4),
                      Expanded(child: Text(member.email!)),
                    ],
                  ),
                  const SizedBox(height: 4),
                ],
                if (member.phone != null) ...[
                  Row(
                    children: [
                      const Icon(Icons.phone, size: 16),
                      const SizedBox(width: 4),
                      Text(member.phone!),
                    ],
                  ),
                  const SizedBox(height: 4),
                ],
                if (member.address != null) ...[
                  Row(
                    children: [
                      const Icon(Icons.location_on, size: 16),
                      const SizedBox(width: 4),
                      Expanded(child: Text(member.address!)),
                    ],
                  ),
                  const SizedBox(height: 4),
                ],
                if (member.birthDate != null) ...[
                  Row(
                    children: [
                      const Icon(Icons.cake, size: 16),
                      const SizedBox(width: 4),
                      Text('${member.birthDate!.year}년 ${member.birthDate!.month}월 ${member.birthDate!.day}일'),
                    ],
                  ),
                  const SizedBox(height: 4),
                ],
                if (member.gender != null) ...[
                  Row(
                    children: [
                      const Icon(Icons.person, size: 16),
                      const SizedBox(width: 4),
                      Text(member.gender!),
                    ],
                  ),
                  const SizedBox(height: 4),
                ],
                const SizedBox(height: 8),
                Row(
                  children: [
                    Container(
                      padding: const EdgeInsets.symmetric(
                        horizontal: 8,
                        vertical: 4,
                      ),
                      decoration: BoxDecoration(
                        color: _getRoleColor(member.role),
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: Text(
                        _getRoleText(member.role),
                        style: const TextStyle(
                          color: Colors.white,
                          fontSize: 12,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                    const SizedBox(width: 8),
                    Container(
                      padding: const EdgeInsets.symmetric(
                        horizontal: 8,
                        vertical: 4,
                      ),
                      decoration: BoxDecoration(
                        color: _getStatusColor(member.status),
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: Text(
                        _getStatusText(member.status),
                        style: const TextStyle(
                          color: Colors.white,
                          fontSize: 12,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                  ],
                ),
              ],
            ),
            trailing: PopupMenuButton<String>(
              onSelected: (value) {
                switch (value) {
                  case 'edit':
                    _showEditMemberDialog(context, member);
                    break;
                  case 'delete':
                    _showDeleteConfirmation(context, member);
                    break;
                }
              },
              itemBuilder: (context) => [
                const PopupMenuItem(
                  value: 'edit',
                  child: Row(
                    children: [
                      Icon(Icons.edit),
                      SizedBox(width: 8),
                      Text('수정'),
                    ],
                  ),
                ),
                const PopupMenuItem(
                  value: 'delete',
                  child: Row(
                    children: [
                      Icon(Icons.delete, color: Colors.red),
                      SizedBox(width: 8),
                      Text('삭제', style: TextStyle(color: Colors.red)),
                    ],
                  ),
                ),
              ],
            ),
          ),
        );
      },
    );
  }

  Color _getRoleColor(MemberRole role) {
    switch (role) {
      case MemberRole.admin:
        return Colors.red;
      case MemberRole.pastor:
        return Colors.purple;
      case MemberRole.leader:
        return Colors.blue;
      case MemberRole.member:
        return Colors.grey;
    }
  }

  String _getRoleText(MemberRole role) {
    switch (role) {
      case MemberRole.admin:
        return '관리자';
      case MemberRole.pastor:
        return '목사';
      case MemberRole.leader:
        return '리더';
      case MemberRole.member:
        return '교인';
    }
  }

  Color _getStatusColor(MemberStatus status) {
    switch (status) {
      case MemberStatus.active:
        return Colors.green;
      case MemberStatus.inactive:
        return Colors.red;
      case MemberStatus.pending:
        return Colors.orange;
    }
  }

  String _getStatusText(MemberStatus status) {
    switch (status) {
      case MemberStatus.active:
        return '활성';
      case MemberStatus.inactive:
        return '비활성';
      case MemberStatus.pending:
        return '대기중';
    }
  }

  void _showAddMemberDialog(BuildContext context) {
    final nameController = TextEditingController();
    final emailController = TextEditingController();
    final phoneController = TextEditingController();
    final addressController = TextEditingController();
    final genderController = TextEditingController();
    MemberRole selectedRole = MemberRole.member;
    MemberStatus selectedStatus = MemberStatus.active;

    showDialog(
      context: context,
      builder: (context) => StatefulBuilder(
        builder: (context, setState) => AlertDialog(
          title: const Text('새 교인 추가'),
          content: SingleChildScrollView(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                TextField(
                  controller: nameController,
                  decoration: const InputDecoration(
                    labelText: '이름 *',
                    border: OutlineInputBorder(),
                  ),
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: emailController,
                  decoration: const InputDecoration(
                    labelText: '이메일',
                    border: OutlineInputBorder(),
                  ),
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: phoneController,
                  decoration: const InputDecoration(
                    labelText: '전화번호',
                    border: OutlineInputBorder(),
                  ),
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: addressController,
                  decoration: const InputDecoration(
                    labelText: '주소',
                    border: OutlineInputBorder(),
                  ),
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: genderController,
                  decoration: const InputDecoration(
                    labelText: '성별',
                    border: OutlineInputBorder(),
                  ),
                ),
                const SizedBox(height: 16),
                DropdownButtonFormField<MemberRole>(
                  value: selectedRole,
                  decoration: const InputDecoration(
                    labelText: '역할',
                    border: OutlineInputBorder(),
                  ),
                  items: MemberRole.values.map((role) {
                    return DropdownMenuItem(
                      value: role,
                      child: Text(_getRoleText(role)),
                    );
                  }).toList(),
                  onChanged: (role) {
                    setState(() {
                      selectedRole = role!;
                    });
                  },
                ),
                const SizedBox(height: 16),
                DropdownButtonFormField<MemberStatus>(
                  value: selectedStatus,
                  decoration: const InputDecoration(
                    labelText: '상태',
                    border: OutlineInputBorder(),
                  ),
                  items: MemberStatus.values.map((status) {
                    return DropdownMenuItem(
                      value: status,
                      child: Text(_getStatusText(status)),
                    );
                  }).toList(),
                  onChanged: (status) {
                    setState(() {
                      selectedStatus = status!;
                    });
                  },
                ),
              ],
            ),
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('취소'),
            ),
            ElevatedButton(
              onPressed: () {
                if (nameController.text.isNotEmpty) {
                  final member = Member(
                    name: nameController.text,
                    email: emailController.text.isNotEmpty 
                        ? emailController.text 
                        : null,
                    phone: phoneController.text.isNotEmpty 
                        ? phoneController.text 
                        : null,
                    address: addressController.text.isNotEmpty 
                        ? addressController.text 
                        : null,
                    gender: genderController.text.isNotEmpty 
                        ? genderController.text 
                        : null,
                    churchId: selectedChurchId!,
                    role: selectedRole,
                    status: selectedStatus,
                  );
                  
                  context.read<MemberProvider>().createMember(member);
                  Navigator.pop(context);
                }
              },
              child: const Text('추가'),
            ),
          ],
        ),
      ),
    );
  }

  void _showEditMemberDialog(BuildContext context, Member member) {
    final nameController = TextEditingController(text: member.name);
    final emailController = TextEditingController(text: member.email ?? '');
    final phoneController = TextEditingController(text: member.phone ?? '');
    final addressController = TextEditingController(text: member.address ?? '');
    final genderController = TextEditingController(text: member.gender ?? '');
    MemberRole selectedRole = member.role;
    MemberStatus selectedStatus = member.status;

    showDialog(
      context: context,
      builder: (context) => StatefulBuilder(
        builder: (context, setState) => AlertDialog(
          title: const Text('교인 정보 수정'),
          content: SingleChildScrollView(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                TextField(
                  controller: nameController,
                  decoration: const InputDecoration(
                    labelText: '이름 *',
                    border: OutlineInputBorder(),
                  ),
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: emailController,
                  decoration: const InputDecoration(
                    labelText: '이메일',
                    border: OutlineInputBorder(),
                  ),
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: phoneController,
                  decoration: const InputDecoration(
                    labelText: '전화번호',
                    border: OutlineInputBorder(),
                  ),
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: addressController,
                  decoration: const InputDecoration(
                    labelText: '주소',
                    border: OutlineInputBorder(),
                  ),
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: genderController,
                  decoration: const InputDecoration(
                    labelText: '성별',
                    border: OutlineInputBorder(),
                  ),
                ),
                const SizedBox(height: 16),
                DropdownButtonFormField<MemberRole>(
                  value: selectedRole,
                  decoration: const InputDecoration(
                    labelText: '역할',
                    border: OutlineInputBorder(),
                  ),
                  items: MemberRole.values.map((role) {
                    return DropdownMenuItem(
                      value: role,
                      child: Text(_getRoleText(role)),
                    );
                  }).toList(),
                  onChanged: (role) {
                    setState(() {
                      selectedRole = role!;
                    });
                  },
                ),
                const SizedBox(height: 16),
                DropdownButtonFormField<MemberStatus>(
                  value: selectedStatus,
                  decoration: const InputDecoration(
                    labelText: '상태',
                    border: OutlineInputBorder(),
                  ),
                  items: MemberStatus.values.map((status) {
                    return DropdownMenuItem(
                      value: status,
                      child: Text(_getStatusText(status)),
                    );
                  }).toList(),
                  onChanged: (status) {
                    setState(() {
                      selectedStatus = status!;
                    });
                  },
                ),
              ],
            ),
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('취소'),
            ),
            ElevatedButton(
              onPressed: () {
                if (nameController.text.isNotEmpty) {
                  final updatedMember = Member(
                    id: member.id,
                    name: nameController.text,
                    email: emailController.text.isNotEmpty 
                        ? emailController.text 
                        : null,
                    phone: phoneController.text.isNotEmpty 
                        ? phoneController.text 
                        : null,
                    address: addressController.text.isNotEmpty 
                        ? addressController.text 
                        : null,
                    gender: genderController.text.isNotEmpty 
                        ? genderController.text 
                        : null,
                    churchId: member.churchId,
                    departmentId: member.departmentId,
                    role: selectedRole,
                    status: selectedStatus,
                  );
                  
                  context.read<MemberProvider>().updateMember(member.id!, updatedMember);
                  Navigator.pop(context);
                }
              },
              child: const Text('수정'),
            ),
          ],
        ),
      ),
    );
  }

  void _showDeleteConfirmation(BuildContext context, Member member) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('교인 삭제'),
        content: Text('${member.name} 교인을 삭제하시겠습니까?\n이 작업은 되돌릴 수 없습니다.'),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('취소'),
          ),
          ElevatedButton(
            onPressed: () {
              context.read<MemberProvider>().deleteMember(member.id!);
              Navigator.pop(context);
            },
            style: ElevatedButton.styleFrom(backgroundColor: Colors.red),
            child: const Text('삭제'),
          ),
        ],
      ),
    );
  }
} 