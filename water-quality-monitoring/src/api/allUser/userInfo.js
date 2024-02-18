import request from '@/utils/request'

//分页查询用户信息
export const reqUserList = (num, size) => request({url: `/admin/select?pageNum=${num}&pageSize=${size}`, method: 'get'})

//修改用户状态 /admin/updateUserStatus
export const requpdateUserStatus = (data) => request({
  url: '/admin/updateUserStatus', method: 'POST', data
})

/**************************************管理员相关接口***********************************************/
export const reqAdministratorList = (num, size) => request({
  url: `/admin/selectAdmin?pageNum=${num}&pageSize=${size}`,
  method: 'get'
})

//修改管理员信息、
export const requpdateAdministratorInfo = (data) => request({url: '/admin/updateAdmin', method: 'POST', data})

// /admin/deleteAdminById 删除单个管理员信息
export const reqDeleteAdmin = (id) => request({url: `/admin/deleteAdminById?adminId=${id}`, method: 'DELETE'})

//添加管理员信息
export const reqAddAdmin = (data) => request({url: '/admin/register', method: 'POST', data})
