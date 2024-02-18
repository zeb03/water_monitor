//将四个模块的请求接口函数统一暴露
import * as trademark from './product/tradeMark'
import * as attr from './product/attr'
import * as sku from './product/sku'
import * as spu from './product/spu'
import * as userInfo from './allUser/userInfo'
import * as stick from './stick/stick'
import * as water from './water/water'
import * as station from './station/station'
import * as excel from './excel'

export default {
  trademark,
  attr,
  sku,
  spu,
  userInfo,
  stick,
  water,
  station,
  excel
}
