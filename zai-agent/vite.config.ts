import { defineConfig } from 'vite'
import path from 'path'
import react from '@vitejs/plugin-react-swc'
import AutoImport from 'unplugin-auto-import/vite'
import { VitePWA } from 'vite-plugin-pwa'

// eslint-disable-next-line @typescript-eslint/no-var-requires
const getViteVersion = () => require("./package.json").version;

// https://vitejs.dev/config/
export default defineConfig(({ mode }) =>({
  plugins: [react(),
  AutoImport({
    eslintrc: {
      enabled: true,
      filepath: './.eslintrc-auto-import.json'
    },
    dts: './src/auto-imports.d.ts',
    imports: [
      'react',
      'react-router-dom',
      'react-i18next',
    ],
    dirs: ['./src/components/**'],
  }),
  VitePWA({
    mode: 'development',
    base: '/',
    outDir: "dist",
    manifest: {
      name: "ZAI Agent",
      short_name: "ZAI Agent",
      description: "ZAI Agent",
      icons: [
        {
          src: './app.png',
          sizes: '512x512',
          type: 'image/png',
        },
      ]
    },

    registerType: 'autoUpdate',
    workbox: {
      maximumFileSizeToCacheInBytes: 5 * 1024 * 1024, // 5 MiB
      globPatterns: ['**/*.{js,css,html,ico,png,jpg,svg}'],
  },
    devOptions: {
      enabled: mode === 'production'
    },
  })
    // visualizer({
    //   open: true,
    //   filename: "stats.html",
    //   gzipSize: true,
    //   brotliSize: true,
    // })
  ],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src'),
    }
  },
  server: {
    proxy: {
      '/dev-api': {
        target: "http://localhost:8080/z-shot",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/dev-api/, '')
      }
    }
  },
  build: {
    rollupOptions: {
      output: {
        chunkFileNames: "static/js/[name]-[hash].js",
        entryFileNames: "static/js/[name]-[hash].js",
        assetFileNames: "static/[ext]/[name]-[hash].[ext]",
        // manualChunks(id) {
        //   if (id.includes('node_modules')) {
        //     return 'vendor';
        //   }
        // }
      }
    }
  },
  define: {
    'process.env': {
      VITE_BUILD_TIME: new Date().toISOString(),
      VITE_APP_VERSION: getViteVersion(),
    }
  }
}))
