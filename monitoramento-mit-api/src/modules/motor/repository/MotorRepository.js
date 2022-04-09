const mongoose = require('../../../config/database');
const MotorSchema = require('../model/MotorSchema');
const repository = mongoose.model('Motor', MotorSchema);

exports.qtdPorDescricao = async descricao =>
    await repository.countDocuments({ descricao: descricao.toUpperCase() });

exports.qtdPorCaminhoArquivo = async caminhoArquivo =>
    await repository.countDocuments({ caminho_arquivo: caminhoArquivo });

exports.qtdPorIdFalso = async id_falso =>
    await repository.countDocuments({
        id_falso: id_falso,
    });

exports.qtdTotal = async () => await repository.countDocuments({});

exports.salvar = async motor => repository.create(motor);

exports.editar = async motor =>
    await repository.findOneAndUpdate({ id_falso: motor.id_falso }, motor, {
        returnOriginal: false,
    });

exports.buscarTodos = async () => await repository.find({});

exports.buscarPorIdFalso = async id => await repository.findOne({ id_falso: id });
