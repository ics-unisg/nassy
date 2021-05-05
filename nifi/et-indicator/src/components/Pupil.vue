<template>
  <div>
    <div class="eye">
      <div class="pupil" :style="style">
      </div>
    </div>
  </div>
</template>

<script>
const MAX_PUPIL_SIZE = 10 //mm
export default {
  name: 'Pupil',
  props: {
    diameter: {
      default: null,
      type: Number
    }
  },
  computed: {
    style() { 
      const { diameter } = this.$props
      const size = diameter > 0 ? (diameter/MAX_PUPIL_SIZE)*100 : 0
      return {
        height: `${size}%`,
        width: `${size}%`,
      }
    }
  }
}
</script>

<style scoped>
.eye {
  height: 100px;
  width: 100px;
  border: 10px solid rgba(0, 0, 0, 0.7);
  border-radius: 60px;
  display: flex;
  justify-content: center;
  align-items: center
}

.pupil {
  height: 50px;
  width: 50px;
  background-color: rgba(0, 0, 0, 0.7);
  border-radius: 60px;
}
</style>
