<template>
  <section class="hero">
    <video class="hero-video" autoplay loop muted playsinline preload="metadata">
      <source src="../assets/videos/hero_neural_network.mp4" type="video/mp4">
    </video>
    <div class="hero-overlay"></div>
    <div class="hero-content">
      <h1 class="hero-title">AI工具箱</h1>
      <p class="hero-subtitle">汇聚前沿AI工具，赋能智能化转型，开启无限可能</p>
      <div class="hero-cta">
        <router-link to="/applications" class="btn btn-primary">探索工具</router-link>
        <router-link to="/chat" class="btn btn-secondary">开始体验</router-link>
      </div>
    </div>
  </section>

  <section class="featured-apps">
    <div class="container">
      <h2 class="section-title">精选AI工具</h2>
      <p class="section-subtitle">覆盖图像、文本、数据等多个领域的高效AI工具</p>
      
      <div v-if="error" class="error-message">
        {{ error }}
      </div>
      
      <div v-if="loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>加载中...</p>
      </div>
      
      <div v-else class="apps-grid">
        <div class="app-card" v-for="app in featuredApps" :key="app.id" @click="openAppUrl(app.url)">
          <img :src="app.icon" :alt="app.name" class="app-icon">
          <h3 class="app-title">{{ app.name }}</h3>
          <p class="app-description">{{ app.description }}</p>
          <div class="app-meta">
            <span class="app-category">{{ app.category }}</span>
            <span class="app-views">{{ app.viewCount }} 次浏览</span>
          </div>
        </div>
        <div v-if="featuredApps.length === 0" class="empty-state">
          <p>暂无应用数据</p>
        </div>
      </div>
    </div>
  </section>

  <section class="knowledge-preview">
    <div class="container">
      <h2 class="section-title">知识库精选</h2>
      <p class="section-subtitle">AI领域前沿知识与实践指南</p>
      <div class="knowledge-grid">
        <div class="knowledge-card" v-for="knowledge in featuredKnowledge" :key="knowledge.id">
          <div class="knowledge-content">
            <h3 class="knowledge-title">{{ knowledge.title }}</h3>
            <p class="knowledge-excerpt">{{ knowledge.excerpt }}</p>
            <div class="knowledge-tags">
              <span class="tag" v-for="tag in knowledge.tags" :key="tag">{{ tag }}</span>
            </div>
            <div class="knowledge-actions">
              <div class="action-btns">
                <button class="icon-btn">
                  <span>♥</span> <span>{{ knowledge.likes }}</span>
                </button>
                <button class="icon-btn">
                  <span>★</span> <span>{{ knowledge.bookmarks }}</span>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { applicationApi } from '../api'
import { useRoute } from 'vue-router'

interface App {
  id: number
  name: string
  category: string
  icon: string
  description: string
  viewCount: number
  status: number
}

interface Knowledge {
  id: number
  title: string
  excerpt: string
  tags: string[]
  likes: number
  bookmarks: number
}

const featuredApps = ref<App[]>([])
const featuredKnowledge = ref<Knowledge[]>([
  {
    id: 1,
    title: '大语言模型入门指南',
    excerpt: '全面了解大语言模型的原理、应用场景和最佳实践...',
    tags: ['LLM', '深度学习', '入门'],
    likes: 342,
    bookmarks: 128
  },
  {
    id: 2,
    title: 'Transformer架构深度解析',
    excerpt: '详细解读Transformer架构的核心原理和创新点...',
    tags: ['Transformer', '架构', '高级'],
    likes: 567,
    bookmarks: 234
  }
])

const loading = ref(false)
const error = ref('')

