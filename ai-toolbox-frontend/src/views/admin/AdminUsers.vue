<template>
  <div class="admin-page">
    <div class="page-header">
      <div class="search-bar">
        <input 
          v-model="searchKeyword" 
          type="text" 
          placeholder="搜索用户..." 
          class="search-input"
          @input="handleSearch"
        >
      </div>
      <button @click="showModal = true" class="add-btn">➕ 添加用户</button>
    </div>

    <div v-if="error" class="error-message">
      {{ error }}
    </div>

    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>加载中...</p>
    </div>

    <div v-else class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>用户名</th>
            <th>邮箱</th>
            <th>角色</th>
            <th>注册时间</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.username }}</td>
            <td>{{ user.email }}</td>
            <td>{{ user.role === 'ADMIN' ? '管理员' : '普通用户' }}</td>
            <td>{{ formatDate(user.createTime) }}</td>
            <td>
              <span :class="['status-badge', user.status === 1 ? 'active' : 'inactive']">
                {{ user.status === 1 ? '正常' : '禁用' }}
              </span>
            </td>
            <td>
              <div class="action-btns">
                <button @click="editUser(user)" class="action-btn edit" :disabled="loading">编辑</button>
                <button @click="deleteUser(user.id)" class="action-btn delete" :disabled="loading">删除</button>
              </div>
            </td>
          </tr>
          <tr v-if="users.length === 0">
            <td colspan="7" class="empty-state">
              <p>暂无用户数据</p>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal">
        <div class="modal-header">
          <h3>{{ isEditing ? '编辑用户' : '添加用户' }}</h3>
          <button @click="closeModal" class="close-btn">×</button>
        </div>
        <form @submit.prevent="saveUser" class="modal-form">
          <div class="form-group">
            <label>用户名</label>
            <input v-model="form.username" type="text" required>
          </div>
          <div class="form-group">
            <label>邮箱</label>
            <input v-model="form.email" type="email" required>
          </div>
          <div class="form-group" v-if="!isEditing">
            <label>密码</label>
            <input v-model="form.password" type="password" required>
          </div>
          <div class="form-group">
            <label>角色</label>
            <select v-model="form.role">
              <option :value="'USER'">普通用户</option>
              <option :value="'ADMIN'">管理员</option>
            </select>
          </div>
          <div class="form-group">
            <label>状态</label>
            <select v-model="form.status">
              <option :value="1">正常</option>
              <option :value="0">禁用</option>
            </select>
          </div>
          <div class="modal-actions">
            <button type="button" @click="closeModal" class="cancel-btn">取消</button>
            <button type="submit" class="submit-btn">{{ isEditing ? '保存' : '添加' }}</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { authApi } from '../../api'

interface User {
  id: number
  username: string
  email: string
  password?: string
  role: string
  status: number
  createTime: string
}

const users = ref<User[]>([])
const searchKeyword = ref('')
const showModal = ref(false)
const isEditing = ref(false)
const loading = ref(false)
const error = ref('')

const form = ref({
  id: 0,
  username: '',
  email: '',
  password: '',
  role: 'USER',
  status: 1
})

const formatDate = (date: string) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN')
}

const handleSearch = () => {
  console.log('搜索:', searchKeyword.value)
  loadUsers()
}

const loadUsers = async () => {
  loading.value = true
  error.value = ''
  try {
    const response = await authApi.getUsers({
      current: 1,
      size: 10
    })
    if (response.code === 200 && response.data) {
      users.value = response.data
    } else {
      error.value = response.message || '获取用户列表失败'
    }
  } catch (e: any) {
    console.error('获取用户列表错误:', e)
    error.value = '网络错误，请稍后重试'
  } finally {
    loading.value = false
  }
}

const editUser = (user: User) => {
  isEditing.value = true
  form.value = { ...user, password: '' }
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  isEditing.value = false
  form.value = { id: 0, username: '', email: '', password: '', role: 'USER', status: 1 }
}

const saveUser = async () => {
  loading.value = true
  error.value = ''
  try {
    if (isEditing.value) {
      const response = await authApi.updateUser(form.value.id, {
        username: form.value.username,
        email: form.value.email,
        role: form.value.role,
        status: form.value.status,
        password: form.value.password
      })
      if (response.code === 200 && response.data) {
        const index = users.value.findIndex(u => u.id === form.value.id)
        if (index !== -1) {
          users.value[index] = response.data
        }
        closeModal()
      } else {
        error.value = response.message || '更新用户失败'
      }
    } else {
      const response = await authApi.register({
        username: form.value.username,
        password: form.value.password,
        email: form.value.email,
        role: form.value.role
      })
      if (response.code === 200) {
        await loadUsers()
        closeModal()
      } else {
        error.value = response.message || '添加用户失败'
      }
    }
  } catch (e: any) {
    console.error('保存用户错误:', e)
    error.value = '网络错误，请稍后重试'
  } finally {
    loading.value = false
  }
}

