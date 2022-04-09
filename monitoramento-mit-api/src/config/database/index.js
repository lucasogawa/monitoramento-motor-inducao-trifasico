const mongoose = require('mongoose');
const properties = require('../../properties');

mongoose.connect(properties.database, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
});
mongoose.Promise = global.Promise;
mongoose.set('useFindAndModify', false);
mongoose.set('useNewUrlParser', true);
mongoose.set('useCreateIndex', true);

module.exports = mongoose;
