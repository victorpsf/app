const AuthService = require('../service/AuthService.js')

module.exports = class AuthController extends AuthService {
  constructor(request, response) { super(request, response) }

  async forgotemPasswordChangeCode() {
    let all = this.all()

    if (this.defaultVerificationRequest(all, 'forgotem')) return

    try {
      var decryptedData = await this.decryptObject(all['forgotem'])

      if (this.validateRequest(decryptedData, {
        code: 'required|interger'
      })) return

      let register = await this.getCode('forgotem', decryptedData)
      if (this.validateCode(register, decryptedData)) return

      return this.defaultResponseJSON({ code: 200, message: 'Código válido' });
    } catch (error) {
      if (error.request && error.code) return this.defaultResponseJSON(error.request, error.code);
      else return this.defaultResponseJSON({ code: 500, message: 'internal server error' }, 200);
    }
  }

  async forgotemPasswordChange() {
    let all = this.all()
    if (this.defaultVerificationRequest(all, 'forgotem')) return

    try {
      var decryptedData = await this.decryptObject(all['forgotem'])

      if (this.validateRequest(decryptedData, {
        code: 'required|interger',
        senha: 'required|string',
        confirm: 'required|string'
      })) return

      let {
        register,
        user
      } = await this.getCode('forgotem', decryptedData, true)

      if (this.validateCode(register, decryptedData)) return
      else if (this.validatePasswordChange(decryptedData)) return

      user.senha = decryptedData.senha

      await Promise.all([ user.save(), register.delete() ])
      return this.defaultResponseJSON({ code: 200, message: 'Senha alterada' });
    } catch (error) {
      if (error.request && error.code) return this.defaultResponseJSON(error.request, error.code)
      else return this.defaultResponseJSON({ code: 500, message: 'internal server error' }, 200);
    }
  }

  async userForgotem() {
    let all = this.all()
    if (this.defaultVerificationRequest(all, 'forgotem')) return

    try {
      var decryptedData = await this.decryptObject(all['forgotem'])

      if (this.validateRequest(decryptedData)) return

      let register = await this.getCode('forgotem', decryptedData)
      await this.sendMail('forgotem', decryptedData, register)

      return this.defaultResponseJSON({ code: 200, message: 'Código enviado' });
    } catch (error) {
      if (error.request && error.code) return this.defaultResponseJSON(error.request, error.code)
      else return this.defaultResponseJSON({ code: 500, message: 'internal server error' }, 200);
    }
  }

  async userLoginCode() {
    let all = this.all()
    if (this.defaultVerificationRequest(all, 'login')) return

    try {
      let decryptedData = await this.decryptObject(all['login'])

      if (this.validateRequest(decryptedData, {
        senha: 'required|string',
        code: 'required|interger'
      })) return

      if (this.validatePassword(user, decryptedData)) return
      let {
        register,
        user
      } = await this.getCode('login', decryptedData, true)

      if (this.validatePassword(user, decryptedData)) return
      else if (this.validateCode(decryptedData, register)) return

      let token = this.jwt({ pid: user.id })
      return this.defaultResponseJSON({ code: 200, message: 'Autenticado', result: { token } });
    } catch (error) {
      if (error.request && error.code) return this.defaultResponseJSON(error.request, error.code)
      else return this.defaultResponseJSON({ code: 500, message: 'internal server error' }, 200);
    }
  }

  async userLogin() {
    let all = this.all()
    if (this.defaultVerificationRequest(all, 'login')) return

    try {
      let decryptedData = await this.decryptObject(all['login'])

      if (this.validateRequest(decryptedData, {
        senha: 'required|string'
      })) return

      let user = await this.getUser(decryptedData)

      if (this.validatePassword(user, decryptedData)) return
      let register = await this.getCode('login', decryptedData, false, user)
      await this.sendMail('login', decryptedData, register)

      return this.defaultResponseJSON({ code: 200, message: 'Código enviado' });
    } catch (error) {
      if (error.request && error.code) return this.defaultResponseJSON(error.request, error.code)
      else return this.defaultResponseJSON({ code: 200, message: 'internal server error' }, 500);
    }
  }
}