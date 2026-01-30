<template>
  <section class="knowledge-section">
    <div class="container">
      <!-- 页面头部 -->
      <h1 class="section-title">知识库</h1>
      <p class="section-subtitle">探索AI领域的专业知识和技术文档</p>

      <!-- 分类筛选 -->
      <div class="category-filter">
        <button 
          v-for="category in categories" 
          :key="category"
          :class="['category-btn', { active: activeCategory === category }]"
          @click="activeCategory = category"
        >
          {{ category }}
        </button>
      </div>

      <!-- 知识库列表 -->
      <div class="knowledge-grid">
        <div 
          v-for="item in knowledgeList" 
          :key="item.id"
          class="knowledge-card"
          @click="navigateToDetail(item.id)"
        >
          <div class="knowledge-content">
            <h3 class="knowledge-title">{{ item.title }}</h3>
            <p class="knowledge-excerpt">{{ item.summary }}</p>
            <div class="knowledge-tags">
              <span class="tag" v-for="tag in getTags(item.tags)" :key="tag">{{ tag }}</span>
            </div>
            <div class="knowledge-meta">
              <span class="knowledge-category">{{ item.category }}</span>
              <span class="knowledge-author">{{ item.author }}</span>
              <span class="knowledge-date">{{ item.createTime }}</span>
            </div>
            <div class="knowledge-actions">
              <div class="action-btns">
                <button class="icon-btn" @click.stop="handleLike(item.id)">
                  <span>♥</span> <span>{{ item.likeCount }}</span>
                </button>
                <button class="icon-btn">
                  <span>👁</span> <span>{{ item.viewCount }}</span>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination">
        <button class="page-btn" :disabled="currentPage === 1" @click="handlePageChange(currentPage - 1)">
          上一页
        </button>
        <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
        <button class="page-btn" :disabled="currentPage === totalPages" @click="handlePageChange(currentPage + 1)">
          下一页
        </button>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { knowledgeApi } from '../api'

const router = useRouter()

// 知识库数据
const knowledgeList = ref<any[]>([])
const loading = ref(false)

// 分类列表
const categories = ref(['全部', '入门指南', '技术解析', '应用指南', '实战指南'])
const activeCategory = ref('全部')

// 分页
const currentPage = ref(1)
const pageSize = ref(6)
const totalPages = ref(1)

// 加载知识库数据
const loadKnowledge = async () => {
  loading.value = true
  try {
    const response = await knowledgeApi.getKnowledgeList({
      current: currentPage.value,
      size: pageSize.value,
      category: activeCategory.value !== '全部' ? activeCategory.value : undefined
    })
    console.log('Knowledge API响应:', response);
    knowledgeList.value = response.data?.records || []
    totalPages.value = Math.ceil((response.data?.total || 0) / pageSize.value)
    console.log('知识库数据:', knowledgeList.value);
    console.log('知识库数据长度:', knowledgeList.value.length);
  } catch (error) {
    console.error('Failed to load knowledge:', error)
  } finally {
    loading.value = false
  }
}

// 处理页面变化
const handlePageChange = (page: number) => {
  currentPage.value = page
  loadKnowledge()
}

// 处理点赞
const handleLike = async (id: number) => {
  try {
    await knowledgeApi.incrementLikeCount(id)
    // 更新本地点赞数
    const item = knowledgeList.value.find(item => item.id === id)
    if (item) {
      item.likeCount++
    }
  } catch (error) {
    console.error('Failed to like knowledge:', error)
  }
}

// 导航到详情页
const navigateToDetail = (id: number) => {
  router.push(`/knowledge/${id}`)
}

// 获取标签列表
const getTags = (tags: string) => {
  if (!tags) return []
  return tags.split(',').map(tag => tag.trim())
}

// 监听分类变化
watch(activeCategory, () => {
  currentPage.value = 1
  loadKnowledge()
})

