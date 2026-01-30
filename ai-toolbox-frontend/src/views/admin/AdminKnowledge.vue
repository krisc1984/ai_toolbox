<template>
  <div class="admin-page">
    <div class="page-header">
      <div class="search-bar">
        <input 
          v-model="searchKeyword" 
          type="text" 
          placeholder="搜索知识库..." 
          class="search-input"
          @keyup.enter="fetchKnowledgeList"
        >
      </div>
      <button @click="openModal()" class="add-btn">➕ 添加知识</button>
    </div>

    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>标题</th>
            <th>分类</th>
            <th>作者</th>
            <th>浏览量</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in knowledgeList" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ item.title }}</td>
            <td>{{ item.category }}</td>
            <td>{{ item.author }}</td>
            <td>{{ item.viewCount }}</td>
            <td>{{ formatDate(item.createTime) }}</td>
            <td>
              <div class="action-btns">
                <button @click="openModal(item)" class="action-btn edit">编辑</button>
                <button @click="deleteItem(item.id)" class="action-btn delete">删除</button>
              </div>
            </td>
          </tr>
          <tr v-if="knowledgeList.length === 0">
            <td colspan="7" style="text-align: center; padding: 40px;">暂无数据</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 分页组件 -->
    <div class="pagination" v-if="total > 0">
      <button 
        @click="changePage(1)" 
        :disabled="currentPage === 1"
        class="page-btn"
      >
        首页
      </button>
      <button 
        @click="changePage(currentPage - 1)" 
        :disabled="currentPage === 1"
        class="page-btn"
      >
        上一页
      </button>
      <span class="page-info">
        {{ currentPage }} / {{ Math.ceil(total / pageSize) }}
      </span>
      <button 
        @click="changePage(currentPage + 1)" 
        :disabled="currentPage >= Math.ceil(total / pageSize)"
        class="page-btn"
      >
        下一页
      </button>
      <button 
        @click="changePage(Math.ceil(total / pageSize))" 
        :disabled="currentPage >= Math.ceil(total / pageSize)"
        class="page-btn"
      >
        末页
      </button>
      <div class="page-size">
        <span>每页显示：</span>
        <select v-model="pageSize" @change="fetchKnowledgeList" class="page-size-select">
          <option value="10">10条</option>
          <option value="20">20条</option>
          <option value="50">50条</option>
        </select>
      </div>
    </div>

    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal modal-lg">
        <div class="modal-header">
          <h3>{{ isEditing ? '编辑知识' : '添加知识' }}</h3>
          <button @click="closeModal" class="close-btn">×</button>
        </div>
        <form @submit.prevent="saveItem" class="modal-form">
          <div class="form-row">
            <div class="form-group">
              <label>标题</label>
              <input v-model="form.title" type="text" required>
            </div>
            <div class="form-group">
              <label>分类</label>
              <select v-model="form.category" required>
                <option value="入门指南">入门指南</option>
                <option value="技术解析">技术解析</option>
                <option value="应用指南">应用指南</option>
                <option value="实战指南">实战指南</option>
              </select>
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>作者</label>
              <input v-model="form.author" type="text" required>
            </div>
            <div class="form-group">
              <label>标签（逗号分隔）</label>
              <input v-model="form.tagsStr" type="text" placeholder="LLM,深度学习,入门">
            </div>
          </div>
          <div class="form-group">
            <label>摘要</label>
            <textarea v-model="form.summary" rows="3" placeholder="请输入摘要..."></textarea>
          </div>
          <div class="form-group">
            <label>内容</label>
            <textarea v-model="form.content" rows="6" placeholder="请输入详细内容..."></textarea>
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
import { knowledgeApi } from '@/api'

interface Knowledge {
  id: number
  title: string
  summary: string
  content: string
  category: string
  author: string
  tags: string
  viewCount: number
  createTime: string
}

const knowledgeList = ref<Knowledge[]>([])
const searchKeyword = ref('')
const showModal = ref(false)
const isEditing = ref(false)

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const form = ref({
  id: 0,
  title: '',
  summary: '',
  content: '',
  category: '入门指南',
  author: '',
  tagsStr: '',
  viewCount: 0,
  createTime: ''
})

const formatDate = (date: string) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN')
}

const fetchKnowledgeList = async () => {
  try {
    const response = await knowledgeApi.getKnowledgeList({
      current: currentPage.value,
      size: pageSize.value,
      keyword: searchKeyword.value
    })
    if (response.code === 200) {
      knowledgeList.value = response.data.records
      total.value = response.data.total
    }
  } catch (error) {
    console.error('获取知识库列表失败:', error)
  }
}

const changePage = (page: number) => {
  if (page < 1) return
  currentPage.value = page
  fetchKnowledgeList()
}

const openModal = (item?: Knowledge) => {
  if (item) {
    isEditing.value = true
    form.value = { 
      ...item, 
      tagsStr: Array.isArray(item.tags) ? item.tags.join(', ') : item.tags 
    }
  } else {
    isEditing.value = false
    form.value = { id: 0, title: '', summary: '', content: '', category: '入门指南', author: '', tagsStr: '', viewCount: 0, createTime: '' }
  }
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  isEditing.value = false
}

const saveItem = async () => {
  try {
    if (isEditing.value) {
      await knowledgeApi.updateKnowledge(form.value.id, {
        title: form.value.title,
        summary: form.value.summary,
        content: form.value.content,
        category: form.value.category,
        author: form.value.author,
        tags: form.value.tagsStr
      })
    } else {
      await knowledgeApi.addKnowledge({
        title: form.value.title,
        summary: form.value.summary,
        content: form.value.content,
        category: form.value.category,
        author: form.value.author,
        tags: form.value.tagsStr
      })
      // 新增成功后返回第一页
      currentPage.value = 1
    }
    await fetchKnowledgeList()
    closeModal()
  } catch (error) {
    console.error('保存知识库失败:', error)
  }
}

const deleteItem = async (id: number) => {
  if (confirm('确定要删除该知识条目吗？')) {
    try {
      await knowledgeApi.deleteKnowledge(id)
      // 检查当前页是否还有数据
      if (knowledgeList.value.length === 1 && currentPage.value > 1) {
        currentPage.value--
      }
      await fetchKnowledgeList()
    } catch (error) {
      console.error('删除知识库失败:', error)
    }
  }
}

onMounted(() => {
  fetchKnowledgeList()
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

/* 分页样式 */
.pagination {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-top: 24px;
  gap: 12px;
}

.page-btn {
  padding: 8px 16px;
  border: 1px solid #f0f0f0;
  border-radius: 6px;
  background: #fff;
  color: #666;
  cursor: pointer;
  font-size: 0.9rem;
}

.page-btn:hover:not(:disabled) {
  border-color: #FF6B35;
  color: #FF6B35;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  font-size: 0.9rem;
  color: #666;
  margin: 0 12px;
}

.page-size {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.9rem;
  color: #666;
}

.page-size-select {
  padding: 6px 12px;
  border: 1px solid #f0f0f0;
  border-radius: 6px;
  background: #fff;
  font-size: 0.9rem;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .pagination {
    flex-wrap: wrap;
    justify-content: center;
  }
  
  .page-size {
    margin-top: 12px;
    width: 100%;
    justify-content: center;
  }
}
</style>
