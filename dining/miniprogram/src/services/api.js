export const API_BASE = 'http://127.0.0.1:8092'
export function request(path, method = 'GET', data) { return new Promise((resolve, reject) => uni.request({url: API_BASE + path, method, data, success: ({statusCode, data}) => statusCode >= 200 && statusCode < 300 ? resolve(data) : reject(new Error(data?.detail || data?.message || '请求失败：' + statusCode)), fail: reject})) }

