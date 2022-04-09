const express = require('express');
const router = express.Router();
const service = require('../service/MonitoramentoService');

router.get('/parametro', (req, res) => {
    service.obterParametros(req, res);
});

router.get('/parametro/:id/:data', (req, res) => {
    service.buscarPorMotorIdEData(req, res);
});

router.get('/graficos/:id', (req, res) => {
    service.obterGraficoPorIdFalso(req, res);
});

module.exports = app => app.use('/monitoramento', router);
