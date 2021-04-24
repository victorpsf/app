const { Router } = require('squirrel_resource')
const bodyparser = require('body-parser')

module.exports = Router()
  .use(function (req, res, next) {
    console.log(`url: ${req.url} method: ${req.method}`)
    next()
  })
  .use(bodyparser.json())
  .use(bodyparser.urlencoded({ extended: true }))
  .responseHeaders({
    'Access-Control-Allow-Origin': '*',
    'Access-Control-Allow-Methods': 'GET, POST, OPTIONS, PUT, PATCH, DELETE',
    'Access-Control-Allow-Headers': '*',
    'Access-Control-Allow-Credentials': false
  })
  .prefix('/api', function (api) {
    return api.prefix('/v1', function (v1) {
      return v1.get('/', 'SyncController.js', 'get')
               .post('/', 'SyncController.js', 'post')
               .prefix('/auth', function (auth) {
                 return auth.post('/', 'AuthController.js', 'userLogin')
                            .post('/code', 'AuthController.js', 'userLoginCode')
               })
               .prefix('/forgotem', function (forgotem) {
                 return forgotem.post('/', 'AuthController.js', 'userForgotem')
                                .post('/code', 'AuthController.js', 'forgotemPasswordChangeCode')
                                .post('/change', 'AuthController.js', 'forgotemPasswordChange')
               })
    })
  })