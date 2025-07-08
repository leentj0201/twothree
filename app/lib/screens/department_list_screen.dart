import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/department_provider.dart';
import '../providers/church_provider.dart';
import '../models/department.dart';
import '../enums/department_category.dart';
import '../enums/department_status.dart';

class DepartmentListScreen extends StatefulWidget {
  const DepartmentListScreen({super.key});

  @override
  State<DepartmentListScreen> createState() => _DepartmentListScreenState();
}

class _DepartmentListScreenState extends State<DepartmentListScreen> {
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
        title: const Text('부서 목록'),
        backgroundColor: Colors.orange,
        foregroundColor: Colors.white,
      ),
      body: Consumer2<ChurchProvider, DepartmentProvider>(
        builder: (context, churchProvider, departmentProvider, child) {
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
                      departmentProvider.loadDepartmentsByChurch(churchId);
                    }
                  },
                ),
              ),

              // 부서 목록
              Expanded(
                child: _buildDepartmentList(departmentProvider),
              ),
            ],
          );
        },
      ),
      floatingActionButton: selectedChurchId != null
          ? FloatingActionButton(
              onPressed: () => _showAddDepartmentDialog(context),
              backgroundColor: Colors.orange,
              foregroundColor: Colors.white,
              child: const Icon(Icons.add),
            )
          : null,
    );
  }

  Widget _buildDepartmentList(DepartmentProvider departmentProvider) {
    if (departmentProvider.isLoading) {
      return const Center(child: CircularProgressIndicator());
    }

    if (departmentProvider.error != null) {
      return Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text('오류: ${departmentProvider.error}'),
            const SizedBox(height: 16),
            ElevatedButton(
              onPressed: () => departmentProvider.loadDepartmentsByChurch(selectedChurchId!),
              child: const Text('다시 시도'),
            ),
          ],
        ),
      );
    }

    if (departmentProvider.departments.isEmpty) {
      return const Center(
        child: Text(
          '등록된 부서가 없습니다.\n새 부서를 추가해주세요.',
          textAlign: TextAlign.center,
          style: TextStyle(fontSize: 18),
        ),
      );
    }

    return ListView.builder(
      padding: const EdgeInsets.all(16),
      itemCount: departmentProvider.departments.length,
      itemBuilder: (context, index) {
        final department = departmentProvider.departments[index];
        return Card(
          margin: const EdgeInsets.only(bottom: 16),
          child: ListTile(
            leading: CircleAvatar(
              backgroundColor: _getCategoryColor(department.category),
              child: Text(
                department.name.isNotEmpty ? department.name[0] : '?',
                style: const TextStyle(
                  color: Colors.white,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
            title: Text(
              department.name,
              style: const TextStyle(fontWeight: FontWeight.bold),
            ),
            subtitle: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                if (department.description != null) ...[
                  Text(department.description!),
                  const SizedBox(height: 4),
                ],
                if (department.color != null) ...[
                  Row(
                    children: [
                      const Icon(Icons.palette, size: 16),
                      const SizedBox(width: 4),
                      Text('색상: ${department.color!}'),
                    ],
                  ),
                  const SizedBox(height: 4),
                ],
                if (department.icon != null) ...[
                  Row(
                    children: [
                      const Icon(Icons.emoji_emotions, size: 16),
                      const SizedBox(width: 4),
                      Text('아이콘: ${department.icon!}'),
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
                        color: _getCategoryColor(department.category),
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: Text(
                        _getCategoryText(department.category),
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
                        color: _getStatusColor(department.status),
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: Text(
                        _getStatusText(department.status),
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
                    _showEditDepartmentDialog(context, department);
                    break;
                  case 'delete':
                    _showDeleteConfirmation(context, department);
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

  Color _getCategoryColor(DepartmentCategory category) {
    switch (category) {
      case DepartmentCategory.education:
        return Colors.blue;
      case DepartmentCategory.evangelism:
        return Colors.red;
      case DepartmentCategory.worship:
        return Colors.purple;
      case DepartmentCategory.service:
        return Colors.green;
      case DepartmentCategory.youth:
        return Colors.orange;
      case DepartmentCategory.children:
        return Colors.pink;
      case DepartmentCategory.prayer:
        return Colors.indigo;
      case DepartmentCategory.finance:
        return Colors.teal;
      case DepartmentCategory.administration:
        return Colors.grey;
      case DepartmentCategory.other:
        return Colors.brown;
    }
  }

  String _getCategoryText(DepartmentCategory category) {
    switch (category) {
      case DepartmentCategory.education:
        return '교육';
      case DepartmentCategory.evangelism:
        return '전도';
      case DepartmentCategory.worship:
        return '예배';
      case DepartmentCategory.service:
        return '봉사';
      case DepartmentCategory.youth:
        return '청년';
      case DepartmentCategory.children:
        return '어린이';
      case DepartmentCategory.prayer:
        return '기도';
      case DepartmentCategory.finance:
        return '재정';
      case DepartmentCategory.administration:
        return '행정';
      case DepartmentCategory.other:
        return '기타';
    }
  }

  Color _getStatusColor(DepartmentStatus status) {
    switch (status) {
      case DepartmentStatus.active:
        return Colors.green;
      case DepartmentStatus.inactive:
        return Colors.red;
    }
  }

  String _getStatusText(DepartmentStatus status) {
    switch (status) {
      case DepartmentStatus.active:
        return '활성';
      case DepartmentStatus.inactive:
        return '비활성';
    }
  }

  void _showAddDepartmentDialog(BuildContext context) {
    final nameController = TextEditingController();
    final descriptionController = TextEditingController();
    final colorController = TextEditingController();
    final iconController = TextEditingController();
    DepartmentCategory selectedCategory = DepartmentCategory.other;
    DepartmentStatus selectedStatus = DepartmentStatus.active;

    showDialog(
      context: context,
      builder: (context) => StatefulBuilder(
        builder: (context, setState) => AlertDialog(
          title: const Text('새 부서 추가'),
          content: SingleChildScrollView(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                TextField(
                  controller: nameController,
                  decoration: const InputDecoration(
                    labelText: '부서명 *',
                    border: OutlineInputBorder(),
                  ),
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: descriptionController,
                  decoration: const InputDecoration(
                    labelText: '설명',
                    border: OutlineInputBorder(),
                  ),
                  maxLines: 3,
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: colorController,
                  decoration: const InputDecoration(
                    labelText: '색상 (예: #FF0000)',
                    border: OutlineInputBorder(),
                  ),
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: iconController,
                  decoration: const InputDecoration(
                    labelText: '아이콘',
                    border: OutlineInputBorder(),
                  ),
                ),
                const SizedBox(height: 16),
                DropdownButtonFormField<DepartmentCategory>(
                  value: selectedCategory,
                  decoration: const InputDecoration(
                    labelText: '카테고리',
                    border: OutlineInputBorder(),
                  ),
                  items: DepartmentCategory.values.map((category) {
                    return DropdownMenuItem(
                      value: category,
                      child: Text(_getCategoryText(category)),
                    );
                  }).toList(),
                  onChanged: (category) {
                    setState(() {
                      selectedCategory = category!;
                    });
                  },
                ),
                const SizedBox(height: 16),
                DropdownButtonFormField<DepartmentStatus>(
                  value: selectedStatus,
                  decoration: const InputDecoration(
                    labelText: '상태',
                    border: OutlineInputBorder(),
                  ),
                  items: DepartmentStatus.values.map((status) {
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
                  final department = Department(
                    name: nameController.text,
                    description: descriptionController.text.isNotEmpty 
                        ? descriptionController.text 
                        : null,
                    color: colorController.text.isNotEmpty 
                        ? colorController.text 
                        : null,
                    icon: iconController.text.isNotEmpty 
                        ? iconController.text 
                        : null,
                    churchId: selectedChurchId!,
                    category: selectedCategory,
                    status: selectedStatus,
                  );
                  
                  context.read<DepartmentProvider>().createDepartment(department);
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

  void _showEditDepartmentDialog(BuildContext context, Department department) {
    final nameController = TextEditingController(text: department.name);
    final descriptionController = TextEditingController(text: department.description ?? '');
    final colorController = TextEditingController(text: department.color ?? '');
    final iconController = TextEditingController(text: department.icon ?? '');
    DepartmentCategory selectedCategory = department.category;
    DepartmentStatus selectedStatus = department.status;

    showDialog(
      context: context,
      builder: (context) => StatefulBuilder(
        builder: (context, setState) => AlertDialog(
          title: const Text('부서 정보 수정'),
          content: SingleChildScrollView(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                TextField(
                  controller: nameController,
                  decoration: const InputDecoration(
                    labelText: '부서명 *',
                    border: OutlineInputBorder(),
                  ),
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: descriptionController,
                  decoration: const InputDecoration(
                    labelText: '설명',
                    border: OutlineInputBorder(),
                  ),
                  maxLines: 3,
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: colorController,
                  decoration: const InputDecoration(
                    labelText: '색상 (예: #FF0000)',
                    border: OutlineInputBorder(),
                  ),
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: iconController,
                  decoration: const InputDecoration(
                    labelText: '아이콘',
                    border: OutlineInputBorder(),
                  ),
                ),
                const SizedBox(height: 16),
                DropdownButtonFormField<DepartmentCategory>(
                  value: selectedCategory,
                  decoration: const InputDecoration(
                    labelText: '카테고리',
                    border: OutlineInputBorder(),
                  ),
                  items: DepartmentCategory.values.map((category) {
                    return DropdownMenuItem(
                      value: category,
                      child: Text(_getCategoryText(category)),
                    );
                  }).toList(),
                  onChanged: (category) {
                    setState(() {
                      selectedCategory = category!;
                    });
                  },
                ),
                const SizedBox(height: 16),
                DropdownButtonFormField<DepartmentStatus>(
                  value: selectedStatus,
                  decoration: const InputDecoration(
                    labelText: '상태',
                    border: OutlineInputBorder(),
                  ),
                  items: DepartmentStatus.values.map((status) {
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
                  final updatedDepartment = Department(
                    id: department.id,
                    name: nameController.text,
                    description: descriptionController.text.isNotEmpty 
                        ? descriptionController.text 
                        : null,
                    color: colorController.text.isNotEmpty 
                        ? colorController.text 
                        : null,
                    icon: iconController.text.isNotEmpty 
                        ? iconController.text 
                        : null,
                    churchId: department.churchId,
                    category: selectedCategory,
                    status: selectedStatus,
                  );
                  
                  context.read<DepartmentProvider>().updateDepartment(department.id!, updatedDepartment);
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

  void _showDeleteConfirmation(BuildContext context, Department department) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('부서 삭제'),
        content: Text('${department.name} 부서를 삭제하시겠습니까?\n이 작업은 되돌릴 수 없습니다.'),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('취소'),
          ),
          ElevatedButton(
            onPressed: () {
              context.read<DepartmentProvider>().deleteDepartment(department.id!);
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