const SyncService = require('../service/SyncService.js')

module.exports = class SyncController extends SyncService {
  constructor(request, response) { super(request, response) }

  async get() {
    try {
      if (!this.keysExists()) {
        this.generateServerKeys()
      }
    } catch (error) { }

    return this.defaultResponseJSON({ code: 200, message: 'in generate key' })
  }

  async post() {
    let validator = this.Validator.make(this.all(), {
      publicKey: 'required|string'
    })

    try {
      if (validator.fails())
        return this.defaultResponseJSON(validator.errorResult())

      let key = await this.getServerPublicKey()
      await this.setAppPublicKey(this.all())
      return this.defaultResponseJSON({ 
        code: 200, 
        message: 'success response',
        result: { publicKey: key.toString("hex") } 
      })
    } catch (error) {
      if (error instanceof Object && error.request)
        return this.defaultResponseJSON(error.request, error.requestCode)
      else
        return this.defaultResponseJSON({ code: 500, message: 'internal server error' }, 500)
    }
  }
}