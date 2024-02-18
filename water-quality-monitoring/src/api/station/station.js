import request from '@/utils/request'
import qs from 'qs'

// 查询指定区域监测船/station/list
export const reqStationList = (station) => request({url: `/station/list?area=${station}`, method: 'GET'})

// 修改监测船/station/edit
export const reqChangeShipInfo = (data) => request({url: '/station/edit', method: 'PUT', data})

//删除监测船/station/delete/{id}
export const reqDeleteShip = (id) => request({url: `/station/delete/${id}`, method: 'DELETE'})

// 添加监测船/station/add
export const reqAddShip = (data) => request({url: '/station/add', method: 'POST', data})
// 添加区域 /area/add
export const reqAddArea = (data) => request({url: '/area/add', method: 'POST', data})
