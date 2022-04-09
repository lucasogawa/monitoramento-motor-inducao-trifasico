exports.alterarId = motorRequest => {
    const motor = {
        ...motorRequest,
        id_falso: motorRequest.id,
    };
    delete motor.id;

    return motor;
};

exports.converterParaResponse = motor => {
    const motorResponse = {
        ...motor,
        id: motor.id_falso,
        data_cadastro: new Date(motor.data_cadastro).toLocaleString('pt-BR', {
            timeZone: 'America/Sao_Paulo',
        }),
    };
    delete motorResponse._id;
    delete motorResponse.__v;
    delete motorResponse.id_falso;

    return motorResponse;
};
