# AI Toolbox Landing Page - Design Specification

## Project Overview
**Purpose:** High-end, Awwwards-level landing page for an AI toolbox platform featuring multiple functional modules.

**Design Philosophy:** Move beyond generic tech aesthetics. Create a warm, sophisticated, and futuristic experience using earth tones and cinematic visuals.

---

## 🎨 Design System

### Color Palette (STRICT: NO BLUE/PURPLE)

**Primary:**
- Ember Orange: #FF6B35 (CTAs, accents)
- Coral Red: #F7931E (hover states, highlights)
- Burnt Sienna: #C44536 (deep accents)

**Neutral:**
- Deep Charcoal: #1A1A1A (primary backgrounds)
- Warm Grey: #2D2D2D (sections, cards)
- Cream White: #FAF9F6 (text, light elements)
- Soft White: #FFFFFF (pure white for contrast)

**Accent:**
- Gold: #D4AF37 (premium touches, badges)
- Soft Green: #88B04B (success states, positive metrics)

**Gradients:**
- Hero Gradient: linear-gradient(135deg, #1A1A1A 0%, #2D2D2D 50%, #3A3A3A 100%)
- Card Gradient: linear-gradient(145deg, rgba(255,107,53,0.05) 0%, rgba(196,69,54,0.05) 100%)
- Button Gradient: linear-gradient(90deg, #FF6B35 0%, #F7931E 100%)

### Typography

**Font Stack:**
```css
--font-display: 'Space Grotesk', -apple-system, BlinkMacSystemFont, sans-serif;
--font-body: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
```

**Scale:**
- H1: 4.5rem / 300 / -0.02em (Hero title)
- H2: 3rem / 700 / -0.01em (Section titles)
- H3: 2rem / 600 / 0 (Card titles)
- Body: 1rem / 400 / 0.01em
- Small: 0.875rem / 400 / 0.02em

### Spacing System
```
4px, 8px, 12px, 16px, 24px, 32px, 48px, 64px, 96px, 128px
```

### Layout Grid
- Container Max-Width: 1440px
- Columns: 12-column grid
- Gutter: 32px
- Margin: 24px (mobile) / 48px (desktop)

---

## 🎥 Hero Section (PRIORITY)

### Hero Strategy: VIDEO-LED

**Video Specifications:**
- **Resolution:** 1080p (1920x1080) MINIMUM
- **Format:** MP4, H.264 codec
- **Duration:** 10-15 seconds looping seamlessly
- **Attributes:** autoplay, loop, muted, playsinline

**Video Prompt (Cinematic Quality):**
```
"Abstract AI neural network visualization, flowing data particles forming geometric patterns, 
glowing orange and gold light rays, depth of field, Shot on Arri Alexa with 35mm lens at f/1.8, 
cinematic lighting with volumetric fog, slow-motion particle systems, hyper-realistic rendering, 
8k resolution, dark background with warm color temperature, professional color grading"
```

**Hero Layout:**
- Full viewport height (100vh)
- Video as background layer (z-index: 0)
- Dark overlay gradient (z-index: 1): rgba(26,26,26,0.7) to rgba(26,26,26,0.3)
- Text content layer (z-index: 2): Centered, floating typography
- NO static image placeholders
- Fallback: Solid #1A1A1A background color

**Hero Content:**
- Main Headline: "AI工具箱" or "AI Powered Toolbox" (80px+, ultra-bold)
- Subheadline: Brief description (24px)
- Primary CTA: "探索工具" (Explore Tools)
- Secondary CTA: "开始体验" (Start Free)

---

## 📦 Functional Modules

### 1. AI Application Module
**Features:**
- Card grid layout (3 columns desktop, 1 mobile)
- Category filter chips (All, 图像生成, 文本处理, 数据分析, etc.)
- Search bar with real-time filtering
- Each card displays:
  - Icon (SVG)
  - App name
  - Brief description
  - Category badge
  - View count
  - "查看详情" button
- Modal overlay for details (NOT new page)

**Card Design:**
- Background: #2D2D2D with subtle gradient
- Hover: Lift effect (translateY(-8px), shadow)
- Border-radius: 16px
- Padding: 24px

### 2. Knowledge Base Module
**Features:**
- Article card grid
- Category tags (可点击筛选)
- Like/Bookmark icons (interactive)
- Tags displayed as pills
- "阅读更多" link

**Layout:**
- Masonry-style grid OR uniform cards
- Sidebar with category filter

### 3. AI Case Studies Module
**Features:**
- Large feature cards with image
- Industry label (金融, 医疗, 零售, etc.)
- Results metrics (提升效率40%, etc.)
- "查看案例" button
- Details in modal or dedicated section

**Visual Style:**
- Hero image per case
- Overlay text on hover
- Progress bars for metrics

### 4. News/Blog Module
**Features:**
- List view with thumbnails
- Publication date
- Category badge
- "Hot" badge for trending
- Excerpt text

### 5. AI Chat Assistant (Fixed Bottom Right)
**Features:**
- Floating chat bubble (fixed position)
- Expandable chat window (400px width)
- Message history
- Input field with send button
- Simulated AI responses (static for demo)
- Typing indicator animation

**Design:**
- Bubble: Gradient button (Orange)
- Window: Glass-morphism effect
- Messages: Left (AI) / Right (User) alignment

---

## 🎬 Motion & Interaction

### Animations
- Scroll-triggered fade-ins (Intersection Observer)
- Hover lift effects on cards (transform: translateY(-8px))
- Button hover: Scale(1.05) + shadow expansion
- Modal: Fade + scale entrance
- Chat bubble pulse animation

### Transitions
```css
transition: all 0.3s cubic-bezier(0.4, 0.0, 0.2, 1);
```

---

## 🛠️ Technical Strategy

### Stack
- **Pure HTML5/CSS3/Vanilla JS**
- No external frameworks (React, Vue, etc.)
- No npm dependencies
- Inline critical CSS

### Asset Protocol
**Directory Structure:**
```
ai_toolbox/
├── videos/
│   └── hero_neural_network.mp4
├── imgs/
│   ├── app_icon_*.png (各个应用图标)
│   ├── case_*.jpg (案例配图)
│   └── news_*.jpg (资讯配图)
└── index.html
```

**Path Safety:**
- Use relative paths: `./videos/hero_neural_network.mp4`
- Verify all generated filenames BEFORE writing HTML
- Implement onerror handlers for images

### Performance
- Lazy loading for images: `loading="lazy"`
- Video preload: `preload="metadata"`
- Minimize DOM queries
- Debounce search input

---

## 📱 Responsive Breakpoints
```css
/* Mobile First */
@media (min-width: 640px) { /* Tablet */ }
@media (min-width: 1024px) { /* Desktop */ }
@media (min-width: 1440px) { /* Large Desktop */ }
```

---

## 🚀 Deployment Checklist
1. ✅ All media assets generated (1080p hero video)
2. ✅ Paths verified (no 404s)
3. ✅ Playwright validation passed
4. ✅ Deployed via deploy tool
5. ✅ URL provided to user

---

## 🎯 Success Criteria
- Hero video is cinematic and impactful (NO blue/purple)
- All 5 modules are fully functional
- Mobile responsive (tested)
- No broken media links
- Deployment successful

---

**Author:** Matrix Agent
**Version:** 1.0
**Date:** 2026-01-28