const loadFeaturedApps = async () => {
  loading.value = true
  error.value = ''
  try {
    const response = await applicationApi.getApplications({ current: 1, size: 6 })
    if (response.code === 200 && response.data) {
      featuredApps.value = response.data.records || []
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

const route = useRoute()

// 监听路由变化，当导航到首页时重新加载数据
watch(() => route.path, (newPath) => {
  if (newPath === '/') {
    loadFeaturedApps()
  }
}, { immediate: true })

// 打开应用URL
const openAppUrl = (url: string) => {
  if (url) {
    window.open(url, '_blank', 'noopener,noreferrer')
  }
}

// 路由监听已经包含了组件挂载时的初始化，不需要单独的onMounted
</script>

<style scoped>
.hero {
  position: relative;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.hero-video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: 0;
}

.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(26,26,26,0.8) 0%, rgba(26,26,26,0.4) 50%, rgba(26,26,26,0.7) 100%);
  z-index: 1;
}

.hero-content {
  position: relative;
  z-index: 2;
  text-align: center;
  padding: 0 24px;
  max-width: 1200px;
  animation: fadeInUp 1s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(40px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.hero-title {
  font-size: clamp(3rem, 8vw, 5.5rem);
  font-weight: 800;
  letter-spacing: -0.02em;
  margin-bottom: 24px;
  background: linear-gradient(135deg, var(--ember-orange) 0%, var(--coral-red) 50%, var(--gold) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-subtitle {
  font-size: clamp(1.2rem, 3vw, 1.8rem);
  font-weight: 300;
  margin-bottom: 48px;
  color: var(--cream-white);
  opacity: 0.9;
}

.hero-cta {
  display: flex;
  gap: 24px;
  justify-content: center;
  flex-wrap: wrap;
}

.btn {
  padding: 16px 40px;
  border-radius: 50px;
  font-size: 1.1rem;
  font-weight: 600;
  border: none;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0.0, 0.2, 1);
  text-decoration: none;
  display: inline-block;
}

.btn-primary {
  background: linear-gradient(90deg, var(--ember-orange) 0%, var(--coral-red) 100%);
  color: var(--soft-white);
  box-shadow: 0 8px 24px rgba(255, 107, 53, 0.3);
}

.btn-primary:hover {
  transform: translateY(-4px) scale(1.05);
  box-shadow: 0 12px 32px rgba(255, 107, 53, 0.5);
}

.btn-secondary {
  background: transparent;
  color: var(--cream-white);
  border: 2px solid var(--ember-orange);
}

.btn-secondary:hover {
  background: var(--ember-orange);
  transform: translateY(-4px);
}

.featured-apps,
.knowledge-preview {
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

.apps-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 32px;
}

.app-card {
  background: linear-gradient(145deg, rgba(255,107,53,0.05) 0%, rgba(196,69,54,0.05) 100%);
  padding: 32px;
  border-radius: 16px;
  border: 1px solid rgba(255, 107, 53, 0.1);
  transition: all 0.3s cubic-bezier(0.4, 0.0, 0.2, 1);
  cursor: pointer;
}

.app-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 16px 48px rgba(255, 107, 53, 0.2);
  border-color: var(--ember-orange);
}

.app-icon {
  width: 64px;
  height: 64px;
  margin-bottom: 20px;
  border-radius: 12px;
  object-fit: cover;
}

.app-title {
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: 12px;
  color: var(--cream-white);
}

.app-description {
  color: rgba(250, 249, 246, 0.7);
  margin-bottom: 20px;
  line-height: 1.6;
}

.app-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.app-category {
  padding: 6px 16px;
  border-radius: 20px;
  background: rgba(255, 107, 53, 0.2);
  color: var(--ember-orange);
  font-size: 0.85rem;
  font-weight: 600;
}

.app-views {
  color: rgba(250, 249, 246, 0.5);
  font-size: 0.9rem;
}

.knowledge-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 32px;
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
}

.knowledge-excerpt {
  color: rgba(250, 249, 246, 0.7);
  margin-bottom: 16px;
  line-height: 1.6;
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

@media (max-width: 768px) {
  .featured-apps,
  .knowledge-preview {
    padding: 64px 24px;
  }

  .apps-grid,
  .knowledge-grid {
    grid-template-columns: 1fr;
  }

  .hero-title {
    font-size: 2.5rem;
  }
}

.error-message {
  background: rgba(244, 67, 54, 0.1);
  border: 1px solid rgba(244, 67, 54, 0.3);
  color: #f44336;
  padding: 16px 24px;
  border-radius: 12px;
  margin-bottom: 32px;
  text-align: center;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

.loading-spinner {
  width: 48px;
  height: 48px;
  border: 4px solid rgba(255, 107, 53, 0.2);
  border-top-color: var(--ember-orange);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 16px;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.empty-state {
  grid-column: 1 / -1;
  text-align: center;
  padding: 80px 0;
  color: rgba(250, 249, 246, 0.6);
  font-size: 1.1rem;
}

.empty-state p {
  margin: 0;
}
</style>