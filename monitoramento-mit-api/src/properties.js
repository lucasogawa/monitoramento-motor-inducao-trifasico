module.exports = {
    port: process.env.PORT || 8080,
    database: process.env.URL_DATABASE,
    secret: process.env.SECRET,
    pathPython:
        process.env.PATH_PYTHON == 'LOCAL' ? '/usr/local/anaconda3/bin/python3.8' : 'python',
};
