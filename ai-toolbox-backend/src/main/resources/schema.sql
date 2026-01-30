-- 创建数据库
CREATE DATABASE IF NOT EXISTS ai_toolbox CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ai_toolbox;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50),
    email VARCHAR(100),
    avatar VARCHAR(255),
    role VARCHAR(20) DEFAULT 'USER',
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- AI应用表
CREATE TABLE IF NOT EXISTS ai_application (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    icon VARCHAR(255),
    url VARCHAR(255),
    is_free TINYINT DEFAULT 1,
    price DECIMAL(10,2),
    rating DECIMAL(3,2) DEFAULT 0.00,
    view_count INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    created_by BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES sys_user(id)
);

-- 知识库表
CREATE TABLE IF NOT EXISTS knowledge_base (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content LONGTEXT,
    category VARCHAR(50),
    tags VARCHAR(500),
    cover_image VARCHAR(255),
    view_count INT DEFAULT 0,
    like_count INT DEFAULT 0,
    is_published TINYINT DEFAULT 1,
    author_id BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES sys_user(id)
);

-- AI案例表
CREATE TABLE IF NOT EXISTS ai_case (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    industry VARCHAR(50),
    company_name VARCHAR(100),
    ai_tools VARCHAR(500),
    images VARCHAR(1000),
    result TEXT,
    view_count INT DEFAULT 0,
    like_count INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    created_by BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES sys_user(id)
);

-- 资讯表
CREATE TABLE IF NOT EXISTS ai_news (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    summary VARCHAR(500),
    content LONGTEXT,
    source VARCHAR(100),
    category VARCHAR(50),
    cover_image VARCHAR(255),
    tags VARCHAR(500),
    view_count INT DEFAULT 0,
    is_hot TINYINT DEFAULT 0,
    is_top TINYINT DEFAULT 0,
    publish_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- AI聊天记录表
CREATE TABLE IF NOT EXISTS ai_chat_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    session_id VARCHAR(50),
    question TEXT NOT NULL,
    answer LONGTEXT,
    model_name VARCHAR(50),
    tokens_used INT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES sys_user(id)
);

-- 文件表
CREATE TABLE IF NOT EXISTS sys_file (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT,
    file_type VARCHAR(50),
    upload_user BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (upload_user) REFERENCES sys_user(id)
);

-- 创建索引
CREATE INDEX idx_ai_application_category ON ai_application(category);
CREATE INDEX idx_ai_application_status ON ai_application(status);
CREATE INDEX idx_ai_application_view_count ON ai_application(view_count);

CREATE INDEX idx_knowledge_base_category ON knowledge_base(category);
CREATE INDEX idx_knowledge_base_author_id ON knowledge_base(author_id);

CREATE INDEX idx_ai_case_industry ON ai_case(industry);
CREATE INDEX idx_ai_case_created_by ON ai_case(created_by);

CREATE INDEX idx_ai_news_category ON ai_news(category);
CREATE INDEX idx_ai_news_publish_time ON ai_news(publish_time);
CREATE INDEX idx_ai_news_is_hot ON ai_news(is_hot);

CREATE INDEX idx_ai_chat_history_user_id ON ai_chat_history(user_id);
CREATE INDEX idx_ai_chat_history_session_id ON ai_chat_history(session_id);
CREATE INDEX idx_ai_chat_history_create_time ON ai_chat_history(create_time);

-- 插入初始数据
INSERT INTO sys_user (username, password, nickname, email, role, status)
VALUES ('admin', '$2a$10$1e0b7b4e4a1b8c7d9e0f1a2b3c4d5e6f7g8h9i0j1k2l3m4n5o6p7q8r9s0t', '管理员', 'admin@aitoolbox.com', 'ADMIN', 1),
       ('test', '$2a$10$1e0b7b4e4a1b8c7d9e0f1a2b3c4d5e6f7g8h9i0j1k2l3m4n5o6p7q8r9s0t', '测试用户', 'test@aitoolbox.com', 'USER', 1);

