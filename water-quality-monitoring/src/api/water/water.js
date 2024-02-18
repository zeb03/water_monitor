import request from '@/utils/request'
import qs from 'qs'
// 查询当日水质数据 /water/quality/day
export const reqQualityDay = (data) => request({url: `/water/quality/day`, method: 'GET', params: data})

//分页查询水质历史数据/water/quality/query
export const reqHistoricalWaterQualityData = (data) => request({
  url: `/water/quality/query`,
  method: 'GET',
  params: data
})

// 查询所有监测区域 /water/quality/areas
// export const reqAllAreas = () => request({ url: "/water/quality/areas", method: "GET" })
export const reqAllAreas = () => request({url: "/area/list", method: "GET"})
//添加水质数据 /water/quality/add
export const addWaterData = (data) => request({url: "/water/quality/add", method: "POST", data})

//删除水质信息 /water/quality/delete/{id}
export const deleteWaterData = (id) => request({url: `/water/quality/delete/${id}`, method: "DELETE"})

// 修改水质数据 /water/quality/edit
export const changeWaterData = (data) => request({url: '/water/quality/edit', method: "PUT", data})

// 获取本月不达标数据 /water/quality/substandard
export const reqSubstandardData = () => request({url: '/water/quality/substandard', method: "GET"})

//获取所有水质参数 /water/quality/indicator
export const reqIndicator = () => request({url: '/water/quality/indicator', method: "GET"})

// 获取数据示意图/water/quality/plot
export const reqWaterPlot = (data) => request({url: '/water/quality/plot', method: 'GET', params: data})

// 查询指定区域监测船/station/list
export const reqStationList = (station) => request({url: `/station/list?area=${station}`, method: 'GET'})


// 修改拐杖绑定状态 /walkingStick/updateBindingState
// export const reqChangeState = (data) => request({ url: '/walkingStick/updateBindingState', method: 'POST', data })

// 删除单个拐杖信息/walkingStick/deleteWalkingStickById
// export const reqDeleteStick = (id) => request({ url: `/walkingStick/deleteWalkingStickById?walkingStickId=${id}`, method: 'DELETE' })

// 批量删除拐杖信息/walkingStick/deleteWalkingStickByList
// export const reqDeleteSticks = (params) => request({
//     url: '/walkingStick/deleteWalkingStickByList',
//     method: 'DELETE',
//     params,
//     paramsSerializer: params => {
//         return qs.stringify({ walkingStickIds: params }, { arrayFormat: 'repeat' })
//     }
// })
