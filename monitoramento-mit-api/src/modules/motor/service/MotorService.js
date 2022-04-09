const repository = require('../repository/MotorRepository');
const utils = require('../utils/MotorUtils');

exports.salvar = async (req, res) => {
    try {
        if (
            (await repository.qtdPorDescricao(req.body.descricao)) != 0 ||
            (await repository.qtdPorCaminhoArquivo(req.body.caminho_arquivo)) != 0
        ) {
            return res.status(400).send({ erro: 'Motor já cadastrado.' });
        }

        return res.send(utils.converterParaResponse((await repository.salvar(req.body))._doc));
    } catch (error) {
        console.log(error);
        return res.status(400).send({ erro: 'Erro ao cadastrar motor.' });
    }
};

exports.editar = async (req, res) => {
    try {
        const motor = utils.alterarId(req.body);

        if ((await repository.qtdPorIdFalso(motor.id_falso)) == 0) {
            return res.status(400).send({ erro: 'Motor não encontrado.' });
        }

        return res.send(utils.converterParaResponse((await repository.editar(motor))._doc));
    } catch (error) {
        console.log(error);
        return res.status(400).send({ erro: 'Erro ao editar motor.' });
    }
};

exports.buscarTodos = async (req, res) => {
    try {
        return res.send(
            (await repository.buscarTodos()).map(motor => utils.converterParaResponse(motor._doc))
        );
    } catch (error) {
        console.log(error);
        return res.status(400).send({ erro: 'Erro ao buscar motor.' });
    }
};

exports.buscarPorIdFalso = async (req, res) => {
    try {
        if ((await repository.qtdPorIdFalso(req.params.id)) == 0) {
            return res.status(400).send({ erro: 'Motor não encontrado.' });
        }

        return res.send(
            utils.converterParaResponse((await repository.buscarPorIdFalso(req.params.id))._doc)
        );
    } catch (error) {
        console.log(error);
        return res.status(400).send({ erro: 'Erro ao buscar motor.' });
    }
};
