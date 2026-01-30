import axios from 'axios';

// 创建axios实例
const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
});

// 请求拦截器
api.interceptors.request.use(
  config => {
    // 从本地存储获取token
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response.data;
  },
  error => {
    console.error('API Error:', error);
    return Promise.reject(error);
  }
);

// 应用相关API
export const applicationApi = {
  // 获取应用列表
  getApplications: (params: {
    current: number;
    size: number;
    category?: string;
    keyword?: string;
  }) => {
    return api.get('/applications/page', { params });
  },
  // 获取应用详情
  getApplicationDetail: (id: number) => {
    return api.get(`/applications/${id}`);
  },
  // 增加浏览量
  incrementViewCount: (id: number) => {
    return api.post(`/applications/${id}/view`);
  },
  // 添加应用
  addApplication: (application: {
    name: string;
    description: string;
    category: string;
    icon: string;
    url: string;
    isFree: number;
    status: number;
  }) => {
    return api.post('/applications', application);
  },
  // 更新应用
  updateApplication: (id: number, application: {
    name: string;
    description: string;
    category: string;
    icon: string;
    url: string;
    isFree: number;
    status: number;
  }) => {
    return api.put(`/applications/${id}`, application);
  },
  // 删除应用
  deleteApplication: (id: number) => {
    return api.delete(`/applications/${id}`);
  },
  // 获取所有应用（包括已下架的）
  getAllApplications: () => {
    return api.get('/applications/admin/all');
  }
};

// 知识库相关API
export const knowledgeApi = {
  // 获取知识库列表
  getKnowledgeList: (params: {
    current: number;
    size: number;
    category?: string;
    keyword?: string;
  }) => {
    return api.get('/knowledge/page', { params });
  },
  // 获取知识库详情
  getKnowledgeDetail: (id: number) => {
    return api.get(`/knowledge/${id}`);
  },
  // 增加点赞数
  incrementLikeCount: (id: number) => {
    return api.post(`/knowledge/${id}/like`);
  },
  // 获取所有知识库（管理后台）
  getAllKnowledge: () => {
    return api.get('/knowledge/admin/all');
  },
  // 添加知识库
  addKnowledge: (knowledge: {
    title: string;
    summary: string;
    content: string;
    category: string;
    author: string;
    tags: string;
  }) => {
    return api.post('/knowledge', knowledge);
  },
  // 更新知识库
  updateKnowledge: (id: number, knowledge: {
    title: string;
    summary: string;
    content: string;
    category: string;
    author: string;
    tags: string;
  }) => {
    return api.put(`/knowledge/${id}`, knowledge);
  },
  // 删除知识库
  deleteKnowledge: (id: number) => {
    return api.delete(`/knowledge/${id}`);
  }
};

// 案例相关API
export const caseApi = {
  // 获取案例列表
  getCases: (params: {
    current: number;
    size: number;
    industry?: string;
    keyword?: string;
  }) => {
    return api.get('/cases/page', { params });
  },
  // 获取案例详情
  getCaseDetail: (id: number) => {
    return api.get(`/cases/${id}`);
  },
  // 增加浏览量
  incrementViewCount: (id: number) => {
    return api.post(`/cases/${id}/view`);
  },
  // 获取所有案例（管理后台）
  getAllCases: () => {
    return api.get('/cases/admin/all');
  },
  // 添加案例
  addCase: (caseData: {
    title: string;
    description: string;
    result: string;
    industry: string;
    companyName: string;
    images: string;
  }) => {
    return api.post('/cases', caseData);
  },
  // 更新案例
  updateCase: (id: number, caseData: {
    title: string;
    description: string;
    result: string;
    industry: string;
    companyName: string;
    images: string;
  }) => {
    return api.put(`/cases/${id}`, caseData);
  },
  // 删除案例
  deleteCase: (id: number) => {
    return api.delete(`/cases/${id}`);
  }
};

// 新闻相关API
export const newsApi = {
  // 获取新闻列表
  getNewsList: (params: {
    current: number;
    size: number;
    category?: string;
    keyword?: string;
  }) => {
    return api.get('/news/page', { params });
  },
  // 获取热门新闻
  getHotNews: () => {
    return api.get('/news/hot');
  },
  // 获取新闻详情
  getNewsDetail: (id: number) => {
    return api.get(`/news/${id}`);
  },
  // 增加浏览量
  incrementViewCount: (id: number) => {
    return api.post(`/news/${id}/view`);
  },
  // 获取所有新闻（管理后台）
  getAllNews: () => {
    return api.get('/news/admin/all');
  },
  // 添加新闻
  addNews: (newsData: {
    title: string;
    summary: string;
    content: string;
    category: string;
    source: string;
    author: string;
    tags: string;
  }) => {
    return api.post('/news', newsData);
  },
  // 更新新闻
  updateNews: (id: number, newsData: {
    title: string;
    summary: string;
    content: string;
    category: string;
    source: string;
    author: string;
    tags: string;
  }) => {
    return api.put(`/news/${id}`, newsData);
  },
  // 删除新闻
  deleteNews: (id: number) => {
    return api.delete(`/news/${id}`);
  }
};

// 认证相关API
export const authApi = {
  // 登录
  login: (params: {
    username: string;
    password: string;
  }) => {
    return api.post('/auth/login', params);
  },
  // 注册
  register: (user: {
    username: string;
    password: string;
    email?: string;
    role?: string;
  }) => {
    return api.post('/auth/register', user);
  },
  // 获取用户信息
  getUserInfo: (username: string) => {
    return api.get('/auth/user/info', { params: { username } });
  },
  // 获取用户列表
  getUsers: (params: {
    current: number;
    size: number;
  }) => {
    return api.get('/auth/users', { params });
  },
  // 获取用户详情
  getUserById: (id: number) => {
    return api.get(`/auth/users/${id}`);
  },
  // 更新用户
  updateUser: (id: number, user: {
    username: string;
    email?: string;
    role: string;
    status: number;
    password?: string;
  }) => {
    return api.put(`/auth/users/${id}`, user);
  },
  // 删除用户
  deleteUser: (id: number) => {
    return api.delete(`/auth/users/${id}`);
  }
};

export default api;