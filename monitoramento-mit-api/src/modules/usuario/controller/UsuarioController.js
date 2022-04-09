const express = require('express');
const router = express.Router();
const authMiddleware = require('../../auth/middleware/AuthMiddleware');
const service = require('../service/UsuarioService');

router.use(authMiddleware);

router.post('/', (req, res) => {
    service.salvar(req, res);
});

router.put('/', (req, res) => {
    service.editar(req, res);
});

router.put('/:id/ativar', (req, res) => {
    service.ativar(req, res);
});

router.put('/:id/inativar', (req, res) => {
    service.inativar(req, res);
});

router.get('/', (req, res) => {
    service.buscarTodos(req, res);
});

router.get('/:id', (req, res) => {
    service.buscarPorIdFalso(req, res);
});

module.exports = app => app.use('/usuario', router);
