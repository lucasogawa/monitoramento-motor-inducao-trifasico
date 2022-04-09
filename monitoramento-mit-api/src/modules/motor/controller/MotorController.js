const express = require('express');
const router = express.Router();
const authMiddleware = require('../../auth/middleware/AuthMiddleware');
const service = require('../service/MotorService');

router.use(authMiddleware);

router.post('/', (req, res) => {
    service.salvar(req, res);
});

router.put('/', (req, res) => {
    service.editar(req, res);
});

router.get('/', (req, res) => {
    service.buscarTodos(req, res);
});

router.get('/:id', (req, res) => {
    service.buscarPorIdFalso(req, res);
});

module.exports = app => app.use('/motor', router);
