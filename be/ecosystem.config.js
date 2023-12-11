module.exports = {
  apps: [
    {
      name: "planj", // 애플리케이션 이름
      script: "dist/main.js", // NestJS 애플리케이션 진입점 파일의 경로
      instances: 0, // 실행할 인스턴스 수 max
      autorestart: true, // 프로세스의 자동 재시작 여부
      watch: false, // 파일의 변경을 감지하고 자동으로 재시작할지 여부
      max_memory_restart: "1G", // 메모리 사용이 일정 수준을 초과하면 프로세스 재시작
      wait_ready: true,
      listen_timeout: 50000,
      kill_timeout: 5000,
      env: {
        NODE_ENV: "development",
        PORT: 3000, // NestJS 애플리케이션의 포트 설정
      },
    },
  ],
};
