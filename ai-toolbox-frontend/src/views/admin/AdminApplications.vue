<template>
  <div class="admin-page">
    <div class="page-header">
      <div class="search-bar">
        <input 
          v-model="searchKeyword" 
          type="text" 
          placeholder="搜索应用..." 
          class="search-input"
        >
      </div>
      <button @click="openModal()" class="add-btn" :disabled="loading">➕ 添加应用</button>
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
            <th>图标</th>
            <th>名称</th>
            <th>分类</th>
            <th>浏览量</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="app in applications" :key="app.id">
            <td>{{ app.id }}</td>
            <td>
              <img :src="app.icon || defaultIcon" :alt="app.name" class="app-icon">
            </td>
            <td>{{ app.name }}</td>
            <td>{{ app.category }}</td>
            <td>{{ app.viewCount }}</td>
            <td>
              <span :class="['status-badge', app.status === 1 ? 'active' : 'inactive']">
                {{ app.status === 1 ? '正常' : '下架' }}
              </span>
            </td>
            <td>
              <div class="action-btns">
                <button @click="openModal(app)" class="action-btn edit" :disabled="loading">编辑</button>
                <button @click="deleteApp(app.id)" class="action-btn delete" :disabled="loading">删除</button>
              </div>
            </td>
          </tr>
          <tr v-if="applications.length === 0">
            <td colspan="7" class="empty-state">
              <p>暂无应用数据</p>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal modal-lg">
        <div class="modal-header">
          <h3>{{ isEditing ? '编辑应用' : '添加应用' }}</h3>
          <button @click="closeModal" class="close-btn">×</button>
        </div>
        <form @submit.prevent="saveApp" class="modal-form">
          <div class="form-row">
            <div class="form-group">
              <label>应用名称</label>
              <input v-model="form.name" type="text" required>
            </div>
            <div class="form-group">
              <label>分类</label>
              <select v-model="form.category" required>
                <option value="图像生成">图像生成</option>
                <option value="文本处理">文本处理</option>
                <option value="数据分析">数据分析</option>
                <option value="语音合成">语音合成</option>
                <option value="视频编辑">视频编辑</option>
                <option value="代码助手">代码助手</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label>图标</label>
            <select v-model="form.icon" required>
              <option value="">请选择图标</option>
              <option v-for="icon in icons" :key="icon.value" :value="icon.value">
                {{ icon.label }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>应用URL</label>
            <input v-model="form.url" type="text" placeholder="https://...">
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>是否免费</label>
              <select v-model="form.isFree">
                <option :value="1">免费</option>
                <option :value="0">付费</option>
              </select>
            </div>
            <div class="form-group">
              <label>状态</label>
              <select v-model="form.status">
                <option :value="1">正常</option>
                <option :value="0">下架</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label>描述</label>
            <textarea v-model="form.description" rows="4" placeholder="请输入应用描述..."></textarea>
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
import { applicationApi } from '../../api'

// 导入图标
import Icon1 from '../../assets/images/1.png'
import Icon2 from '../../assets/images/2.png'
import Icon3 from '../../assets/images/3.png'
import Icon4 from '../../assets/images/4.png'
import Icon5 from '../../assets/images/5.png'
import Icon6 from '../../assets/images/6.png'

const icons = [
  { value: Icon1, label: '图标1' },
  { value: Icon2, label: '图标2' },
  { value: Icon3, label: '图标3' },
  { value: Icon4, label: '图标4' },
  { value: Icon5, label: '图标5' },
  { value: Icon6, label: '图标6' }
]

interface Application {
  id: number
  name: string
  description: string
  category: string
  icon: string
  url: string
  isFree: number
  status: number
  viewCount: number
}

const defaultIcon = 'data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><rect fill="#f0f0f0" width="100" height="100"/><text x="50" y="50" text-anchor="middle" dy=".3em" font-size="40">🚀</text></svg>'

const applications = ref<Application[]>([])
const searchKeyword = ref('')
const showModal = ref(false)
const isEditing = ref(false)
const loading = ref(false)
const error = ref('')

const form = ref({
  id: 0,
  name: '',
  description: '',
  category: '图像生成',
  icon: '',
  url: '',
  isFree: 1,
  status: 1,
  viewCount: 0
})

const loadApplications = async () => {
  loading.value = true
  error.value = ''
  try {
    const response = await applicationApi.getAllApplications()
    if (response.code === 200 && response.data) {
      applications.value = response.data
    } else {
      error.value = '获取应用列表失败'
    }
  } catch (e) {
    console.error('获取应用列表错误:', e)
    error.value = '网络错误，请稍后重试'
  } finally {
    loading.value = false
  }
}

const openModal = (app?: Application) => {
  if (app) {
    isEditing.value = true
    form.value = { ...app }
  } else {
    isEditing.value = false
    form.value = { id: 0, name: '', description: '', category: '图像生成', icon: '', url: '', isFree: 1, status: 1, viewCount: 0 }
  }
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  isEditing.value = false
}

const saveApp = async () => {
  loading.value = true
  error.value = ''
  try {
    if (isEditing.value) {
      const response = await applicationApi.updateApplication(form.value.id, {
        name: form.value.name,
        description: form.value.description,
        category: form.value.category,
        icon: form.value.icon,
        url: form.value.url,
        isFree: form.value.isFree,
        status: form.value.status
      })
      if (response.code === 200) {
        await loadApplications()
        closeModal()
      } else {
        error.value = '更新应用失败'
      }
    } else {
      const response = await applicationApi.addApplication({
        name: form.value.name,
        description: form.value.description,
        category: form.value.category,
        icon: form.value.icon,
        url: form.value.url,
        isFree: form.value.isFree,
        status: form.value.status
      })
      if (response.code === 200) {
        await loadApplications()
        closeModal()
      } else {
        error.value = '添加应用失败'
      }
    }
  } catch (e) {
    console.error('保存应用错误:', e)
    error.value = '网络错误，请稍后重试'
  } finally {
    loading.value = false
  }
}

const deleteApp = async (id: number) => {
  if (confirm('确定要删除该应用吗？')) {
    loading.value = true
    error.value = ''
    try {
      const response = await applicationApi.deleteApplication(id)
      if (response.code === 200) {
        await loadApplications()
      } else {
        error.value = '删除应用失败'
      }
    } catch (e) {
      console.error('删除应用错误:', e)
      error.value = '网络错误，请稍后重试'
    } finally {
      loading.value = false
    }
  }
}

onMounted(() => {
  loadApplications()
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
  color: #000;
}

.data-table th {
  font-weight: 600;
  color: #666;
  background: #fafafa;
}

.data-table tbody tr:hover {
  background: #fafafa;
}

.app-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  object-fit: cover;
}

.status-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
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
  cursor: pointer;
  font-size: 0.85rem;
}

.action-btn.edit {
  background: rgba(33, 150, 243, 0.1);
  color: #2196f3;
}

.action-btn.delete {
  background: rgba(244, 67, 54, 0.1);
  color: #f44336;
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
}

.modal-lg {
  max-width: 600px;
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

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
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
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #f0f0f0;
  border-radius: 10px;
  font-size: 0.95rem;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
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
  cursor: pointer;
}

.submit-btn {
  padding: 12px 24px;
  border: none;
  border-radius: 10px;
  background: linear-gradient(90deg, #FF6B35 0%, #F7931E 100%);
  color: #fff;
  cursor: pointer;
}

.submit-btn:hover {
  transform: translateY(-2px);
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
</style>
