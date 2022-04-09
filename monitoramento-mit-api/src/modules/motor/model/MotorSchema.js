const mongoose = require('../../../config/database');
const Motor = require('./Motor');
const repository = require('../repository/MotorRepository');

const MotorSchema = new mongoose.Schema(Motor);

MotorSchema.pre('save', async function (next) {
    this.id_falso = (await repository.qtdTotal()) + 1;
    this.descricao = this.descricao.toUpperCase();
    this.data_cadastro = new Date();

    next();
});

module.exports = MotorSchema;
