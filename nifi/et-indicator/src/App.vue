<template>
  <div id="app">
    <div class="row">
      <Pupil :diameter="diameter.left"/>
      <Pupil :diameter="diameter.right"/>
    </div>
  </div>
</template>

<script>
import Pupil from './components/Pupil.vue'

export default {
  name: 'App',
  components: {
    Pupil
  },
  data() { return { 
    diameter: {
      left: null,
      right: null,
    }
  }},
  mounted() {
    this.$options.sockets.onmessage = (event) =>  {
      const data = JSON.parse(event.data)

      this.diameter.left = data.diameter?.left
      this.diameter.right = data.diameter?.right
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

.row {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}
</style>
