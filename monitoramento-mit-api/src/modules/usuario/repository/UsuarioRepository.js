const mongoose = require('../../../config/database/index');
const UsuarioSchema = require('../model/UsuarioSchema');
const usuarioRepository = mongoose.model('Usuario', UsuarioSchema);

exports.qtdPorCpf = async cpf => await usuarioRepository.countDocuments({ cpf: cpf });

exports.qtdPorUsuario = async usuario =>
    await usuarioRepository.countDocuments({
        usuario: usuario.toUpperCase(),
    });

exports.qtdPorIdFalso = async id_falso =>
    await usuarioRepository.countDocuments({
        id_falso: id_falso,
    });

exports.qtdTotal = async () => await usuarioRepository.countDocuments({});

exports.salvar = async usuario => await usuarioRepository.create(usuario);

exports.editar = async usuario =>
    await usuarioRepository.findOneAndUpdate({ id_falso: usuario.id_falso }, usuario, {
        returnOriginal: false,
    });

exports.ativar = async id =>
    await usuarioRepository.findOneAndUpdate(
        { id_falso: id },
        { situacao: 'ATIVO' },
        { returnOriginal: false }
    );

exports.inativar = async id =>
    await usuarioRepository.findOneAndUpdate(
        { id_falso: id },
        { situacao: 'INATIVO' },
        { returnOriginal: false }
    );

exports.buscarTodos = async () => await usuarioRepository.find({});

exports.buscarPorIdFalso = async id_falso =>
    await usuarioRepository.findOne({ id_falso: id_falso }).exec();

exports.buscarPorUsuario = async usuario =>
    await usuarioRepository.findOne({ usuario: usuario }).select('+senha');