// 初始化加载数据
onMounted(() => {
  loadKnowledge()
})
</script>

<style scoped>
.knowledge-section {
  padding: 96px 48px;
}

.container {
  max-width: 1440px;
  margin: 0 auto;
}

.section-title {
  font-size: clamp(2rem, 5vw, 3rem);
  font-weight: 700;
  text-align: center;
  margin-bottom: 16px;
  color: var(--cream-white);
}

.section-subtitle {
  text-align: center;
  font-size: 1.1rem;
  color: rgba(250, 249, 246, 0.7);
  margin-bottom: 64px;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.category-filter {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-bottom: 48px;
  flex-wrap: wrap;
}

.category-btn {
  padding: 8px 16px;
  border: 1px solid rgba(255, 107, 53, 0.3);
  border-radius: 20px;
  background: rgba(255, 107, 53, 0.1);
  color: var(--ember-orange);
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
}

.category-btn:hover {
  border-color: var(--ember-orange);
  background: rgba(255, 107, 53, 0.2);
}

.category-btn.active {
  background: var(--ember-orange);
  color: var(--cream-white);
  border-color: var(--ember-orange);
}

.knowledge-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 32px;
  margin-bottom: 48px;
}

.knowledge-card {
  background: var(--warm-grey);
  border-radius: 16px;
  overflow: hidden;
  transition: all 0.3s;
  border: 1px solid transparent;
}

.knowledge-card:hover {
  transform: translateY(-8px);
  border-color: var(--ember-orange);
  box-shadow: 0 16px 48px rgba(255, 107, 53, 0.2);
}

.knowledge-content {
  padding: 24px;
}

.knowledge-title {
  font-size: 1.3rem;
  font-weight: 600;
  margin-bottom: 12px;
  color: var(--cream-white);
  line-height: 1.4;
}

.knowledge-excerpt {
  color: rgba(250, 249, 246, 0.7);
  margin-bottom: 16px;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.knowledge-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-bottom: 16px;
}

.tag {
  padding: 4px 12px;
  border-radius: 12px;
  background: rgba(212, 175, 55, 0.2);
  color: var(--gold);
  font-size: 0.85rem;
}

.knowledge-meta {
  display: flex;
  gap: 15px;
  margin-bottom: 16px;
  font-size: 12px;
  color: rgba(250, 249, 246, 0.7);
  flex-wrap: wrap;
}

.knowledge-category {
  background: rgba(255, 107, 53, 0.2);
  padding: 2px 8px;
  border-radius: 10px;
  color: var(--ember-orange);
}

.knowledge-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid rgba(250, 249, 246, 0.1);
}

.action-btns {
  display: flex;
  gap: 16px;
}

.icon-btn {
  background: none;
  border: none;
  color: rgba(250, 249, 246, 0.6);
  cursor: pointer;
  font-size: 1.1rem;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 6px;
}

.icon-btn:hover {
  color: var(--ember-orange);
  transform: scale(1.1);
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  margin-top: 30px;
}

.page-btn {
  padding: 10px 20px;
  border: 1px solid rgba(255, 107, 53, 0.3);
  border-radius: 25px;
  background: transparent;
  color: var(--ember-orange);
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
}

.page-btn:hover:not(:disabled) {
  border-color: var(--ember-orange);
  background: rgba(255, 107, 53, 0.2);
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  font-size: 14px;
  color: rgba(250, 249, 246, 0.7);
}

@media (max-width: 768px) {
  .knowledge-section {
    padding: 64px 24px;
  }

  .category-filter {
    gap: 8px;
  }

  .category-btn {
    padding: 6px 12px;
    font-size: 12px;
  }

  .knowledge-grid {
    grid-template-columns: 1fr;
    gap: 24px;
  }

  .knowledge-content {
    padding: 20px;
  }

  .knowledge-title {
    font-size: 1.2rem;
  }

  .knowledge-meta {
    gap: 10px;
  }
}
</style>
