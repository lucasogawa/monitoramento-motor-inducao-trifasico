const mongoose = require('../../../config/database');
const ParametroSchema = require('../model/ParametroSchema');
const repository = mongoose.model('Parametro', ParametroSchema);

exports.qtdTotal = async motor_id => await repository.countDocuments({ motor_id: motor_id });

exports.salvar = async motor => await repository.create(motor);

exports.buscarPorMotorIdEDataCadastro = async (motorId, dataInicio, dataFim) =>
    await repository
        .find({
            motor_id: motorId,
            data_cadastro: {
                $gt: dataInicio,
                $lt: dataFim,
            },
        })
        .sort([['data_cadastro', 'asc']]);
