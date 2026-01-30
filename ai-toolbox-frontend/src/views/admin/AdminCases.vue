<template>
  <div class="admin-page">
    <div class="page-header">
      <div class="search-bar">
        <input 
          v-model="searchKeyword" 
          type="text" 
          placeholder="搜索案例..." 
          class="search-input"
        >
      </div>
      <button @click="openModal()" class="add-btn">➕ 添加案例</button>
    </div>

    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>标题</th>
            <th>行业</th>
            <th>公司</th>
            <th>浏览量</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="caseItem in cases" :key="caseItem.id">
            <td>{{ caseItem.id }}</td>
            <td>{{ caseItem.title }}</td>
            <td>{{ caseItem.industry }}</td>
            <td>{{ caseItem.companyName }}</td>
            <td>{{ caseItem.viewCount }}</td>
            <td>
              <div class="action-btns">
                <button @click="openModal(caseItem)" class="action-btn edit">编辑</button>
                <button @click="deleteCase(caseItem.id)" class="action-btn delete">删除</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal modal-lg">
        <div class="modal-header">
          <h3>{{ isEditing ? '编辑案例' : '添加案例' }}</h3>
          <button @click="closeModal" class="close-btn">×</button>
        </div>
        <form @submit.prevent="saveCase" class="modal-form">
          <div class="form-row">
            <div class="form-group">
              <label>标题</label>
              <input v-model="form.title" type="text" required>
            </div>
            <div class="form-group">
              <label>行业</label>
              <select v-model="form.industry" required>
                <option value="金融">金融</option>
                <option value="医疗">医疗</option>
                <option value="零售">零售</option>
                <option value="制造业">制造业</option>
                <option value="教育">教育</option>
                <option value="交通">交通</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label>公司名称</label>
            <input v-model="form.companyName" type="text" required>
          </div>
          <div class="form-group">
            <label>描述</label>
            <textarea v-model="form.description" rows="3" placeholder="请输入案例描述..."></textarea>
          </div>
          <div class="form-group">
            <label>项目成果</label>
            <textarea v-model="form.result" rows="3" placeholder="请输入项目成果..."></textarea>
          </div>
          <div class="form-group">
            <label>图片URL</label>
            <input v-model="form.images" type="text" placeholder="https://...">
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
import { caseApi } from '@/api'

interface Case {
  id: number
  title: string
  description: string
  result: string
  industry: string
  companyName: string
  images: string
  viewCount: number
}

const cases = ref<Case[]>([])
const searchKeyword = ref('')
const showModal = ref(false)
const isEditing = ref(false)

const form = ref({
  id: 0,
  title: '',
  description: '',
  result: '',
  industry: '金融',
  companyName: '',
  images: '',
  viewCount: 0
})

const fetchCases = async () => {
  try {
    const response = await caseApi.getAllCases()
    if (response.code === 200) {
      cases.value = response.data
    }
  } catch (error) {
    console.error('获取案例列表失败:', error)
  }
}

const openModal = (caseItem?: Case) => {
  if (caseItem) {
    isEditing.value = true
    form.value = { ...caseItem }
  } else {
    isEditing.value = false
    form.value = { id: 0, title: '', description: '', result: '', industry: '金融', companyName: '', images: '', viewCount: 0 }
  }
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  isEditing.value = false
}

const saveCase = async () => {
  try {
    if (isEditing.value) {
      await caseApi.updateCase(form.value.id, {
        title: form.value.title,
        description: form.value.description,
        result: form.value.result,
        industry: form.value.industry,
        companyName: form.value.companyName,
        images: form.value.images
      })
    } else {
      await caseApi.addCase({
        title: form.value.title,
        description: form.value.description,
        result: form.value.result,
        industry: form.value.industry,
        companyName: form.value.companyName,
        images: form.value.images
      })
    }
    await fetchCases()
    closeModal()
  } catch (error) {
    console.error('保存案例失败:', error)
  }
}

const deleteCase = async (id: number) => {
  if (confirm('确定要删除该案例吗？')) {
    try {
      await caseApi.deleteCase(id)
      await fetchCases()
    } catch (error) {
      console.error('删除案例失败:', error)
    }
  }
}

onMounted(() => {
  fetchCases()
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
  background: #fafafa;
}

.data-table tbody tr:hover {
  background: #fafafa;
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
  max-height: 70vh;
  overflow-y: auto;
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
</style>
