<template>
  <section class="chat-section">
    <div class="container">
      <!-- 页面头部 -->
      <h1 class="section-title">AI问答</h1>
      <p class="section-subtitle">与智能AI助手进行对话，获取专业的回答和建议</p>

      <!-- 聊天界面 -->
      <div class="chat-interface">
        <!-- 聊天历史 -->
        <div class="chat-history">
          <div 
            v-for="(message, index) in messages" 
            :key="index"
            :class="['chat-message', message.role === 'user' ? 'user-message' : 'ai-message']"
          >
            <div class="message-avatar">
              {{ message.role === 'user' ? '用户' : 'AI' }}
            </div>
            <div class="message-content">
              <div class="message-text">{{ message.content }}</div>
              <div class="message-time">{{ message.timestamp }}</div>
            </div>
          </div>
          <div v-if="isLoading" class="loading-message">
            <div class="loading-avatar">AI</div>
            <div class="loading-content">
              <div class="loading-dots">
                <span></span>
                <span></span>
                <span></span>
              </div>
            </div>
          </div>
        </div>

        <!-- 输入区域 -->
        <div class="chat-input-area">
          <div class="input-container">
            <textarea 
              v-model="inputMessage"
              placeholder="输入你的问题..."
              class="chat-input"
              @keydown.enter.exact="sendMessage"
              @keydown.enter.shift="inputMessage += '\n'"
            ></textarea>
            <button 
              class="send-button"
              @click="sendMessage"
              :disabled="!inputMessage.trim() || isLoading"
            >
              发送
            </button>
          </div>
          <div class="input-tips">
            <p>提示: 按 Enter 发送消息，Shift + Enter 换行</p>
          </div>
        </div>
      </div>

      <!-- 常用问题 -->
      <div class="common-questions">
        <h3 class="common-questions-title">常用问题</h3>
        <div class="questions-grid">
          <button 
            v-for="(question, index) in commonQuestions" 
            :key="index"
            class="question-btn"
            @click="inputMessage = question; sendMessage"
          >
            {{ question }}
          </button>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'

// 聊天消息
interface Message {
  role: 'user' | 'ai'
  content: string
  timestamp: string
}

const messages = ref<Message[]>([
  {
    role: 'ai',
    content: '你好！我是AI助手，有什么可以帮助你的吗？',
    timestamp: new Date().toLocaleTimeString()
  }
])

const inputMessage = ref('')
const isLoading = ref(false)

// 常用问题
const commonQuestions = ref([
  '什么是人工智能？',
  'AI技术有哪些应用领域？',
  '如何学习机器学习？',
  'AI的发展趋势是什么？'
])

// 发送消息
const sendMessage = async () => {
  const content = inputMessage.value.trim()
  if (!content || isLoading.value) return

  // 添加用户消息
  messages.value.push({
    role: 'user',
    content,
    timestamp: new Date().toLocaleTimeString()
  })

  inputMessage.value = ''
  isLoading.value = true

  // 模拟AI回复
  setTimeout(() => {
    const aiResponses: Record<string, string> = {
      '什么是人工智能？': '人工智能（Artificial Intelligence，简称AI）是研究、开发用于模拟、延伸和扩展人的智能的理论、方法、技术及应用系统的一门新的技术科学。',
      'AI技术有哪些应用领域？': 'AI技术广泛应用于医疗、金融、教育、交通、制造、娱乐等多个领域，包括图像识别、自然语言处理、自动驾驶、智能推荐等。',
      '如何学习机器学习？': '学习机器学习需要掌握数学基础（线性代数、微积分、概率统计）、编程技能（Python）、机器学习算法原理，然后通过项目实践来巩固知识。',
      'AI的发展趋势是什么？': 'AI的发展趋势包括大模型的持续进化、多模态融合、边缘AI、联邦学习、AI伦理规范的完善等。'
    }

    let aiResponse = aiResponses[content] || '这是一个很好的问题！AI技术正在不断发展，我会持续学习以提供更准确的信息。'

    messages.value.push({
      role: 'ai',
      content: aiResponse,
      timestamp: new Date().toLocaleTimeString()
    })

    isLoading.value = false
  }, 1000)
}
</script>

<style scoped>
.chat-section {
  padding: 96px 48px;
}

