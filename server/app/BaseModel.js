const { Model } = require('squirrel_orm')
const { Config } = require('squirrel_resource')

module.exports = class BaseModel extends Model {
  constructor() { super(Config.mysql()) }
}