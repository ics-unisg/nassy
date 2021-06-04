<template>
  <div id="app">
    <div class="row">
      {{meta.study}} - {{meta.subject}} | {{type}}
    </div>
    <div class="row mt-10">
      <Pupil label="left pupil" :diameter="diameter.left" :missing="diameter.missing.right"/>
      <Pupil label="right pupil" :diameter="diameter.right" :missing="diameter.missing.right"/>
    </div>

    <div class="row">
      <Bar label="CL predicted by the model" :value="prediction.value" :timestamp="prediction.timestamp" class="mt-10"/>
    </div>
    <div class="row">
      <Bar label="LHIPA" :value="lhipa.value" :timestamp="lhipa.timestamp" :max-value="100" class="mt-10"/>
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
    meta: {
      study: null,
      subject: null
    },
    lhipa: {
      value: null,
      timestamp: null
    },
    prediction: {
      value: null,
      timestamp: null
    },
    type: "RESET",
    diameter: {
      left: null,
      right: null,
      missing: {
        left: true,
        right: true
      }
    }
  }},
  mounted() {
    this.$options.sockets.onmessage = (event) =>  {
      const data = JSON.parse(event.data)
      if(data.type) this.type = data.type


      if (data.study) this.meta.study = data.study
      if (data.subject) this.meta.subject = data.subject

      if (data.diameter) {
        if (data.diameter?.left > 0) this.diameter.left = data.diameter?.left
        if (data.diameter?.right > 0) this.diameter.right = data.diameter?.right

        this.diameter.missing.left = data.diameter?.left <= 0
        this.diameter.missing.right = data.diameter?.right <= 0
      }

      if (typeof data.prediction !== 'undefined') {
        console.log(data)
        this.prediction.value = data.prediction
        this.prediction.timestamp = data.timestamp
      }

      if (typeof data.lhipa !== 'undefined') {
        this.lhipa.value = data.lhipa
        this.lhipa.timestamp = data.timestamp
      }

      if (data.reset) {
        this.type = "RESET"
        this.diameter.left = null
        this.diameter.right = null

        this.prediction.value = null
        this.prediction.timestamp = null

        this.lhipa.value = null
        this.lhipa.timestamp = null

        this.meta.subject = null
        this.meta.study = null
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
