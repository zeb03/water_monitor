//这个模块主要获取品牌管理模块
import request from '@/utils/request'

// 获取品牌列表接口
// /admin/product/baseTrademark/{page}/{limit}
export const reqTradeMark = (page, limit) => request({
  url: `/admin/product/baseTrademark/${page}/${limit}`,
  method: 'get'
})

// 添加品牌/admin/product/baseTrademark/save,   POST, logoUrl (string, optional): 品牌logo的图片路径 ,tmName (string, optional): 属性值
// 修改品牌/admin/product/baseTrademark/update PUT id logoUrl tmName
export const reqAddOrUpdataTradeMark = (trademark) => {
  if (trademark.id) {
    //修改
    return request({url: '/admin/product/baseTrademark/update', method: 'put', data: trademark})
  } else {
    //新增
    return request({url: '/admin/product/baseTrademark/save', method: 'POST', data: trademark})
  }
}

//删除品牌 /admin/product/baseTrademark/remove/{id}
export const reqDeleteTradeMark = (id) => request({url: `/admin/product/baseTrademark/remove/${id}`, method: 'delete'})
