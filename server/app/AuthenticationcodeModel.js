const BaseModel = require('./BaseModel.js')

module.exports = class AuthenticationcodeModel extends BaseModel {
  // table name
  table = ''
  fields = {
    id: {
      type: this.DataTypes.INTERGER,
      auto_increment: true,
      nullable: true,
      primary_key: true
    },
    code: {
      type: this.DataTypes.INTERGER,
      nullable: false
    },
    usaged_at: {
      type: this.DataTypes.DATE_TIME,
      nullable: true
    },
    user_id: {
      type: this.DataTypes.INTERGER,
      nullable: false
    }
  }

  cast = {}

  relation = {
    user: require('./UserModel')
  }

  constructor() { super() }

  user() {
    return this.belongsTo('user_id', this.relation.user)
  }
}