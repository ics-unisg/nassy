<template>
  <div>
    <div class="label">
        {{label}} ({{Math.round(value*100)/100}})
    </div>
    <div class="container">
      <div class="bar" :style="style">
      </div>
    </div>
    <div class="timestamp">
        {{timestamp}}
    </div>
  </div>
</template>

<script>
export default {
  name: 'Bar',
  props: {
    value: {
      default: null,
      type: Number
    },
    maxValue: {
      default: 1,
      type: Number
    },
    timestamp: {
      default: null,
      type: Number
    },
    label: {
      default: null,
      type: String
    }
  },
  computed: {
    style() { 
      const { value, maxValue } = this.$props
      const size = value > 0 ? (value/maxValue)*100 : 0
      return {
        height: `100%`,
        width: `${size}%`,
      }
    }
  }
}
</script>

<style scoped>
.container {
  height: 30px;
  width: 220px;
  border: 10px solid rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center
}

.bar {
  height: 30px;
  width: 0px;
  background-color: rgba(0, 0, 0, 0.7);
}

.timestamp {
  width: 220px;
}
</style>
