const repository = require('../repository/UsuarioRepository');
const usuarioUtils = require('../utils/UsuarioUtils');

exports.salvar = async (req, res) => {
    try {
        if (!isAdministrador(req)) {
            return res.status(401).send({ erro: 'Usuário sem permissão.' });
        }

        if (
            (await repository.qtdPorCpf(req.body.cpf)) != 0 ||
            (await repository.qtdPorUsuario(req.body.usuario)) != 0
        ) {
            return res.status(400).send({ erro: 'Usuário já cadastrado.' });
        }

        return res.send(
            usuarioUtils.converterParaResponse(
                (await repository.salvar(usuarioUtils.colocarSituacao(req.body, 'ATIVO')))._doc
            )
        );
    } catch (error) {
        console.log(error);
        return res.status(400).send({ erro: 'Erro ao cadastrar usuário.' });
    }
};

exports.editar = async (req, res) => {
    try {
        if (!isAdministrador(req)) {
            return res.status(401).send({ erro: 'Usuário sem permissão.' });
        }

        const usuario = usuarioUtils.alterarId(req.body);

        if (await repository.qtdPorIdFalso(usuario.falso_id)) {
            return res.status(400).send({ erro: 'Usuário não encontrado.' });
        }

        return res.send(
            usuarioUtils.converterParaResponse((await repository.editar(usuario))._doc)
        );
    } catch (error) {
        console.log(error);
        return res.status(400).send({ erro: 'Erro ao editar usuário.' });
    }
};

exports.ativar = async (req, res) => {
    try {
        if (!isAdministrador(req)) {
            return res.status(401).send({ erro: 'Usuário sem permissão.' });
        }

        if ((await repository.qtdPorIdFalso(req.params.id)) == 0) {
            return res.status(400).send({ erro: 'Usuário não encontrado.' });
        }

        return res.send(
            usuarioUtils.converterParaResponse((await repository.ativar(req.params.id))._doc)
        );
    } catch (error) {
        console.log(error);
        return res.status(400).send({ erro: 'Erro ao ativar usuário.' });
    }
};

exports.inativar = async (req, res) => {
    try {
        if (!isAdministrador(req)) {
            return res.status(401).send({ erro: 'Usuário sem permissão.' });
        }

        if ((await repository.qtdPorIdFalso(req.params.id)) == 0) {
            return res.status(400).send({ erro: 'Usuário não encontrado.' });
        }

        return res.send(
            usuarioUtils.converterParaResponse((await repository.inativar(req.params.id))._doc)
        );
    } catch (error) {
        console.log(error);
        return res.status(400).send({ erro: 'Erro ao inativar usuário.' });
    }
};

exports.buscarTodos = async (req, res) => {
    try {
        if (!isAdministrador(req)) {
            return res.status(401).send({ erro: 'Usuário sem permissão.' });
        }

        return res.send(
            (await repository.buscarTodos()).map(usuario =>
                usuarioUtils.converterParaResponse(usuario._doc)
            )
        );
    } catch (error) {
        console.log(error);
        return res.status(400).send({ erro: 'Erro ao buscar usuários.' });
    }
};

exports.buscarPorIdFalso = async (req, res) => {
    try {
        if (!isAdministrador(req)) {
            return res.status(401).send({ erro: 'Usuário sem permissão.' });
        }

        if ((await repository.qtdPorIdFalso(req.params.id)) == 0) {
            return res.status(400).send({ erro: 'Usuário não encontrado.' });
        }

        return res.send(
            usuarioUtils.converterParaResponse(
                (await repository.buscarPorIdFalso(req.params.id))._doc
            )
        );
    } catch (error) {
        console.log(error);
        return res.status(400).send({ erro: 'Erro ao buscar usuário.' });
    }
};

function isAdministrador(req) {
    if (req.perfil == 'ADMINISTRADOR') {
        return true;
    }

    return false;
}
