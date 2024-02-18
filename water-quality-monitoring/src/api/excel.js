import request from '@/utils/request'

export const upload = (data) => request({
  url: '/excel/uploadExcel',
  method: 'POST',
  data,
  headers: {
    'Content-Type': 'multipart/form-data'
  }
})
