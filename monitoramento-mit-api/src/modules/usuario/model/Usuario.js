const Perfil = require('../enum/Perfil');
const Situacao = require('../enum/Situacao');

const Usuario = {
    id_falso: {
        unique: true,
        type: Number,
        equire: true,
    },
    nome: {
        type: String,
        require: true,
        min: 5,
        max: 255,
    },
    cpf: {
        unique: true,
        type: String,
        require: true,
        min: 11,
        max: 11,
    },
    usuario: {
        unique: true,
        type: String,
        require: true,
        min: 5,
        max: 255,
    },
    senha: {
        type: String,
        require: true,
        select: false,
    },
    perfil: {
        type: String,
        enum: Perfil,
        require: true,
    },
    situacao: {
        type: String,
        enum: Situacao,
        require: true,
    },
    data_cadastro: {
        type: Date,
        require: true,
    },
};

module.exports = Usuario;
