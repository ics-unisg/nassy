<template>
  <div>
    <div class="label">
        {{label}}
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
const MAX_BAR_VALUE = 1
export default {
  name: 'Bar',
  props: {
    value: {
      default: null,
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
      const { value } = this.$props
      const size = value > 0 ? (value/MAX_BAR_VALUE)*100 : 0
      return {
        height: `${size}%`,
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
