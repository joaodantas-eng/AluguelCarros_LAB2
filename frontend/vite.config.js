import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // Tudo que começar com /api é repassado para o back-end Spring Boot.
    // Assim o front (porta 5173) e o back (porta 8080) conversam sem problema de CORS.
    // Usamos 127.0.0.1 em vez de "localhost" porque, no Mac, o "localhost" às vezes
    // vira o endereço IPv6 (::1) e a conexão com o Spring falha.
    proxy: {
      '/api': 'http://127.0.0.1:8080',
    },
  },
})
