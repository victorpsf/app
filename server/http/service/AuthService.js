const RsaService = require('./RsaService')
const { Utilities } = require('squirrel_resource')

module.exports = class AuthService extends RsaService {
  user = require('../../app/UserModel')
  forgotemCode = require('../../app/ForgotemcodeModel')
  authenticationCode = require('../../app/AuthenticationcodeModel')

  constructor(request, response) { super(request, response) }

  async getUser(data) {
    let users = await this.user.where({ column: 'email', value: data.email }).get();
    let user = users.first()

    if (!user) throw { request: { code: 400, message: 'Valores inválidos, tente outro' }, code: 200 }

    return user
  }

  async getRegisterCode(user, constraint, modelPropertie) {
    let codes = await user[constraint]()

    let register = codes.filter(function (register) {
      return !register.usaged_at
    }).first()

    if (!register) {
      let code = (new Utilities()).randomNumber(100000, 999999)

      register = await this[modelPropertie].create({
        code,
        user_id: user.id
      })
    }

    return register
  }

  async getCode(type, data, getUser = false, user = null) {
    let returnValue = { register: null, user: null }

    returnValue.user = (user === null) ? await this.getUser(data): user

    if (type == 'forgotem')
      returnValue.register = await this.getRegisterCode(returnValue.user, 'forgotem_code', 'forgotemCode')
    else 
      returnValue.register = await this.getRegisterCode(returnValue.user, 'autentication', 'authenticationCode')

    return (getUser) ? returnValue : returnValue.register
  }

  async sendMail(type, { email }, { code }) {
    try {
      let mail = this.Smtp.setReceiver(email),
          date = new Date()
  
      if (type == 'forgotem') {
        mail.setSubject(`Solicitação de alteração: ${date.toLocaleString()}`)
            .setHtml('sendCodeForgotem.html', [[/\[CODE\]/g, `${code}`]])
      } else {
        mail.setSubject(`Solicitação de acesso: ${date.toLocaleString()}`)
            .setHtml('sendCodeLogin.html', [[/\[CODE\]/g, `${code}`]])
      }
  
      await mail.send()
    } catch (error) {
      throw {
        request: { code: 500, message: 'não foi possível enviar e-mail' },
        code: 200
      }
    }
  }

  validateCode(register, data) {
    if (register.code == data.code) {
      return false
    } else {
      this.defaultResponseJSON({
        code: 400,
        message: 'código invalido'
      }, 200)
      return true
    }
  }

  validatePasswordChange(data) {
    let bool = (data.senha != data.confirm)
    if (bool) this.defaultResponseJSON({ code: 400, message: 'Senha diferente da confirmação' }, 200)
    return bool
  }

  validatePassword(register, data) {
    if (register.senha === data.senha) {
      return false
    } else {
      this.defaultResponseJSON({
        code: 400,
        message: 'Usuário ou senha inválida'
      }, 200)
      return true
    }
  }

  validateRequest(data, complement) {
    let _default = {
      email: 'required|email'
    }

    if (typeof complement && complement instanceof Object)
      _default = Object.assign(_default, complement)

    let validator = this.Validator.make(data, _default);

    if (validator.fails())
      this.defaultResponseJSON(validator.errorResult())

    return validator.fails()
  }

  defaultVerificationRequest(data, field) {
    let rule = {}

    rule[field] = 'required|object'

    let validator = this.Validator.make(data, rule)

    if (validator.fails())
      this.defaultResponseJSON(validator.errorResult())

    return validator.fails()
  }
}