<template>
    <div>
        <div id="preview" @click="$refs.inputUpload.click()">
            <img v-if="value" :src="value" width="200"/>
            <img v-else src="/camera.png" width="200"  />
            <p class="text-center">Click to change image</p>
        </div>
        <input type="file" @change="onFileChange" v-show="false" ref="inputUpload" />
    </div>
</template>
<script>
    export default {
        props: {
            value: null
        },
        methods: {
            onFileChange(e) {
                const file = e.target.files[0];
                var lectorDeImagen = new FileReader();
                lectorDeImagen.readAsDataURL(file);
                return (lectorDeImagen.onload = () => {
                  if (this.value == null) {
                    this.value = {};
                  }
                  this.$emit("input", lectorDeImagen.result)
                  this.$forceUpdate();
                });
                lectorDeImagen.onerror = function(error) {
                  console.log("Error: ", error);
                };                
                
            }
        }
    }
</script>
<style>
    #preview {
    }

    #preview img {
        max-width: 100%;
        max-height: 500px;
    }
</style>