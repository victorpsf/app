const { Storage, Middleware } = require('squirrel_resource')

Storage.load()
const Api = require('./api')
const server = Middleware.build()
  .router(Api)
  .listen(function ({ protocol, host, port }) {
    console.log(`open server in ${protocol}://${host}:${port}`)
  })