.container {
  max-width: 1000px;
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

.chat-interface {
  border: 1px solid rgba(255, 107, 53, 0.1);
  border-radius: 16px;
  overflow: hidden;
  margin-bottom: 48px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.chat-history {
  height: 500px;
  overflow-y: auto;
  padding: 32px;
  background: var(--warm-grey);
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.chat-message {
  display: flex;
  gap: 16px;
  max-width: 80%;
  animation: fadeIn 0.3s ease;
}

.user-message {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.ai-message {
  align-self: flex-start;
}

.message-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: var(--ember-orange);
  color: var(--cream-white);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: bold;
  flex-shrink: 0;
}

.user-message .message-avatar {
  background: var(--warm-grey);
  border: 2px solid var(--ember-orange);
}

.message-content {
  flex: 1;
}

.message-text {
  padding: 16px 20px;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.1);
  line-height: 1.6;
  font-size: 14px;
  color: var(--cream-white);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.ai-message .message-text {
  border-bottom-left-radius: 8px;
  background: rgba(255, 107, 53, 0.1);
  border: 1px solid rgba(255, 107, 53, 0.2);
}

.user-message .message-text {
  border-bottom-right-radius: 8px;
  background: rgba(255, 107, 53, 0.2);
  color: var(--cream-white);
}

.message-time {
  font-size: 12px;
  color: rgba(250, 249, 246, 0.5);
  margin-top: 8px;
  text-align: right;
}

.loading-message {
  display: flex;
  gap: 16px;
  max-width: 80%;
  align-self: flex-start;
  animation: fadeIn 0.3s ease;
}

.loading-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: var(--ember-orange);
  color: var(--cream-white);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: bold;
  flex-shrink: 0;
}

.loading-content {
  flex: 1;
}

.loading-dots {
  display: flex;
  gap: 8px;
  padding: 16px 20px;
  border-radius: 24px;
  background: rgba(255, 107, 53, 0.1);
  border-bottom-left-radius: 8px;
  border: 1px solid rgba(255, 107, 53, 0.2);
}

.loading-dots span {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: var(--ember-orange);
  animation: loading 1.4s ease infinite;
}

.loading-dots span:nth-child(2) {
  animation-delay: 0.2s;
}

.loading-dots span:nth-child(3) {
  animation-delay: 0.4s;
}

.chat-input-area {
  border-top: 1px solid rgba(255, 107, 53, 0.1);
  padding: 32px;
  background: var(--warm-grey);
}

.input-container {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
}

.chat-input {
  flex: 1;
  padding: 16px;
  border: 1px solid rgba(255, 107, 53, 0.3);
  border-radius: 25px;
  resize: none;
  min-height: 100px;
  font-size: 14px;
  line-height: 1.6;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.05);
  color: var(--cream-white);
}

.chat-input:focus {
  outline: none;
  border-color: var(--ember-orange);
  box-shadow: 0 0 0 3px rgba(255, 107, 53, 0.1);
}

.chat-input::placeholder {
  color: rgba(250, 249, 246, 0.5);
}

.send-button {
  padding: 0 32px;
  border: none;
  border-radius: 25px;
  background: var(--ember-orange);
  color: var(--cream-white);
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  align-self: flex-end;
  height: 48px;
  box-shadow: 0 4px 12px rgba(255, 107, 53, 0.3);
}

.send-button:hover:not(:disabled) {
  background: var(--coral-red);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(255, 107, 53, 0.4);
}

.send-button:disabled {
  background: rgba(255, 107, 53, 0.3);
  cursor: not-allowed;
  box-shadow: none;
}

.input-tips {
  font-size: 12px;
  color: rgba(250, 249, 246, 0.5);
  text-align: right;
}

.common-questions {
  margin-top: 32px;
}

.common-questions-title {
  font-size: 1.3rem;
  font-weight: 600;
  margin-bottom: 24px;
  color: var(--cream-white);
  border-left: 4px solid var(--ember-orange);
  padding-left: 16px;
}

.questions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}

.question-btn {
  padding: 16px 24px;
  border: 1px solid rgba(255, 107, 53, 0.3);
  border-radius: 24px;
  background: rgba(255, 107, 53, 0.05);
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
  text-align: left;
  color: var(--cream-white);
}

.question-btn:hover {
  border-color: var(--ember-orange);
  background: rgba(255, 107, 53, 0.1);
  transform: translateY(-2px);
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes loading {
  0%, 60%, 100% {
    transform: translateY(0);
  }
  30% {
    transform: translateY(-10px);
  }
}

@media (max-width: 768px) {
  .chat-section {
    padding: 64px 24px;
  }

  .chat-history {
    height: 400px;
    padding: 24px;
    gap: 16px;
  }

  .chat-message {
    max-width: 90%;
    gap: 12px;
  }

  .message-avatar {
    width: 40px;
    height: 40px;
    font-size: 12px;
  }

  .message-text {
    padding: 12px 16px;
    font-size: 13px;
  }

  .input-container {
    flex-direction: column;
    gap: 12px;
  }

  .send-button {
    align-self: flex-end;
    height: 44px;
  }

  .questions-grid {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .question-btn {
    padding: 12px 16px;
    font-size: 13px;
  }
}
</style>
