<template>
  <section class="news-section">
    <div class="container">
      <!-- 页面头部 -->
      <h1 class="section-title">AI资讯</h1>
      <p class="section-subtitle">了解AI领域的最新动态和技术发展</p>

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

      <!-- 热门资讯 -->
      <div v-if="hotNews.length > 0" class="hot-news-section">
        <h2 class="hot-news-title">热门资讯</h2>
        <div class="hot-news-card">
          <div class="hot-news-content">
            <h3 class="news-title">{{ hotNews[0].title }}</h3>
            <p class="news-summary">{{ hotNews[0].summary }}</p>
            <div class="news-meta">
              <span class="news-category">{{ hotNews[0].category }}</span>
              <span class="news-source">{{ hotNews[0].source }}</span>
              <span class="news-date">{{ hotNews[0].publishTime }}</span>
            </div>
            <div class="news-stats">
              <span class="news-views">{{ hotNews[0].viewCount }} 次浏览</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 资讯列表 -->
      <div class="news-grid">
        <div 
          v-for="news in newsList" 
          :key="news.id"
          class="news-item"
          @click="navigateToDetail(news.id)"
        >
          <div class="news-content">
            <h3 class="news-title">{{ news.title }}</h3>
            <p class="news-summary">{{ news.summary }}</p>
            <div class="news-meta">
              <span class="news-category">{{ news.category }}</span>
              <span class="news-source">{{ news.source }}</span>
              <span class="news-date">{{ news.publishTime }}</span>
            </div>
            <div class="news-stats">
              <span class="news-views">{{ news.viewCount }} 次浏览</span>
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
import { newsApi } from '../api'

const router = useRouter()

// 资讯数据
const newsList = ref<any[]>([])
const hotNews = ref<any[]>([])
const loading = ref(false)

// 分类列表
const categories = ref(['全部', '技术', '创新', '应用', '政策'])
const activeCategory = ref('全部')

// 分页
const currentPage = ref(1)
const pageSize = ref(6)
const totalPages = ref(1)

// 加载热门资讯
const loadHotNews = async () => {
  try {
    const response = await newsApi.getHotNews()
    console.log('Hot News API响应:', response);
    hotNews.value = response.data?.data || []
    console.log('热门资讯数据:', hotNews.value);
    console.log('热门资讯数据长度:', hotNews.value.length);
  } catch (error) {
    console.error('Failed to load hot news:', error)
  }
}

// 加载资讯列表
const loadNews = async () => {
  loading.value = true
  try {
    const response = await newsApi.getNewsList({
      current: currentPage.value,
      size: pageSize.value,
      category: activeCategory.value !== '全部' ? activeCategory.value : undefined
    })
    console.log('News API响应:', response);
    newsList.value = response.data?.records || []
    totalPages.value = Math.ceil((response.data?.total || 0) / pageSize.value)
    console.log('资讯数据:', newsList.value);
    console.log('资讯数据长度:', newsList.value.length);
  } catch (error) {
    console.error('Failed to load news:', error)
  } finally {
    loading.value = false
  }
}

// 处理页面变化
const handlePageChange = (page: number) => {
  currentPage.value = page
  loadNews()
}

// 导航到详情页
const navigateToDetail = (id: number) => {
  router.push(`/news/${id}`)
}

// 监听分类变化
watch(activeCategory, () => {
  currentPage.value = 1
  loadNews()
})

// 初始化加载数据
onMounted(() => {
  loadHotNews()
  loadNews()
})
</script>

<style scoped>
.news-section {
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

.hot-news-section {
  margin-bottom: 48px;
}

.hot-news-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 24px;
  color: var(--cream-white);
  border-left: 4px solid var(--ember-orange);
  padding-left: 16px;
}

.hot-news-card {
  background: var(--warm-grey);
  border-radius: 16px;
  overflow: hidden;
  transition: all 0.3s;
  border: 1px solid transparent;
}

.hot-news-card:hover {
  transform: translateY(-4px);
  border-color: var(--ember-orange);
  box-shadow: 0 12px 32px rgba(255, 107, 53, 0.2);
}

.hot-news-content {
  padding: 32px;
}

.news-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 24px;
  margin-bottom: 48px;
}

.news-item {
  background: var(--warm-grey);
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s;
  border: 1px solid transparent;
}

.news-item:hover {
  transform: translateY(-4px);
  border-color: var(--ember-orange);
  box-shadow: 0 8px 24px rgba(255, 107, 53, 0.2);
}

.news-content {
  padding: 24px;
}

.news-title {
  font-size: 1.3rem;
  font-weight: 600;
  margin-bottom: 12px;
  color: var(--cream-white);
  line-height: 1.4;
}

.news-summary {
  color: rgba(250, 249, 246, 0.7);
  margin-bottom: 16px;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-meta {
  display: flex;
  gap: 15px;
  margin-bottom: 12px;
  font-size: 12px;
  color: rgba(250, 249, 246, 0.7);
  flex-wrap: wrap;
}

.news-category {
  background: rgba(255, 107, 53, 0.2);
  padding: 2px 8px;
  border-radius: 10px;
  color: var(--ember-orange);
  font-weight: 600;
}

.news-source {
  font-weight: bold;
  color: rgba(250, 249, 246, 0.8);
}

.news-stats {
  font-size: 12px;
  color: rgba(250, 249, 246, 0.5);
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
  .news-section {
    padding: 64px 24px;
  }

  .category-filter {
    gap: 8px;
  }

  .category-btn {
    padding: 6px 12px;
    font-size: 12px;
  }

  .hot-news-content {
    padding: 24px;
  }

  .hot-news-title {
    font-size: 1.8rem;
  }

  .news-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .news-content {
    padding: 20px;
  }

  .news-title {
    font-size: 1.2rem;
  }

  .news-meta {
    gap: 10px;
  }
}
</style>
