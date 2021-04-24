const { BaseController, Storage } = require('squirrel_resource')
const NodeRSA = require('node-rsa')
const JWT = require('jsonwebtoken')

module.exports = class RsaService extends BaseController {
  _storage = new Storage()

  constructor(request, response) { super(request, response) }

  getPath() {
    let path = this._storage.get('private')
    return this._storage.join_path(path, 'rsa')
  }

  getFileName() {
    return `${this.request.socket.remoteAddress}-rsa.json`
  }

  async generateServerKeys() {
    let rsa = new NodeRSA({ b: 256 })

    let pair = rsa.generateKeyPair(2048)

    this.setKeys({
      server: {
        publicKey: pair.exportKey('pkcs8-public-der').toString('hex'),
        privateKey: pair.exportKey('pkcs8-private-der').toString('hex')
      }
    })
  }

  pathExists() {
    let exists = this._storage.path_exists(this.getPath())

    if (exists.status == false) this._storage.mkdir(exists.path || this.getPath())
  }

  keysExists() {
    this.pathExists()
    let exists = this._storage.is_file(this._storage.join_path(this.getPath(), this.getFileName()))

    return exists.status
  }

  setKeys(keys) {
    this._storage.save_file({
      path: this.getPath(),
      filename: this.getFileName(),
      value: keys,
      parser: 'json',
      encoding: 'utf-8'
    })
  }

  async getKeys() {
    let count = 0,
        maxTrying = 40,
        value = null

    while(count < maxTrying) {
      try {
        value = this._storage.read_file({
          path: this.getPath(),
          filename: this.getFileName(),
          parser: 'json',
          encoding: 'utf-8'
        })
      } catch (error) {  }
      await this.sleep(0.25)
      count++
    }

    if (!value) throw {
      request: { 
        code: 200,
        message: 'failure in read keys or not generated'
      },
      requestCode: 500
    }

    return value
  }

  async getServerPublicKey() {
    let keys = await this.getKeys()
    try {
      let {
        server: {
          publicKey
        }
      } = keys

      return Buffer.from(publicKey, 'hex')
    } catch (error) { 
      throw { 
        request: {
          code: 200,
          message: 'failure in get server publicKey'
        },
        requestCode: 500
      }
    }
  }

  async getServerPrivateKey() {
    let keys = await this.getKeys()
    try {
      let {
        server: {
          privateKey
        }
      } = keys

      return Buffer.from(privateKey, 'hex')
    } catch (error) { 
      throw { 
        request: {
          code: 200,
          message: 'failure in get server privateKey'
        },
        requestCode: 500
      }
    }
  }

  async getAppPublicKey() {
    let keys = await this.getKeys()
    try {
      let {
        app: {
          publicKey
        }
      } = keys

      return Buffer.from(publicKey, 'hex')
    } catch (error) { 
      throw { 
        request: {
          code: 200,
          message: 'failure in get publicKey'
        },
        requestCode: 500
      }
    }
  }

  async setAppPublicKey({ publicKey }) {
    let keys = await this.getKeys()

    let rsa = new NodeRSA({ b: 256 })

    let pair = rsa.importKey(Buffer.from(publicKey, 'hex'), "pkcs8-public-der")
    keys.app = {
      publicKey: pair.exportKey("pkcs8-public-der").toString('hex')
    }

    this.setKeys(keys)
  }

  decryptPrivate(key, value) {
    var pair = new NodeRSA({ b: 64 })

    pair.importKey(key, "pkcs8-private-der")

    return pair.decrypt(Buffer.from(value, "hex"), 'utf8')
  }

  async decryptObject(object) {
    let json = {},
        key = await this.getServerPrivateKey()

    for(let index in object) {
      try {
        let _index_ = this.decryptPrivate(key, index)
        let _value_ = this.decryptPrivate(key, object[index])
  
        json[_index_] = _value_
      } catch (error) { console.log(error) }
    }

    return json
  }

  jwt(data) {
    return JWT.sign(data, this.request.secret(), { expiresIn: 86400 })
  }
}