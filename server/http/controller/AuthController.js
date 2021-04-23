const AuthService = require('../service/AuthService.js')

module.exports = class AuthController extends AuthService {
  constructor(request, response) { super(request, response) }

  async code() {
    let all = this.all()
    let validator = this.Validator.make(all, {
      forgotem: 'required|object'
    })

    try {
      if (validator.fails()) return this.defaultResponseJSON(validator.errorResult())
      var object = await this.decryptObject(all['forgotem'])

      let internalValidator = this.Validator.make(object, {
        email: 'required|email',
        code: 'required|interger'
      })

      if (internalValidator.fails()) return this.defaultResponseJSON(internalValidator.errorResult(), 400)

      let register = await this.getCode('forgotem', object)

      if (register.code != object.code) 
        return this.defaultResponseJSON({ code: 200, message: 'invalid code' }, 400);

        return this.defaultResponseJSON({ code: 200, message: 'success response' });
    } catch (error) {
      return this.defaultResponseJSON({ code: 200, message: 'internal server error' }, 500);
    }
  }

  async change() {
    let all = this.all()
    let validator = this.Validator.make(all, {
      forgotem: 'required|object'
    })

    try {
      if (validator.fails()) return this.defaultResponseJSON(validator.errorResult())
      var object = await this.decryptObject(all['forgotem'])

      let internalValidator = this.Validator.make(object, {
        email: 'required|email',
        code: 'required|interger',
        senha: 'required|string',
        confirm: 'required|string'
      })

      if (internalValidator.fails()) return this.defaultResponseJSON(internalValidator.errorResult(), 400)

      let register = await this.getCode('forgotem', object)

      if (register.code != object.code) {
        return this.defaultResponseJSON({ code: 200, message: 'invalid code' }, 400);
      } else if (object.senha != object.confirm) {
        return this.defaultResponseJSON({ code: 200, message: 'invalid values' }, 400);
      }

      let user = await register.user()
      user.senha = this.Crypto.Hash().update(object.senha)

      await Promise.all([
        user.save(),
        register.delete()
      ])

      return this.defaultResponseJSON({ code: 200, message: 'success response' });
    } catch (error) {
      return this.defaultResponseJSON({ code: 200, message: 'internal server error' }, 500);
    }
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

      // await this.Smtp.setReceiver(object.email)
      //          .setSubject(`Solicitação de alteração: ${date.toLocaleString()}`)
      //          .setHtml('sendCodeForgotem.html', [[/\[CODE\]/g, `${register.code}`]])
      //          .send()

      return this.defaultResponseJSON({ code: 200, message: 'success' });
    } catch (error) {
      return this.defaultResponseJSON({ code: 200, message: 'internal server error' }, 500);
    }
  }

  async login() {

  }
}