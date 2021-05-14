<template>
  <div id="app">
    <div class="row">
      <Pupil label="left pupil" :diameter="diameter.left"/>
      <Pupil label="right pupil" :diameter="diameter.right"/>
    </div>

    <div class="row">
      <Bar label="CL predicted by the model" :value="prediction.value" :timestamp="prediction.timestamp" class="mt-10"/>
    </div>
    <div class="row">
      <Bar label="LHIPA" :value="lhipa.value" :timestamp="lhipa.timestamp" :max-value="10" class="mt-10"/>
    </div>
  </div>
</template>

<script>
import Pupil from './components/Pupil.vue'
import Bar from './components/Bar.vue'

export default {
  name: 'App',
  components: {
    Pupil, Bar
  },
  data() { return {
    lhipa: {
      value: null,
      timestamp: null
    },
    prediction: {
      value: null,
      timestamp: null
    },
    diameter: {
      left: null,
      right: null,
    }
  }},
  mounted() {
    this.$options.sockets.onmessage = (event) =>  {
      const data = JSON.parse(event.data)


      if (data.diameter) {
        this.diameter.left = data.diameter?.left
        this.diameter.right = data.diameter?.right
      }

      if (typeof data.prediction !== 'undefined') {
        this.prediction.value = data.prediction
        this.prediction.timestamp = data.timestamp
      }

      if (typeof data.lhipa !== 'undefined') {
        this.lhipa.value = data.lhipa
        this.lhipa.timestamp = data.timestamp
      }

      if (data.reset) {
        this.diameter.left = null
        this.diameter.right = null

        this.prediction.value = null
        this.prediction.timestamp = null

        this.lhipa.value = null
        this.lhipa.timestamp = null
      }

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

.mt-10 {
  margin-top: 2rem;
}

.label {
  color: rgba(0, 0, 0, 0.7)
}
</style>