const deleteUser = async (id: number) => {
  if (confirm('确定要删除该用户吗？')) {
    loading.value = true
    error.value = ''
    try {
      const response = await authApi.deleteUser(id)
      if (response.code === 200) {
        users.value = users.value.filter(u => u.id !== id)
      } else {
        error.value = response.message || '删除用户失败'
      }
    } catch (e: any) {
      console.error('删除用户错误:', e)
      error.value = '网络错误，请稍后重试'
    } finally {
      loading.value = false
    }
  }
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.admin-page {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.search-bar {
  flex: 1;
  max-width: 400px;
}

.search-input {
  width: 100%;
  padding: 12px 20px;
  border: 2px solid #f0f0f0;
  border-radius: 10px;
  font-size: 0.95rem;
  transition: border-color 0.3s;
}

.search-input:focus {
  outline: none;
  border-color: #FF6B35;
}

.add-btn {
  padding: 12px 24px;
  background: linear-gradient(90deg, #FF6B35 0%, #F7931E 100%);
  color: #fff;
  border: none;
  border-radius: 10px;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.add-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 107, 53, 0.3);
}

.table-container {
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: 14px 16px;
  text-align: left;
  border-bottom: 1px solid #f0f0f0;
}

.data-table th {
  font-weight: 600;
  color: #666;
  font-size: 0.9rem;
  background: #fafafa;
}

.data-table td {
  color: #333;
}

.data-table tbody tr:hover {
  background: #fafafa;
}

.status-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
}

.status-badge.active {
  background: rgba(76, 175, 80, 0.1);
  color: #4caf50;
}

.status-badge.inactive {
  background: rgba(244, 67, 54, 0.1);
  color: #f44336;
}

.action-btns {
  display: flex;
  gap: 8px;
}

.action-btn {
  padding: 6px 14px;
  border: none;
  border-radius: 6px;
  font-size: 0.85rem;
  cursor: pointer;
  transition: all 0.3s;
}

.action-btn.edit {
  background: rgba(33, 150, 243, 0.1);
  color: #2196f3;
}

.action-btn.edit:hover {
  background: rgba(33, 150, 243, 0.2);
}

.action-btn.delete {
  background: rgba(244, 67, 54, 0.1);
  color: #f44336;
}

.action-btn.delete:hover {
  background: rgba(244, 67, 54, 0.2);
}

.action-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.error-message {
  background: rgba(244, 67, 54, 0.1);
  border: 1px solid rgba(244, 67, 54, 0.3);
  color: #f44336;
  padding: 12px 16px;
  border-radius: 8px;
  margin-bottom: 20px;
  font-size: 0.9rem;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 0;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid rgba(255, 107, 53, 0.2);
  border-top-color: var(--ember-orange);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  margin-bottom: 16px;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.empty-state {
  text-align: center;
  padding: 60px 0;
  color: #999;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background: #fff;
  border-radius: 16px;
  width: 100%;
  max-width: 480px;
  animation: slideIn 0.3s ease;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #f0f0f0;
}

.modal-header h3 {
  font-size: 1.2rem;
  font-weight: 600;
  color: #1a1a2e;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  color: #999;
  cursor: pointer;
}

.modal-form {
  padding: 24px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-weight: 500;
  color: #333;
  margin-bottom: 8px;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #f0f0f0;
  border-radius: 10px;
  font-size: 0.95rem;
  transition: border-color 0.3s;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #FF6B35;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
}

.cancel-btn {
  padding: 12px 24px;
  border: 2px solid #f0f0f0;
  border-radius: 10px;
  background: #fff;
  color: #666;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.cancel-btn:hover {
  border-color: #ddd;
}

.submit-btn {
  padding: 12px 24px;
  border: none;
  border-radius: 10px;
  background: linear-gradient(90deg, #FF6B35 0%, #F7931E 100%);
  color: #fff;
  font-weight: 500;
  cursor: pointer;
  transition: transform 0.3s;
}

.submit-btn:hover {
  transform: translateY(-2px);
}
</style>
