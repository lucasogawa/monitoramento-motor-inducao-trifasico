const properties = require('../../../properties');
const motorRepository = require('../../motor/repository/MotorRepository');
const parametroRepository = require('../repository/ParametroRepository');
const parametroUtils = require('../utils/ParametroUtils');
const { spawn } = require('child_process');
const caminhoPython = properties.pathPython;
const caminhoScriptParametro = './src/modules/monitoramento/script/parametro.py';
const caminhoScriptGrafico = './src/modules/monitoramento/script/grafico.py';
const caminhoImagens = './src/static/png/';

Date.prototype.addDays = function (days) {
    var date = new Date(this.valueOf());
    date.setDate(date.getDate() + days);
    return date;
};

exports.obterParametros = async (req, res) => {
    try {
        const motores = await motorRepository.buscarTodos();

        if (Array.isArray(motores) && motores.length == 0) {
            return res.status(400).send({ erro: 'Nenhum motor encontrado.' });
        }

        const response = [];
        const motoresObj = motores.map(motor => ({
            id: motor.id_falso,
            motor: motor._doc.descricao,
            caminhoArquivo: motor._doc.caminho_arquivo,
        }));

        for (const motor of motoresObj) {
            response.push(
                await executarScriptParametros(motor.id, motor.motor, motor.caminhoArquivo)
            );
        }

        response.map(
            async parametro =>
                await parametroRepository.salvar(parametroUtils.converterModel(parametro))
        );

        return res.send(response);
    } catch (error) {
        console.log(error);
        return res.status(400).send({ erro: 'Erro ao obter parâmetros.' });
    }
};

async function executarScriptParametros(id, motor, caminhoArquivo) {
    const response = {};
    const error = [];
    const scriptParametro = spawn(caminhoPython, [
        caminhoScriptParametro,
        id,
        motor,
        caminhoArquivo,
    ]);

    for await (const data of scriptParametro.stdout) {
        const dados = data.toString().split('\n');

        dados.map(dados => {
            const aux = dados.split(':');
            const key = aux[0];
            const value = aux[1];

            if (key == 'id' || key == 'motor' || key == 'torque' || key == 'velocidade') {
                response[key] = value;
            } else {
                const dados2 = dados.split('*');
                const key2 = dados2[0];
                const value2 = dados2[1].split('\t');
                const objeto2 = {};

                value2.map(dados => {
                    const aux = dados.split(';');
                    const key = aux[0];
                    const value = aux[1];

                    objeto2[key] = value;
                });

                response[key2] = objeto2;
            }
        });
    }

    for await (const data of scriptParametro.stderr) {
        error.push(data.toString());
    }

    const exitCode = await new Promise((resolve, reject) => {
        scriptParametro.on('close', resolve);
    });

    if (exitCode) {
        throw new Error(error);
    }

    return response;
}

exports.buscarPorMotorIdEData = async (req, res) => {
    try {
        if ((await motorRepository.qtdPorIdFalso(req.params.id)) == 0) {
            return res.status(400).send({ erro: 'Motor não encontrado.' });
        }

        const dataInicio = new Date(req.params.data).setHours(2, 59, 29);
        const dataFim = new Date(req.params.data).addDays(1).setHours(23, 59, 59);
        return res.send(
            (
                await parametroRepository.buscarPorMotorIdEDataCadastro(
                    req.params.id,
                    dataInicio,
                    dataFim
                )
            ).map(parametro => parametroUtils.converterParaResponse(parametro._doc))
        );
    } catch (error) {
        console.log(error);
        return res.status(400).send({ erro: 'Erro ao obter histórico de parâmetros.' });
    }
};

exports.executarAgendadorParametro = async () => {
    try {
        const motores = await motorRepository.buscarTodos();

        if (Array.isArray(motores) && motores.length == 0) {
            return res.status(400).send({ erro: 'Nenhum motor encontrado.' });
        }

        const response = [];
        const motoresObj = motores.map(motor => ({
            id: motor.id_falso,
            motor: motor._doc.descricao,
            caminhoArquivo: motor._doc.caminho_arquivo,
        }));

        for (const motor of motoresObj) {
            response.push(
                await executarScriptParametros(motor.id, motor.motor, motor.caminhoArquivo)
            );
        }

        response.map(
            async parametro =>
                await parametroRepository.salvar(parametroUtils.converterModel(parametro))
        );
    } catch (error) {
        console.log(error);
        return res.status(400).send({ erro: 'Erro ao executar agendador parâmetro.' });
    }
};

exports.obterGraficoPorIdFalso = async (req, res) => {
    try {
        if ((await motorRepository.qtdPorIdFalso(req.params.id)) == 0) {
            return res.status(400).send({ erro: 'Motor não encontrado.' });
        }

        const motor = await motorRepository.buscarPorIdFalso(req.params.id);

        return res.send(
            await executarScriptGraficos(motor._doc.descricao, motor._doc.caminho_arquivo)
        );
    } catch (error) {
        console.log(error);
        return res.status(400).send({ erro: 'Erro ao obter gráficos.' });
    }
};

async function executarScriptGraficos(motor, caminhoArquivo) {
    const response = {};
    const error = [];
    const scriptGraficos = spawn(caminhoPython, [
        caminhoScriptGrafico,
        caminhoImagens,
        motor,
        caminhoArquivo,
    ]);

    for await (const data of scriptGraficos.stdout) {
        const dados = data.toString().split('\n');

        dados.map(dados => {
            const aux = dados.split(':');
            const key = aux[0];
            const value = aux[1];

            response[key] = value;
        });
    }

    for await (const data of scriptGraficos.stderr) {
        error.push(data.toString());
    }

    const exitCode = await new Promise((resolve, reject) => {
        scriptGraficos.on('close', resolve);
    });

    if (exitCode) {
        throw new Error(error);
    }

    return response;
}
