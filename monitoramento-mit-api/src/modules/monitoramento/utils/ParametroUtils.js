exports.converterModel = parametro => {
    return {
        motor_id: parametro.id,
        torque: parametro.torque,
        velocidade: parametro.velocidade,
        rms_vab: parametro.rms.vab,
        rms_vbc: parametro.rms.vbc,
        rms_vca: parametro.rms.vca,
        rms_ia: parametro.rms.ia,
        rms_ib: parametro.rms.ib,
        rms_ic: parametro.rms.ic,
        dht_vab: parametro.dht.vab,
        dht_vbc: parametro.dht.vbc,
        dht_vca: parametro.dht.vca,
        dht_ia: parametro.dht.ia,
        dht_ib: parametro.dht.ib,
        dht_ic: parametro.dht.ic,
    };
};

exports.converterParaResponse = parametro => {
    const parametroResponse = {
        ...parametro,
        id: parametro.id_falso,
        data_cadastro: new Date(parametro.data_cadastro).toLocaleString('pt-BR', {
            timeZone: 'America/Sao_Paulo',
        }),
    };
    delete parametroResponse._id;
    delete parametroResponse.__v;
    delete parametroResponse.id_falso;

    return parametroResponse;
};
