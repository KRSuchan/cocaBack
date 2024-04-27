const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
    app.use(
        '/test',
        createProxyMiddleware({
            target: 'http://localhost:8080',	// 백엔드 서버 URL or localhost:설정한포트번호
            changeOrigin: true,
        })
    );
};