exports.colocarSituacao = (usuarioRequest, situacao) => {
    return {
        ...usuarioRequest,
        situacao: situacao,
    };
};

exports.alterarId = usuarioRequest => {
    const usuario = {
        ...usuarioRequest,
        id_falso: usuarioRequest.id,
    };
    delete usuario.id;

    return usuario;
};

exports.converterParaResponse = usuario => {
    const usuarioResponse = {
        ...usuario,
        id: usuario.id_falso,
        data_cadastro: new Date(usuario.data_cadastro).toLocaleString('pt-BR', {
            timeZone: 'America/Sao_Paulo',
        }),
    };
    delete usuarioResponse._id;
    delete usuarioResponse.id_falso;
    delete usuarioResponse.senha;
    delete usuarioResponse.__v;

    return usuarioResponse;
};
