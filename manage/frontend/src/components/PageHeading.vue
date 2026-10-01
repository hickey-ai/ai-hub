<script setup>
defineProps({ eyebrow: String, title: String, desc: String })
</script>
<template><div class="heading"><div><div class="eyebrow">{{eyebrow}}</div><h1>{{title}}</h1><p>{{desc}}</p></div></div></template>