-- 插入测试应用数据
INSERT INTO ai_application (name, description, category, icon, url, is_free, view_count, created_by)
VALUES ('AI图像生成', '通过文本描述生成高质量图像，支持多种风格和分辨率', '图像生成', './videos/3b5abb58-e209-467c-94e2-1b457cfebdb5.png', 'https://example.com/image-generator', 1, 15420, 1),
       ('AI文本处理', '智能文本分析、摘要生成、情感识别等全方位处理', '文本处理', './videos/36199aa2-cd6d-4099-aa15-571104713337.png', 'https://example.com/text-processor', 1, 12380, 1),
       ('AI数据分析', '自动化数据清洗、可视化和预测分析', '数据分析', './videos/cc4aca62-4292-4480-a4cc-63aa0904fde2.png', 'https://example.com/data-analyzer', 1, 9850, 1),
       ('AI语音合成', '自然流畅的语音生成，支持多种语言和声音风格', '语音合成', './videos/e02f4f67-2702-4101-8512-0a3616d3d662.png', 'https://example.com/voice-synthesis', 1, 8920, 1),
       ('AI视频编辑', '智能视频剪辑、特效添加、自动字幕生成', '视频编辑', './videos/2f130319-e44d-4710-afe8-f632aa8cbe27.png', 'https://example.com/video-editor', 1, 11250, 1),
       ('AI代码助手', '智能代码补全、错误检测、代码优化建议', '代码助手', './videos/5e4b884e-ddcd-4e90-9df2-13728efc4307.png', 'https://example.com/code-assistant', 1, 18700, 1);

-- 插入测试案例数据
INSERT INTO ai_case (title, description, industry, company_name, images, result, view_count, created_by)
VALUES ('智能风控系统', 'AI驱动的风险识别与预警系统，提升风控效率', '金融', '某银行', './videos/42a06465-a84e-43dd-87c5-189c69f9e08a.jpg', '实现了40%的效率提升和85%的准确率', 5000, 1),
       ('AI辅助诊断', '基于深度学习的医学影像分析与诊断辅助', '医疗', '某医院', './videos/b0d76ae8-2781-4ff3-9ae6-10590f0b6217.jpg', '诊断准确率达到92%，节省60%的时间', 8000, 1),
       ('智能推荐系统', 'AI驱动的个性化商品推荐，提升转化率', '零售', '某电商', './videos/5ccc2d66-535c-4263-808e-ffc0ff31e273.jpg', '转化率提升35%，用户满意度达到50%', 6500, 1);

-- 插入测试资讯数据
INSERT INTO ai_news (title, summary, content, source, category, publish_time, is_hot)
VALUES ('GPT-5发布：多模态能力再升级', '最新一代大语言模型GPT-5正式发布，带来革命性的多模态交互体验', 'GPT-5在语言理解、图像生成、语音交互等多个方面实现了重大突破...', '科技日报', '技术', NOW(), 1),
       ('AI绘画工具突破新纪录', '新一代AI绘画工具在图像质量和生成速度上实现双重突破', '最新的AI绘画工具通过创新的扩散模型，实现了更高质量的图像生成...', '艺术前沿', '创新', NOW(), 1),
       ('自动驾驶技术进入商用阶段', '多家科技公司宣布自动驾驶技术正式进入商业化应用阶段', '随着技术的成熟和法规的完善，自动驾驶技术开始在多个城市落地...', '汽车之家', '应用', NOW(), 0),
       ('AI伦理规范全球标准出台', '国际组织发布AI伦理与安全使用的全球统一标准', '为了确保AI技术的负责任发展，国际组织制定了全球统一的伦理规范...', '环球时报', '政策', NOW(), 0);

-- 插入测试知识库数据
INSERT INTO knowledge_base (title, content, category, tags, cover_image, view_count, like_count, author_id, is_published)
VALUES ('大语言模型入门指南', '大语言模型（LLM）是一种基于深度学习的人工智能模型，能够理解和生成人类语言...', '入门指南', 'LLM,深度学习,入门', NULL, 3420, 342, 1, 1),
       ('Transformer架构深度解析', 'Transformer架构是现代NLP模型的核心，通过自注意力机制实现了并行计算...', '技术解析', 'Transformer,架构,高级', NULL, 5670, 567, 1, 1),
       ('AI绘画技术发展与应用', 'AI绘画技术从早期的GAN到现在的扩散模型，经历了快速的发展...', '应用指南', '图像生成,AIGC,应用', NULL, 4230, 423, 1, 1),
       ('强化学习实战案例', '强化学习是一种通过与环境交互学习最优策略的机器学习方法...', '实战指南', '强化学习,实战,机器学习', NULL, 2980, 298, 1, 1);
