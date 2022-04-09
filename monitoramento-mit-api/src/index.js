const routes = require('./routes');
const express = require('express');
const bodyParser = require('body-parser');
const properties = require('./properties');
const app = express();
const popularDadosIniciais = require('./dadosIniciais');
const cron = require('node-cron');
const monitoramentoService = require('./modules/monitoramento/service/MonitoramentoService');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use('/imagens', express.static(__dirname + '/static/png'));

routes(app);

popularDadosIniciais.executar();

cron.schedule('0 */30 * * * *', async () => {
    console.log('Iniciando agendador parâmetro...');
    await monitoramentoService.executarAgendadorParametro();
    console.log('Finalizando agendador parâmetro...');
});

app.listen(properties.port);
