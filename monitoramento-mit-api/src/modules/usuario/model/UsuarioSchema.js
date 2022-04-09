const bcrypt = require('bcryptjs');
const mongoose = require('../../../config/database');
const Usuario = require('./Usuario');
const UsuarioRepository = require('../repository/UsuarioRepository');

const UsuarioSchema = new mongoose.Schema(Usuario);

UsuarioSchema.pre('save', async function (next) {
    this.id_falso = (await UsuarioRepository.qtdTotal()) + 1;
    this.nome = this.nome.toUpperCase();
    this.cpf = this.cpf.replace(/\D/g, '');
    this.usuario = this.usuario.toUpperCase();
    this.senha = await bcrypt.hash(this.senha, 10);
    this.data_cadastro = new Date();

    next();
});

module.exports = UsuarioSchema;
