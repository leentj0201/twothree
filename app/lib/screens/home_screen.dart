import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/church_provider.dart';
import '../providers/member_provider.dart';
import '../providers/department_provider.dart';
import '../models/church.dart';
import 'church_list_screen.dart';
import 'member_list_screen.dart';
import 'department_list_screen.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  @override
  void initState() {
    super.initState();
    // 앱 시작 시 교회 목록 로드
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<ChurchProvider>().loadChurches();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('교회 관리 시스템'),
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

          return Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                const Text(
                  '교회 관리 시스템에 오신 것을 환영합니다!',
                  style: TextStyle(
                    fontSize: 24,
                    fontWeight: FontWeight.bold,
                  ),
                  textAlign: TextAlign.center,
                ),
                const SizedBox(height: 32),
                
                // 교회 목록 카드
                Card(
                  child: ListTile(
                    leading: const Icon(Icons.church, size: 40, color: Colors.blue),
                    title: const Text('교회 관리'),
                    subtitle: Text('총 ${churchProvider.churches.length}개의 교회'),
                    trailing: const Icon(Icons.arrow_forward_ios),
                    onTap: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => const ChurchListScreen(),
                        ),
                      );
                    },
                  ),
                ),
                
                const SizedBox(height: 16),
                
                // 교회가 선택된 경우에만 멤버와 부서 관리 표시
                if (churchProvider.churches.isNotEmpty) ...[
                  Card(
                    child: ListTile(
                      leading: const Icon(Icons.people, size: 40, color: Colors.green),
                      title: const Text('교인 관리'),
                      subtitle: const Text('교인 정보 관리'),
                      trailing: const Icon(Icons.arrow_forward_ios),
                      onTap: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => const MemberListScreen(),
                          ),
                        );
                      },
                    ),
                  ),
                  
                  const SizedBox(height: 16),
                  
                  Card(
                    child: ListTile(
                      leading: const Icon(Icons.business, size: 40, color: Colors.orange),
                      title: const Text('부서 관리'),
                      subtitle: const Text('부서 정보 관리'),
                      trailing: const Icon(Icons.arrow_forward_ios),
                      onTap: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => const DepartmentListScreen(),
                          ),
                        );
                      },
                    ),
                  ),
                ],
                
                const Spacer(),
                
                // 새 교회 추가 버튼
                ElevatedButton.icon(
                  onPressed: () => _showAddChurchDialog(context),
                  icon: const Icon(Icons.add),
                  label: const Text('새 교회 추가'),
                  style: ElevatedButton.styleFrom(
                    padding: const EdgeInsets.all(16),
                    backgroundColor: Colors.blue,
                    foregroundColor: Colors.white,
                  ),
                ),
              ],
            ),
          );
        },
      ),
    );
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
} 