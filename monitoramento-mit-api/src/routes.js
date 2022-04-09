const AuthController = require('./modules/auth/controller/AuthController');
const UsuarioController = require('./modules/usuario/controller/UsuarioController');
const MotorController = require('./modules/motor/controller/MotorController');
const MonitoramentoController = require('./modules/monitoramento/controller/MonitoramentoController');

module.exports = app => {
    AuthController(app);
    UsuarioController(app);
    MotorController(app);
    MonitoramentoController(app);
};
