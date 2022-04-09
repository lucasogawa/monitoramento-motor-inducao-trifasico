const jwt = require('jsonwebtoken');
const propreties = require('../../../properties');

module.exports = (req, res, next) => {
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

            req.id = decoded.id;
            req.perfil = decoded.perfil;
            return next();
        });
    }
};
