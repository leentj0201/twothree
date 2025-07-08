import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/church_provider.dart';
import '../models/church.dart';
import '../enums/church_status.dart';

class ChurchListScreen extends StatelessWidget {
  const ChurchListScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('교회 목록'),
        backgroundColor: Colors.blue,
        foregroundColor: Colors.white,
      ),
      body: Consumer<ChurchProvider>(
        builder: (context, churchProvider, child) {
          if (churchProvider.isLoading) {
            return const Center(child: CircularProgressIndicator());
          }

          if (churchProvider.error != null) {
            return Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text('오류: ${churchProvider.error}'),
                  const SizedBox(height: 16),
                  ElevatedButton(
                    onPressed: () => churchProvider.loadChurches(),
                    child: const Text('다시 시도'),
                  ),
                ],
              ),
            );
          }

          if (churchProvider.churches.isEmpty) {
            return const Center(
              child: Text(
                '등록된 교회가 없습니다.\n새 교회를 추가해주세요.',
                textAlign: TextAlign.center,
                style: TextStyle(fontSize: 18),
              ),
            );
          }

          return ListView.builder(
            padding: const EdgeInsets.all(16),
            itemCount: churchProvider.churches.length,
            itemBuilder: (context, index) {
              final church = churchProvider.churches[index];
              return Card(
                margin: const EdgeInsets.only(bottom: 16),
                child: ListTile(
                  leading: CircleAvatar(
                    backgroundColor: Colors.blue,
                    child: Text(
                      church.name.isNotEmpty ? church.name[0] : '?',
                      style: const TextStyle(
                        color: Colors.white,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                  title: Text(
                    church.name,
                    style: const TextStyle(fontWeight: FontWeight.bold),
                  ),
                  subtitle: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      if (church.description != null) ...[
                        Text(church.description!),
                        const SizedBox(height: 4),
                      ],
                      if (church.address != null) ...[
                        Row(
                          children: [
                            const Icon(Icons.location_on, size: 16),
                            const SizedBox(width: 4),
                            Expanded(child: Text(church.address!)),
                          ],
                        ),
                        const SizedBox(height: 4),
                      ],
                      if (church.phone != null) ...[
                        Row(
                          children: [
                            const Icon(Icons.phone, size: 16),
                            const SizedBox(width: 4),
                            Text(church.phone!),
                          ],
                        ),
                        const SizedBox(height: 4),
                      ],
                      if (church.email != null) ...[
                        Row(
                          children: [
                            const Icon(Icons.email, size: 16),
                            const SizedBox(width: 4),
                            Text(church.email!),
                          ],
                        ),
                      ],
                      const SizedBox(height: 8),
                      Container(
                        padding: const EdgeInsets.symmetric(
                          horizontal: 8,
                          vertical: 4,
                        ),
                        decoration: BoxDecoration(
                          color: _getStatusColor(church.status),
                          borderRadius: BorderRadius.circular(12),
                        ),
                        child: Text(
                          _getStatusText(church.status),
                          style: const TextStyle(
                            color: Colors.white,
                            fontSize: 12,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                    ],
                  ),
                  trailing: PopupMenuButton<String>(
                    onSelected: (value) {
                      switch (value) {
                        case 'edit':
                          _showEditChurchDialog(context, church);
                          break;
                        case 'delete':
                          _showDeleteConfirmation(context, church);
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
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => _showAddChurchDialog(context),
        backgroundColor: Colors.blue,
        foregroundColor: Colors.white,
        child: const Icon(Icons.add),
      ),
    );
  }

  Color _getStatusColor(ChurchStatus status) {
    switch (status) {
      case ChurchStatus.active:
        return Colors.green;
      case ChurchStatus.inactive:
        return Colors.red;
      case ChurchStatus.pending:
        return Colors.orange;
    }
  }

  String _getStatusText(ChurchStatus status) {
    switch (status) {
      case ChurchStatus.active:
        return '활성';
      case ChurchStatus.inactive:
        return '비활성';
      case ChurchStatus.pending:
        return '대기중';
    }
  }

  void _showAddChurchDialog(BuildContext context) {
    final nameController = TextEditingController();
    final descriptionController = TextEditingController();
    final addressController = TextEditingController();
    final phoneController = TextEditingController();
    final emailController = TextEditingController();

    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('새 교회 추가'),
        content: SingleChildScrollView(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(
                controller: nameController,
                decoration: const InputDecoration(
                  labelText: '교회명 *',
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
                controller: addressController,
                decoration: const InputDecoration(
                  labelText: '주소',
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
                controller: emailController,
                decoration: const InputDecoration(
                  labelText: '이메일',
                  border: OutlineInputBorder(),
                ),
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
                final church = Church(
                  name: nameController.text,
                  description: descriptionController.text.isNotEmpty 
                      ? descriptionController.text 
                      : null,
                  address: addressController.text.isNotEmpty 
                      ? addressController.text 
                      : null,
                  phone: phoneController.text.isNotEmpty 
                      ? phoneController.text 
                      : null,
                  email: emailController.text.isNotEmpty 
                      ? emailController.text 
                      : null,
                );
                
                context.read<ChurchProvider>().createChurch(church);
                Navigator.pop(context);
              }
            },
            child: const Text('추가'),
          ),
        ],
      ),
    );
  }

  void _showEditChurchDialog(BuildContext context, Church church) {
    final nameController = TextEditingController(text: church.name);
    final descriptionController = TextEditingController(text: church.description ?? '');
    final addressController = TextEditingController(text: church.address ?? '');
    final phoneController = TextEditingController(text: church.phone ?? '');
    final emailController = TextEditingController(text: church.email ?? '');

    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('교회 정보 수정'),
        content: SingleChildScrollView(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(
                controller: nameController,
                decoration: const InputDecoration(
                  labelText: '교회명 *',
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
                controller: addressController,
                decoration: const InputDecoration(
                  labelText: '주소',
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
                controller: emailController,
                decoration: const InputDecoration(
                  labelText: '이메일',
                  border: OutlineInputBorder(),
                ),
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
                final updatedChurch = Church(
                  id: church.id,
                  name: nameController.text,
                  description: descriptionController.text.isNotEmpty 
                      ? descriptionController.text 
                      : null,
                  address: addressController.text.isNotEmpty 
                      ? addressController.text 
                      : null,
                  phone: phoneController.text.isNotEmpty 
                      ? phoneController.text 
                      : null,
                  email: emailController.text.isNotEmpty 
                      ? emailController.text 
                      : null,
                  status: church.status,
                );
                
                context.read<ChurchProvider>().updateChurch(church.id!, updatedChurch);
                Navigator.pop(context);
              }
            },
            child: const Text('수정'),
          ),
        ],
      ),
    );
  }

  void _showDeleteConfirmation(BuildContext context, Church church) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('교회 삭제'),
        content: Text('${church.name} 교회를 삭제하시겠습니까?\n이 작업은 되돌릴 수 없습니다.'),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('취소'),
          ),
          ElevatedButton(
            onPressed: () {
              context.read<ChurchProvider>().deleteChurch(church.id!);
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