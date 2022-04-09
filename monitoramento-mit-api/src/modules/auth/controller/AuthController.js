const express = require('express');
const router = express.Router();
const service = require('../service/AuthService');

router.post('/', (req, res) => {
    service.autenticar(req, res);
});

router.get('/verificar-token', (req, res) => {
    service.verificarToken(req, res);
});

module.exports = app => app.use('/auth', router);
