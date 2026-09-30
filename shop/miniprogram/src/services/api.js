// 开发工具本地调试可使用 http://127.0.0.1:8081；真机/发布环境必须替换为备案 HTTPS 域名。
export const API_BASE = 'http://127.0.0.1:8081'
export function request(path, options = {}) {
  return new Promise((resolve, reject) => {
    uni.request({ url: `${API_BASE}${path}`, ...options, success: resolve, fail: reject })
  })
}
