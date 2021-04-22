const BaseModel = require('./BaseModel.js')

module.exports = class UserModel extends BaseModel {
  // table name
  table = 'user'
  fields = {
    id: {
      type: this.DataTypes.INTERGER,
      auto_increment: true,
      nullable: true,
      primary_key: true
    },
    email: {
      type: this.DataTypes.STRING,
      nullable: false
    },
    senha: {
      type: this.DataTypes.TEXT,
      nullable: false
    }
  }

  cast = {}

  relation = {
    forgotem_code: require('./ForgotemcodeModel'),
    authentication_code: require('./AuthenticationcodeModel')
  }

  constructor() { super() }

  autentication() {
    return this.hasMany('user_id', this.relation.authentication_code)
  }

  forgotem_code() {
    return this.hasMany('user_id', this.relation.forgotem_code)
  }
}