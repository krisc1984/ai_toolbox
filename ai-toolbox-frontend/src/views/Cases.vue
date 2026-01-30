<template>
  <section class="cases-section">
    <div class="container">
      <!-- 页面头部 -->
      <h1 class="section-title">AI案例</h1>
      <p class="section-subtitle">探索AI技术在各个行业的实际应用案例</p>

      <!-- 行业筛选 -->
      <div class="industry-filter">
        <button 
          v-for="industry in industries" 
          :key="industry"
          :class="['industry-btn', { active: activeIndustry === industry }]"
          @click="activeIndustry = industry"
        >
          {{ industry }}
        </button>
      </div>

      <!-- 案例列表 -->
      <div class="cases-grid">
        <div 
          v-for="caseItem in cases" 
          :key="caseItem.id"
          class="case-card"
          @click="navigateToDetail(caseItem.id)"
        >
          <div class="case-image">
            <img :src="caseItem.images" :alt="caseItem.title">
          </div>
          <div class="case-content">
            <h3 class="case-title">{{ caseItem.title }}</h3>
            <p class="case-description">{{ caseItem.description }}</p>
            <div class="case-info">
              <span class="case-industry">{{ caseItem.industry }}</span>
              <span class="case-company">{{ caseItem.companyName }}</span>
            </div>
            <div class="case-result">
              <h4>项目成果</h4>
              <p>{{ caseItem.result }}</p>
            </div>
            <div class="case-stats">
              <span class="case-views">{{ caseItem.viewCount }} 次浏览</span>
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
import { caseApi } from '../api'

const router = useRouter()

// 案例数据
const cases = ref<any[]>([])
const loading = ref(false)

// 行业列表
const industries = ref(['全部', '金融', '医疗', '零售', '制造业', '教育', '交通'])
const activeIndustry = ref('全部')

// 分页
const currentPage = ref(1)
const pageSize = ref(6)
const totalPages = ref(1)

// 加载案例数据
const loadCases = async () => {
  loading.value = true
  try {
    const response = await caseApi.getCases({
      current: currentPage.value,
      size: pageSize.value,
      industry: activeIndustry.value !== '全部' ? activeIndustry.value : undefined
    })
    console.log('Cases API响应:', response);
    cases.value = response.data?.records || []
    totalPages.value = Math.ceil((response.data?.total || 0) / pageSize.value)
    console.log('案例数据:', cases.value);
    console.log('案例数据长度:', cases.value.length);
  } catch (error) {
    console.error('Failed to load cases:', error)
  } finally {
    loading.value = false
  }
}

// 处理页面变化
const handlePageChange = (page: number) => {
  currentPage.value = page
  loadCases()
}

// 导航到详情页
const navigateToDetail = (id: number) => {
  router.push(`/cases/${id}`)
}

// 监听行业变化
watch(activeIndustry, () => {
  currentPage.value = 1
  loadCases()
})

// 初始化加载数据
onMounted(() => {
  loadCases()
})
</script>

<style scoped>
.cases-section {
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

.industry-filter {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-bottom: 48px;
  flex-wrap: wrap;
}

.industry-btn {
  padding: 8px 16px;
  border: 1px solid rgba(255, 107, 53, 0.3);
  border-radius: 20px;
  background: rgba(255, 107, 53, 0.1);
  color: var(--ember-orange);
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
}

.industry-btn:hover {
  border-color: var(--ember-orange);
  background: rgba(255, 107, 53, 0.2);
}

.industry-btn.active {
  background: var(--ember-orange);
  color: var(--cream-white);
  border-color: var(--ember-orange);
}

.cases-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 32px;
  margin-bottom: 48px;
}

.case-card {
  border: 1px solid rgba(255, 107, 53, 0.1);
  border-radius: 16px;
  background: var(--warm-grey);
  cursor: pointer;
  transition: all 0.3s ease;
  overflow: hidden;
}

.case-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 16px 48px rgba(255, 107, 53, 0.2);
  border-color: var(--ember-orange);
}

.case-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.case-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.case-card:hover .case-image img {
  transform: scale(1.05);
}

.case-content {
  padding: 24px;
}

.case-title {
  font-size: 1.3rem;
  font-weight: 600;
  margin-bottom: 12px;
  color: var(--cream-white);
}

.case-description {
  font-size: 14px;
  color: rgba(250, 249, 246, 0.7);
  margin-bottom: 16px;
  line-height: 1.4;
}

.case-info {
  display: flex;
  gap: 15px;
  margin-bottom: 16px;
  font-size: 12px;
  flex-wrap: wrap;
}

.case-industry {
  background: rgba(255, 107, 53, 0.2);
  padding: 4px 10px;
  border-radius: 12px;
  color: var(--ember-orange);
  font-weight: 600;
}

.case-company {
  font-weight: bold;
  color: rgba(250, 249, 246, 0.8);
}

.case-result {
  margin-bottom: 16px;
  padding: 16px;
  background: rgba(255, 107, 53, 0.05);
  border-radius: 8px;
  border: 1px solid rgba(255, 107, 53, 0.1);
}

.case-result h4 {
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 8px;
  color: var(--ember-orange);
}

.case-result p {
  font-size: 13px;
  color: rgba(250, 249, 246, 0.7);
  line-height: 1.4;
}

.case-stats {
  font-size: 12px;
  color: rgba(250, 249, 246, 0.5);
  text-align: right;
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
  .cases-section {
    padding: 64px 24px;
  }

  .industry-filter {
    gap: 8px;
  }

  .industry-btn {
    padding: 6px 12px;
    font-size: 12px;
  }

  .cases-grid {
    grid-template-columns: 1fr;
    gap: 24px;
  }

  .case-image {
    height: 180px;
  }

  .case-content {
    padding: 20px;
  }

  .case-title {
    font-size: 1.2rem;
  }
}
</style>
