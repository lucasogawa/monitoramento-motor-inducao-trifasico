const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');
const propreties = require('../../../properties');
const repository = require('../../usuario/repository/UsuarioRepository');
const usuarioUtils = require('../../usuario/utils/UsuarioUtils');

exports.autenticar = async (req, res) => {
    try {
        const usuario = await repository.buscarPorUsuario(req.body.usuario);

        if (!usuario) {
            return res.status(400).send({ erro: 'Usuário não encontrado.' });
        } else if (!(await bcrypt.compare(req.body.senha, usuario.senha))) {
            return res.status(400).send({ erro: 'Senha inválida.' });
        } else if (usuario._doc.situacao != 'ATIVO') {
            return res.status(400).send({ erro: 'Usuário inativo.' });
        } else {
            return res.send({
                usuario: usuarioUtils.converterParaResponse(usuario._doc),
                token: jwt.sign({ id: usuario._id, perfil: usuario.perfil }, propreties.secret, {
                    expiresIn: 86400,
                }),
            });
        }
    } catch (error) {
        console.log(error);
        return res.status(400).send({ erro: 'Erro ao autenticar.' });
    }
};

exports.verificarToken = async (req, res) => {
    try {
        const authHeader = req.headers.authorization;

        if (!authHeader) {
            return res.status(401).send({ erro: 'Usuário não autenticado.' });
        } else {
            const parts = authHeader.split(' ');

            if (!parts.length === 2) {
                return res.status(401).send({ erro: 'Erro de token.' });
            }

            const [scheme, token] = parts;

            if (!/^Bearer$/i.test(scheme)) {
                return res.status(401).send({ erro: 'Erro de formato de token.' });
            }

            jwt.verify(token, propreties.secret, (err, decoded) => {
                if (err) {
                    return res.status(401).send({ erro: 'Token inválido.' });
                }

                return res.send({ message: 'Token válido.' });
            });
        }
    } catch (error) {
        console.log(error);
        return res.status(400).send({ erro: 'Erro ao autenticar.' });
    }
};
