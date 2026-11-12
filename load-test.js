import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  // Нагрузочный сценарий: разгоняемся до 50 виртуальных пользователей за 10 секунд, держим 30 секунд
  stages: [
    { duration: '10s', target: 50 },
    { duration: '30s', target: 50 },
    { duration: '10s', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<200'], // 95% запросов должны отвечать быстрее чем за 200мс
  },
};

const BASE_URL = 'http://localhost:8080';

// Функция предварительной настройки (выполняется 1 раз перед тестом)
export function setup() {
  // 1. Регистрируем тестового юзера (на случай если его нет)
  http.post(`${BASE_URL}/api/auth/register`, JSON.stringify({
    username: 'k6_user',
    password: 'password123'
  }), {
    headers: { 'Content-Type': 'application/json' },
  });

  // 2. Логинимся и забираем JWT-токен
  let loginRes = http.post(`${BASE_URL}/api/auth/login`, JSON.stringify({
    username: 'k6_user',
    password: 'password123'
  }), {
    headers: { 'Content-Type': 'application/json' },
  });

  let token = loginRes.json('token');
  return { token: token };
}

// Основной сценарий, который крутят виртуальные пользователи
export default function (data) {
  let headers = {
    'Authorization': `Bearer ${data.token}`,
    'Content-Type': 'application/json',
  };

  let res = http.get(`${BASE_URL}/tasks`, { headers: headers });

  check(res, {
    'status is 200': (r) => r.status === 200,
    'response time < 50ms': (r) => r.timings.duration < 50,
  });

  sleep(1); // Пауза 1 секунда между запросами одного юзера
}