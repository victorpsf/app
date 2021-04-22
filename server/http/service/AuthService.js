const RsaService = require('./RsaService')
const { Utilities } = require('squirrel_resource')

module.exports = class AuthService extends RsaService {
  user = require('../../app/UserModel')
  forgotemCode = require('../../app/ForgotemcodeModel')
  authenticationCode = require('../../app/AuthenticationcodeModel')

  constructor(request, response) { super(request, response) }

  verify_code(code, type) {
    switch (type) {
      case 'forgotem':
        break
      case 'authentication':
        break
      default:
        return false
    }
  }

  async getForgotemCode(user) {
    let codes = await user.forgotem_code()

    codes = codes.filter(function (code) {
      return !code.usaged_at
    })

    let register = codes.first()

    if (!register) {
      let code = (new Utilities()).randomNumber(100000, 999999)

      register = await this.forgotemCode.create({
        code,
        user_id: user.id
      })
    }

    return register
  }

  async getCode(type, data) {
    switch (type) {
      case 'forgotem':
        let users = await this.user.where({ column: 'email', value: data.email }).get(),
            user = users.first()

        if (!user) { throw { request: { code: 404, message: 'don\'t found' } } }

        return this.getForgotemCode(user)
      default:
        break;
    }
  }
}