const Motor = {
    id_falso: {
        unique: true,
        type: Number,
        equire: true,
    },
    descricao: {
        unique: true,
        type: String,
        equire: true,
        min: 5,
        max: 255,
    },
    caminho_arquivo: {
        unique: true,
        type: String,
        equire: true,
    },
    data_cadastro: {
        type: Date,
        require: true,
    },
};

module.exports = Motor;
