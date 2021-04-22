const AuthService = require('../service/AuthService.js')

module.exports = class AuthController extends AuthService {
  constructor(request, response) { super(request, response) }

  async forgotemCode() {

  }

  async forgotemChange() {

  }

  async forgotem() {
    let all = this.all()
    let validator = this.Validator.make(all, {
      forgotem: 'required|object'
    })

    try {
      if (validator.fails()) return this.defaultResponseJSON(validator.errorResult(), 400)
      var object = await this.decryptObject(all['forgotem'])

      let internalValidator = this.Validator.make(object, {
        email: 'required|email'
      })

      if (internalValidator.fails()) return this.defaultResponseJSON(internalValidator.errorResult(), 400)

      let register = await this.getCode('forgotem', object)
      let date = new Date()

      await this.Smtp.setReceiver(object.email)
               .setSubject(`Solicitação de alteração: ${date.toLocaleString()}`)
               .setHtml('sendCodeForgotem.html', [[/\[CODE\]/g, `${register.code}`]])
               .send()

      return this.defaultResponseJSON({ code: 200, message: 'success' });
    } catch (error) {
      console.log('error: ', error)
      return this.defaultResponseJSON({ code: 200, message: 'success' });
    }
  }

  async login() {

  }
}