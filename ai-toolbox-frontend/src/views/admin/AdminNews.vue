<template>
  <div class="admin-page">
    <div class="page-header">
      <div class="search-bar">
        <input 
          v-model="searchKeyword" 
          type="text" 
          placeholder="搜索资讯..." 
          class="search-input"
        >
      </div>
      <button @click="openModal()" class="add-btn">➕ 发布资讯</button>
    </div>

    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>标题</th>
            <th>分类</th>
            <th>来源</th>
            <th>浏览量</th>
            <th>发布时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="news in newsList" :key="news.id">
            <td>{{ news.id }}</td>
            <td>{{ news.title }}</td>
            <td>{{ news.category }}</td>
            <td>{{ news.source }}</td>
            <td>{{ news.viewCount }}</td>
            <td>{{ formatDate(news.publishTime) }}</td>
            <td>
              <div class="action-btns">
                <button @click="openModal(news)" class="action-btn edit">编辑</button>
                <button @click="deleteNews(news.id)" class="action-btn delete">删除</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal modal-lg">
        <div class="modal-header">
          <h3>{{ isEditing ? '编辑资讯' : '发布资讯' }}</h3>
          <button @click="closeModal" class="close-btn">×</button>
        </div>
        <form @submit.prevent="saveNews" class="modal-form">
          <div class="form-row">
            <div class="form-group">
              <label>标题</label>
              <input v-model="form.title" type="text" required>
            </div>
            <div class="form-group">
              <label>分类</label>
              <select v-model="form.category" required>
                <option value="技术">技术</option>
                <option value="创新">创新</option>
                <option value="应用">应用</option>
                <option value="政策">政策</option>
              </select>
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>来源</label>
              <input v-model="form.source" type="text" placeholder="新闻来源">
            </div>
            <div class="form-group">
              <label>作者</label>
              <input v-model="form.author" type="text" placeholder="作者">
            </div>
          </div>
          <div class="form-group">
            <label>摘要</label>
            <textarea v-model="form.summary" rows="3" placeholder="请输入资讯摘要..."></textarea>
          </div>
          <div class="form-group">
            <label>内容</label>
            <textarea v-model="form.content" rows="6" placeholder="请输入资讯详细内容..."></textarea>
          </div>
          <div class="form-group">
            <label>标签（逗号分隔）</label>
            <input v-model="form.tagsStr" type="text" placeholder="AI,机器学习,技术">
          </div>
          <div class="modal-actions">
            <button type="button" @click="closeModal" class="cancel-btn">取消</button>
            <button type="submit" class="submit-btn">{{ isEditing ? '保存' : '发布' }}</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { newsApi } from '@/api'

interface News {
  id: number
  title: string
  summary: string
  content: string
  category: string
  source: string
  author: string
  tags: string
  viewCount: number
  publishTime: string
}

const newsList = ref<News[]>([])
const searchKeyword = ref('')
const showModal = ref(false)
const isEditing = ref(false)

const form = ref({
  id: 0,
  title: '',
  summary: '',
  content: '',
  category: '技术',
  source: '',
  author: '',
  tagsStr: '',
  viewCount: 0,
  publishTime: ''
})

const formatDate = (date: string) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN')
}

const fetchNewsList = async () => {
  try {
    const response = await newsApi.getAllNews()
    newsList.value = response
  } catch (error) {
    console.error('获取资讯列表失败:', error)
  }
}

const openModal = (news?: News) => {
  if (news) {
    isEditing.value = true
    form.value = { 
      ...news, 
      tagsStr: Array.isArray(news.tags) ? news.tags.join(', ') : news.tags 
    }
  } else {
    isEditing.value = false
    form.value = { id: 0, title: '', summary: '', content: '', category: '技术', source: '', author: '', tagsStr: '', viewCount: 0, publishTime: '' }
  }
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  isEditing.value = false
}

const saveNews = async () => {
  try {
    const newsData = {
      title: form.value.title,
      summary: form.value.summary,
      content: form.value.content,
      category: form.value.category,
      source: form.value.source,
      author: form.value.author,
      tags: form.value.tagsStr
    }

    if (isEditing.value) {
      await newsApi.updateNews(form.value.id, newsData)
    } else {
      await newsApi.addNews(newsData)
    }
    await fetchNewsList()
    closeModal()
  } catch (error) {
    console.error('保存资讯失败:', error)
  }
}

const deleteNews = async (id: number) => {
  if (confirm('确定要删除该资讯吗？')) {
    try {
      await newsApi.deleteNews(id)
      await fetchNewsList()
    } catch (error) {
      console.error('删除资讯失败:', error)
    }
  }
}

onMounted(() => {
  fetchNewsList()
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
