const mongoose = require('../../../config/database');
const parametro = require('./Parametro');
const repository = require('../repository/ParametroRepository');

const ParametroSchema = new mongoose.Schema(parametro);

ParametroSchema.pre('save', async function (next) {
    this.id_falso = (await repository.qtdTotal(this.motor_id)) + 1;
    this.data_cadastro = new Date();

    next();
});

module.exports = ParametroSchema;
