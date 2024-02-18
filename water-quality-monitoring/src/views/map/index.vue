<template>
  <el-upload
    action="https://jsonplaceholder.typicode.com/posts/"
    :http-request="onUpload"
  >
    <el-button size="small" type="primary">点击上传</el-button>
  </el-upload>
<!--  <div id="map"></div>-->
</template>

<script>
  import L from 'leaflet';
  import axios from "axios";
  import request from "@/utils/request";
  import Vue from "vue";
  import API from "@/api";
  Vue.prototype.$API = API

  export default {

    name: "Ship",
    data() {
      return {
        map: null,
        markers: {},
      };
    },
    methods: {
      onUpload(file) {
        let formData = new FormData()
        formData.append('file', file.file)
        this.$API.excel.upload(formData).then((res) => {
          console.log(res)
          this.$message.success(this.$t('UPLOAD_SUCCESS'))
        }).catch((e) => {
          this.$message.error(e.message)
        })
      },
    },
    mounted() {
      // 初始化地图
      this.map = L.map('map').setView([113.288537, 23.112292], 13);

      // 添加地图瓦片
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
      }).addTo(this.map);

      // 创建一个新的EventSource实例
      var source = new EventSource("/station/position/all");

      // 监听message事件来获取数据
      source.onmessage = event => {
        // 解析接收到的数据
        var data = JSON.parse(event.data);
        var shipName = data.station;
        var lat = data.lat;
        var lng = data.lng;

        // 更新或创建标记
        if (this.markers[shipName]) {
          this.markers[shipName].setLatLng([lat, lng]);
        } else {
          this.markers[shipName] = L.marker([lat, lng]).addTo(this.map);
        }
      };
    },
  };
</script>

<style>
  #map {
    height: 600px;
  }
</style>
