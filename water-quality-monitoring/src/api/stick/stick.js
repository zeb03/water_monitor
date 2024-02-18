import request from '@/utils/request'
import qs from 'qs'
// 组合查询拐杖信息 /walkingStick/selectByWalkingStickDto
export const reqStickList = (num, size, data) => request({
  url: `/walkingStick/selectByWalkingStickDto?pageNum=${num}&pageSize=${size}`,
  method: 'POST',
  data
})

// 修改拐杖绑定状态 /walkingStick/updateBindingState
export const reqChangeState = (data) => request({url: '/walkingStick/updateBindingState', method: 'POST', data})

// 删除单个拐杖信息/walkingStick/deleteWalkingStickById
export const reqDeleteStick = (id) => request({
  url: `/walkingStick/deleteWalkingStickById?walkingStickId=${id}`,
  method: 'DELETE'
})

// 批量删除拐杖信息/walkingStick/deleteWalkingStickByList
export const reqDeleteSticks = (params) => request({
  url: '/walkingStick/deleteWalkingStickByList',
  method: 'DELETE',
  params,
  paramsSerializer: params => {
    return qs.stringify({walkingStickIds: params}, {arrayFormat: 'repeat'})
  }
})
