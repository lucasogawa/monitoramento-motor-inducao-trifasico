const usuarioRepository = require('../src/modules/usuario/repository/UsuarioRepository');
const Motor = require('./modules/motor/model/Motor');
const motorRepository = require('./modules/motor/repository/MotorRepository');

const usuario1 = {
    nome: 'Administrador Teste 1',
    cpf: '000.000.000-00',
    usuario: 'admteste1',
    senha: 'admteste1',
    perfil: 'ADMINISTRADOR',
    situacao: 'ATIVO',
};

const usuario2 = {
    nome: 'Usuario Teste 1',
    cpf: '111.111.111-11',
    usuario: 'usuarioteste1',
    senha: 'usuarioteste1',
    perfil: 'USUARIO',
    situacao: 'ATIVO',
};

const motor1 = {
    descricao: 'Motor 1',
    caminho_arquivo: './src/static/txt/Motor01.txt',
};

const motor2 = {
    descricao: 'Motor 2',
    caminho_arquivo: './src/static/txt/Motor02.txt',
};

exports.executar = async function () {
    if ((await usuarioRepository.qtdTotal()) == 0) {
        await usuarioRepository.salvar(usuario1);
        await usuarioRepository.salvar(usuario2);
    }
    if ((await motorRepository.qtdTotal()) == 0) {
        await motorRepository.salvar(motor1);
        await motorRepository.salvar(motor2);
    }
};
