import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/applications',
    name: 'Applications',
    component: () => import('../views/Application.vue')
  },
  {
    path: '/applications/:id',
    name: 'ApplicationDetail',
    component: () => import('../views/ApplicationDetail.vue')
  },
  {
    path: '/knowledge',
    name: 'Knowledge',
    component: () => import('../views/Knowledge.vue')
  },
  {
    path: '/knowledge/:id',
    name: 'KnowledgeDetail',
    component: () => import('../views/KnowledgeDetail.vue')
  },
  {
    path: '/cases',
    name: 'Cases',
    component: () => import('../views/Cases.vue')
  },
  {
    path: '/cases/:id',
    name: 'CaseDetail',
    component: () => import('../views/CaseDetail.vue')
  },
  {
    path: '/news',
    name: 'News',
    component: () => import('../views/News.vue')
  },
  {
    path: '/news/:id',
    name: 'NewsDetail',
    component: () => import('../views/NewsDetail.vue')
  },
  {
    path: '/chat',
    name: 'Chat',
    component: () => import('../views/Chat.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/admin',
    component: () => import('../views/admin/AdminLayout.vue'),
    children: [
      {
        path: '',
        name: 'dashboard',
        component: () => import('../views/admin/AdminDashboard.vue')
      },
      {
        path: 'users',
        name: 'users',
        component: () => import('../views/admin/AdminUsers.vue')
      },
      {
        path: 'applications',
        name: 'applications',
        component: () => import('../views/admin/AdminApplications.vue')
      },
      {
        path: 'knowledge',
        name: 'knowledge',
        component: () => import('../views/admin/AdminKnowledge.vue')
      },
      {
        path: 'cases',
        name: 'cases',
        component: () => import('../views/admin/AdminCases.vue')
      },
      {
        path: 'news',
        name: 'news',
        component: () => import('../views/admin/AdminNews.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router