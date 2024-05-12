import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    port : 3000,
    proxy : {
      '/api' : {
        target: 'http://localhost:8080', // 실제 API 서버의 주소
        changeOrigin: true,
        // rewrite: (path) => path.replace(/^\/api/, '') // 요청 URL에서 '/api'를 제거하여 프록시 요청합니다.
      }
    }
  }
})
