const { Router } = require('squirrel_resource')
const bodyparser = require('body-parser')

module.exports = Router()
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
               .post('/auth', 'AuthController.js', 'login')
               .prefix('/forgotem', function (forgotem) {
                 return forgotem.post('/', 'AuthController.js', 'forgotem')
                                .post('/code', 'AuthController.js', 'forgotemCode')
                                .post('/change', 'AuthController.js', 'forgotemChange')
               })
    })
  })